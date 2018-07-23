package Game;

import engine.OpenGL.ShaderProgram;
import engine.OpenGL.Texture;
import engine.OpenGL.VAO;

public class LevelBase
{
    int width;
    int height;
    char[][] layout = new char[width][height];
    
    public static VAO tileObj;
    public static Texture floorTexture;
    public static Texture wallTexture;
    public static ShaderProgram levelProgram;

    public LevelBase(String level, int width, int height)
    {
    	if (tileObj == null) {
    		tileObj = new VAO(-0.2f, -0.2f, 0.4f, 0.4f);
    		floorTexture = new Texture("present-floor.png");
    		wallTexture = new Texture("present-wall.png");
    		levelProgram = new ShaderProgram("levelShader");
		}
		layout = new char[width][height];
        for (int i = 0; i < width; i++)
        {
            for (int j = 0; j < height; j++)
            {
                layout[i][j] =  level.charAt(width*i+j);
            }
        }
    }
	
    public void render() {
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
				
    			tileObj.drawTriangles();
			}
		}
		tileObj.unbind();
	}
}
