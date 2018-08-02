package Game;

import engine.Entities.Camera;
import engine.OpenGL.ShaderProgram;
import engine.OpenGL.Texture;
import engine.OpenGL.VAO;
import org.joml.Matrix4f;
import sun.applet.Main;

import java.util.ArrayList;
import java.util.logging.Level;

import static Game.Util.absMin;

public class Entity
{
    //This array holds a whole bunch of x and y coordinates regarding the entity's position. To get the position in the nth timezone,
    //the indexes are n*2-2 and n*2-1
    //positions[0], positions[1] is the location of the entity in the first timezone.
    // In the 4th timezone, positions[6] and positions[7] are the relevant indexes.
    public float[] xpos;
    public float[] ypos;
    public int border;
    public int arrayIndex;
    public int startZone;
    public float hspeed;
    public float vspeed;

    Texture sprite;
    VAO spriteVAO;
    public static ShaderProgram levelProgram;

    public Entity(float startX, float startY, int amountOfTimezones, int startZone, int arrayLocation)
    {
        border = 15;
        arrayIndex = arrayLocation;
        xpos = new float[amountOfTimezones];
        ypos = new float[amountOfTimezones];
        xpos[startZone] = startX;
        ypos[startZone] = startY;
        for (int i = 0; i < amountOfTimezones; i++)
        {
            if (i != startZone)
            {
                xpos[i] = -1;
                ypos[i] = -1;
            }
        }

        sprite = new Texture("res/sprites/crateEntity.png");
        spriteVAO = new VAO(-40, 10, 30f, 30f);
        levelProgram = new ShaderProgram("levelShader");
    }
    //Check for collision between player and crate
	public boolean testAABB(float x, float y) {
		if (xpos[MainView.currentLevel.currentTZ] + border > x - 15) {
			if (xpos[MainView.currentLevel.currentTZ] - border < x + 15) {
				if (ypos[MainView.currentLevel.currentTZ] + border > y - 15 ) {
					if (ypos[MainView.currentLevel.currentTZ] - border < y + 15) {
						return true;
					}
				}
			}
		}
		return false;
	}
    public boolean entityCollision(Entity a, Entity b, float xvel, float yvel) {
        if (a.xpos[MainView.currentLevel.currentTZ] + a.border > b.xpos[MainView.currentLevel.currentTZ] - 15 + xvel) {
            if (a.xpos[MainView.currentLevel.currentTZ] - a.border < b.xpos[MainView.currentLevel.currentTZ] + 15 + xvel) {
                if (a.ypos[MainView.currentLevel.currentTZ] + a.border > b.ypos[MainView.currentLevel.currentTZ] - 15 + yvel) {
                    if (a.ypos[MainView.currentLevel.currentTZ] - a.border < b.xpos[MainView.currentLevel.currentTZ] + 15 + yvel) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    /*public void moveBoxX(float entX, float entY, float xVel, float yVel) {
    	if (playerAABB(entX, entY, xVel, yVel)) {
    	
		}
	}*/
    //sets box movement
    public float[] getBoxMovement(float entX, float entY, float entXVel, float entYVel) {
    	float xOffset = entXVel;
    	float yOffset = entYVel;
        int lastX = (int)entX;
        int lastY = (int)entY;
        LevelBase currentLevel = MainView.currentLevel;
        int timeZone = currentLevel.currentTZ;

    	if (testAABB(entX + entXVel, entY + entYVel)) {
            for (int i = 0; i < MainView.currentLevel.entities.size(); i++) {
                //The box we are colliding with
                Entity b = MainView.currentLevel.entities.get(i);
                if (b != this) {
                	if (!testAABB(b.xpos[timeZone], b.ypos[timeZone])) {
						float[] bVels = b.getBoxMovement(xpos[timeZone], ypos[timeZone], entXVel, entYVel);
						xOffset = absMin(xOffset, bVels[0]);
						yOffset = absMin(yOffset, bVels[1]);
					}
                }
            }

            if ((xOffset > 0 && entX + 29 < xpos[timeZone])) {
                xOffset = CamCollision.horizontalMove(xpos[timeZone], ypos[timeZone], border, xOffset, currentLevel.levelseries.get(currentLevel.currentTZ), MainView.solidBlocks);
                float fit = 30.1f + entX + entXVel - xpos[timeZone];
                xpos[timeZone] += absMin(xOffset, fit);
				//MainView.cam.x = xpos[timeZone] - 36f;
                for (int i = currentLevel.currentTZ; i < currentLevel.levelseries.size(); i++) {
                    if (xpos[i] > -1) {
                        xpos[i] = xpos[timeZone];
                        ypos[i] = ypos[timeZone];
                    }
                }
            } else if ((xOffset < 0 && entX - 29 > xpos[timeZone])) {
                xOffset = CamCollision.horizontalMove(xpos[timeZone], ypos[timeZone], border, xOffset, currentLevel.levelseries.get(currentLevel.currentTZ), MainView.solidBlocks);
				float fit = xpos[timeZone] - 30.1f - entX - entXVel;
                xOffset = absMin(xOffset, fit);
                xpos[timeZone] += xOffset;
                //MainView.cam.x = xpos[timeZone] + 36f;
                for (int i = currentLevel.currentTZ; i < currentLevel.levelseries.size(); i++) {
                    if (xpos[i] > -1) {
                        xpos[i] = xpos[timeZone];
                        ypos[i] = ypos[timeZone];
                    }
                }
            }
            if ((yOffset > 0 && entY + 29 < ypos[timeZone])) {
                yOffset = CamCollision.verticalMove(xpos[timeZone], ypos[timeZone], border, yOffset, currentLevel.levelseries.get(currentLevel.currentTZ), MainView.solidBlocks);
				float fit = 30.1f + entY + entYVel - ypos[timeZone];
                ypos[timeZone] += absMin(yOffset, fit);
                //MainView.cam.y = ypos[timeZone] - 36f;
                for (int i = currentLevel.currentTZ; i < currentLevel.levelseries.size(); i++) {
                    if (xpos[i] > -1) {
                        xpos[i] = xpos[timeZone];
                        ypos[i] = ypos[timeZone];
                    }
                }
            } else if ((yOffset < 0 && entY - 29 > ypos[timeZone])) {
            	yOffset = CamCollision.verticalMove(xpos[timeZone], ypos[timeZone], border, yOffset, currentLevel.levelseries.get(currentLevel.currentTZ), MainView.solidBlocks);
				float fit = xpos[timeZone] - 30.1f - entX - entXVel;
            	ypos[timeZone] += absMin(yOffset, fit);
                //MainView.cam.y = ypos[timeZone] + 36f;
                for (int i = currentLevel.currentTZ; i < currentLevel.levelseries.size(); i++) {
                    if (xpos[i] > -1) {
                        xpos[i] = xpos[timeZone];
                        ypos[i] = ypos[timeZone];
                    }
                }
            }
        }
        return new float[] {xOffset, yOffset};
    }

    public void render(Camera cam, int timeZone){
        if(xpos[timeZone] >= 0) {
            if(ypos[timeZone] >= 0) {
                levelProgram.enable();
                spriteVAO.prepareRender();
                float x = xpos[timeZone];
                float y = -ypos[timeZone];
                sprite.bind();
                levelProgram.shaders[0].uniforms[0].set(cam.getCameraMatrix(x, y + 2 * cam.y, 0));
                levelProgram.shaders[0].uniforms[1].set(new Matrix4f());
                spriteVAO.fullRender();
            }
        }
    }

}