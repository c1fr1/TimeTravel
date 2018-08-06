package Game.Views;

import Game.Buttons.SpriteButton;
import Game.StringRenderer;
import engine.EnigView;
import engine.OpenGL.*;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ENTER;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class WinScreen extends EnigView {
	public static VAO fullScreen;
	public static Texture wonScreenTaxture;
	public static VAO wonSreamVEO;
	public Texture t;
	public static ShaderProgram normieShader;
	public static ShaderProgram aespectShader;
	public StringRenderer jumpCounter;
	public WinScreen(Texture background, float aespectionrations) {
		super(EnigWindow.mainWindow);
		if (wonScreenTaxture == null) {
			wonScreenTaxture = new Texture("res/menu/levelComplete.png");
			wonSreamVEO = new VAO(aespectionrations * -0.5f, -0.5f, aespectionrations, 1f);
		}
		t = background;
		window = EnigWindow.mainWindow;
		jumpCounter = new StringRenderer(window.getWidth(), window.getHeight(), 55, 0f, 60f);
		runLoop();
	}
	@Override
	public void setup() {
	}
	
	@Override
	public boolean loop() {
		FBO.prepareDefaultRender();
		normieShader.enable();
		t.bind();
		fullScreen.fullRender();
		wonScreenTaxture.bind();
		wonSreamVEO.fullRender();
		SpriteButton.shader.enable();
		SpriteButton.shader.shaders[0].uniforms[0].set((float) EnigWindow.mainWindow.getHeight() / (float) EnigWindow.mainWindow.getWidth());
		if (EnigWindow.mainWindow.keys[GLFW_KEY_ENTER] > 0 || EnigWindow.mainWindow.mouseButtons[GLFW_MOUSE_BUTTON_LEFT] > 0) {
			MainView.jumps = 0;
			return true;
		}
		jumpCounter.renderNum("travels " + MainView.jumps);
		return false;
	}
	
	@Override
	public String getName() {
		return "adfad";
	}
}
