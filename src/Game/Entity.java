package Game;

import sun.applet.Main;

import java.util.logging.Level;

public class Entity
{
    //This array holds a whole bunch of x and y coordinates regarding the entity's position. To get the position in the nth timezone,
    //the indexes are n*2-2 and n*2-1
    //positions[0], positions[1] is the location of the entity in the first timezone.
    // In the 4th timezone, positions[6] and positions[7] are the relevant indexes.
    public static float[] xpos;
    public static float[] ypos;
    public static int border;
    public static int arrayIndex;
    public float hspeed;
    public float vspeed;

    public Entity(float startX, float startY, int amountOfTimezones, int startZone, int arrayLocation)
    {
        arrayIndex = arrayLocation;
        xpos = new float[amountOfTimezones];
        ypos = new float[amountOfTimezones];
        xpos[startZone] = startX;
        xpos[startZone] = startY;
        for (int i = 0; i < amountOfTimezones; i++)
        {
            if (i != startZone)
            {
                xpos[i] = -1;
                ypos[i] = -1;
            }
        }
    }

    public static boolean entityCollision(Entity a, Entity b, int timeZone)
    {
        boolean overlap = false;
        if (a.xpos[timeZone] > -1 && b.xpos[timeZone] > -1)
        {
            if (a.xpos[timeZone] + a.border >= b.xpos[timeZone] - b.border &&
                    a.xpos[timeZone] - a.border <= b.xpos[timeZone] + b.border)
            {
                if (a.ypos[timeZone] + a.border >= b.ypos[timeZone] - b.border &&
                        a.ypos[timeZone] - a.border <= b.ypos[timeZone] + b.border)
                {
                    overlap = true;
                }
            }
        }
        return overlap;
    }

    public static boolean camEntityCollision(Entity a, int timeZone)
    {
        boolean overlap = false;

        if (a.xpos[timeZone] > -1 && MainView.cam.x > -1) {
            if (a.xpos[timeZone] + a.border >= MainView.cam.x - 15 &&
                    a.xpos[timeZone] - a.border <= MainView.cam.x + 15) {
                if (a.ypos[timeZone] + a.border >= MainView.cam.y - 15 &&
                        a.ypos[timeZone] - a.border <= MainView.cam.y + 15) {
                    overlap = true;
                }
            }
        }
        return overlap;
    }

    public static Entity entityCheck(int timeZone)
    {
        for (int i = 0; i < LevelBase.entities.size(); i++)
        {
            //you are not looking at your own index and are colliding with another entity in the given timezone
            if (i != arrayIndex)
            {
                if (entityCollision(LevelBase.entities.get(arrayIndex), LevelBase.entities.get(i), timeZone))
                {
                    return LevelBase.entities.get(i);
                }
            }
        }
        return null;
    }

    public static Entity cameraCheck(int timeZone)
    {
        for (int i = 0; i < LevelBase.entities.size(); i++)
        {
            //you are not looking at your own index and are colliding with another entity in the given timezone
            if (i != arrayIndex)
            {
                if (camEntityCollision(LevelBase.entities.get(i), timeZone))
                {
                    return LevelBase.entities.get(i);
                }
            }
        }
        return null;
    }

}
/*for (int i = 0; i < MainView.entities.size(); i ++) {
            CamCollision.verticalMove(MainView.entities.get(i).positions[MainView.currentLevel.currentTZ*2],cam.y,15,vSpeed,
                    currentLevel.levelseries.get(currentLevel.currentTZ),solidBlocks);
        }
        */