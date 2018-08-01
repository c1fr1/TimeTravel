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

    public static boolean camEntityCollision(Entity a, int timeZone)
    {
        boolean overlap = false;
        if (a.xpos[timeZone] + a.border >= MainView.cam.x - 15 &&
                a.xpos[timeZone] - a.border <= MainView.cam.x + 15)
        {//this is literally the hardest possible organization for readability. if you want to do multiple if statements, do four if statements. If you want one if statement, do one if statement.
            if (a.ypos[timeZone] + a.border >= MainView.cam.y - 15 &&
                    a.ypos[timeZone] - a.border <= MainView.cam.y + 15)
            {
                overlap = true;//declaration of the overlap variable is useless, overlap will never be set to false after this, so just return true here
            }
        }
        return overlap;//and return false here.
    }
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

    //sets box movement
    public void getBoxMovement(LevelBase currentLevel, int timeZone, float camHSpeed, float camVSpeed) {//x and y are much more standard than horizontal and vertical, think 3d.
        //if you are colliding with the camera
		//Nathans old
        /*if (camEntityCollision(currentLevel.entities.get(arrayIndex), currentLevel.currentTZ))  {
            if ((camHSpeed > 0 && MainView.cam.x < xpos[timeZone])) {
                xpos[timeZone] = MainView.cam.x + camHSpeed + 30f;
            }else if ((camHSpeed < 0 && MainView.cam.x > xpos[timeZone])) {
				xpos[timeZone] = MainView.cam.x + camHSpeed - 30f;
			}
			
            /*CamCollision.horizontalMove(xpos[timeZone], ypos[timeZone], border, camHSpeed,
                    currentLevel.levelseries.get(currentLevel.currentTZ), MainView.solidBlocks); FOR FUTURE STUFF*//*

            
            if ((camVSpeed > 0 && MainView.cam.y < ypos[timeZone])) {
                ypos[timeZone] = MainView.cam.y + camVSpeed + 30f;
            }else if ((camVSpeed < 0 && MainView.cam.y > ypos[timeZone])) {
				ypos[timeZone] = MainView.cam.y + camVSpeed - 30f;
			}
        }*/
		if (playerAABB(camHSpeed, camVSpeed)) {
			if ((camHSpeed > 0 && MainView.cam.x + 29 < xpos[timeZone])) {
				xpos[timeZone] = MainView.cam.x + camHSpeed + 30f;
			}else if ((camHSpeed < 0 && MainView.cam.x - 29 > xpos[timeZone])) {
				xpos[timeZone] = MainView.cam.x + camHSpeed - 30f;
			}
			if ((camVSpeed > 0 && MainView.cam.y + 29 < ypos[timeZone])) {
				ypos[timeZone] = MainView.cam.y + camVSpeed + 30f;
			}else if ((camVSpeed < 0 && MainView.cam.y - 29 > ypos[timeZone])) {
				ypos[timeZone] = MainView.cam.y + camVSpeed - 30f;
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
    /* DO NOT REMOVE
    public static boolean entityCollision(Entity a, Entity b, int timeZone)
    {
        boolean overlap = false;
        if (b.xpos[timeZone] > -1)
        {
            if ((a.xpos[timeZone] + a.border >= b.xpos[timeZone] - b.border &&
                 a.xpos[timeZone] + a.border <= b.xpos[timeZone] + b.border) ||
                (a.xpos[timeZone] - a.border >= b.xpos[timeZone] - b.border &&
                 a.xpos[timeZone] - a.border <= b.xpos[timeZone] + b.border))
            {
                if ((a.ypos[timeZone] + a.border >= b.ypos[timeZone] - b.border &&
                     a.ypos[timeZone] + a.border <= b.ypos[timeZone] + b.border) ||
                    (a.ypos[timeZone] - a.border >= b.ypos[timeZone] - b.border &&
                     a.ypos[timeZone] - a.border <= b.ypos[timeZone] + b.border))
                {
                    overlap = true;
                }
            }
        }
        return overlap;
    }

    public Entity entityCheck(LevelBase currentLevel, int timeZone)
    {
        if (currentLevel.entities.get(arrayIndex).xpos[timeZone] > -1) {
            for (int i = 0; i < currentLevel.entities.size(); i++) {
                //you are not looking at your own index and are colliding with another entity in the given timezone
                if (i != arrayIndex) {
                    if (entityCollision(currentLevel.entities.get(arrayIndex), currentLevel.entities.get(i), timeZone)) {
                        return currentLevel.entities.get(i);
                    }
                }
            }
        }
        return null;
    }

    public Entity cameraCheck(LevelBase currentLevel, int timeZone)
    {
        if (currentLevel.entities.get(arrayIndex).xpos[timeZone] > -1) {
            for (int i = 0; i < currentLevel.entities.size(); i++) {
                //you are not looking at your own index and are colliding with another entity in the given timezone
                if (i != arrayIndex) {
                    if (camEntityCollision(currentLevel.entities.get(i), timeZone)) {
                        return currentLevel.entities.get(i);
                    }
                }
            }
        }
        return null;
    }
*/

}