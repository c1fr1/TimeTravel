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

    public static char checkAllCollision(float x, float y, int border, ArrayList<Character[]> room, char[] obstacles)
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
    public static float horizontalMovement(float x, float y,
                                           float hspeed, float vspeed, ArrayList<Character[]> room, char[] obstacles)
    {
        float xsave = x;
        x += hspeed;
        //tests if you are going to collide with a solid
        char blocking = checkAllCollision(x,y,15,room,obstacles);
        if (blocking != '`')
        {
            x = xsave;
            float speed = (float)Math.sqrt(vspeed * vspeed + hspeed * hspeed);
            float xstep = hspeed/speed;
            while (!isColliding(x,y,15,room,blocking))
            {
                //increment x until you reach the obstacle and then stop
                x += xstep;
            }
            x -= xstep;
        }
        //if you will collide, move to contact
        return x - xsave;
    }

    public static float verticalMovement(float x, float y,
                                           float hspeed, float vspeed, ArrayList<Character[]> room, char[] obstacles)
    {
        float ysave = y;
        y += vspeed;
        char blocking = checkAllCollision(x,y,15,room,obstacles);
        //tests if you are going to collide with a solid
        if (blocking == '^')
        {
            if (vspeed <= 0)
            {
                vspeed = 0;
                blocking = '`';
            }

        }

        if (blocking != '`')
        {
            y = ysave;
            float speed = (float)Math.sqrt(vspeed * vspeed + hspeed * hspeed);
            float ystep = vspeed/speed;
            while (!isColliding(x,y,15,room,blocking))
            {
                //increment y until you reach the obstacle and then stop
                y += ystep;
            }
            y -= ystep;
        }
        //if you will collide, move to contact
        return y - ysave;
    }
}
