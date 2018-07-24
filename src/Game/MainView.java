package Game;

import engine.*;
import engine.Entities.Camera;
import engine.Entities.GameObject;
import engine.Entities.Player;
import engine.OpenAL.Sound;
import engine.OpenAL.SoundSource;
import engine.OpenGL.*;
import engine.Platform.Box3d;
import engine.Platform.ModelPlatform;
import engine.Platform.PlatformSegment;

import java.util.logging.Level;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_K;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_L;
import static org.lwjgl.opengl.GL11.*;

public class MainView extends EnigView {
	public static MainView main;
	
	public Camera cam;
	
	//project variables
	int currentTZ = 0;

	public LevelBase level1;
	
	public ShaderProgram guiShader;
	public ShaderProgram textureShader;
	
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

	@Override
	public void setup() {
		//set variables here
		level1 = new LevelBase("res/Levels/Level1");
		cam = new Camera((float)window.getWidth(), (float)window.getHeight());
		guiShader = new ShaderProgram("guiShader");
		ttoGUI = new Texture("res/timeTravelGUI.png");
		ttoGUIVAO = new VAO(-0.5f, -0.125f, 1f, 0.25f);
		playerVAO = new VAO(-45, 0f, 30f, 30f);
		
		spriteTexture = new Texture[4];//down left right up;
		spriteTexture[0] = new Texture("res/future-wall.png");
		spriteTexture[1] = new Texture("res/sprite-left.png");
		spriteTexture[2] = new Texture("res/sprite-right.png");
		spriteTexture[3] = new Texture("res/sprite-up.png");

		pauseGUI = new Texture("res/timeTravelGUI.png");
		pauseGUIVAO = new VAO(-0.5f, -0.125f, 1f, 0.25f);
		
		textureShader = new ShaderProgram("textureShaders");
		mainFBO = new FBO(new Texture(window.getWidth(), window.getHeight()));
		screenVAO = new VAO(-1f, -1f, 2f, 2f);

		cam.x = level1.ystart[currentTZ] * 50;
		cam.y = level1.xstart[currentTZ] * 50;
	}
	
	@Override
	public boolean loop() {
		if(UserControls.pause(window)){
			if(!cooldown){
				pause = !pause;
			}
			cooldown = true;
			
		} else if(!UserControls.pause(window)){
			cooldown = false;
		}
		if(pause){
			guiShader.enable();
			guiShader.shaders[0].uniforms[0].set((float)window.getHeight()/(float)window.getWidth());
			pauseGUI.bind();
			pauseGUIVAO.fullRender();
		}else {
			long time = System.nanoTime();
			int delta_time = (int) ((time - window.lastTime) / 1000000);
			//window.lastTime = time;
			
			//game here
			glEnable(GL_DEPTH_TEST);
			mainFBO.prepareForTexture();
			glDisable(GL_DEPTH_TEST);
			level1.render(cam, currentTZ);
			float vSpeed = 0;
			float hSpeed = 0;
			
			if (UserControls.forward(window)) {
				vSpeed -= delta_time / 3;
			}
			if (UserControls.backward(window)) {
				vSpeed += delta_time / 3;
			}
			if (UserControls.left(window)) {
				hSpeed -= delta_time / 3;
			}
			if (UserControls.right(window)) {
				hSpeed += delta_time / 3;
			}
			if (hSpeed != 0) {
				vSpeed *= Math.sqrt(2) / 2;
			}
			if (vSpeed != 0) {
				hSpeed *= Math.sqrt(2) / 2;
			}
			if (new CamCollision().collisionV(cam.x + (getSign(hSpeed) * 15) + (hSpeed), cam.y + (getSign(vSpeed) * 15) + (vSpeed), level1, vSpeed, currentTZ) != '#') {
				cam.y += vSpeed;
			}
			if (CamCollision.collisionH(cam.x + (getSign(hSpeed) * 15) + (hSpeed), cam.y + (getSign(vSpeed) * 15) + (vSpeed), level1, hSpeed, currentTZ) != '#') {
				cam.x += hSpeed;
			}
			
			LevelBase.levelProgram.enable();
			LevelBase.levelProgram.shaders[0].uniforms[0].set(cam.getCameraMatrix(cam.x, cam.y, 0));
			spriteTexture[0].bind();
			playerVAO.fullRender();
			
			guiShader.enable();
			guiShader.shaders[0].uniforms[0].set((float) window.getHeight() / (float) window.getWidth());
			//render tto gui if the player is on the tto
			if (new CamCollision().collisionH(cam.x, cam.y, level1, 0, currentTZ) == 't') {
				ttoGUI.bind();
				ttoGUIVAO.fullRender();
			}
		}
		
		FBO.prepareDefaultRender();
		textureShader.enable();
		mainFBO.getBoundTexture().bind();
		screenVAO.fullRender();
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
