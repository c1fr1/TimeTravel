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
	
	public LevelBase level1;
	
	@Override
	public void setup() {
		//set variables here
		level1 = new LevelBase("res/Levels/Level1");
		cam = new Camera((float)window.getWidth(), (float)window.getHeight());
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
		level1.render(cam);
		float vSpeed = 0;
		float hSpeed = 0;

		if (UserControls.forward(window)) {
			vSpeed += delta_time/3;
		}
		if (UserControls.backward(window)) {
			vSpeed -= delta_time/3;
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
		cam.y += vSpeed;
		cam.x += hSpeed;
		if (UserControls.quit(window)) {
			return true;
		}

		return false;
	}
	
	public static void main(String[] args) {
		main = new MainView();
	}
	
	@Override
	public String getName() {
		return "Time Travel Puzzle game";
	}
}
