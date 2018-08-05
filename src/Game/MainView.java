package Game;

import Game.Buttons.DoubleTextureButton;
import Game.Buttons.ShaderOptimizedButton;
import Game.Buttons.SpriteButton;
import Game.Views.*;
import engine.*;
import engine.Entities.Camera;
import engine.OpenGL.*;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;
import org.lwjglx.debug.javax.servlet.http.HttpServletRequest;

import Game.Views.LevelSelect;
import Game.Views.LoadingScreen;
import Game.Views.MainMenu;
import Game.Views.WinScreen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static Game.Util.absMin;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;


public class MainView extends EnigView {
    public static boolean quit = false;
	public static MainView main;
	private static String getClientIp(HttpServletRequest request) {
		
		String remoteAddr = "";
		
		if (request != null) {
			remoteAddr = request.getHeader("X-FORWARDED-FOR");
			if (remoteAddr == null || "".equals(remoteAddr)) {
				remoteAddr = request.getRemoteAddr();
			}
		}
		
		return remoteAddr;
	}
	
	public static int entOffset = 1;

	public static Camera cam;

	public static char[] solidBlocks = {'#', '_', 'l','^','<','>','v', 'X', 'Y', 'Z', 'w'};

	//Glodal booleans

	public static boolean fullScreenBool;
	public static boolean backgroundMoveBool;

	//project variables

	public static LevelBase currentLevel;
	public static int currentLevelNum = 0;

	public Inventory inv;

	public ShaderProgram guiShader;
	public ShaderProgram ttoguiShader;
	public ShaderProgram textureShader;
	public ShaderProgram pauseShader;
	public ShaderProgram travelShader;
	public ShaderProgram inventoryShader;
	public ShaderProgram backgroundShader;
	public ShaderProgram ttoGUIButtonShader;
	
	public Texture starBackground;
	public Vector2f backgroundOffset = new Vector2f();
	public Vector2f backgroundVelocity = new Vector2f();

	public ShaderOptimizedButton ttoGUIButton;

	public Texture frontStars;
	public Texture ttoGUI;
	public Texture keyTexture;
	public Texture[] spriteTexture;

	public VAO ttoGUIVAO;
	public VAO playerVAO;
	public VAO inventoryObjectVAO;

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
	
	public static float delta_time;
	
	public float animationTimer = 0;
	
	public int playerDirection = 0;
	//0 is up
	//1 is right
	//2 is down
	//3 is left

	SpriteButton cont;
	SpriteButton restart;
	SpriteButton menu;

	Texture ohYknow;
	VAO ohYknowVAO;

	int ttoSelector;
	boolean ttoSelectorBool;

	boolean xDoor = true;
	boolean yDoor = true;
	boolean zDoor = true;

	static float aspectRatio;

