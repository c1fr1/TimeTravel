package Game;

import engine.Entities.Camera;
import engine.OpenGL.ShaderProgram;
import engine.OpenGL.Texture;
import engine.OpenGL.VAO;
import org.joml.Matrix4f;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class LevelBase
{
	ArrayList<ArrayList<Character[]>> levelseries;
	Texture[] BackgroundTextures;
	float[] xstart;
	float[] ystart;
	int currentTZ;
    
    public static VAO tileObj;
    public static Texture floorTexture;
    public static Texture wallTexture;
	public static Texture gateTexture;
    public static Texture lockTexture;
	public static Texture newwallTexture;
    public static Texture upGateTexture;
    public static Texture downGateTexture;
    public static Texture leftGateTexture;
    public static Texture rightGateTexture;
    public static Texture keyTexture;
	public static Texture boxTexture;
	public static Texture[] ttoTexture;
    public static Texture controllerTexture;
    public static ShaderProgram levelProgram;
    
    public ArrayList<Boolean[]> timeZonePossibilities = new ArrayList<>();
    public float[] ttoFrameCounter;


    //gets the map
	public LevelBase(String filename) {
        init(filename);
		for (int i = 0; i < timeZonePossibilities.size();++i) {
			timeZonePossibilities.set(i, new Boolean[levelseries.size()]);
			for (int j = 0;j < timeZonePossibilities.get(i).length;++j) {
				timeZonePossibilities.get(i)[j] = true;
			}
		}
	}
	public LevelBase(String filename, ArrayList<Boolean[]> ttoAbilities) {
		init(filename);
		timeZonePossibilities = ttoAbilities;
	}
	public void init(String filename) {
		if (tileObj == null) {
			tileObj = new VAO(-25f, -25f, 50f, 50f);
			floorTexture = new Texture("res/sprites/present-floor.png");
			wallTexture = new Texture("res/sprites/present-wall.png");
			newwallTexture = new Texture("res/sprites/future-wall.png");
			keyTexture = new Texture("res/sprites/key.png");
			gateTexture = new Texture("res/sprites/gate.png");
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
			controllerTexture = new Texture("res/sprites/controller-tto.png");
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
					if (lane.charAt(k) == 's') {
						xstart[i] = j;
						ystart[i] = k;
						currentTZ = i;
					}else if (lane.charAt(k) == 'b') {
						Entity tempEnt = new Entity(xstart[i], ystart[i], levelseries.size());
						MainView.entities.add(tempEnt);
						levelrow[k] = ' ';
					}else if (lane.charAt(k) == 't') {
						if (levelseries.size() > 0) {
							char prevLevel = levelseries.get(levelseries.size() - 1).get(j)[k];
							if (Util.isNumericValue(prevLevel)) {
								levelrow[k] = prevLevel;
							}else {
								levelrow[k] = (char) (timeZonePossibilities.size() + '0');
								timeZonePossibilities.add(new Boolean[0]);
							}
						}else {
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
					if (currentChar == 'w' || currentChar == 'G' || currentChar == 'K' || currentChar == 'C' || currentChar == '-' || currentChar == 'S' || currentChar == 'T' || currentChar == '/' || currentChar == '*' || currentChar == '.' || currentChar == ',' || currentChar == 'V') {
						levelProgram.shaders[0].uniforms[1].set(new Matrix4f().translate(x, y, 0f).scale(0.001f));
					}
					if (currentChar == ' ' || currentChar == 's' || currentChar == 'b') {
    					floorTexture.bind();
					}else if (currentChar == '#') {
    					newwallTexture.bind();
					}else if (Util.isNumericValue(currentChar)) {
						ttoTexture[(int) ttoFrameCounter[Character.getNumericValue(currentChar)]].bind();
					}else if (currentChar == 'g') {
						gateTexture.bind();
					}
					else if (currentChar == 'k') {
						keyTexture.bind();
					}
					else if (currentChar == 'l') {
						lockTexture.bind();
					}
					else if (currentChar == '^') {
					    upGateTexture.bind();
                    }
					else if (currentChar == 'v') {
						downGateTexture.bind();
					}
					else if (currentChar == '>') {
						rightGateTexture.bind();
					}
					else if (currentChar == '<') {
						leftGateTexture.bind();
					}
					levelProgram.shaders[0].uniforms[0].set(cam.getCameraMatrix(x, y + 2*cam.y, 0));
					tileObj.drawTriangles();
				}
			}
		}
		tileObj.unbind();
	}
	
	public char charAtPos(float x, float y) {
		return levelseries.get(currentTZ).get((int)(y/50f))[(int)(x/50f)];
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
