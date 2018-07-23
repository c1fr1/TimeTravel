package Game;

import engine.*;
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
	
	//project variables
	
	public LevelBase level1;
	
	@Override
	public void setup() {
		//set variables here
		EnigWindow.mainWindow.toggleCursorInput();
		level1 = new LevelBase("####" +
				"#  #" +
				"#  #" +
				"####", 4, 4);
	}
	
	@Override
	public boolean loop() {
		//game here
		glEnable(GL_DEPTH_TEST);
		FBO.prepareDefaultRender();
		glDisable(GL_DEPTH_TEST);
		level1.render();
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
		return "Enignets Game";
	}
}
