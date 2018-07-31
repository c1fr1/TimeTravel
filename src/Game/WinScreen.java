package Game;

import engine.EnigView;
import engine.OpenGL.EnigWindow;
import engine.OpenGL.Texture;
import engine.OpenGL.VAO;

import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;

public class WinScreen extends EnigView {
	public static VAO fullScreen;
	public static Texture wonScreenTaxture;
	public static VAO wonSreamVEO;
	public Texture t;
	public WinScreen(Texture background, float aespectionrations) {
		if (wonScreenTaxture == null) {
			wonScreenTaxture = new Texture("winScreen.png");
			wonSreamVEO = new VAO(aespectionrations * -0.5f, -0.5f, aespectionrations, 1f);
		}
		t = background;
		window = EnigWindow.mainWindow;
		while ( !glfwWindowShouldClose(EnigWindow.mainWindow.id) ) {
			++framesSinceStart;
			if (loop()) {
				cleanUp();
				break;
			}
			window.update();
			cleanUp();
		}
	}
	@Override
	public void setup() {
	
	}
	
	@Override
	public boolean loop() {
		t.bind();
		fullScreen.fullRender();
		return false;
	}
	
	@Override
	public String getName() {
		return "adfad";
	}
}
