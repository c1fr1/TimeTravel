package Game;

import engine.Entities.Camera;
import java.util.ArrayList;
public class CamCollision {
    /**
     * Using level, camera position and speed predicts what object(in character form) will be in the move
     * @param cam Camera object
     * @param level LevelBase object
     * @param speed Speed of movement
     * @return character at intended move
     */
    public char collisionH(Camera cam, LevelBase level, float speed){
        float x = cam.x;
        float y = cam.y;
        float yPosition = y/50f;
        ArrayList<Character[]> current = level.levelseries.get(level.currentTZ);
        return (current.get((int)yPosition)[(int)((x + speed)/50f)]);

    }
    /**
     * Using level, camera position and speed predicts what object(in character form) will be in the move
     * @param cam Camera object
     * @param level LevelBase object
     * @param speed Speed of movement
     * @return character at intended move
     */
    public char collisionV(Camera cam, LevelBase level, float speed){
        float x = cam.x;
        float y = cam.y;
        float xPosition = x/50f;
        ArrayList<Character[]> current = level.levelseries.get(level.currentTZ);
        return (current.get((int)xPosition)[(int)((y + speed)/50f)]);
    }
}
