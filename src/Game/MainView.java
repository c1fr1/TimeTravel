package Game;

import engine.*;
import engine.Entities.Camera;
import engine.OpenGL.*;
import org.joml.Vector2f;

import java.io.File;
import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class MainView extends EnigView {
	public static MainView main;

	public static Camera cam;

	public static char[] solidBlocks = {'#', '_', 'l','^','<','>','v'};
	public static ArrayList<Entity> entities = new ArrayList<Entity>();

	//project variables

	public static LevelBase currentLevel;
	public static int currentLevelNum = 0;

	public Inventory inv;

	public ShaderProgram guiShader;
	public ShaderProgram ttoguiShader;
	public ShaderProgram textureShader;
	public ShaderProgram pauseShader;
	public ShaderProgram travelShader;
	public ShaderProgram inventoryShader;
	public ShaderProgram backgroundShader;
	public ShaderProgram ttoGUIButtonShader;
	
	public Texture starBackground;
	public Vector2f backgroundOffset = new Vector2f();
	public Vector2f backgroundVelocity = new Vector2f();

	public SpriteButton ttoGUIButton;

	public Texture frontStars;
	public Texture ttoGUI;
	public Texture keyTexture;
	public Texture[] spriteTexture;

	public VAO ttoGUIVAO;
	public VAO playerVAO;
	public VAO inventoryObjectVAO;

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

	int ttoSelector;
	boolean ttoSelectorBool;

	@Override
	public void setup() {
		//set variables here
		glDisable(GL_DEPTH_TEST);

		//needs to be generalized to use level selected - level path is a parameter
		SpriteButton.shader = new ShaderProgram("buttonShader");
		float aspectRatio = (float) window.getHeight() / (float) window.getWidth();
        currentLevel = new LevelBase("res/Levels/Level"+currentLevelNum+".txt");
		cam = new Camera((float)window.getWidth(), (float)window.getHeight());
		guiShader = new ShaderProgram("guiShader");
		ttoGUI = new Texture("res/sprites/timeTravelGUI.png");
		keyTexture = new Texture("res/sprites/inventoryKey.png");
		inventoryObjectVAO = new VAO(-1f, -0.9f, 0.1f, 0.1f);
		ttoGUIVAO = new VAO(-0.5f, 0.125f, 1f, 0.25f);
		playerVAO = new VAO(-40f, 10f, 30f, 30f);
		inv = new Inventory();

		spriteTexture = new Texture[4];//down left right up;
		spriteTexture[0] = new Texture("res/sprites/future-wall.png");
		//spriteTexture[1] = new Texture("res/sprites/sprite-left.png");
		//spriteTexture[2] = new Texture("res/sprites/sprite-right.png");
		//spriteTexture[3] = new Texture("res/sprites/sprite-up.png");

        cont = new SpriteButton(-0.5f,0.525f,1f,0.25f,"res/sprites/continueButton.png", aspectRatio);
        restart = new SpriteButton(-0.5f, -0.125f, 1f, 0.25f,"res/sprites/restart.png", aspectRatio);
        menu = new SpriteButton(-0.5f, -0.725f, 1f, 0.25f,"res/sprites/mainMenu.png", aspectRatio);

		textureShader = new ShaderProgram("textureShaders");
		pauseShader = new ShaderProgram("pauseShaders");
		ttoguiShader = new ShaderProgram("ttoGUIShader");
		travelShader = new ShaderProgram("travelShaders");
		inventoryShader = new ShaderProgram("inventoryShaders");
		backgroundShader = new ShaderProgram("backgroundShader");
		
		frontStars = new Texture("res/sprites/frontstars.png");
		frontStars.bind();
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		Texture.unbind();
		starBackground = new Texture("res/sprites/stars.png");
		starBackground.bind();
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		Texture.unbind();
		backgroundOffset = new Vector2f(0f, 0f);
		backgroundVelocity = new Vector2f(0f, 0f);

		ttoGUIButton = new SpriteButton(-0.06f, 0.4f, 0.12f, 0.12f, "res/sprites/ttoguiButton.png");
		ttoGUIButtonShader = new ShaderProgram("ttoGUIButtonShader");

		mainFBO = new FBO(new Texture(window.getWidth(), window.getHeight()));
		screenVAO = new VAO(-1f, -1f, 2f, 2f);

		cam.x = currentLevel.ystart[currentLevel.currentTZ] * 50 + 25;
		cam.y = currentLevel.xstart[currentLevel.currentTZ] * 50 + 25;

		ttoSelector = currentLevel.currentTZ;
		ttoSelectorBool = false;

		//System.out.println(entities.get(0));

	}

	public boolean nextLevel(int increment) {
		inv.reset();
		File test = new File("res/Levels");
		if(test.listFiles().length > currentLevelNum+increment && !(currentLevelNum+increment < 0)) {
            currentLevelNum+=increment;
            currentLevel = new LevelBase("res/Levels/Level" + currentLevelNum + ".txt");

            cam.x = currentLevel.ystart[currentLevel.currentTZ] * 50 + 25;
            cam.y = currentLevel.xstart[currentLevel.currentTZ] * 50 + 25;
            return false;
        }else {
			return true;
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

	/**
	 *
	 * @param x index of replace
	 * @param y index of replace
	 * @param replacement tile you are replacing it with
	 * @return
	 */
	public char replaceTile(int x, int y, char replacement) {
		char current = currentLevel.levelseries.get(currentLevel.currentTZ).get(y)[x];
		for (int i = currentLevel.currentTZ; i < currentLevel.levelseries.size(); i ++) {
			currentLevel.levelseries.get(i).get(y)[x] = replacement;
		}
		
		//currentLevel.levelseries.get(tempIntY)
		return current;
	}


	@Override
	public boolean loop() {
		//System.out.println(ttoSelector);
		long time = System.nanoTime();
		float delta_time = ((float)(time - lastTime) / 1000000f);
		if (delta_time > 60f) {
			delta_time = 60f;
		}
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
			
			SpriteButton.shader.enable();

			SpriteButton.shader.shaders[0].uniforms[0].set(aspectRatio);
			
			if (menu.render(window.cursorXFloat,window.cursorYFloat)) {
                if (window.mouseButtons[GLFW_MOUSE_BUTTON_LEFT] == 1) {
					System.out.println("Menu Clicked");
				}
			}
			
            if (restart.render(window.cursorXFloat,window.cursorYFloat)) {
             	if (window.mouseButtons[GLFW_MOUSE_BUTTON_LEFT] == 1) {
					framesPaused = 0;
					pause = !pause;
					nextLevel(0);
            	}
            }
            
            if (cont.render(window.cursorXFloat, window.cursorYFloat)) {
            	if (window.mouseButtons[GLFW_MOUSE_BUTTON_LEFT] == 1) {
                    framesPaused = 0;
                    pause = !pause;
            	}
            }
		}
		//Time Travel animation
		else if (timeTravelFrames > 0) {
			FBO.prepareDefaultRender();

			ttoSelector = currentLevel.currentTZ;
			ttoSelectorBool = false;

			backgroundShader.enable();
			backgroundShader.shaders[2].uniforms[0].set(backgroundOffset.mul(0.5f, new Vector2f()));
			starBackground.bind();
			screenVAO.prepareRender();
			screenVAO.drawTriangles();
			frontStars.bind();
			backgroundShader.shaders[2].uniforms[0].set(backgroundOffset);
			screenVAO.drawTriangles();
			screenVAO.unbind();
			
			
			
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
			
			backgroundShader.enable();
			backgroundShader.shaders[2].uniforms[0].set(backgroundOffset);
			starBackground.bind();
			screenVAO.prepareRender();
			screenVAO.drawTriangles();
			frontStars.bind();
			backgroundShader.shaders[2].uniforms[0].set(backgroundOffset.mul(0.5f, new Vector2f()));
			screenVAO.drawTriangles();
			screenVAO.unbind();
			
			backgroundVelocity.mul(0.99f);
			backgroundVelocity.add((float) (delta_time * 0.000007f * (Math.random() - 0.5)), (float) (delta_time * 0.000007f * (Math.random() - 0.5)));
			backgroundOffset.add(backgroundVelocity);

			lastTime = time;

			//game here
			currentLevel.render(cam);

			//MOVEMENT
			Movement m = new Movement(delta_time, window, cam, currentLevel, solidBlocks);
			cam.x += m.getXOffset();
			cam.y += m.getYOffset();
			backgroundOffset.x += m.getXOffset() * 0.0005;
			backgroundOffset.y += m.getYOffset() * 0.0005;
			

			LevelBase.levelProgram.enable();
			LevelBase.levelProgram.shaders[0].uniforms[0].set(cam.getCameraMatrix(cam.x, cam.y, 0));
			spriteTexture[0].bind();
			playerVAO.fullRender();

			int[] nearesTTOCheck = new int[4];
			nearesTTOCheck[0] = Util.numVal(currentLevel.charAtPos(cam.x + 15f, cam.y - 15f));
			nearesTTOCheck[1] = Util.numVal(currentLevel.charAtPos(cam.x + 15f, cam.y + 15f));
			nearesTTOCheck[2] = Util.numVal(currentLevel.charAtPos(cam.x - 15f, cam.y - 15f));
			nearesTTOCheck[3] = Util.numVal(currentLevel.charAtPos(cam.x - 15f, cam.y + 15f));
			int ttoOnInd = -1;
			for (int i:nearesTTOCheck) {
				if (i >= 0) {

					ttoOnInd = i;
				}
			}
			if (ttoOnInd >= 0) {
				ttoguiShader.enable();
				ttoguiShader.shaders[0].uniforms[0].set(aspectRatio);

				ttoGUIButtonShader.enable();
				ttoGUIButtonShader.shaders[0].uniforms[0].set(aspectRatio);
				ttoGUIButton.sprite.bind();
				ttoGUIButton.vao.prepareRender();
				float leftOffset = 0.025f * (float) currentLevel.levelseries.size();
				//Arrow key switching in the tto
				if(UserControls.leftArrowPress(window)){
					if(ttoSelector-1 >= 0) {
						ttoSelector--;
					}
				}
				if(UserControls.rightArrowPress(window)){
					if(ttoSelector+1 < currentLevel.levelseries.size()) {
						ttoSelector++;
					}
				}
				//regular switching
				for (int i = 0; i < currentLevel.levelseries.size(); ++i) {
					float floati = (float) i;
					//float x = floati * 0.2f + window.cursorXFloat * aspectRatio;
					ttoGUIButtonShader.shaders[0].uniforms[1].set(-leftOffset + 0.1f * floati - ttoGUIButton.width/2);
					if (!currentLevel.timeZonePossibilities.get(ttoOnInd)[i]) {
						ttoGUIButtonShader.shaders[2].uniforms[0].set(0f);
					} else if (currentLevel.currentTZ == i) {
						ttoGUIButtonShader.shaders[2].uniforms[0].set(2f);
					} else if (ttoGUIButton.hoverCheck((window.cursorXFloat - floati * 0.1f + leftOffset + ttoGUIButton.width/2)/aspectRatio, window.cursorYFloat) || ttoSelector == i) {
						if(ttoGUIButton.hoverCheck((window.cursorXFloat - floati * 0.1f + leftOffset + ttoGUIButton.width/2)/aspectRatio, window.cursorYFloat)) {
							ttoSelector = i;
							if (UserControls.leftMB(window)) {
								currentLevel.currentTZ = i;
								++timeTravelFrames;
							}
						}
						if(ttoSelector == i){
							if(UserControls.enter(window)){
								currentLevel.currentTZ = i;
								++timeTravelFrames;
							}
							ttoGUIButtonShader.shaders[2].uniforms[0].set(3f);
						}
					} else {
						ttoGUIButtonShader.shaders[2].uniforms[0].set(1f);
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
			arrpossibilities[0] = Util.numVal(currentLevel.charAtPos(cam.x - spriteSize, cam.y));
			arrpossibilities[1] = Util.numVal(currentLevel.charAtPos(cam.x + spriteSize, cam.y));
			arrpossibilities[2] = Util.numVal(currentLevel.charAtPos(cam.x, cam.y + spriteSize));
			arrpossibilities[3] = Util.numVal(currentLevel.charAtPos(cam.x, cam.y - spriteSize));
			arrpossibilities[4] = Util.numVal(currentLevel.charAtPos(cam.x-spriteSize, cam.y-spriteSize));
			arrpossibilities[5] = Util.numVal(currentLevel.charAtPos(cam.x+spriteSize, cam.y-spriteSize));
			arrpossibilities[6] = Util.numVal(currentLevel.charAtPos(cam.x-spriteSize, cam.y+spriteSize));
			arrpossibilities[7] = Util.numVal(currentLevel.charAtPos(cam.x+spriteSize, cam.y+spriteSize));
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
            	if (nextLevel(1)) {
            		return true;
				}
            }
            if (CamCollision.isColliding(cam.x, cam.y, 1, currentLevel.levelseries.get(currentLevel.currentTZ),'k'))
            {
                inv.add(replaceTile(cam.x, cam.y, ' '));
            }


			int gateCheckXIndex = (int)((cam.x + Util.getSign(m.getHSpeed())*20f)/50f);
			int gateCheckYIndex = (int)((cam.y + Util.getSign(m.getVSpeed())*20f)/50f);
			if(currentLevel.charAtPos(gateCheckXIndex, gateCheckYIndex) == 'l'){
				
                if(inv.check('k')){
                    replaceTile(gateCheckXIndex, gateCheckYIndex, ' ');
                    inv.getAndRemove('k');
                }
            }
            inventoryShader.enable();
			inventoryShader.shaders[0].uniforms[0].set(aspectRatio);
            inventoryObjectVAO.prepareRender();
            for (int i = 0; i < inv.getSize(); ++i) {
				inventoryShader.shaders[0].uniforms[1].set((float) i * 0.1f);
				if (inv.get(i) == 'k') {
					keyTexture.bind();
					inventoryObjectVAO.draw();
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


	public static void main(String[] args) {
		main = new MainView();
	}

	@Override
	public String getName() {
		return "Time Travel Puzzle game";
	}
}
