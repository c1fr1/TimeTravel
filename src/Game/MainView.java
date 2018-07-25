package Game;

import engine.*;
import engine.Entities.Camera;
import engine.OpenGL.*;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
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

	public Texture ttoGUI;
	public Texture[] spriteTexture;
	
	public VAO ttoGUIVAO;
	public VAO playerVAO;

	public Texture pauseGUI;
	public VAO pauseGUIVAO;

	public VAO screenVAO;

	public FBO mainFBO;

	public boolean pause = false;
	public boolean cooldown = false;
	
	public int framesPaused;

	public int timeTravelFrames = 0;
	
	public int animationFrameCounter = 0;
	
	public long lastTime = System.nanoTime();

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
		playerVAO = new VAO(-15, -15f, 30f, 30f);
		
		spriteTexture = new Texture[4];//down left right up;
		spriteTexture[0] = new Texture("res/future-wall.png");
		spriteTexture[1] = new Texture("res/sprite-left.png");
		spriteTexture[2] = new Texture("res/sprite-right.png");
		spriteTexture[3] = new Texture("res/sprite-up.png");

		pauseGUI = new Texture("res/timeTravelGUI.png");
		pauseGUIVAO = new VAO(-0.5f, -0.125f, 1f, 0.25f);

		textureShader = new ShaderProgram("textureShaders");
		pauseShader = new ShaderProgram("pauseShaders");
		ttoguiShader = new ShaderProgram("ttoGUIShader");
		travelShader = new ShaderProgram("travelShaders");
		mainFBO = new FBO(new Texture(window.getWidth(), window.getHeight()));
		screenVAO = new VAO(-1f, -1f, 2f, 2f);

		cam.x = currentLevel.ystart[currentLevel.currentTZ] * 50;
		cam.y = currentLevel.xstart[currentLevel.currentTZ] * 50;
	}
	
	@Override
	public boolean loop() {
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
			pauseGUI.bind();
			pauseGUIVAO.fullRender();
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

			++timeTravelFrames;
			if (timeTravelFrames >= 50) {
				timeTravelFrames = 0;
			}
		}
		//run game
		else {
			mainFBO.prepareForTexture();
			long time = System.nanoTime();
			float delta_time = ((float)(time - lastTime) / 1000000f);


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
				vSpeed *= Math.sqrt(2) / 2;
			}
			if (vSpeed != 0) {
				hSpeed *= Math.sqrt(2) / 2;
			}
			//if(CamCollision.checkCollision(cam.x,cam.y, hSpeed, vSpeed, currentLevel.levelseries.get(currentLevel.currentTZ)) != '#'){
			cam.x = CamCollision.getMoveX(cam.x,cam.y, hSpeed, vSpeed, currentLevel.levelseries.get(currentLevel.currentTZ), '#');
			cam.y = CamCollision.getMoveY(cam.x,cam.y, hSpeed, vSpeed, currentLevel.levelseries.get(currentLevel.currentTZ), '#');
            //}


			LevelBase.levelProgram.enable();
			LevelBase.levelProgram.shaders[0].uniforms[0].set(cam.getCameraMatrix(cam.x, cam.y, 0));
			spriteTexture[0].bind();
			playerVAO.fullRender();

			//render tto gui if the player is on the tto
			if (CamCollision.checkCollision(cam.x, cam.y, hSpeed, vSpeed, currentLevel.levelseries.get(currentLevel.currentTZ)) == 't') {
				ttoguiShader.enable();
				ttoguiShader.shaders[0].uniforms[0].set(aspectRatio);
				ttoguiShader.shaders[2].uniforms[0].set(-1f);
				if (window.cursorXFloat * aspectRatio > -0.25 && window.cursorXFloat * aspectRatio < -0.2) {
					if (window.cursorYFloat > 0.125 && window.cursorYFloat < 0.375) {
						if (window.mouseButtons[GLFW_MOUSE_BUTTON_LEFT] > 0) {
							if (currentLevel.currentTZ > 0) {
								if (timeTravelFrames == 0) {
									--currentLevel.currentTZ;
								}
								++timeTravelFrames;
							}
							//time travel backward
						}
						ttoguiShader.shaders[2].uniforms[0].set(0.15f);
					}
				}
				if (window.cursorXFloat * aspectRatio > 0.2 && window.cursorXFloat * aspectRatio < 0.25) {
					if (window.cursorYFloat > 0.125 && window.cursorYFloat < 0.375) {
						if (window.mouseButtons[GLFW_MOUSE_BUTTON_LEFT] > 0) {
							if (currentLevel.currentTZ + 1 < currentLevel.levelseries.size()) {
								if (timeTravelFrames == 0) {
									++currentLevel.currentTZ;
								}
								++timeTravelFrames;
							}
							//time travel forward
						}
						ttoguiShader.shaders[2].uniforms[0].set(0.85f);
					}
				}
				//ttoguiShader.shaders[2].uniforms[0].set(0.2f);
				if (timeTravelFrames == 0) {
					ttoGUI.bind();
					ttoGUIVAO.fullRender();
				}
				++animationFrameCounter;
			}else {
				--animationFrameCounter;
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
