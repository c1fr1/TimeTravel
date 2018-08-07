package Game;

import Game.Views.MainView;
import engine.Entities.Camera;
import engine.OpenGL.ShaderProgram;
import engine.OpenGL.Texture;
import engine.OpenGL.VAO;
import org.joml.Matrix4f;
import sun.applet.Main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class LevelBase
{
	public ArrayList<ArrayList<Character[]>> levelseries;
	public Texture[] BackgroundTextures;
	public float[] xstart;
	public float[] ystart;
	public int currentTZ;
    
    public static VAO tileObj;
    public static Texture[] floorTexture = new Texture[5];
    public static Texture[] wallTexture = new Texture[5];
	public static Texture endLevelTexture;
    public static Texture lockTexture;
    public static Texture upGateTexture;
    public static Texture downGateTexture;
    public static Texture leftGateTexture;
    public static Texture rightGateTexture;
    public static Texture keyTexture;
	public static Texture boxTexture;
	public static Texture[] ttoTexture;
    public static ShaderProgram levelProgram;
    
    public static Texture[] xElectricTextures;
	public static Texture[] yElectricTextures;
	public static Texture[] zElectricTextures;

    public Texture[] background;
    
    public ArrayList<Boolean[]> timeZonePossibilities = new ArrayList<>();
	public ArrayList<Entity> entities = new ArrayList<>();
    public float[] ttoFrameCounter;


    //gets the map
	public LevelBase(String filename) {
        init(filename, new String[0]);
		for (int i = 0; i < timeZonePossibilities.size();++i) {
			timeZonePossibilities.set(i, new Boolean[levelseries.size()]);
			for (int j = 0;j < timeZonePossibilities.get(i).length;++j) {
				timeZonePossibilities.get(i)[j] = true;
			}
		}
	}
	public LevelBase(String filename, ArrayList<Boolean[]> ttoAbilities) {
		init(filename, new String[0]);
		timeZonePossibilities = ttoAbilities;
	}
	public LevelBase(String filename, String[] textures) {
		init(filename, textures);
		for (int i = 0; i < timeZonePossibilities.size();++i) {
			timeZonePossibilities.set(i, new Boolean[levelseries.size()]);
			for (int j = 0;j < timeZonePossibilities.get(i).length;++j) {
				timeZonePossibilities.get(i)[j] = true;
			}
		}
	}

	public LevelBase(String filename, ArrayList<Boolean[]> ttoAbilities, String[] textures) {
		init(filename, textures);
		timeZonePossibilities = ttoAbilities;
	}

	public void init(String filename, String[] textures) {
		if (tileObj == null) {
			tileObj = new VAO(-25f, -25f, 50f, 50f);
			floorTexture[0] = new Texture("res/tilesets/floor-mud.png");
			wallTexture[0] = new Texture("res/tilesets/wall-mud.png");
			floorTexture[1] = new Texture("res/tilesets/floor-industrial.png");
			wallTexture[1] = new Texture("res/tilesets/wall-industrial.png");
			floorTexture[2] = new Texture("res/tilesets/floor-industrial1.png");
			wallTexture[2] = new Texture("res/tilesets/wall-industrial1.png");
			floorTexture[3] = new Texture("res/tilesets/floor-future.png");
			wallTexture[3] = new Texture("res/tilesets/wall-future.png");
			floorTexture[4] = new Texture("res/tilesets/floor-apocalypse.png");
			wallTexture[4] = new Texture("res/tilesets/wall-apocalypse.png");
			
			keyTexture = new Texture("res/sprites/inventoryKey.png");
			endLevelTexture = new Texture("res/sprites/endLevel.png");
			upGateTexture = new Texture("res/sprites/upGate.png");
			downGateTexture = new Texture("res/sprites/downGate.png");
			leftGateTexture = new Texture("res/sprites/leftGate.png");
			rightGateTexture = new Texture("res/sprites/rightGate.png");
			lockTexture = new Texture("res/sprites/locked-gate.png");

			//changing texture for tto
			ttoTexture = new Texture[9];
			ttoTexture[0] = new Texture("res/anims/tto-1.png");
			ttoTexture[1] = new Texture("res/anims/tto-2.png");
			ttoTexture[2] = new Texture("res/anims/tto-3.png");
			ttoTexture[3] = new Texture("res/anims/tto-4.png");
			ttoTexture[4] = new Texture("res/anims/tto-5.png");
			ttoTexture[5] = new Texture("res/anims/tto-6.png");
			ttoTexture[6] = new Texture("res/anims/tto-7.png");
			ttoTexture[7] = new Texture("res/anims/tto-8.png");
			ttoTexture[8] = new Texture("res/anims/tto-9.png");

			levelProgram = new ShaderProgram("levelShader");

			//button, gate closed, gate open
			xElectricTextures = new Texture[3];
			yElectricTextures = new Texture[3];
			zElectricTextures = new Texture[3];
			xElectricTextures[0] = new Texture("res/sprites/xbutton.png");
			xElectricTextures[1] = new Texture("res/sprites/xgate.png");
			xElectricTextures[2] = new Texture("res/sprites/xgateopen.png");
			yElectricTextures[0] = new Texture("res/sprites/ybutton.png");
			yElectricTextures[1] = new Texture("res/sprites/ygate.png");
			yElectricTextures[2] = new Texture("res/sprites/ygateopen.png");
			zElectricTextures[0] = new Texture("res/sprites/zbutton.png");
			zElectricTextures[1] = new Texture("res/sprites/zgate.png");
			zElectricTextures[2] = new Texture("res/sprites/zgateopen.png");
		}

		Scanner fileInput;
		String roomlist = "";
		//stores level rooms
		levelseries = new ArrayList<ArrayList<Character[]>>();
		//get the rooms
		try {
			fileInput = new Scanner(new File(filename));
			while (fileInput.hasNextLine()) {
				String nextline = fileInput.nextLine();
				if (!nextline.endsWith(",")) {
					roomlist += nextline + "\n";
				}else {
					roomlist += nextline;
				}
			}
		}catch (FileNotFoundException ex) {
			ex.printStackTrace();
			System.exit(0);
		}
		
		//splits the main string into level strings
		String[] rooms = roomlist.split(",");
		//start position of avatar
		xstart = new float[rooms.length];
		ystart = new float[rooms.length];
		
		//goes through each room
		for (int i = 0; i < rooms.length; i++) {//level depth
			//splits level string into rows
			String[] levelslices = rooms[i].split("\n");
			ArrayList<Character[]> levelroom = new ArrayList<Character[]>();
			//goes through each row
			for (int j = 0; j < levelslices.length; j++) {//character row number
				//gets a row from the level string split
				String lane = levelslices[j];
				Character[] levelrow = new Character[levelslices[j].length()];
				//goes through the row
				for (int k = 0; k < lane.length(); k++) {//character column number
					//puts a character into the char array
					levelrow[k] = lane.charAt(k);
					if (lane.charAt(k) == 's')
					{
						xstart[i] = j;
						ystart[i] = k;
						currentTZ = i;
						levelrow[k] = ' ';
					}
					else if (lane.charAt(k) == 'b')
					{
						float startX = (float)(k * 50 + 25);
						float startY = (float)(j * 50 + 25);
						boolean taken = false;
						//Entity tempEnt = new Entity(startX, startY);
						for (int l = 0; l < entities.size(); l++)
						{
							if (Math.abs(entities.get(l).xpos[entities.get(l).startZone] - startX) < 1 && Math.abs(entities.get(l).ypos[entities.get(l).startZone] - startY) < 1)
							{
								entities.get(l).xpos[i] = startX;
								entities.get(l).ypos[i] = startY;
								taken = true;
								break;
							}
						}
						if (!taken)
						{
							Entity tempEnt = new Entity(startX, startY, rooms.length, i, entities.size());
							entities.add(tempEnt);
						}
						levelrow[k] = ' ';
					}
					else if (lane.charAt(k) == 't')
					{
						if (levelseries.size() > 0)
						{
							char prevLevel = levelseries.get(levelseries.size() - 1).get(j)[k];
							if (Util.isNumericValue(prevLevel))
							{
								levelrow[k] = prevLevel;
							}
							else
							{
								levelrow[k] = (char) (timeZonePossibilities.size() + '0');
								timeZonePossibilities.add(new Boolean[0]);
							}
						}
						else
						{
							levelrow[k] = (char) (timeZonePossibilities.size() + '0');
							timeZonePossibilities.add(new Boolean[0]);
						}
					}
				}
				//adds the row char array to the level arraylist
				levelroom.add(levelrow);
			}
			//adds the level arraylist to the levelseries arraylist
			levelseries.add(levelroom);
		}
		ttoFrameCounter = new float[timeZonePossibilities.size()];
		background = new Texture[textures.length];
		for (int i = 0;i < textures.length;++i) {
			background[i] = new Texture(textures[i]);
		}
	}

	public void updateTTO(int[] locationInArray, float deltaTime) {
		for (int i = 0; i < ttoFrameCounter.length;++i) {
			boolean exists = false;
			for (int j: locationInArray) {
				if (j == i) {
					exists = true;
					break;
				}
			}
			if (exists) {
				ttoFrameCounter[i] += 0.5*deltaTime*0.03;
			}else {
				ttoFrameCounter[i] -= deltaTime*0.03;
			}
			if (ttoFrameCounter[i] < 0) {
				ttoFrameCounter[i] = 0;
			}
			if (ttoFrameCounter[i] > 8) {
				ttoFrameCounter[i] = 8;
			}
		}
	}
	public void render(Camera cam) {
    	levelProgram.enable();
    	tileObj.prepareRender();
    	for (int row = 0; row < levelseries.get(currentTZ).size();++row) {
    		for (int chr = 0; chr < levelseries.get(currentTZ).get(row).length; ++chr) {
				float x = ((float) chr) * 50f;
				float y = -((float) row) * 50f;
    			char currentChar = levelseries.get(currentTZ).get(row)[chr];
				if (currentChar != '_') {
					levelProgram.shaders[0].uniforms[1].set(new Matrix4f());
					float xoffset = x - cam.x;
					float yoffset = y + cam.y;
					Matrix4f camMatrix = new Matrix4f(cam.projectionMatrix).scale(MainView.scale).translate(xoffset, yoffset, 0);
					levelProgram.shaders[0].uniforms[0].set(camMatrix/*cam.getCameraMatrix(x * MainView.scale, (y * MainView.scale + 2*cam.y), 0).scale(MainView.scale)*/);
					if (currentChar == 'w' || currentChar == 'G' || currentChar == 'K' || currentChar == 'C' || currentChar == '-' || currentChar == 'S' || currentChar == 'T' || currentChar == '/' || currentChar == '*' || currentChar == '.' || currentChar == ',' || currentChar == 'V') {
						background[currentTZ].bind();
						levelProgram.shaders[0].uniforms[1].set(new Matrix4f().scale(0.02f).translate((float)chr, (float)row, 0f));
					}else if (currentChar == ' ') {//sbiop
    					floorTexture[currentTZ].bind();
					}else if (currentChar == '#') {
    					wallTexture[currentTZ].bind();
					}else if (Util.isNumericValue(currentChar)) {
						ttoTexture[(int) ttoFrameCounter[Character.getNumericValue(currentChar)]].bind();
					}else if (currentChar == 'g') {
                        endLevelTexture.bind();
					}else if (currentChar == 'k') {
						floorTexture[currentTZ].bind();
						tileObj.drawTriangles();
						keyTexture.bind();
					}else if (currentChar == 'l') {
						lockTexture.bind();
					}else if (currentChar == '^') {
					    upGateTexture.bind();
                    }else if (currentChar == 'v') {
						downGateTexture.bind();
					}else if (currentChar == '>') {
						rightGateTexture.bind();
					}else if (currentChar == '<') {
						leftGateTexture.bind();
					}else if (currentChar == 'x') {
						floorTexture[currentTZ].bind();
						tileObj.drawTriangles();
						xElectricTextures[0].bind();
					}else if (currentChar == 'y') {
						floorTexture[currentTZ].bind();
						tileObj.drawTriangles();
						yElectricTextures[0].bind();
					}else if (currentChar == 'z') {
						floorTexture[currentTZ].bind();
						tileObj.drawTriangles();
						zElectricTextures[0].bind();
					}else if (currentChar == 'X') {
						floorTexture[currentTZ].bind();
						tileObj.drawTriangles();
						xElectricTextures[1].bind();
					}else if (currentChar == 'Y') {
						floorTexture[currentTZ].bind();
						tileObj.drawTriangles();
						yElectricTextures[1].bind();
					}else if (currentChar == 'Z') {
						floorTexture[currentTZ].bind();
						tileObj.drawTriangles();
						zElectricTextures[1].bind();
					}else if (currentChar == 'i') {
						floorTexture[currentTZ].bind();
						tileObj.drawTriangles();
						xElectricTextures[2].bind();
					}else if (currentChar == 'o') {
						floorTexture[currentTZ].bind();
						tileObj.drawTriangles();
						yElectricTextures[2].bind();
					}else if (currentChar == 'p') {
						floorTexture[currentTZ].bind();
						tileObj.drawTriangles();
						zElectricTextures[2].bind();
					}else {
						//floorTexture.bind();
					}
					tileObj.drawTriangles();
				}
			}
		}
		tileObj.unbind();

    	for(Entity i: entities){
    		i.render(cam, currentTZ);
		}

	}
	
	public char charAtPos(float x, float y) {
		return levelseries.get(currentTZ).get((int)(y/50f + 0.5))[(int)(x/50f + 0.5)];
	}
	public char charAtPos(int x, int y) {
		return levelseries.get(currentTZ).get(y)[x];
	}
	public char charAtPosSafe(float x, float y) {
		int xi = (int)(x/50f);
		int yi = (int)(y/50f);
		if (xi < 0) {
			return '\n';
		}
		if (yi < 0) {
			return '\n';
		}
		if (yi >= levelseries.get(currentTZ).size()) {
			return '\n';
		}
		if (xi >= levelseries.get(currentTZ).get(yi).length) {
			return '\n';
		}
		return levelseries.get(currentTZ).get(yi)[xi];
	}
}
