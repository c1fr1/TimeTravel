package Game;

import engine.Entities.Camera;
import engine.OpenGL.EnigWindow;

import java.util.logging.Level;

public class Movement {

    private float xOffset;
    private float yOffset;
    private float vSpeed = 0;
    private float hSpeed = 0;


    /**
     *
     * @param delta_time Delta Time
     * @param window Display window
     * @param cam Camera
     * @param currentLevel Current Level
     * @param solidBlocks Solid Blocks
     */
    public Movement(float delta_time, EnigWindow window, Camera cam, LevelBase currentLevel, char[] solidBlocks){

        if (UserControls.forward(window)) {
            vSpeed -= delta_time / 3f;
        }
        if (UserControls.backward(window)) {
            vSpeed += delta_time / 3f;
        }
        if (UserControls.left(window)) {
            hSpeed -= delta_time / 3f;
        }
        if (UserControls.right(window)) {
            hSpeed += delta_time / 3f;
        }
        if (hSpeed != 0) {
            vSpeed *= 0.70710678118f;
        }
        if (vSpeed != 0) {
            hSpeed *= 0.70710678118f;
        }

        xOffset = CamCollision.horizontalMove(cam.x,cam.y,15,hSpeed,
                currentLevel.levelseries.get(currentLevel.currentTZ),solidBlocks);
        yOffset = CamCollision.verticalMove(cam.x,cam.y,15,vSpeed,
                currentLevel.levelseries.get(currentLevel.currentTZ),solidBlocks);
    }

    /**
     *
     * @return gets X offset/move for the player
     */
    public float getXOffset(){
        return xOffset;
    }

    /**
     *
     * @return gets X offset/move for the player
     */
    public float getYOffset(){
        return yOffset;
    }

    /**
     *
     * @return gets vSpeed before collision
     */
    public float getVSpeed() {
        return vSpeed;
    }

    /**
     *
     * @return gets hSpeed before collision
     */
    public float getHSpeed() {
        return hSpeed;
    }
}