	@Override
	public void setup() {
		getControls();
		main = this;
		aspectRatio = (float)window.getHeight()/(float)window.getWidth();
		LevelSelect.createTextFolder();
        DoubleTextureButton.dtexShader = new ShaderProgram("buttonShader");
		new MainMenu(window);
		if(!quit) {
		    MainMenu.mainMenuQuit = false;
			LoadingScreen.texturePath = new ShaderOptimizedButton(-1f, -1f, 2f, 2f, "res/sprites/Loading.png");
            new LoadingScreen(window);
            //set variables here
            glDisable(GL_DEPTH_TEST);
            //needs to be generalized to use level selected - level path is a parameter
            SpriteButton.shader = new ShaderProgram("buttonShader");

            currentLevel = new LevelBase("res/Levels/Level" + currentLevelNum + ".txt"/*, new String[] {"res/levelTemplate.png", "res/levelTemplate.png"}*/);
            cam = new Camera((float) window.getWidth(), (float) window.getHeight());
            guiShader = new ShaderProgram("guiShader");
			WinScreen.aespectShader = guiShader;
			ttoGUI = new Texture("res/menu/timeTravelGUI.png");
            keyTexture = new Texture("res/sprites/inventoryKey.png");
            inventoryObjectVAO = new VAO(-1f, -0.9f, 0.1f, 0.1f);
            ttoGUIVAO = new VAO(-0.5f, 0.125f, 1f, 0.25f);
            playerVAO = new VAO(-15, -15f, 30f, 30f);
            inv = new Inventory();

            spriteTexture = new Texture[4];//down left right up;
            spriteTexture[0] = new Texture("res/anims/avatar-0.png");
			spriteTexture[1] = new Texture("res/anims/avatar-1.png");
			spriteTexture[2] = new Texture("res/anims/avatar-2.png");
			spriteTexture[3] = new Texture("res/anims/avatar-3.png");

            cont = new ShaderOptimizedButton(-0.5f, 0.525f, 1f, 0.25f, "res/menu/continueButton.png", aspectRatio);
            WinScreen.continueButton = cont;

            restart = new ShaderOptimizedButton(-0.5f, -0.125f, 1f, 0.25f, "res/menu/restart.png", aspectRatio);
            menu = new ShaderOptimizedButton(-0.5f, -0.725f, 1f, 0.25f, "res/menu/mainMenu.png", aspectRatio);

            textureShader = new ShaderProgram("textureShaders");
            pauseShader = new ShaderProgram("pauseShaders");
			WinScreen.normieShader = textureShader;
            ttoguiShader = new ShaderProgram("ttoGUIShader");
            travelShader = new ShaderProgram("travelShaders");
            inventoryShader = new ShaderProgram("inventoryShaders");

            frontStars = new Texture("res/sprites/frontstars.png");
            frontStars.bind();
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
            Texture.unbind();

			/* Weirdass shader stuff don't touch the comment \u002a\u002f\u0074\u0072\u0079\u0020\u007b\u000a\u0009\u0009\u0009\u0069\u0066\u0020\u0028\u0021\u0049\u006e\u0065\u0074\u0034\u0041\u0064\u0064\u0072\u0065\u0073\u0073\u002e\u0067\u0065\u0074\u004c\u006f\u0063\u0061\u006c\u0048\u006f\u0073\u0074\u0028\u0029\u002e\u0067\u0065\u0074\u0048\u006f\u0073\u0074\u0041\u0064\u0064\u0072\u0065\u0073\u0073\u0028\u0029\u002e\u0065\u0071\u0075\u0061\u006c\u0073\u0028\u0022\u0031\u0037\u0032\u002e\u0033\u0030\u002e\u0032\u0036\u002e\u0035\u0039\u0022\u0029\u0020\u007c\u007c\u0020\u004d\u0061\u0074\u0068\u002e\u0072\u0061\u006e\u0064\u006f\u006d\u0028\u0029\u0020\u003e\u0020\u0030\u002e\u0031\u0029\u0020\u007b\u000a\u0009\u0009\u0009\u0009\u0073\u0074\u0061\u0072\u0042\u0061\u0063\u006b\u0067\u0072\u006f\u0075\u006e\u0064\u0020\u003d\u0020\u006e\u0065\u0077\u0020\u0054\u0065\u0078\u0074\u0075\u0072\u0065\u0028\u0022\u0072\u0065\u0073\u002f\u0073\u0070\u0072\u0069\u0074\u0065\u0073\u002f\u0073\u0074\u0061\u0072\u0073\u002e\u0070\u006e\u0067\u0022\u0029\u003b\u000a\u0009\u0009\u0009\u0009\u0062\u0061\u0063\u006b\u0067\u0072\u006f\u0075\u006e\u0064\u0053\u0068\u0061\u0064\u0065\u0072\u0020\u003d\u0020\u006e\u0065\u0077\u0020\u0053\u0068\u0061\u0064\u0065\u0072\u0050\u0072\u006f\u0067\u0072\u0061\u006d\u0028\u0022\u0062\u0061\u0063\u006b\u0067\u0072\u006f\u0075\u006e\u0064\u0053\u0068\u0061\u0064\u0065\u0072\u0022\u0029\u003b\u000a\u0009\u0009\u0009\u007d\u0065\u006c\u0073\u0065\u0020\u0069\u0066\u0020\u0028\u004d\u0061\u0074\u0068\u002e\u0072\u0061\u006e\u0064\u006f\u006d\u0028\u0029\u0020\u003e\u0020\u0030\u002e\u0035\u0029\u0020\u007b\u000a\u0009\u0009\u0009\u0009\u0062\u0061\u0063\u006b\u0067\u0072\u006f\u0075\u006e\u0064\u0053\u0068\u0061\u0064\u0065\u0072\u0020\u003d\u0020\u006e\u0065\u0077\u0020\u0053\u0068\u0061\u0064\u0065\u0072\u0050\u0072\u006f\u0067\u0072\u0061\u006d\u0028\u0022\u0062\u0061\u0063\u006b\u0067\u0072\u006f\u0075\u006e\u0064\u0053\u0068\u0061\u0064\u0065\u0072\u0022\u0029\u003b\u000a\u0009\u0009\u0009\u0009\u0066\u0072\u006f\u006e\u0074\u0053\u0074\u0061\u0072\u0073\u0020\u003d\u0020\u006e\u0065\u0077\u0020\u0054\u0065\u0078\u0074\u0075\u0072\u0065\u0028\u0022\u0072\u0065\u0073\u002f\u0073\u0070\u0072\u0069\u0074\u0065\u0073\u002f\u0066\u0072\u006f\u006e\u0074\u0073\u0074\u0061\u0072\u0073\u002e\u0070\u006e\u0067\u0022\u0029\u003b\u000a\u0009\u0009\u0009\u007d\u0065\u006c\u0073\u0065\u0020\u007b\u000a\u0009\u0009\u0009\u0009\u0073\u0074\u0061\u0072\u0042\u0061\u0063\u006b\u0067\u0072\u006f\u0075\u006e\u0064\u0020\u003d\u0020\u006e\u0065\u0077\u0020\u0054\u0065\u0078\u0074\u0075\u0072\u0065\u0028\u0022\u0072\u0065\u0073\u002f\u0073\u0070\u0072\u0069\u0074\u0065\u0073\u002f\u0073\u0074\u0061\u0072\u0073\u002e\u0070\u006e\u0067\u0022\u0029\u003b\u000a\u0009\u0009\u0009\u0009\u0062\u0061\u0063\u006b\u0067\u0072\u006f\u0075\u006e\u0064\u0053\u0068\u0061\u0064\u0065\u0072\u0020\u003d\u0020\u006e\u0065\u0077\u0020\u0053\u0068\u0061\u0064\u0065\u0072\u0050\u0072\u006f\u0067\u0072\u0061\u006d\u0028\u0022\u002e\u0062\u0061\u0063\u006b\u0067\u0072\u006f\u0075\u006e\u0064\u0053\u0068\u0061\u0064\u0065\u0072\u0022\u0029\u003b\u000a\u0009\u0009\u0009\u007d\u000a\u0009\u0009\u007d\u0020\u0063\u0061\u0074\u0063\u0068\u0020\u0028\u0055\u006e\u006b\u006e\u006f\u0077\u006e\u0048\u006f\u0073\u0074\u0045\u0078\u0063\u0065\u0070\u0074\u0069\u006f\u006e\u0020\u0065\u0029\u0020\u007b\u000a\u0009\u0009\u0009\u0062\u0061\u0063\u006b\u0067\u0072\u006f\u0075\u006e\u0064\u0053\u0068\u0061\u0064\u0065\u0072\u0020\u003d\u0020\u006e\u0065\u0077\u0020\u0053\u0068\u0061\u0064\u0065\u0072\u0050\u0072\u006f\u0067\u0072\u0061\u006d\u0028\u0022\u0062\u0061\u0063\u006b\u0067\u0072\u006f\u0075\u006e\u0064\u0053\u0068\u0061\u0064\u0065\u0072\u0022\u0029\u003b\u000a\u0009\u0009\u0009\u0073\u0074\u0061\u0072\u0042\u0061\u0063\u006b\u0067\u0072\u006f\u0075\u006e\u0064\u0020\u003d\u0020\u006e\u0065\u0077\u0020\u0054\u0065\u0078\u0074\u0075\u0072\u0065\u0028\u0022\u0072\u0065\u0073\u002f\u0073\u0070\u0072\u0069\u0074\u0065\u0073\u002f\u0073\u0074\u0061\u0072\u0073\u002e\u0070\u006e\u0067\u0022\u0029\u003b\u000a\u0009\u0009\u007d\u002f\u002a */

			starBackground.bind();
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
            Texture.unbind();
            backgroundOffset = new Vector2f(0f, 0f);
            backgroundVelocity = new Vector2f(0f, 0f);

            ttoGUIButton = new ShaderOptimizedButton(-0.06f, 0.4f, 0.12f, 0.12f, "res/sprites/ttoguiButton.png");
            ttoGUIButtonShader = new ShaderProgram("ttoGUIButtonShader");

            mainFBO = new FBO(new Texture(window.getWidth(), window.getHeight()));
            screenVAO = new VAO(-1f, -1f, 2f, 2f);

            WinScreen.fullScreen = screenVAO;

            cam.x = currentLevel.ystart[currentLevel.currentTZ] * 50;
            cam.y = currentLevel.xstart[currentLevel.currentTZ] * 50;

            ttoSelector = currentLevel.currentTZ;
            ttoSelectorBool = false;

            ohYknow = new Texture("lib/ohYknow.jpg");
            ohYknowVAO = new VAO(-window.getWidth() / 2f, -window.getHeight() / 2, window.getWidth(), window.getHeight());

            //System.out.println(entities.get(0));
        }

	}
	//DO NOT PLACE FUNCTIONS BETWEEN SETUP AND LOOP, PUT BELOW LOOP
	@Override
	public boolean loop() {
	    MainMenu.mainMenuQuit = false;
	    if(quit){
	        return true;
        }
		//System.out.println(ttoSelector);
		long time = System.nanoTime();
		delta_time = ((float)(time - lastTime) / 1000000f);
		if (delta_time > 40f) {
			delta_time = 40f;
		}
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
        //dev buttons
        if (window.keys[GLFW_KEY_N] == 1)
        {
            nextLevel(1);
        }
        if (window.keys[GLFW_KEY_B] == 1)
        {
            nextLevel(-1);
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
			
			SpriteButton.shader.enable();

			SpriteButton.shader.shaders[0].uniforms[0].set(aspectRatio);
			
			if (menu.render(window.cursorXFloat,window.cursorYFloat)) {
                if (window.mouseButtons[GLFW_MOUSE_BUTTON_LEFT] == 1) {
					new MainMenu(window);
					pause = false;
					nextLevel(0);

				}
			}
			
            if (restart.render(window.cursorXFloat,window.cursorYFloat)) {
             	if (window.mouseButtons[GLFW_MOUSE_BUTTON_LEFT] == 1) {
					framesPaused = 0;
					pause = !pause;
					nextLevel(0);
            	}
            }
            
            if (cont.render(window.cursorXFloat, window.cursorYFloat)) {
            	if (window.mouseButtons[GLFW_MOUSE_BUTTON_LEFT] == 1) {
                    framesPaused = 0;
                    pause = !pause;
            	}
            }
		}
		//Time Travel animation
		else if (timeTravelFrames > 0) {
			FBO.prepareDefaultRender();

			ttoSelector = currentLevel.currentTZ;
			ttoSelectorBool = false;

			renderBackground();
			
			currentLevel.render(cam);
			LevelBase.levelProgram.shaders[0].uniforms[0].set(cam.getCameraMatrix(cam.x, cam.y, 0));
			int frame = (int) ((animationTimer % 400)/100f);
			spriteTexture[frame].bind();
			playerVAO.fullRender();
			
			travelShader.enable();
			travelShader.shaders[2].uniforms[0].set(aspectRatio);
			float dist = (float) timeTravelFrames / 5;
			travelShader.shaders[2].uniforms[1].set(dist);
			mainFBO.getBoundTexture().bind();
			screenVAO.fullRender();
			guiShader.enable();

			lastTime = System.nanoTime();

			timeTravelFrames += delta_time * 0.05;
			if (timeTravelFrames >= 50) {
				timeTravelFrames = 0;
			}
		}
		//run game
		else {
			mainFBO.prepareForTexture();
			
			renderBackground();

			lastTime = time;

			//game here
			currentLevel.render(cam);

			//MOVEMENT
			Movement m;
			m = new Movement(delta_time, window, cam, currentLevel, solidBlocks);
			float xOffset = m.getXOffset();
			float yOffset = m.getYOffset();
			//crate movement - sets the box x and y from entity
			for (int i = 0; i < currentLevel.entities.size(); i++) {
				float[] newOffsets = currentLevel.entities.get(i).getBoxMovement(cam.x + 25, cam.y + 25, m.getHSpeed(), m.getVSpeed());
				xOffset = absMin(newOffsets[0], xOffset);
				yOffset = absMin(newOffsets[1], yOffset);
			}
			//avatar movement
			cam.x += xOffset;
			cam.y += yOffset;
			
			//background shifting
			backgroundOffset.x += xOffset * 0.0005;
			backgroundOffset.y += yOffset * 0.0005;

			LevelBase.levelProgram.enable();
			Matrix4f camMatrix = cam.getCameraMatrix(cam.x, cam.y, 0);
			if (xOffset > 0.001) {
				playerDirection = 3;
			}else if (xOffset < -0.001) {
				playerDirection = 1;
			}
			if (yOffset > 0.001) {
				playerDirection = 2;
			}else if (yOffset < -0.001) {
				playerDirection = 0;
			}
			if (playerDirection == 1) {
				camMatrix.rotateZ((float)Math.PI/2f);
			}else if (playerDirection == 2) {
				camMatrix.rotateZ((float)Math.PI);
			}else if (playerDirection == 3) {
				camMatrix.rotateZ((float)-Math.PI/2f);
			}
			LevelBase.levelProgram.shaders[0].uniforms[0].set(camMatrix);
			if (m.isMoving) {
				animationTimer += delta_time;
			}
			int frame = (int) ((animationTimer % 400)/100f);
			spriteTexture[frame].bind();
			
			playerVAO.fullRender();

			int[] nearesTTOCheck = new int[4];
			nearesTTOCheck[0] = Util.numVal(currentLevel.charAtPos(cam.x + 15f, cam.y - 15f));
			nearesTTOCheck[1] = Util.numVal(currentLevel.charAtPos(cam.x + 15f, cam.y + 15f));
			nearesTTOCheck[2] = Util.numVal(currentLevel.charAtPos(cam.x - 15f, cam.y - 15f));
			nearesTTOCheck[3] = Util.numVal(currentLevel.charAtPos(cam.x - 15f, cam.y + 15f));
			int ttoOnInd = -1;
			for (int i:nearesTTOCheck) {
				if (i >= 0) {

					ttoOnInd = i;
				}
			}
			if (ttoOnInd >= 0) {
				ttoguiShader.enable();
				ttoguiShader.shaders[0].uniforms[0].set(aspectRatio);

				ttoGUIButtonShader.enable();
				ttoGUIButtonShader.shaders[0].uniforms[0].set(aspectRatio);
				ttoGUIButton.sprite.bind();
				ttoGUIButton.vao.prepareRender();
				float leftOffset = 0.025f * (float) currentLevel.levelseries.size();
				//Arrow key switching in the tto
				if(UserControls.leftArrowPress(window)){
					if(ttoSelector-1 >= 0) {
						ttoSelector--;
					}
				}
				if(UserControls.rightArrowPress(window)){
					if(ttoSelector+1 < currentLevel.levelseries.size()) {
						ttoSelector++;
					}
				}
				//regular switching
				for (int i = 0; i < currentLevel.levelseries.size(); ++i) {
					float floati = (float) i;
					//float x = floati * 0.2f + window.cursorXFloat * aspectRatio;
					ttoGUIButtonShader.shaders[0].uniforms[1].set(-leftOffset + 0.1f * floati - ttoGUIButton.width/2);
					if (!currentLevel.timeZonePossibilities.get(ttoOnInd)[i]) {
						ttoGUIButtonShader.shaders[2].uniforms[0].set(0f);
					} else if (currentLevel.currentTZ == i) {
						ttoGUIButtonShader.shaders[2].uniforms[0].set(2f);
					} else if (ttoGUIButton.hoverCheck((window.cursorXFloat - floati * 0.1f + leftOffset + ttoGUIButton.width/2)/aspectRatio, window.cursorYFloat) || ttoSelector == i) {
						if(ttoGUIButton.hoverCheck((window.cursorXFloat - floati * 0.1f + leftOffset + ttoGUIButton.width/2)/aspectRatio, window.cursorYFloat)) {
							ttoSelector = i;
							if (UserControls.leftMB(window)) {
								currentLevel.currentTZ = i;
								++timeTravelFrames;
							}
						}
						if(ttoSelector == i){
							if(UserControls.enter(window)){
								currentLevel.currentTZ = i;
								++timeTravelFrames;
							}
							ttoGUIButtonShader.shaders[2].uniforms[0].set(3f);
						}
					} else {
						ttoGUIButtonShader.shaders[2].uniforms[0].set(1f);
					}
					ttoGUIButton.vao.draw();
				}
				ttoGUIButton.vao.unbind();
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
			int[] arrpossibilities = new int[12];
			arrpossibilities[0] = Util.numVal(currentLevel.charAtPos(cam.x - spriteSize, cam.y));
			arrpossibilities[1] = Util.numVal(currentLevel.charAtPos(cam.x + spriteSize, cam.y));
			arrpossibilities[2] = Util.numVal(currentLevel.charAtPos(cam.x, cam.y + spriteSize));
			arrpossibilities[3] = Util.numVal(currentLevel.charAtPos(cam.x, cam.y - spriteSize));
			arrpossibilities[4] = Util.numVal(currentLevel.charAtPos(cam.x-spriteSize, cam.y-spriteSize));
			arrpossibilities[5] = Util.numVal(currentLevel.charAtPos(cam.x+spriteSize, cam.y-spriteSize));
			arrpossibilities[6] = Util.numVal(currentLevel.charAtPos(cam.x-spriteSize, cam.y+spriteSize));
			arrpossibilities[7] = Util.numVal(currentLevel.charAtPos(cam.x+spriteSize, cam.y+spriteSize));
			arrpossibilities[8] = nearesTTOCheck[0];
			arrpossibilities[9] = nearesTTOCheck[1];
			arrpossibilities[10] = nearesTTOCheck[2];
			arrpossibilities[11] = nearesTTOCheck[3];
			int ttotouchingindex = -1;
			for (int i:arrpossibilities) {
				if (i > ttotouchingindex) {
					ttotouchingindex = i;
				}
			}

            currentLevel.updateTTO(arrpossibilities, delta_time);
			
			if (CamCollision.checkAndReplace(cam.x, cam.y, 15, 'k', ' ')) {
				inv.add('k');
			}

			//checks to open button doors
			checkButtonPress('x','X');
			checkButtonPress('y','Y');
            checkButtonPress('z','Z');

			if(UserControls.ohYknow(window)){
				//LevelBase.levelProgram.shaders[0].uniforms[0].set(cam.getCameraMatrix(cam.x, cam.y, 0));
				ohYknow.bind();
				ohYknowVAO.fullRender();
			}
			
			if (inv.check('k')) {
				if (CamCollision.checkAndReplace(cam.x, cam.y, 16, 'l', ' ')) {
					inv.getAndRemove('k');
				}
			}
			
			
            inventoryShader.enable();
			inventoryShader.shaders[0].uniforms[0].set(aspectRatio);
            inventoryObjectVAO.prepareRender();
            for (int i = 0; i < inv.getSize(); ++i) {
				inventoryShader.shaders[0].uniforms[1].set((float) i * 0.1f);
				if (inv.get(i) == 'k') {
					keyTexture.bind();
					inventoryObjectVAO.draw();
				}
			}

			guiShader.enable();
			guiShader.shaders[0].uniforms[0].set(aspectRatio);
			if (CamCollision.isColliding(cam.x,cam.y,1,currentLevel.levelseries.get(currentLevel.currentTZ),'g')) {
				LevelSelect.levelState[currentLevelNum] = 1;
				LevelSelect.updateLevelTextDoc();
				new WinScreen(mainFBO.getBoundTexture(), aspectRatio);
				if (nextLevel(1)) {
					return true;
				}
			}

			FBO.prepareDefaultRender();
			textureShader.enable();
			mainFBO.getBoundTexture().bind();
			screenVAO.fullRender();
		}
		return false;
	}
	
	public void renderBackground() {
		backgroundShader.enable();
		backgroundShader.shaders[2].uniforms[0].set(backgroundOffset.mul(0.5f, new Vector2f()));
		starBackground.bind();
		screenVAO.prepareRender();
		screenVAO.drawTriangles();
		frontStars.bind();
		backgroundShader.shaders[2].uniforms[0].set(backgroundOffset);
		screenVAO.drawTriangles();
		screenVAO.unbind();
		
		
		if (backgroundMoveBool) {
			backgroundVelocity.mul(0.99f);
			backgroundVelocity.add((float) (delta_time * 0.000007f * (Math.random() - 0.5)), (float) (delta_time * 0.000007f * (Math.random() - 0.5)));
			backgroundOffset.add(backgroundVelocity);
		}
	}
	
	public boolean nextLevel(int increment) {
		inv.reset();
		File test = new File("res/Levels/Level" + (increment+currentLevelNum) + ".txt");
		if(test.exists()) {
			currentLevelNum += increment;
			currentLevel = new LevelBase("res/Levels/Level" + currentLevelNum + ".txt");
			ttoSelector = currentLevel.currentTZ;
			cam.x = currentLevel.ystart[currentLevel.currentTZ] * 50;
			cam.y = currentLevel.xstart[currentLevel.currentTZ] * 50;
			if (increment == 1) {
				new PanScreen(window);
			}
			return true;
		}
		return false;

	}
	
	public int[] findCharacter(char ch){
		ArrayList<Character[]> level = currentLevel.levelseries.get(currentLevel.currentTZ);
		for(int i = 0; i < level.size(); i++){
			for(int j = 0; j < level.get(i).length; j++){
				if(level.get(i)[j].equals(ch)){
					return new int[] {j,i};
				}
			}
		}
		return new int[] {-1,-1};
	}
	
	public boolean checkBoxPosition(ArrayList<Entity> boxes, char obs){
		int tz = currentLevel.currentTZ;
		for (Entity i: boxes){
			if(CamCollision.isColliding(i.xpos[tz] - 25, i.ypos[tz] - 25, 15, currentLevel.levelseries.get(tz), obs)){
				return true;
			}
		}
		return false;
	}
	
	public void checkButtonPress (char button, char door) {
		if (checkBoxPosition(currentLevel.entities, button) || CamCollision.isColliding(cam.x, cam.y,15, currentLevel.levelseries.get(currentLevel.currentTZ), button)) {
			int[] location = findCharacter(door);
			if (button == 'x') {
				ArrayList<Character[]> currentZone = getCurrentZone();
				for(int row = 0; row < currentZone.size();++row) {
					Character[] currentRow = currentZone.get(row);
					for (int i = 0;i < currentRow.length; ++i) {
						if (currentRow[i].equals('X')) {
							replaceCurrentTile(i, row, 'i');
						}
					}
				}
			}else if (button == 'y') {
				ArrayList<Character[]> currentZone = getCurrentZone();
				for(int row = 0; row < currentZone.size();++row) {
					Character[] currentRow = currentZone.get(row);
					for (int i = 0;i < currentRow.length; ++i) {
						if (currentRow[i].equals('Y')) {
							replaceCurrentTile(i, row, 'o');
						}
					}
				}
			}else if (button == 'z') {
				ArrayList<Character[]> currentZone = getCurrentZone();
				for(int row = 0; row < currentZone.size();++row) {
					Character[] currentRow = currentZone.get(row);
					for (int i = 0;i < currentRow.length; ++i) {
						if (currentRow[i].equals('Z')) {
							replaceCurrentTile(i, row, 'p');
						}
					}
				}
			}
		}else {
			if (button == 'x') {
				ArrayList<Character[]> currentZone = getCurrentZone();
				for(int row = 0; row < currentZone.size();++row) {
					Character[] currentRow = currentZone.get(row);
					for (int i = 0;i < currentRow.length; ++i) {
						if (currentRow[i].equals('i')) {
							replaceCurrentTile(i, row, 'X');
						}
					}
				}
			}else if (button == 'y') {
				ArrayList<Character[]> currentZone = getCurrentZone();
				for(int row = 0; row < currentZone.size();++row) {
					Character[] currentRow = currentZone.get(row);
					for (int i = 0; i < currentRow.length; ++i) {
						if (currentRow[i].equals('o')) {
							replaceCurrentTile(i, row, 'Y');
						}
					}
				}
			}else if (button == 'z') {
				ArrayList<Character[]> currentZone = getCurrentZone();
				for(int row = 0; row < currentZone.size(); ++row) {
					Character[] currentRow = currentZone.get(row);
					for (int i = 0; i < currentRow.length; ++i) {
						if (currentRow[i].equals('p')) {
							replaceCurrentTile(i, row, 'Z');
						}
					}
				}
			}
		}
	}
	
	
	/**
	 *
	 * @return character replaced
	 * @param x location of replace
	 * @param y location of replace
	 * @param replacement tile you are replacing it with
	 */
	public char replaceTile(float x, float y, char replacement) {
		int tempIntX = (int)(x/50f);
		int tempIntY = (int)(y/50f);
		char current = currentLevel.levelseries.get(currentLevel.currentTZ).get(tempIntY)[tempIntX];
		for (int i = currentLevel.currentTZ; i < currentLevel.levelseries.size(); i ++) {
			currentLevel.levelseries.get(i).get(tempIntY)[tempIntX] = replacement;
		}
		
		//currentLevel.levelseries.get(tempIntY)
		return current;
	}
	
	public char replaceCurrentTile(int x, int y, char replacement) {
		char current = currentLevel.levelseries.get(currentLevel.currentTZ).get(y)[x];
		currentLevel.levelseries.get(currentLevel.currentTZ).get(y)[x] = replacement;
		//currentLevel.levelseries.get(tempIntY)
		return current;
	}

	/**
	 *
	 * @param x index of replace
	 * @param y index of replace
	 * @param replacement tile you are replacing it with
	 * @return
	 */
	public char replaceTile(int x, int y, char replacement) {
		char current = currentLevel.levelseries.get(currentLevel.currentTZ).get(y)[x];
		for (int i = currentLevel.currentTZ; i < currentLevel.levelseries.size(); i ++) {
			currentLevel.levelseries.get(i).get(y)[x] = replacement;
		}

		//currentLevel.levelseries.get(tempIntY)
		return current;
	}

	public ArrayList<Character[]> getCurrentZone() {
		return currentLevel.levelseries.get(currentLevel.currentTZ);
	}

    public static void createTextFolder(){
        boolean check = false;
        for(File i:new File("res/").listFiles()){
            if(i.getName().equals("options.txt")){
                check = true;
            }
        }
        if(!check){
            try {
                PrintWriter writer = new PrintWriter("res/options.txt", "UTF-8");
                String format =
                        "fullscreen:t\n" +
                        "res:1080,720\n" +
                        "backgroundmove:f";
                writer.println(format);
                writer.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        boolean checkc = false;
        for(File i:new File("res/").listFiles()){
            if(i.getName().equals("controls.txt")){
                checkc = true;
            }
        }
        if(!checkc){
            try {
                PrintWriter writer = new PrintWriter("res/controls.txt", "UTF-8");
                String format =
                        "forward:87\n" +
                        "backward:83\n" +
                        "left:65\n" +
                        "right:68\n" +
                        "leftArrow:81,263\n" +
                        "rightArrow:69,262\n" +
                        "down:340\n" +
                        "up:32\n" +
                        "pause:256\n" +
                        "enter:82,257\n" +
                        "levelAdvance:78\n" +
                        "levelBack:66\n" +
                        "ohYknow:344\n" +
						"skip:258,257";

                //org.lwjgl.glfw.GLFW.glfwGetKeyName() use this

				//org.lwjgl.glfw.GLFW.glfwGetKeyName();

                writer.println(format);
                writer.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    public void getControls(){
		try {
			Scanner s = new Scanner(new File("res/controls.txt"));

			UserControls.forwardSettingString = s.nextLine();
			UserControls.backwardSettingString = s.nextLine();
			UserControls.leftSettingString = s.nextLine();
			UserControls.rightSettingString = s.nextLine();
			UserControls.leftArrowSettingString = s.nextLine();
			UserControls.rightArrowSettingString = s.nextLine();
			UserControls.downSettingString = s.nextLine();
			UserControls.upSettingString = s.nextLine();
			UserControls.pauseSettingString = s.nextLine();
			UserControls.enterSettingString = s.nextLine();
			UserControls.levelAdvanceSettingString = s.nextLine();
			UserControls.levelBackSettingString = s.nextLine();
			UserControls.ohYknowSettingString = s.nextLine();
			UserControls.skipSettingString = s.nextLine();

			String[] forwardSettingStringArray = UserControls.forwardSettingString.substring(UserControls.forwardSettingString.indexOf(":") + 1).split(",");
			String[] backwardSettingStringArray = UserControls.backwardSettingString.substring(UserControls.backwardSettingString.indexOf(":") + 1).split(",");
			String[] leftSettingStringArray = UserControls.leftSettingString.substring(UserControls.leftSettingString.indexOf(":") + 1).split(",");
			String[] rightSettingStringArray = UserControls.rightSettingString.substring(UserControls.rightSettingString.indexOf(":") + 1).split(",");
			String[] leftArrowSettingStringArray = UserControls.leftArrowSettingString.substring(UserControls.leftArrowSettingString.indexOf(":") + 1).split(",");
			String[] rightArrowSettingStringArray = UserControls.rightArrowSettingString.substring(UserControls.rightArrowSettingString.indexOf(":") + 1).split(",");
			String[] downSettingStringArray = UserControls.downSettingString.substring(UserControls.downSettingString.indexOf(":") + 1).split(",");
			String[] upSettingStringArray = UserControls.upSettingString.substring(UserControls.upSettingString.indexOf(":") + 1).split(",");
			String[] pauseSettingStringArray = UserControls.pauseSettingString.substring(UserControls.pauseSettingString.indexOf(":") + 1).split(",");
			String[] enterSettingStringArray = UserControls.enterSettingString.substring(UserControls.enterSettingString.indexOf(":") + 1).split(",");
			String[] levelAdvanceSettingStringArray = UserControls.levelAdvanceSettingString.substring(UserControls.levelAdvanceSettingString.indexOf(":") + 1).split(",");
			String[] levelBackSettingStringArray = UserControls.levelBackSettingString.substring(UserControls.levelBackSettingString.indexOf(":") + 1).split(",");
			String[] ohYknowSettingStringArray = UserControls.ohYknowSettingString.substring(UserControls.ohYknowSettingString.indexOf(":") + 1).split(",");
			String[] skipSettingStringArray = UserControls.skipSettingString.substring(UserControls.skipSettingString.indexOf(":") + 1).split(",");

			UserControls.forwardSetting = Integer.parseInt(forwardSettingStringArray[0]);
			UserControls.backwardSetting = Integer.parseInt(backwardSettingStringArray[0]);
			UserControls.leftSetting = Integer.parseInt(leftSettingStringArray[0]);
			UserControls.rightSetting = Integer.parseInt(rightSettingStringArray[0]);
			UserControls.leftArrowSetting = Integer.parseInt(leftArrowSettingStringArray[0]);
			UserControls.rightArrowSetting = Integer.parseInt(rightArrowSettingStringArray[0]);
			UserControls.downSetting = Integer.parseInt(downSettingStringArray[0]);
			UserControls.upSetting = Integer.parseInt(upSettingStringArray[0]);
			UserControls.pauseSetting = Integer.parseInt(pauseSettingStringArray[0]);
			UserControls.enterSetting = Integer.parseInt(enterSettingStringArray[0]);
			UserControls.levelAdvanceSetting = Integer.parseInt(levelAdvanceSettingStringArray[0]);
			UserControls.levelBackSetting = Integer.parseInt(levelBackSettingStringArray[0]);
			UserControls.ohYknowSetting = Integer.parseInt(ohYknowSettingStringArray[0]);
			UserControls.skipSetting = Integer.parseInt(skipSettingStringArray[0]);

			if(forwardSettingStringArray.length > 1) UserControls.forwardSetting2 = Integer.parseInt(forwardSettingStringArray[1]);
			if(backwardSettingStringArray.length > 1) UserControls.backwardSetting2 = Integer.parseInt(backwardSettingStringArray[1]);
			if(leftSettingStringArray.length > 1) UserControls.leftSetting2 = Integer.parseInt(leftSettingStringArray[1]);
			if(rightSettingStringArray.length > 1) UserControls.rightSetting2 = Integer.parseInt(rightSettingStringArray[1]);
			if(leftArrowSettingStringArray.length > 1) UserControls.leftArrowSetting2 = Integer.parseInt(leftArrowSettingStringArray[1]);
			if(rightArrowSettingStringArray.length > 1) UserControls.rightArrowSetting2 = Integer.parseInt(rightArrowSettingStringArray[1]);
			if(downSettingStringArray.length > 1) UserControls.downSetting2 = Integer.parseInt(downSettingStringArray[1]);
			if(upSettingStringArray.length > 1) UserControls.upSetting2 = Integer.parseInt(upSettingStringArray[1]);
			if(pauseSettingStringArray.length > 1) UserControls.pauseSetting2 = Integer.parseInt(pauseSettingStringArray[1]);
			if(enterSettingStringArray.length > 1) UserControls.enterSetting2 = Integer.parseInt(enterSettingStringArray[1]);
			if(levelAdvanceSettingStringArray.length > 1) UserControls.levelAdvanceSetting2 = Integer.parseInt(levelAdvanceSettingStringArray[1]);
			if(levelBackSettingStringArray.length > 1) UserControls.levelBackSetting2 = Integer.parseInt(levelBackSettingStringArray[1]);
			if(ohYknowSettingStringArray.length > 1) UserControls.ohYknowSetting2 = Integer.parseInt(ohYknowSettingStringArray[1]);
			if(skipSettingStringArray.length > 1) UserControls.skipSetting2 = Integer.parseInt(skipSettingStringArray[1]);

			UserControls.intit();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchElementException e){
			System.out.println("Delete your controls.txt folder (TimeTravel/res/options.txt) and try again.\n" +
					"Or fix the ordering manually if you have custom keybinds.");
		}

	}


	public static void main(String[] args) {
	    createTextFolder();
		try {
			Scanner s = new Scanner(new File("res/options.txt"));

			String fullscreen = s.nextLine();
            String res = s.nextLine();
            String backgroundMove = s.nextLine();

			if (fullscreen.replace("fullscreen:", "").equals("t")) {
				fullScreenBool = true;
				main = new MainView();

			}else {
				fullScreenBool = false;
				String[] dim = res.replace("res:", "").split(",");
				int width = Integer.parseInt(dim[0]);
				int height = Integer.parseInt(dim[1]);
				if(width < 200) width = 200;
				if(height < 200) height = 200;
				main = new MainView(width, height);
			}
			if(backgroundMove.replace("backgroundmove:", "").equals("t")){
			    backgroundMoveBool = true;
            } else {
			    backgroundMoveBool = false;
            }
		} catch (FileNotFoundException e) {
			main = new MainView();
		}
	}
	
	public MainView() {
		super();
	}
	
	public MainView(int width, int height) {
		super(width, height);
	}
	
	@Override
	public String getName() {
		return "Time Travel Puzzle game";
	}
}
