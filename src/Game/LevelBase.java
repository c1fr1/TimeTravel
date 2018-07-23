package Game;

public class LevelBase
{
    int width;
    int height;
    char[][] layout = new char[width][height];

    public void roomCreator(String level,int width,int height)
    {
        char[][] layout = new char[width][height];
        for (int i = 0; i < width; i++)
        {
            for (int j = 0; j < height; j++)
            {
                layout[i][j] =  level.charAt(i+j);
            }
        }
    }

}
