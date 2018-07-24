package Game;

import engine.Entities.Camera;
import engine.OpenGL.ShaderProgram;
import engine.OpenGL.Texture;
import engine.OpenGL.VAO;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class LevelBase
{
    int width;
    int height;
	ArrayList<ArrayList<Character[]>> levelseries;
    int currentTZ = 0;
    
    
    public static VAO tileObj;
    public static Texture floorTexture;
    public static Texture wallTexture;
    public static ShaderProgram levelProgram;
//Render Crap
    /*public LevelBase(String level, int width, int height)
    {
    	if (tileObj == null) {
			tileObj = new VAO(-25f, -25f, 50f, 50f);
    		floorTexture = new Texture("res/present-floor.png");
    		wallTexture = new Texture("res/present-wall.png");
    		levelProgram = new ShaderProgram("levelShader");
		}
		//creates a room from a string
		layout = new char[width][height];
        for (int i = 0; i < width; i++)
        {
            for (int j = 0; j < height; j++)
            {
                layout[i][j] =  level.charAt(i+j*width);
            }
        }
    }*/
    //gets the map
	public LevelBase(String filename)
	{
        if (tileObj == null) {
			tileObj = new VAO(-25f, -25f, 50f, 50f);
            floorTexture = new Texture("res/present-floor.png");
            wallTexture = new Texture("res/present-wall.png");
            levelProgram = new ShaderProgram("levelShader");
        }

		Scanner fileInput;
		String roomlist = "";
		//stores level rooms
		levelseries = new ArrayList<ArrayList<Character[]>>();

		//get the rooms
		try
		{
			fileInput = new Scanner(new File(filename));

			while (fileInput.hasNextLine())
			{
				String nextline = fileInput.nextLine();
				roomlist += nextline;
			}
		}
		catch (FileNotFoundException ex)
		{
			ex.printStackTrace();
			System.exit(0);
		}

		//splits the main string into level strings
		String[] rooms = roomlist.split(",");
		//goes through each room
		for (int i = 0; i < rooms.length; i++)//level depth
		{
			//splits level string into rows
			String[] levelslices = rooms[i].split("@");
			ArrayList<Character[]> levelroom = new ArrayList<Character[]>();
			//goes through each row
			for (int j = 0; j < levelslices.length; j++)//character row number
			{
				//gets a row from the level string split
				String lane = levelslices[j];
				Character[] levelrow = new Character[levelslices[j].length()];
				//goes through the row
				for (int k = 0; k < lane.length(); k++)//character column number
				{
					//puts a character into the char array
					levelrow[k] = lane.charAt(k);
				}
				//adds the row char array to the level arraylist
				levelroom.add(levelrow);
			}
			//adds the level arraylist to the levelseries arraylist
			levelseries.add(levelroom);
		}

		//LEVEL EXTRACTION
		/*
		String output = "";
		int tense = 2;//time period - 0,1,2
		for (int j = 0; j < levelseries.get(tense).size(); j++)
		{
			for (int k = 0; k < levelseries.get(tense).get(j).length; k++)
			{
				output += levelseries.get(tense).get(j)[k];
			}
			output += "\n";
		}
		System.out.println(output);
		*/
	}

    public void render(Camera cam) {
    	levelProgram.enable();
    	tileObj.prepareRender();
    	for (int row = 0; row < levelseries.get(currentTZ).size();++row) {
    		for (int chr = 0; chr < levelseries.get(currentTZ).get(row).length; ++chr) {
    			char currentChar = levelseries.get(currentTZ).get(row)[chr];
				if (currentChar == ' ') {
    				floorTexture.bind();
				}else if (currentChar == '#') {
    				wallTexture.bind();
				}
				float x = ((float) row) * 50f;
				float y = ((float) chr) * 50f;
				levelProgram.shaders[0].uniforms[0].set(cam.getCameraMatrix(x, -y, 0));
    			tileObj.drawTriangles();
			}
		}
		tileObj.unbind();
	}
}
