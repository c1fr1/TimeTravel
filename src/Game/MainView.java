package Game;

import engine.*;
import engine.Entities.Camera;
import engine.OpenGL.*;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static org.lwjgl.glfw.GLFW.glfwSetWindowAspectRatio;
import static org.lwjgl.opengl.GL11.*;

public class MainView extends EnigView {
	public static MainView main;
	
	public Camera cam;
	
	//project variables

	public LevelBase currentLevel;
	
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
		
		glDisable(GL_DEPTH_TEST);
		//needs to be generalized to use level selected - level path is a parameter
		currentLevel = new LevelBase("res/Levels/Level1");
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

        cont = new SpriteButton(-0.5f,0.525f,1f,0.4f,"res/menu/continue.png");
		restart = new SpriteButton(-0.39f, -0.125f, .78f, 0.55f,"res/menu/restart.png");
		menu = new SpriteButton(-0.71f, -0.725f, 1.42f, 0.25f,"res/menu/menu.png");

		textureShader = new ShaderProgram("textureShaders");
		pauseShader = new ShaderProgram("pauseShaders");
		ttoguiShader = new ShaderProgram("ttoGUIShader");
		travelShader = new ShaderProgram("travelShaders");
		
		ttoGUIButton = new SpriteButton(-0.06f, 0.4f, 0.12f, 0.12f, "res/ttoguiButton.png");
		ttoGUIButton.shader = new ShaderProgram("ttoGUIButtonShader");
		
		mainFBO = new FBO(new Texture(window.getWidth(), window.getHeight()));
		screenVAO = new VAO(-1f, -1f, 2f, 2f);

		cam.x = currentLevel.ystart[currentLevel.currentTZ] * 50;
		cam.y = currentLevel.xstart[currentLevel.currentTZ] * 50;
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
			}
			else
			{
				menu.setPath("res/menu/menu.png");
			}
			//restart highlighting
			//continue highlighting

            //window.cursorxfloat
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

			//if(CamCollision.checkCollision(cam.x,cam.y, hSpeed, vSpeed, currentLevel.levelseries.get(currentLevel.currentTZ)) != '#' || CamCollision.checkCollision(cam.x,cam.y, hSpeed, vSpeed, currentLevel.levelseries.get(currentLevel.currentTZ)) != '_'){
				cam.x = CamCollision.getMoveX(cam.x + getSign(hSpeed)*15f, cam.y, hSpeed, vSpeed, currentLevel.levelseries.get(currentLevel.currentTZ), '#', '_') - getSign(hSpeed)*15f;
				cam.y = CamCollision.getMoveY(cam.x, cam.y + getSign(vSpeed)*15f, hSpeed, vSpeed, currentLevel.levelseries.get(currentLevel.currentTZ), '#', '_') - getSign(vSpeed)*15f;
			//}


			LevelBase.levelProgram.enable();
			LevelBase.levelProgram.shaders[0].uniforms[0].set(cam.getCameraMatrix(cam.x, cam.y, 0));
			spriteTexture[0].bind();
			playerVAO.fullRender();


            //CamCollision.checkCollision(cam.x, cam.y, hSpeed, vSpeed, currentLevel.levelseries.get(currentLevel.currentTZ)) == 't'
			//render tto gui if the player is on the tto
			if (CamCollision.checkCollision(cam.x - getSign(hSpeed)*20f, cam.y + getSign(vSpeed)*20f, hSpeed, vSpeed, currentLevel.levelseries.get(currentLevel.currentTZ)) == 't' ||
                	CamCollision.checkCollision(cam.x - getSign(hSpeed)*20f, cam.y - getSign(vSpeed)*20f, hSpeed, vSpeed, currentLevel.levelseries.get(currentLevel.currentTZ)) == 't' ||
                	CamCollision.checkCollision(cam.x + getSign(hSpeed)*20f, cam.y + getSign(vSpeed)*20f, hSpeed, vSpeed, currentLevel.levelseries.get(currentLevel.currentTZ)) == 't' ||
                	CamCollision.checkCollision(cam.x + getSign(hSpeed)*20f, cam.y - getSign(vSpeed)*20f, hSpeed, vSpeed, currentLevel.levelseries.get(currentLevel.currentTZ)) == 't') {
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
					ttoGUIButton.shader.shaders[0].uniforms[1].set(-leftOffset + 0.1f * floati);
					if (false) {
						ttoGUIButton.shader.shaders[2].uniforms[0].set(0f);
					} else if (currentLevel.currentTZ == i) {
						ttoGUIButton.shader.shaders[2].uniforms[0].set(2f);
					} else if (ttoGUIButton.hoverCheck(window.cursorXFloat - floati * 0.1f + leftOffset, window.cursorYFloat)) {
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
			if (    CamCollision.checkCollision(cam.x-spriteSize, cam.y, 0, 0, currentLevel.levelseries.get(currentLevel.currentTZ)) == 't' ||
					CamCollision.checkCollision(cam.x+spriteSize, cam.y, 0, 0, currentLevel.levelseries.get(currentLevel.currentTZ)) == 't' ||
					CamCollision.checkCollision(cam.x, cam.y+spriteSize, 0, 0, currentLevel.levelseries.get(currentLevel.currentTZ)) == 't' ||
					CamCollision.checkCollision(cam.x, cam.y-spriteSize, 0, 0, currentLevel.levelseries.get(currentLevel.currentTZ)) == 't' ||
					
					CamCollision.checkCollision(cam.x-spriteSize, cam.y-spriteSize, 0, 0, currentLevel.levelseries.get(currentLevel.currentTZ)) == 't' ||
					CamCollision.checkCollision(cam.x+spriteSize, cam.y-spriteSize, 0, 0, currentLevel.levelseries.get(currentLevel.currentTZ)) == 't' ||
					CamCollision.checkCollision(cam.x-spriteSize, cam.y+spriteSize, 0, 0, currentLevel.levelseries.get(currentLevel.currentTZ)) == 't' ||
					CamCollision.checkCollision(cam.x+spriteSize, cam.y+spriteSize, 0, 0, currentLevel.levelseries.get(currentLevel.currentTZ)) == 't' ||
					
					CamCollision.checkCollision(cam.x, cam.y, 0, 0, currentLevel.levelseries.get(currentLevel.currentTZ)) == 't') {
				animationFrameCounter+=0.5*delta_time*0.03;
			} else {
				animationFrameCounter-=delta_time*0.03;
			}
            if (animationFrameCounter < 0) {
                animationFrameCounter = 0;
            }
            if (animationFrameCounter > 8) {
                animationFrameCounter = 8;
            }
            LevelBase.updateTTO(Math.round(animationFrameCounter));


			guiShader.enable();
			guiShader.shaders[0].uniforms[0].set(aspectRatio);
			FBO.prepareDefaultRender();
			textureShader.enable();
			mainFBO.getBoundTexture().bind();
			screenVAO.fullRender();
		}

		return false;
	}
	
	public static void main(String[] args) {
		main = new MainView();
	}

	public int getSign(float thing){
		if(thing >= 0){
			return 1;
		} else {
			return -1;
		}
	}

	@Override
	public String getName() {
		return "Time Travel Puzzle game";
	}
}
