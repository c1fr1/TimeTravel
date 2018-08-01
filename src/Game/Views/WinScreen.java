package Game.Views;

import Game.SpriteButton;
import engine.EnigView;
import engine.OpenGL.*;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ENTER;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;

public class WinScreen extends EnigView {
	public static VAO fullScreen;
	public static Texture wonScreenTaxture;
	public static VAO wonSreamVEO;
	public static SpriteButton continueButton;
	public Texture t;
	public static ShaderProgram normieShader;
	public static ShaderProgram aespectShader;
	public WinScreen(Texture background, float aespectionrations) {
		super(EnigWindow.mainWindow);
		if (wonScreenTaxture == null) {
			wonScreenTaxture = new Texture("res/menu/winScreen.png");
			wonSreamVEO = new VAO(aespectionrations * -0.5f, -0.5f, aespectionrations, 1f);
		}
		t = background;
		window = EnigWindow.mainWindow;
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
		if (EnigWindow.mainWindow.keys[GLFW_KEY_ENTER] > 0 || (continueButton.render(EnigWindow.mainWindow.cursorXFloat, EnigWindow.mainWindow.cursorYFloat) && EnigWindow.mainWindow.mouseButtons[GLFW_MOUSE_BUTTON_LEFT] > 0)) {
			return true;
		}
		return false;
	}
	
	@Override
	public String getName() {
		return "adfad";
	}
}
