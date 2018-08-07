package Game.Views;

import Game.Buttons.ShaderOptimizedButton;
import Game.Buttons.SpriteButton;
import Game.UserControls;
import engine.EnigView;
import engine.OpenGL.*;

import static Game.Views.MainView.screenVAO;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class PauseView extends EnigView {
	public int menuSelect = -1;
	public int framesPaused = 0;
	public Texture background;
	
	public static ShaderProgram pauseShader;
	
	public static ShaderOptimizedButton menu;
	public static ShaderOptimizedButton cont;
	public static ShaderOptimizedButton restart;
	
	public boolean shouldRestart = false;
	
	public PauseView(Texture background) {
		super(EnigWindow.mainWindow);
		this.background = background;
		runLoop();
	}
	
	@Override
	public void setup() {
		if (menu == null) {
			pauseShader = new ShaderProgram("pauseShaders");
			menu = new ShaderOptimizedButton(-0.5f, -0.725f, 1f, 0.25f, "res/menu/mainMenu.png", MainView.aspectRatio);
			cont = new ShaderOptimizedButton(-0.5f, 0.525f, 1f, 0.25f, "res/menu/continueButton.png", MainView.aspectRatio);
			restart = new ShaderOptimizedButton(-0.5f, -0.125f, 1f, 0.25f, "res/menu/restart.png", MainView.aspectRatio);
		}
	}
	
	@Override
	public boolean loop() {
		for (int i:UserControls.pause) {
			if (window.keys[i] == 1) {
				window.keys[i] = 2;
				return true;
			}
		}
		if(menuSelect > 0) {
			if (UserControls.upArrowPress(window)) {
				menuSelect--;
			}
		}
		if(menuSelect < 2){
			if(UserControls.downArrowPress(window)){
				menuSelect++;
			}
		}
		if(menuSelect == -1){
			if(UserControls.upArrowPress(window)){
				menuSelect = 2;
			}
		}
		++framesPaused;
		float scalar = 1-((float)framesPaused/50f);
		if (scalar < 0.5f) {
			scalar = 0.5f;
		}
		
		
		FBO.prepareDefaultRender();
		EnigWindow.checkGLError();
		pauseShader.enable();
		EnigWindow.checkGLError();
		pauseShader.shaders[2].uniforms[0].set(scalar);
		background.bind();
		screenVAO.fullRender();
		
		SpriteButton.shader.enable();
		SpriteButton.shader.shaders[0].uniforms[0].set(MainView.aspectRatio);
		
		if (menu.render(window.cursorXFloat,window.cursorYFloat)) {
			menuSelect = 2;
			if (window.mouseButtons[GLFW_MOUSE_BUTTON_LEFT] == 1) {
				new MainMenu(window);
				ShaderProgram.disable();
				return true;
			}
		}
		
		if (restart.render(window.cursorXFloat,window.cursorYFloat)) {
			menuSelect = 1;
			if (window.mouseButtons[GLFW_MOUSE_BUTTON_LEFT] == 1) {
				shouldRestart = true;
				ShaderProgram.disable();
				return true;//nextLevel(0)
			}
		}
		
		if (cont.render(window.cursorXFloat, window.cursorYFloat)) {
			menuSelect = 0;
			if (window.mouseButtons[GLFW_MOUSE_BUTTON_LEFT] == 1) {
				menuSelect = -1;
				framesPaused = 0;
				ShaderProgram.disable();
				return true;
			}
		}
		
		if(!cont.render(window.cursorXFloat, window.cursorYFloat) && !menu.render(window.cursorXFloat, window.cursorYFloat) && !restart.render(window.cursorXFloat, window.cursorYFloat)){
			if(menuSelect == 0){
				cont.render(window.cursorXFloat, window.cursorYFloat, true);
				if(UserControls.enter(window)){
					ShaderProgram.disable();
					return true;
				}
			}
			if(menuSelect == 1){
				restart.render(window.cursorXFloat, window.cursorYFloat, true);
				if(UserControls.enter(window)){
					shouldRestart = true;
					ShaderProgram.disable();
					return true;
				}
			}
			if(menuSelect == 2){
				menu.render(window.cursorXFloat, window.cursorYFloat, true);
				if(UserControls.enter(window)){
					menuSelect = -1;
					new MainMenu(window);
					ShaderProgram.disable();
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public String getName() {
		return null;
	}
}
