package Game.Views;

import Game.*;
import Game.Buttons.DoubleTextureButton;
import Game.Buttons.ShaderOptimizedButton;
import Game.Buttons.SpriteButton;
import engine.*;
import engine.Entities.Camera;
import engine.OpenGL.*;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjglx.debug.org.eclipse.jetty.server.Authentication;

import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static Game.Util.absMin;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;


public class MainView extends EnigView {
	public static String controlsDefault =
			"forward:87\n" +
			"backward:83\n" +
			"left:65\n" +
			"right:68\n" +
			"arrowLeft:263,81\n" +
			"arrowRight:262,69\n" +
			"arrowUp:265,87\n" +
			"arrowDown:264,83\n" +
			"down:340\n" +
			"up:32\n" +
			"pause:256\n" +
			"enter:257\n" +
			"skip:258,257\n" +
			"restart:82";

    public static boolean quit = false;
	public static MainView main;
	
	public static int entOffset = 1;

	public static Camera cam;

	public static char[] solidBlocks = {'#', '_', 'l','^','<','>','v', 'X', 'Y', 'Z', 'W'};
	public static char[] semiSolidBlocks = {'#', '_', 'l','X', 'Y', 'Z', 'W'};

	//Glodal booleans

	public static boolean fullScreenBool;
	public static boolean backgroundMoveBool;

	//project variables

	public static LevelBase currentLevel;
	public static int currentLevelNum = 0;

	public static Inventory inv;

	public ShaderProgram guiShader;
	public ShaderProgram textureShader;
	public ShaderProgram travelShader;
	public ShaderProgram inventoryShader;
	public ShaderProgram backgroundShader;
	public ShaderProgram playerShader;
	
	public Texture starBackground;
	public static Vector2f backgroundOffset = new Vector2f();
	public static Vector2f backgroundVelocity = new Vector2f();

	public Texture frontStars;
	public Texture keyTexture;
	public Texture inventoryTexture;
	public Texture[] spriteTexture;
	
	public VAO playerVAO;
	public VAO inventoryObjectVAO;
	
	public StringRenderer jumpCounterText;

	public static VAO screenVAO;

	public FBO mainFBO;
	
	public static StringRenderer levelMarker;

	public float timeTravelFrames = 0;
	public float animationFrameCounter = 0;
	public float mousesavex = 0;
	public static float mouseChangeFrameX = 0;

	public long lastTime = System.nanoTime();
	
	public static float delta_time;
	
	public float animationTimer = 0;
	
	public int playerDirection = 0;
	
	public static int jumps = 0;
	
	public static TTOGUI ttogui;
	
	//0 is up
	//1 is right
	//2 is down
	//3 is left

	Texture ohYknow;
	VAO ohYknowVAO;

	int ttoSelector;
	boolean ttoSelectorBool;

	boolean xDoor = true;
	boolean yDoor = true;
	boolean zDoor = true;

	public static float aspectRatio;
	
	public int ttoOnInd = -1;

	int menuSelect;
	
	public static float scale = 2f;

	public StringRenderer instructions;

	public static boolean timerBool;
	StringRenderer timer;



