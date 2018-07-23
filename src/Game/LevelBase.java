package Game;

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
    char[][] layout = new char[width][height];
    
    public static VAO tileObj;
    public static Texture floorTexture;
    public static Texture wallTexture;
    public static ShaderProgram levelProgram;
//Render Crap
    public LevelBase(String level, int width, int height)
    {
    	if (tileObj == null) {
    		tileObj = new VAO(-0.2f, -0.2f, 0.4f, 0.4f);
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
                layout[i][j] =  level.charAt(width*i+j);
            }
        }
    }
    //gets the map
	public LevelBase(String filename)
	{
        if (tileObj == null) {
            tileObj = new VAO(-0.2f, -0.2f, 0.4f, 0.4f);
            floorTexture = new Texture("present-floor.png");
            wallTexture = new Texture("present-wall.png");
            levelProgram = new ShaderProgram("levelShader");
        }

		Scanner fileInput;
        ArrayList<Character[]> room = new ArrayList<Character[]>();

		try
		{
			fileInput = new Scanner(new File(filename));
			while (fileInput.hasNextLine())
			{
                String nextline = fileInput.nextLine();
                Character[] row = new Character[nextline.length()];
                for (int i = 0; i < nextline.length(); i++)
                {
                    row[i] = nextline.charAt(i);
                }
                room.add(row);
			}
		}
		catch (FileNotFoundException ex)
		{
			ex.printStackTrace();
			System.exit(0);
		}

		layout = new char[room.size()][0];
		for (int i = 0; i < room.size(); i++)
		{
			Character[] lane = room.get(i);
			layout[i] = new char[lane.length];
			for (int j = 0; j < lane.length; j++)
			{
				layout[i][j] = lane[j];
			}
		}
	}

    public void render() {
    	levelProgram.enable();
    	tileObj.prepareRender();
    	for (int row = 0; row < layout.length;++row) {
    		for (int chr = 0; chr < layout[row].length; ++row) {
    			switch (layout[row][chr]) {
					case ' ' :
						floorTexture.bind();
					case '#' :
						floorTexture.bind();
					default:
						System.out.println("missing tile texture");
				}
				float x = ((float) row) * 0.4f;
				float y = ((float) chr) * 0.4f;
				levelProgram.shaders[0].uniforms[0].set(new float[]{x, y});
    			tileObj.drawTriangles();
			}
		}
		tileObj.unbind();
	}
}
