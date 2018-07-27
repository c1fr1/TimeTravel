package Game;

import engine.*;
import engine.Entities.Camera;
import engine.OpenGL.*;

import java.io.File;
import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class MainView extends EnigView {
	public static MainView main;

	public Camera cam;

	public static char[] solidBlocks = {'#', '_', 'l'};

	//project variables

	public LevelBase currentLevel;
	public static int currentLevelNum = 0;

	public char inventory[];
	public int inventoryCounter = 0;

	public ShaderProgram guiShader;
	public ShaderProgram ttoguiShader;
	public ShaderProgram textureShader;
	public ShaderProgram pauseShader;
	public ShaderProgram travelShader;

	public SpriteButton ttoGUIButton;

	public Texture ttoGUI;
	public Texture[] spriteTexture;

	public VAO ttoGUIVAO;
	public VAO playerVAO;

	public Texture[] pauseGUI;
	public VAO[] pauseGUIVAO;

	public VAO screenVAO;

	public FBO mainFBO;

	public boolean pause = false;
	public boolean cooldown = false;

	public int framesPaused;

	public float timeTravelFrames = 0;
	public float animationFrameCounter = 0;

	public long lastTime = System.nanoTime();

	SpriteButton cont;
	SpriteButton restart;
	SpriteButton menu;

	@Override
	public void setup() {
		//set variables here
		inventory = new char[9];
		glDisable(GL_DEPTH_TEST);
		//needs to be generalized to use level selected - level path is a parameter
		float aspectRatio = (float) window.getHeight() / (float) window.getWidth();
        currentLevel = new LevelBase("res/Levels/Level"+currentLevelNum+".txt");
		cam = new Camera((float)window.getWidth(), (float)window.getHeight());
		guiShader = new ShaderProgram("guiShader");
		ttoGUI = new Texture("res/timeTravelGUI.png");
		ttoGUIVAO = new VAO(-0.5f, 0.125f, 1f, 0.25f);
		playerVAO = new VAO(-40f, 10f, 30f, 30f);

		spriteTexture = new Texture[4];//down left right up;
		spriteTexture[0] = new Texture("res/future-wall.png");
		spriteTexture[1] = new Texture("res/sprite-left.png");
		spriteTexture[2] = new Texture("res/sprite-right.png");
		spriteTexture[3] = new Texture("res/sprite-up.png");

        cont = new SpriteButton(-0.5f,0.525f,1f,0.25f,"res/menu/continue.png", aspectRatio);
        restart = new SpriteButton(-0.5f, -0.125f, 1f, 0.25f,"res/menu/restart.png", aspectRatio);
        menu = new SpriteButton(-0.5f, -0.725f, 1f, 0.25f,"res/menu/menu.png", aspectRatio);

		textureShader = new ShaderProgram("textureShaders");
		pauseShader = new ShaderProgram("pauseShaders");
		ttoguiShader = new ShaderProgram("ttoGUIShader");
		travelShader = new ShaderProgram("travelShaders");

		ttoGUIButton = new SpriteButton(-0.06f, 0.4f, 0.12f, 0.12f, "res/ttoguiButton.png");
		ttoGUIButton.shader = new ShaderProgram("ttoGUIButtonShader");

		mainFBO = new FBO(new Texture(window.getWidth(), window.getHeight()));
		screenVAO = new VAO(-1f, -1f, 2f, 2f);

		cam.x = currentLevel.ystart[currentLevel.currentTZ] * 50 + 25;
		cam.y = currentLevel.xstart[currentLevel.currentTZ] * 50 + 25;
	}
	public void nextLevel(int increment) {

		File test = new File("res/Levels");
		if(test.listFiles().length > currentLevelNum+increment && !(currentLevelNum+increment < 0)) {
            currentLevelNum+=increment;
            inventoryCounter = 0;
            inventory = new char[9];
            currentLevel = new LevelBase("res/Levels/Level" + currentLevelNum + ".txt");

            cam.x = currentLevel.ystart[currentLevel.currentTZ] * 50 + 25;
            cam.y = currentLevel.xstart[currentLevel.currentTZ] * 50 + 25;
        }
    }

	/**
	 *
     * @return character replaced
	 * @param x location of replace
	 * @param y location of replace
	 * @param replacement tile you are replacing it with
	 */
	public char replaceTile(float x, float y, char replacement) {
		int tempIntX = (int)(x/50f);
		int tempIntY = (int)(y/50f);
        char current = currentLevel.levelseries.get(currentLevel.currentTZ).get(tempIntY)[tempIntX];
		for (int i = currentLevel.currentTZ; i < currentLevel.levelseries.size(); i ++) {
			currentLevel.levelseries.get(i).get(tempIntY)[tempIntX] = replacement;
		}

		//currentLevel.levelseries.get(tempIntY)
		return current;
	}
	
	public char replaceTile(int x, int y, char replacement) {
		char current = currentLevel.levelseries.get(currentLevel.currentTZ).get(y)[x];
		for (int i = currentLevel.currentTZ; i < currentLevel.levelseries.size(); i ++) {
			currentLevel.levelseries.get(i).get(y)[x] = replacement;
		}
		
		//currentLevel.levelseries.get(tempIntY)
		return current;
	}
	
	public boolean getFromInventory(char item){
	    for(int i = 0; i < inventory.length; i++){
	        if(inventory[i] == item){
	            for(int j = i; j < inventory.length-1; j++){
	                inventory[j] = inventory[j+1];
                }
                inventoryCounter--;
                return true;
            }
        }
        return false;
    }

    public boolean checkInventory(char item){
        for(char i: inventory){
            if(i == item){
                return true;
            }
        }
        return false;
    }


	@Override
	public boolean loop() {
		long time = System.nanoTime();
		float delta_time = ((float)(time - lastTime) / 1000000f);
		float aspectRatio = (float) window.getHeight() / (float) window.getWidth();
		if(UserControls.pause(window)){
			if(!cooldown){
				framesPaused = 0;
				pause = !pause;
			}
			cooldown = true;

		} else if(!UserControls.pause(window)){
			cooldown = false;
		}
        //dev buttons
        if (window.keys[GLFW_KEY_N] == 1)
        {
            nextLevel(1);
        }
        if (window.keys[GLFW_KEY_B] == 1)
        {
            nextLevel(-1);
        }

		//Pause menu
		if(pause){
			++framesPaused;
			float scalar = 1-((float)framesPaused/50f);
			if (scalar < 0.5f) {
				scalar = 0.5f;
			}


			lastTime = System.nanoTime();

			FBO.prepareDefaultRender();
			pauseShader.enable();
			pauseShader.shaders[2].uniforms[0].set(scalar);
			mainFBO.getBoundTexture().bind();
			screenVAO.fullRender();
			guiShader.enable();

			guiShader.shaders[0].uniforms[0].set((float)window.getHeight()/(float)window.getWidth());
			cont.render();
			restart.render();
			menu.render();

			//hover highlighting
			if (menu.hoverCheck(window.cursorXFloat,window.cursorYFloat))
			{
				menu.setPath("res/menu/restart.png");
                if (window.mouseButtons[GLFW_MOUSE_BUTTON_LEFT] == 1)
				{
					System.out.println("Menu Clicked");
				}
			}
			else
			{
				menu.setPath("res/menu/menu.png");
			}

			//restart highlighting
            if (restart.hoverCheck(window.cursorXFloat,window.cursorYFloat))
            {
                restart.setPath("res/menu/menu.png");
             	if (window.mouseButtons[GLFW_MOUSE_BUTTON_LEFT] == 1)
                {
                    System.out.println("Restart Clicked");
					framesPaused = 0;
					pause = !pause;
					nextLevel(0);
            	}
            }
            else
            {
                restart.setPath("res/menu/restart.png");
            }
			//continue highlighting
            if (cont.hoverCheck(window.cursorXFloat,window.cursorYFloat))
            {
                cont.setPath("res/menu/restart.png");
            	if (window.mouseButtons[GLFW_MOUSE_BUTTON_LEFT] == 1)
            	{
            		System.out.println("Continue Clicked");
                    framesPaused = 0;
                    pause = !pause;
            	}
            }
            else
            {
            	cont.setPath("res/menu/continue.png");
            }
		}
		//Time Travel animation
		else if (timeTravelFrames > 0) {
			FBO.prepareDefaultRender();

			currentLevel.render(cam);
			LevelBase.levelProgram.shaders[0].uniforms[0].set(cam.getCameraMatrix(cam.x, cam.y, 0));
			spriteTexture[0].bind();
			playerVAO.fullRender();

			travelShader.enable();
			travelShader.shaders[2].uniforms[0].set(aspectRatio);
			float dist = (float) timeTravelFrames / 5;
			travelShader.shaders[2].uniforms[1].set(dist);
			mainFBO.getBoundTexture().bind();
			screenVAO.fullRender();
			guiShader.enable();

			lastTime = System.nanoTime();

			timeTravelFrames+=delta_time*0.05;
			if (timeTravelFrames >= 50) {
				timeTravelFrames = 0;
			}
		}
		//run game
		else {
			mainFBO.prepareForTexture();

			lastTime = time;

			//game here
			currentLevel.render(cam);

			//sets the speed of the avatar
			float vSpeed = 0;
			float hSpeed = 0;

			if (UserControls.forward(window)) {
				vSpeed -= delta_time / 3f;
			}
			if (UserControls.backward(window)) {
				vSpeed += delta_time / 3f;
			}
			if (UserControls.left(window)) {
				hSpeed -= delta_time / 3f;
			}
			if (UserControls.right(window)) {
				hSpeed += delta_time / 3f;
			}
			if (hSpeed != 0) {
				vSpeed *= 0.70710678118f;
			}
			if (vSpeed != 0) {
				hSpeed *= 0.70710678118f;
			}
			//dictates avatar movement
			cam.x = CamCollision.horizontalMovement(cam.x,cam.y,hSpeed,vSpeed,
                    currentLevel.levelseries.get(currentLevel.currentTZ),solidBlocks);
			cam.y = CamCollision.verticalMovement(cam.x,cam.y,hSpeed,vSpeed,
                    currentLevel.levelseries.get(currentLevel.currentTZ),solidBlocks);

			LevelBase.levelProgram.enable();
			LevelBase.levelProgram.shaders[0].uniforms[0].set(cam.getCameraMatrix(cam.x, cam.y, 0));
			spriteTexture[0].bind();
			playerVAO.fullRender();

			int[] nearesTTOCheck = new int[4];
			nearesTTOCheck[0] = numVal(currentLevel.charAtPos(cam.x + 15f, cam.y - 15f));
			nearesTTOCheck[1] = numVal(currentLevel.charAtPos(cam.x + 15f, cam.y + 15f));
			nearesTTOCheck[2] = numVal(currentLevel.charAtPos(cam.x - 15f, cam.y - 15f));
			nearesTTOCheck[3] = numVal(currentLevel.charAtPos(cam.x - 15f, cam.y + 15f));
			int ttoOnInd = -1;
			for (int i:nearesTTOCheck) {
				if (i >= 0) {
					ttoOnInd = i;
				}
			}
			if (ttoOnInd >= 0) {
				ttoguiShader.enable();
				ttoguiShader.shaders[0].uniforms[0].set(aspectRatio);

				ttoGUIButton.shader.enable();
				ttoGUIButton.shader.shaders[0].uniforms[0].set(aspectRatio);
				ttoGUIButton.sprite.bind();
				ttoGUIButton.vao.prepareRender();
				float leftOffset = 0.025f * (float) currentLevel.levelseries.size();
				for (int i = 0; i < currentLevel.levelseries.size(); ++i) {
					float floati = (float) i;
					//float x = floati * 0.2f + window.cursorXFloat * aspectRatio;
					ttoGUIButton.shader.shaders[0].uniforms[1].set(-leftOffset + 0.1f * floati - ttoGUIButton.width/2);
					if (!currentLevel.timeZonePossibilities.get(ttoOnInd)[i]) {
						ttoGUIButton.shader.shaders[2].uniforms[0].set(0f);
					} else if (currentLevel.currentTZ == i) {
						ttoGUIButton.shader.shaders[2].uniforms[0].set(2f);
					} else if (ttoGUIButton.hoverCheck((window.cursorXFloat - floati * 0.1f + leftOffset + ttoGUIButton.width/2)/aspectRatio, window.cursorYFloat)) {
						if (UserControls.leftMB(window)) {
							currentLevel.currentTZ = i;
							++timeTravelFrames;
						}
						ttoGUIButton.shader.shaders[2].uniforms[0].set(3f);
					} else {
						ttoGUIButton.shader.shaders[2].uniforms[0].set(1f);
					}
					ttoGUIButton.vao.draw();
				}
				ttoGUIButton.vao.unbind();
				/*if (ttoGUIButton.hoverCheck(window.cursorXFloat * aspectRatio, window.cursorYFloat)) {
					ttoGUIButton.shader.enable();
					ttoGUIButton.shader.shaders[0].uniforms[0].set(aspectRatio);

				}*/
				ttoguiShader.enable();
				ttoGUIButton.shader.shaders[0].uniforms[0].set(aspectRatio);
				if (timeTravelFrames == 0) {
					ttoGUI.bind();
					ttoGUIVAO.fullRender();
				}
				//ttoGUIButtonTexture.bind();
				//ttoGUIButtonVAO.fullRender();

			}
			int spriteSize = 35;
			int[] arrpossibilities = new int[12];
			arrpossibilities[0] = numVal(currentLevel.charAtPos(cam.x - spriteSize, cam.y));
			arrpossibilities[1] = numVal(currentLevel.charAtPos(cam.x + spriteSize, cam.y));
			arrpossibilities[2] = numVal(currentLevel.charAtPos(cam.x, cam.y + spriteSize));
			arrpossibilities[3] = numVal(currentLevel.charAtPos(cam.x, cam.y - spriteSize));
			arrpossibilities[4] = numVal(currentLevel.charAtPos(cam.x-spriteSize, cam.y-spriteSize));
			arrpossibilities[5] = numVal(currentLevel.charAtPos(cam.x+spriteSize, cam.y-spriteSize));
			arrpossibilities[6] = numVal(currentLevel.charAtPos(cam.x-spriteSize, cam.y+spriteSize));
			arrpossibilities[7] = numVal(currentLevel.charAtPos(cam.x+spriteSize, cam.y+spriteSize));
			arrpossibilities[8] = nearesTTOCheck[0];
			arrpossibilities[9] = nearesTTOCheck[1];
			arrpossibilities[10] = nearesTTOCheck[2];
			arrpossibilities[11] = nearesTTOCheck[3];
			int ttotouchingindex = -1;
			for (int i:arrpossibilities) {
				if (i > ttotouchingindex) {
					ttotouchingindex = i;
				}
			}

            currentLevel.updateTTO(arrpossibilities, delta_time);

            if (CamCollision.isColliding(cam.x,cam.y,1,currentLevel.levelseries.get(currentLevel.currentTZ),'g'))
            {
                nextLevel(1);
            }
            if (CamCollision.isColliding(cam.x, cam.y, 1, currentLevel.levelseries.get(currentLevel.currentTZ),'k'))
            {
                inventory[inventoryCounter] = replaceTile(cam.x, cam.y, ' ');
                inventoryCounter ++;
            }

			int gateCheckXIndex = (int)((cam.x + getSign(hSpeed)*20f)/50f);
			int gateCheckYIndex = (int)((cam.y + getSign(vSpeed)*20f)/50f);
			if(currentLevel.charAtPos(gateCheckXIndex, gateCheckYIndex) == 'l'){
				
                if(checkInventory('k')){
                    replaceTile(gateCheckXIndex, gateCheckYIndex, ' ');
                    getFromInventory('k');
                }
            }

			guiShader.enable();
			guiShader.shaders[0].uniforms[0].set(aspectRatio);
			FBO.prepareDefaultRender();
			textureShader.enable();
			mainFBO.getBoundTexture().bind();
			screenVAO.fullRender();
		}
		return false;
	}

	public ArrayList<Character[]> getCurrentZone() {
		return currentLevel.levelseries.get(currentLevel.currentTZ);
	}
	public static boolean isNumericValue(char character) {
		int val = Character.getNumericValue(character);
		return val >= 0 && val < 10;
	}
	public static int numVal(char character) {
		int val = Character.getNumericValue(character);
		if (val >= 10) {
			return -1;
		}
		return val;
	}

	public static void main(String[] args) {
		main = new MainView();
	}

	public int getSign(float thing){
		if(thing > 0){
			return 1;
		} else if(thing < 0){
			return -1;
		} else {
		    return 0;
        }
	}

	@Override
	public String getName() {
		return "Time Travel Puzzle game";
	}
}