	@Override
	public void setup() {
		UserControls.getControls();
		cam = new Camera((float) window.getWidth(), (float) window.getHeight());
		main = this;
		aspectRatio = (float)window.getHeight()/(float)window.getWidth();
		if(window.getWidth() > window.getHeight()){
			scale = window.getHeight()/720f;
		} else {
			scale = window.getWidth()/1080f;
		}
		LevelSelect.createTextFolder();
        DoubleTextureButton.dtexShader = new ShaderProgram("buttonShader");
		new MainMenu(window);
		if(!quit) {
		    MainMenu.mainMenuQuit = false;
			LoadingScreen.texturePath = new ShaderOptimizedButton(-1f, -1f, 2f, 2f, "res/sprites/Loading.png");
            new LoadingScreen(window);
            //set variables here
            SpriteButton.shader = new ShaderProgram("buttonShader");

            currentLevel = new LevelBase("res/levels/Level" + currentLevelNum + ".txt"/*, new String[] {"res/levelTemplate.png", "res/levelTemplate.png"}*/);

            guiShader = new ShaderProgram("guiShader");
			WinScreen.aespectShader = guiShader;
            keyTexture = new Texture("res/sprites/inventoryKey.png");
            inventoryObjectVAO = new VAO(-0.5f, -1f, 0.2f, 0.2f);
            playerVAO = new VAO(-15, -15f, 30f, 30f);


            spriteTexture = new Texture[4];//down left right up;
            spriteTexture[0] = new Texture("res/anims/avatar-0.png");
			spriteTexture[1] = new Texture("res/anims/avatar-1.png");
			spriteTexture[2] = new Texture("res/anims/avatar-2.png");
			spriteTexture[3] = new Texture("res/anims/avatar-3.png");
			
			levelMarker = new StringRenderer(100, 0, 1000);
			jumpCounterText = new StringRenderer(100, -1850, 1000);
			jumpCounterText.centered = false;

            textureShader = new ShaderProgram("textureShaders");
			WinScreen.normieShader = textureShader;
            travelShader = new ShaderProgram("travelShaders");
            inventoryShader = new ShaderProgram("inventoryShaders");
            playerShader = new ShaderProgram("playerShader");

            frontStars = new Texture("res/sprites/frontstars.png");
            frontStars.bind();
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
            Texture.unbind();
            inventoryTexture = new Texture("res/sprites/inventory.png");
			
			backgroundShader = new ShaderProgram("backgroundShader");
			starBackground = new Texture("res/sprites/stars.png");
			
			starBackground.bind();
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
            Texture.unbind();
            backgroundOffset = new Vector2f(0f, 0f);
            backgroundVelocity = new Vector2f(0f, 0f);

            EnigWindow.checkGLError();
            mainFBO = new FBO(new Texture(window.getWidth(), window.getHeight()));
            EnigWindow.checkGLError();
            screenVAO = new VAO(-1f, -1f, 2f, 2f);
            EnigWindow.checkGLError();

            WinScreen.fullScreen = screenVAO;

            cam.x = currentLevel.ystart[currentLevel.currentTZ] * 50;
            cam.y = currentLevel.xstart[currentLevel.currentTZ] * 50;

            ttoSelector = currentLevel.currentTZ;
            ttoSelectorBool = false;

            ohYknow = new Texture("lib/ohYknow.jpg");
            ohYknowVAO = new VAO(-window.getWidth() / 2f, -window.getHeight() / 2, window.getWidth(), window.getHeight());
            
			menuSelect = -1;
			
			ttogui = new TTOGUI();

			nextLevel(currentLevelNum);

			instructions = new StringRenderer(90, 0, 880);
			instructions.color = new Vector4f(.65f, 1f, 1f, 1f);

			timer = new StringRenderer(100, 1000, 1000);
			timer.centered = false;
        }

	}
	//DO NOT PLACE FUNCTIONS BETWEEN SETUP AND LOOP, PUT BELOW LOOP
	@Override
	public boolean loop() {
	    MainMenu.mainMenuQuit = false;
	    if(quit){
	        return true;
        }
		long time = System.nanoTime();
		delta_time = ((float)(time - lastTime) / 1000000f);
		if (delta_time > 40f) {
			delta_time = 40f;
		}
		//aspectRatio = (float) window.getHeight() / (float) window.getWidth();
		mouseChangeFrameX = Math.abs(EnigWindow.mainWindow.cursorXFloat - mousesavex);

		for (int i:UserControls.pause) {
			if (window.keys[i] == 1) {
				window.keys[i] = 2;
				if (new PauseView(mainFBO.getBoundTexture()).shouldRestart) {
					nextLevel(0);
				}
				return false;
			}
		}
        //dev buttons
        if (window.keys[GLFW_KEY_N] == 1) {
            nextLevel(1);
        }
        if (window.keys[GLFW_KEY_B] == 1) {
            nextLevel(-1);
        }

		//time travel animation
		if (timeTravelFrames > 0) {
			FBO.prepareDefaultRender();

			ttoSelector = currentLevel.currentTZ;
			ttoSelectorBool = false;

			renderBackground();
			
			currentLevel.render(cam);
			levelMarker.renderStr("Level " + (currentLevelNum + 1));
			jumpCounterText.renderStr(jumps + " Jumps");
			
			//mvmt
			Movement m;
			m = new Movement(delta_time, window, cam, currentLevel, solidBlocks);
			float xOffset = m.getXOffset();
			float yOffset = m.getYOffset();
			//crate movement - sets the box x and y from entity
			for (int i = 0; i < currentLevel.entities.size(); i++) {
				float[] newOffsets = currentLevel.entities.get(i).getBoxMovement(cam.x + 25, cam.y + 25, m.getXOffset(), m.getYOffset());
				xOffset = absMin(newOffsets[0], xOffset);
				yOffset = absMin(newOffsets[1], yOffset);
			}
			//avatar movement
			if (checkCorner(xOffset, yOffset)) {
				xOffset = 0;
			}
			cam.x += xOffset;
			cam.y += yOffset;
			
			backgroundOffset.x += xOffset * 0.0003;
			backgroundOffset.y += yOffset * 0.0003;
			
			if(currentLevelNum == 0) {
				instructions.renderStr("Move with " + IHATEGLFW.getKeyName(UserControls.forwardSetting) + "," +
						IHATEGLFW.getKeyName(UserControls.leftSetting) + "," +
						IHATEGLFW.getKeyName(UserControls.backwardSetting) + "," +
						IHATEGLFW.getKeyName(UserControls.rightSetting));
			} else if(currentLevelNum == 4){
				instructions.renderStr("Use " + IHATEGLFW.getKeyName(UserControls.leftArrowSetting) + " and " + IHATEGLFW.getKeyName(UserControls.rightArrowSetting) + " to move and " + IHATEGLFW.getKeyName(UserControls.enterSetting) + " to select");
			} else if(currentLevelNum == 2 || currentLevelNum == 10){
				instructions.renderStr("Press " + IHATEGLFW.getKeyName(UserControls.restartSetting) + " to restart");
			}
			
			renderPlayer(0f, 0f, false);
			
			if (CamCollision.checkAndReplace(cam.x, cam.y, 15, 'k', ' ')) {
				inv.add('k');
			}
			if (inv.check('k')) {
				if (CamCollision.checkAndReplace(cam.x + m.getHSpeed(), cam.y + m.getVSpeed(), 14, 'l', ' ')) {
					inv.getAndRemove('k');
				}
			}
			
			ttogui.render(ttoOnInd, true);
			
			renderInventory();
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
				++jumps;
				timeTravelFrames = 0;
			}
		}
		//run game
		else {
			menuSelect = -1;
			mainFBO.prepareForTexture();
			
			renderBackground();

			lastTime = time;

			//game here
			currentLevel.render(cam);
			levelMarker.renderStr("level " + (currentLevelNum + 1));
			jumpCounterText.renderStr(jumps + " jumps");

			//MOVEMENT
			Movement m;
			m = new Movement(delta_time, window, cam, currentLevel, solidBlocks);
			float xOffset = m.getXOffset();
			float yOffset = m.getYOffset();
			//crate movement - sets the box x and y from entity
			for (int i = 0; i < currentLevel.entities.size(); i++) {
				float[] newOffsets = currentLevel.entities.get(i).getBoxMovement(cam.x + 25, cam.y + 25, m.getXOffset(), m.getYOffset());
				xOffset = absMin(newOffsets[0], xOffset);
				yOffset = absMin(newOffsets[1], yOffset);
			}
			//avatar movement
			if (checkCorner(xOffset, yOffset)) {
				xOffset = 0;
			}
			cam.x += xOffset;
			cam.y += yOffset;
			
			backgroundOffset.x += xOffset * 0.0003;
			backgroundOffset.y += yOffset * 0.0003;
			
			renderPlayer(xOffset, yOffset, m.isMoving);
			
			if(UserControls.ohYknow(window)){
				ohYknow.bind();
				ohYknowVAO.fullRender();
			}
			if(currentLevelNum == 0) {
				instructions.renderStr("Move with " + IHATEGLFW.getKeyName(UserControls.forwardSetting) + "," +
						IHATEGLFW.getKeyName(UserControls.leftSetting) + "," +
						IHATEGLFW.getKeyName(UserControls.backwardSetting) + "," +
						IHATEGLFW.getKeyName(UserControls.rightSetting));
			} else if(currentLevelNum == 4){
				instructions.renderStr("Use " + IHATEGLFW.getKeyName(UserControls.leftArrowSetting) + " and " + IHATEGLFW.getKeyName(UserControls.rightArrowSetting) + " to move and " + IHATEGLFW.getKeyName(UserControls.enterSetting) + " to select");
			} else if(currentLevelNum == 2 || currentLevelNum == 10){
				instructions.renderStr("Press " + IHATEGLFW.getKeyName(UserControls.restartSetting) + " to restart");
			}

			int[] nearesTTOCheck = new int[4];
			nearesTTOCheck[0] = Util.numVal(currentLevel.charAtPos(cam.x + 15f, cam.y - 15f));
			nearesTTOCheck[1] = Util.numVal(currentLevel.charAtPos(cam.x + 15f, cam.y + 15f));
			nearesTTOCheck[2] = Util.numVal(currentLevel.charAtPos(cam.x - 15f, cam.y - 15f));
			nearesTTOCheck[3] = Util.numVal(currentLevel.charAtPos(cam.x - 15f, cam.y + 15f));
			ttoOnInd = -1;
			for (int i:nearesTTOCheck) {
				if (i >= 0) {
					ttoOnInd = i;
				}
			}
			int newTZ = ttogui.render(ttoOnInd, ttoOnInd >= 0);
			if (ttoOnInd >= 0) {
				if (newTZ != currentLevel.currentTZ) {
					currentLevel.currentTZ = newTZ;
					for (int i = 0; i < currentLevel.entities.size(); i ++) {
						if (CamCollision.isInWall(currentLevel.entities.get(i).xpos[currentLevel.currentTZ], currentLevel.entities.get(i).ypos[currentLevel.currentTZ], currentLevel.levelseries.get(currentLevel.currentTZ))) {
							currentLevel.entities.get(i).ypos[currentLevel.currentTZ] = -1;
						}
					}
					++timeTravelFrames;
				}
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

            currentLevel.updateTTO(arrpossibilities, delta_time);
			
			if (CamCollision.checkAndReplace(cam.x, cam.y, 15, 'k', ' ')) {
				inv.add('k');
			}

			//checks to open button doors
			checkButtonPress('x','X');
			checkButtonPress('y','Y');
            checkButtonPress('z','Z');
			checkButtonPress('w','W');
			
			if (inv.check('k')) {
				if (CamCollision.checkAndReplace(cam.x + m.getHSpeed(), cam.y + m.getVSpeed(), 14, 'l', ' ')) {
					inv.getAndRemove('k');
				}
			}
			
            renderInventory();

			if (checkWin()) {
				return true;
			}

			if(UserControls.restart(window)){
				nextLevel(0);
			}
			if (timerBool){
				if(currentLevel.startSecondTime < 1d){
					currentLevel.startSecondTime = System.nanoTime();
				}
				int secondTime = (int)(System.nanoTime()/1000000000d) - (int)(currentLevel.startSecondTime/1000000000d);
				String thing;
				if(secondTime % 60 < 10){
					thing = "Time " + secondTime/60 + ":0" + secondTime % 60;
				} else {
					thing = "Time " + secondTime/60 + ":" + secondTime % 60;
				}
				if(!(secondTime < 0)) {
					timer.renderStr(thing);
				}
			}

			FBO.prepareDefaultRender();
			textureShader.enable();
			mainFBO.getBoundTexture().bind();
			screenVAO.fullRender();
		}
		mousesavex = EnigWindow.mainWindow.cursorXFloat;
		return false;
	}
	
