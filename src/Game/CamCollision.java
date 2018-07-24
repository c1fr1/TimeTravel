package Game;

import engine.Entities.Camera;
import java.util.ArrayList;
public class CamCollision {
    /**
     * Using level, camera position and speed predicts what object(in character form) will be in the move
     * @param x,y  origin for collision
     * @param level LevelBase object
     * @param speed Speed of movement
     * @return character at intended move
     */
    public static char collisionH(float x, float y, LevelBase level, float speed, int currentTZ){
        float newX = x;
        float newY = y;
        float yPosition = newY/50f;
        ArrayList<Character[]> current = level.levelseries.get(currentTZ);
        return (current.get((int)yPosition)[(int)((newX + speed)/50f)]);

    }
    /**
     * Using level, camera position and speed predicts what object(in character form) will be in the move
     * @param x,y origin for collision
     * @param level LevelBase object
     * @param speed Speed of movement
     * @return character at intended move
     */
    public static char collisionV(float x, float y, LevelBase level, float speed, int currentTZ){
        float newX = x;
        float newY = y;
        float xPosition = newX/50f;
        ArrayList<Character[]> current = level.levelseries.get(currentTZ);
        return (current.get((int)((newY + speed)/50f))[(int)xPosition]);
    }
}
