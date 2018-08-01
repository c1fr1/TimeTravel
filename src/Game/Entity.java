package Game;

import engine.Entities.Camera;
import engine.OpenGL.ShaderProgram;
import engine.OpenGL.Texture;
import engine.OpenGL.VAO;
import org.joml.Matrix4f;
import sun.applet.Main;

import java.util.ArrayList;
import java.util.logging.Level;

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
	public boolean playerAABB(float xvel, float yvel) {
		if (xpos[MainView.currentLevel.currentTZ] + border > MainView.cam.x - 15 + xvel) {
			if (xpos[MainView.currentLevel.currentTZ] - border < MainView.cam.x + 15 + xvel) {
				if (ypos[MainView.currentLevel.currentTZ] + border > MainView.cam.y - 15 + yvel) {
					if (ypos[MainView.currentLevel.currentTZ] - border < MainView.cam.y + 15 + yvel) {
						return true;
					}
				}
			}
		}
		return false;
	}
    public static boolean entityCollision(Entity a, Entity b, int timeZone, float xvel, float yvel) {
        boolean overlap = false;
        if (b.xpos[timeZone] > -1) {
            if (a.xpos[MainView.currentLevel.currentTZ] + 15 + xvel > b.ypos[MainView.currentLevel.currentTZ] - 15) {
                if (a.xpos[MainView.currentLevel.currentTZ] - 15 + xvel< b.xpos[MainView.currentLevel.currentTZ] + 15) {
                    if (a.ypos[MainView.currentLevel.currentTZ] + 15 + yvel> b.ypos[MainView.currentLevel.currentTZ] - 15) {
                        if (a.ypos[MainView.currentLevel.currentTZ] - 15 + yvel < b.ypos[MainView.currentLevel.currentTZ] + 15) {
                            return true;
                        }
                    }
                }
            }
        }
        return overlap;
    }
    //sets box movement
    public void getBoxMovement(LevelBase currentLevel, int timeZone, float camHSpeed, float camVSpeed) {
        int lastX = (int)MainView.cam.x;
        int lastY = (int)MainView.cam.y;
    	if (playerAABB(camHSpeed, camVSpeed)) {
			if ((camHSpeed > 0 && MainView.cam.x + 29 < xpos[timeZone])) {
				xpos[timeZone] += CamCollision.horizontalMove(xpos[timeZone], ypos[timeZone], border, camHSpeed, currentLevel.levelseries.get(currentLevel.currentTZ), MainView.solidBlocks);
                for (int i = currentLevel.currentTZ; i < currentLevel.levelseries.size(); i ++) {
                    xpos[i] = xpos[timeZone];
                    ypos[i] = ypos[timeZone];
                }
                MainView.cam.x = xpos[timeZone]-38f;
			}else if ((camHSpeed < 0 && MainView.cam.x - 29 > xpos[timeZone])) {
				xpos[timeZone] += CamCollision.horizontalMove(xpos[timeZone], ypos[timeZone], border, camHSpeed, currentLevel.levelseries.get(currentLevel.currentTZ), MainView.solidBlocks);
                for (int i = currentLevel.currentTZ; i < currentLevel.levelseries.size(); i ++) {
                    xpos[i] = xpos[timeZone];
                    ypos[i] = ypos[timeZone];
                    MainView.cam.x = xpos[timeZone]+38f;
                }
			}
			if ((camVSpeed > 0 && MainView.cam.y + 29 < ypos[timeZone])) {
				ypos[timeZone] += CamCollision.verticalMove(xpos[timeZone], ypos[timeZone], border, camVSpeed, currentLevel.levelseries.get(currentLevel.currentTZ), MainView.solidBlocks);
                for (int i = currentLevel.currentTZ; i < currentLevel.levelseries.size(); i ++) {
                    xpos[i] = xpos[timeZone];
                    ypos[i] = ypos[timeZone];
                    MainView.cam.y = ypos[timeZone]-38f;
                }
			}else if ((camVSpeed < 0 && MainView.cam.y - 29 > ypos[timeZone])) {
				ypos[timeZone] += CamCollision.verticalMove(xpos[timeZone], ypos[timeZone], border, camVSpeed, currentLevel.levelseries.get(currentLevel.currentTZ), MainView.solidBlocks);
                for (int i = currentLevel.currentTZ; i < currentLevel.levelseries.size(); i ++) {
                    xpos[i] = xpos[timeZone];
                    ypos[i] = ypos[timeZone];
                    MainView.cam.y = ypos[timeZone]+38f;
                }
			}
			if (lastX == (int)MainView.cam.x && lastY == (int)MainView.cam.y) {
                MainView.entOffset = 0;
                System.out.println("a");
            }
            else {
			    MainView.entOffset = 1;
            }
		}
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