	public boolean checkWin() {
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
		return false;
	}

	public void renderInventory() {
		inventoryShader.enable();
		inventoryObjectVAO.prepareRender();
		inventoryShader.shaders[0].uniforms[0].set(aspectRatio);
		for (int i = 0; i < 5;++i) {
			inventoryShader.shaders[0].uniforms[1].set((float) i * 0.2f * aspectRatio);
			inventoryTexture.bind();
			inventoryObjectVAO.draw();
		}
		for (int i = 0; i < inv.getSize(); ++i) {
			inventoryShader.shaders[0].uniforms[1].set((float) i * 0.2f * aspectRatio);
			if (inv.get(i) == 'k') {
				keyTexture.bind();
				inventoryObjectVAO.draw();
			}
		}
	}

	public boolean checkCorner(float xvel, float yvel) {
		char a = currentLevel.charAtPos(cam.x + 15f + xvel, cam.y - 15f + yvel);
		char b = currentLevel.charAtPos(cam.x + 15f + xvel, cam.y + 15f + yvel);
		char c = currentLevel.charAtPos(cam.x - 15f + xvel, cam.y - 15f + yvel);
		char d = currentLevel.charAtPos(cam.x - 15f + xvel, cam.y + 15f + yvel);
		for (char chr:semiSolidBlocks) {
			if (a == chr) {
				return true;
			}
			if (b == chr) {
				return true;
			}
			if (c == chr) {
				return true;
			}
			if (d == chr) {
				return true;
			}
		}
		return false;
	}
	
