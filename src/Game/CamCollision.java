package Game;

import engine.Entities.Camera;
import org.joml.Vector2f;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.logging.Level;

public class CamCollision
{
    //tests if you are colliding with the obstacle in your current position
    public static boolean isColliding(float x, float y, int border, ArrayList<Character[]> room, char obstacle)
    {
        boolean colliding = false;
        int[] xsquares = new int[4];
        int[] ysquares = new int[4];
        //point arrays
        xsquares[0] = (int)((x - border)/50);
        xsquares[1] = (int)((x + border)/50);
        xsquares[2] = (int)((x + border)/50);
        xsquares[3] = (int)((x - border)/50);

        ysquares[0] = (int)((y + border)/50);
        ysquares[1] = (int)((y + border)/50);
        ysquares[2] = (int)((y - border)/50);
        ysquares[3] = (int)((y - border)/50);

        for (int i = 0; i < 4; i++)
        {
            if (room.get(ysquares[i])[xsquares[i]] == obstacle)
            {
                colliding = true;
                break;
            }
        }
        return colliding;
    }

    public static boolean camEntityCollision(Entity a, int timeZone, float x, float y, int camBorder)
    {
        boolean colliding = false;
        //if the entity exists in the given timezone
        if (a.xpos[timeZone] > -1) {
            //if the camera is colliding with the entity
            if (a.xpos[timeZone] + a.border >= x - camBorder &&
                    a.xpos[timeZone] - a.border <= x + camBorder) {
                if (a.ypos[timeZone] + a.border >= y - camBorder &&
                        a.ypos[timeZone] - a.border <= y + camBorder) {
                    colliding = true;
                }
            }
        }
        return colliding;
    }

    public static Entity checkEntityColliding(LevelBase currentLevel, int timeZone, float x, float y)
    {
        for (int i = 0; i < currentLevel.entities.size(); i++)
        {
            if (camEntityCollision(currentLevel.entities.get(i),timeZone, x, y, 15))
            {
                return currentLevel.entities.get(i);
            }
        }
        return null;
    }

    //snaps to collisions with entities
    public static float entitySnapCollisionX(LevelBase currentLevel, int timeZone, float x, float y, float hspeed)
    {
        float newx = x;
        Entity e = checkEntityColliding(currentLevel, timeZone, x, y);
        if (e != null)
        {
            float xsave = x - hspeed;
            //you are on the side
            if ((e.ypos[timeZone] + e.border >= y - 15 &&
                    e.ypos[timeZone] - e.border <= y + 15))
            {
                //you are to the left and moving right
                if (xsave < e.xpos[timeZone] && hspeed > 0)
                {
                    float snap = e.xpos[timeZone] - e.border;
                    newx = snap * Math.round(xsave/snap);
                    newx += 15.001 * Util.getSign(-hspeed);
                }
                else if (xsave > e.xpos[timeZone] && hspeed < 0)
                {
                    float snap = e.xpos[timeZone] + e.border;
                    newx = snap * Math.round(xsave/snap);
                    newx += 15.001 * Util.getSign(-hspeed);
                }
            }
        }
        return newx - x;
    }

    public static float entitySnapCollisionY(LevelBase currentLevel, int timeZone, float x, float y, float vspeed)
    {
        float newy = y;
        Entity e = checkEntityColliding(currentLevel, timeZone, x, y);
        if (e != null)
        {
            float ysave = y - vspeed;
            //you are on the side
            if ((e.xpos[timeZone] + e.border >= x - 15 &&
                    e.xpos[timeZone] - e.border <= x + 15))
            {
                //you are to the left and moving right
                if (ysave < e.ypos[timeZone] && vspeed > 0)
                {
                    float snap = e.ypos[timeZone] - e.border;
                    newy = snap * Math.round(ysave/snap);
                    newy += 15.001 * Util.getSign(-vspeed);
                }
                else if (ysave > e.ypos[timeZone] && vspeed < 0)
                {
                    float snap = e.ypos[timeZone] + e.border;
                    newy = snap * Math.round(ysave/snap);
                    newy += 15.001 * Util.getSign(-vspeed);
                }
            }
        }
        return newy - y;
    }

    public static char checkCharColliding(float x, float y, int border, ArrayList<Character[]> room, char[] obstacles)
    {
        char solid = '`';
        for (int i = 0; i < obstacles.length; i++)
        {
            //revert to last position before colliding - touching the wall
            if (isColliding(x,y,border,room,obstacles[i]))
            {
                solid = obstacles[i];
                break;
            }
        }
        return solid;
    }

    //moves you in the direction until you hit a wall and then moves you to touching the wall
    public static float horizontalMove(float x, float y, int border, float hspeed,
                                       ArrayList<Character[]> room, char[] obstacles)
    {
        float xsave = x;
        x += hspeed;
        char block = checkCharColliding(x,y,border,room,obstacles);
        //pass doors
        if (block == '>')
        {
            //set block to open
            block = '`';
            //check for moving in wrong direction and wrong side of door
            if (hspeed < 0 && !isColliding(xsave,y,border,room,'>'))
            {
                //if on outside of door, parse as wall
                block = '#';
            }
        }
        else if (block == '<')
        {
            block = '`';
            if (hspeed > 0 && !isColliding(xsave,y,border,room,'<'))
            {
                block = '#';
            }
        }
        else if (block == '^' || block == 'v')
        {
            block = '`';
        }
        //snap to collision contact
        if (block != '`')
        {
            x = 50 * Math.round(xsave/50);
            x += 15.001 * Util.getSign(-hspeed);
        }

        return x - xsave;
    }

    public static float verticalMove(float x, float y, int border, float vspeed,
                                     ArrayList<Character[]> room, char[] obstacles)
    {
        float ysave = y;
        y += vspeed;
        char block = checkCharColliding(x,y,border,room,obstacles);
        //pass one way doors
        if (block == '^')
        {
            //set block to open
            block = '`';
            //check for moving in wrong direction and wrong side of door
            if (vspeed > 0 && !isColliding(x,ysave,border,room,'^'))
            {
                //if on outside of door, parse as wall
                block = '#';
            }
        }
        else if (block == 'v')
        {
            block = '`';
            if (vspeed < 0 && !isColliding(x,ysave,border,room,'v'))
            {
                block = '#';
            }
        }
        else if (block == '<' || block == '>')
        {
            block = '`';
        }
        //snap to collision contact
        if (block != '`')
        {
            y = 50 * Math.round(ysave/50);
            y += 15.001 * Util.getSign(-vspeed);
        }
        return y - ysave;
    }
}