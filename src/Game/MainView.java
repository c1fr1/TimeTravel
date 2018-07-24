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
	
	public Texture ttoGUI;
	public VAO ttoGUIVAO;

	public Texture pauseGUI;
	public VAO pauseGUIVAO;

	boolean pause = false;

	@Override
	public void setup() {
		//set variables here
		level1 = new LevelBase("res/Levels/Level1");
		cam = new Camera((float)window.getWidth(), (float)window.getHeight());
		guiShader = new ShaderProgram("guiShader");
		ttoGUI = new Texture("res/timeTravelGUI.png");
		ttoGUIVAO = new VAO(-0.5f, -0.125f, 1f, 0.25f);

		pauseGUI = new Texture("res/timeTravelGUI.png");
		pauseGUIVAO = new VAO(-0.5f, -0.125f, 1f, 0.25f);

		cam.x = level1.ystart[currentTZ] * 50;
		cam.y = level1.xstart[currentTZ] * 50;
	}
	
	@Override
	public boolean loop() {

		long time = System.nanoTime();
		int delta_time = (int) ((time - window.lastTime) / 1000000);
		//window.lastTime = time;

		//game here
		glEnable(GL_DEPTH_TEST);
		FBO.prepareDefaultRender();
		glDisable(GL_DEPTH_TEST);
		level1.render(cam, currentTZ);
		float vSpeed = 0;
		float hSpeed = 0;

		if (UserControls.forward(window)) {
			vSpeed -= delta_time/3;
		}
		if (UserControls.backward(window)) {
			vSpeed += delta_time/3;
		}
		if (UserControls.left(window)) {
			hSpeed -= delta_time/3;
		}
		if (UserControls.right(window)) {
			hSpeed += delta_time/3;
		}
		if(hSpeed != 0){
			vSpeed *= Math.sqrt(2)/2;
		}
		if(vSpeed != 0){
			hSpeed *= Math.sqrt(2)/2;
		}
		if(new CamCollision().collisionV(cam.x+(getSign(hSpeed)*15)+(hSpeed), cam.y+(getSign(vSpeed)*15)+(vSpeed), level1, vSpeed, currentTZ) != '#') {
            cam.y += vSpeed;
        }
        if(new CamCollision().collisionH(cam.x+(getSign(hSpeed)*15)+(hSpeed), cam.y+(getSign(vSpeed)*15)+(vSpeed), level1, hSpeed, currentTZ) != '#') {
            cam.x += hSpeed;
        }
        if (new CamCollision().collisionH(cam.x, cam.y, level1, 0, currentTZ) == 't') {
			guiShader.enable();
			guiShader.shaders[0].uniforms[0].set((float)window.getHeight()/(float)window.getWidth());
        	ttoGUI.bind();
        	ttoGUIVAO.fullRender();
		}
		if(UserControls.pause(window)){
			pause = !pause;
		}
		if(pause){
			guiShader.enable();
			guiShader.shaders[0].uniforms[0].set((float)window.getHeight()/(float)window.getWidth());
			pauseGUI.bind();
			pauseGUIVAO.fullRender();
		}
		/*
		if (UserControls.quit(window)) {
			return true;
		}
		*/

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