	public void renderPlayer(float xOffset, float yOffset, boolean isMoving) {
		playerShader.enable();
		Matrix4f camMatrix = cam.getCameraMatrix(cam.x, cam.y, 0).scale(scale);
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
		playerShader.shaders[2].uniforms[0].set(new Vector4f(1, 1, 0, 1));
		if (xOffset * xOffset + yOffset * yOffset > 0.001) {
			playerShader.shaders[2].uniforms[0].set(new Vector4f(0, 1, 0, 1));
			animationTimer += delta_time;
		}else if (isMoving) {
			playerShader.shaders[2].uniforms[0].set(new Vector4f(1, 0, 0, 1));
		}
		int frame = (int) ((animationTimer % 400)/100f);
		spriteTexture[frame].bind();
		
		playerVAO.fullRender();
	}
	
	public void renderBackground() {
		backgroundShader.enable();
		backgroundShader.shaders[2].uniforms[0].set(backgroundOffset.mul(0.6f, new Vector2f()));
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
		jumps = 0;
		File test = new File("res/levels/Level" + (increment+currentLevelNum) + ".txt");
		if(test.exists()) {
			currentLevelNum += increment;
			int currentTTO = 0;

			Scanner fileInput;
			String boolstr = "";
			//get the rooms
			try {
				fileInput = new Scanner(new File("res/levels/Level" + currentLevelNum + ".cfg"));
				while (fileInput.hasNextLine()) {
					String nextline = fileInput.nextLine();
					if (!nextline.startsWith("//")) {
						boolstr += nextline + "\n";
					}
				}
			}catch (FileNotFoundException ex) {
				ex.printStackTrace();
				System.exit(0);
			}

			ArrayList<Boolean[]> ttoTimeRange = new ArrayList<Boolean[]>();
			String[] roomBool = boolstr.split("\n");
			for (int i = 0; i < roomBool.length; i++)
			{
				String boolSlice = roomBool[i];
				Boolean[] boolSlices = new Boolean[roomBool[i].length()];
				for (int j = 0; j < boolSlice.length(); j++)
				{
					if (boolSlice.charAt(j) == '1')
					{
						boolSlices[j] = true;
					}
					else
					{
						boolSlices[j] = false;
					}
				}
				ttoTimeRange.add(boolSlices);
			}

			currentLevel = new LevelBase("res/levels/Level" + currentLevelNum + ".txt", ttoTimeRange);
			ttoSelector = currentLevel.currentTZ;
			cam.x = currentLevel.ystart[currentLevel.currentTZ] * 50;
			cam.y = currentLevel.xstart[currentLevel.currentTZ] * 50;
			//checks if n is not pressed dev auto skip
			if (increment == 1 && window.keys[GLFW_KEY_N] == 0) {
				new PanScreen(window);
			}
			return false;
		}
		return true;

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
			}else if (button == 'w') {
				ArrayList<Character[]> currentZone = getCurrentZone();
				for(int row = 0; row < currentZone.size();++row) {
					Character[] currentRow = currentZone.get(row);
					for (int i = 0;i < currentRow.length; ++i) {
						if (currentRow[i].equals('W')) {
							replaceCurrentTile(i, row, 'u');
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
			}else if (button == 'w') {
				ArrayList<Character[]> currentZone = getCurrentZone();
				for(int row = 0; row < currentZone.size(); ++row) {
					Character[] currentRow = currentZone.get(row);
					for (int i = 0; i < currentRow.length; ++i) {
						if (currentRow[i].equals('u')) {
							replaceCurrentTile(i, row, 'W');
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
        Path dir = Paths.get("res/");
		try {
			DirectoryStream<Path> dirStream = Files.newDirectoryStream(dir);
			for(Path i:dirStream){
				System.out.println(i.getFileName().toString());
				if(i.getFileName().toString().equals("options.txt")){
					check = true;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

        if(!check){
            try {
                PrintWriter writer = new PrintWriter("res/options.txt", "UTF-8");
                String format =
                        "fullscreen:t\n" +
                        "res:1080,720\n" +
                        "backgroundmove:f\n" +
						"texLock:f\n" +
						"timer:f\n" +
						"boxNum:f";
                writer.println(format);
                writer.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        boolean checkc = false;
		Path dirc = Paths.get("res/");
		try {
			DirectoryStream<Path> dirStreamc = Files.newDirectoryStream(dirc);
			for(Path i:dirStreamc){
				System.out.println(i.getFileName().toString());
				if(i.getFileName().toString().equals("controls.txt")){
					checkc = true;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
        if(!checkc){
            try {
                PrintWriter writer = new PrintWriter("res/controls.txt", "UTF-8");

                writer.println(controlsDefault);
                writer.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
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
