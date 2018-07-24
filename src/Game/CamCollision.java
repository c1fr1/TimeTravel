package Game;

import engine.Entities.Camera;
import java.util.ArrayList;
public class CamCollision {
    Camera cam;
    LevelBase level;

    public CamCollision(Camera cam, LevelBase level){
        this.cam = cam;
        this.level = level;
    }

    public boolean collisionNS(int speed){
        float x = cam.x;
        float y = cam.y;
        ArrayList<Character[]> current = level.levelseries.get(level.currentTZ);

        return false;
    }
}
