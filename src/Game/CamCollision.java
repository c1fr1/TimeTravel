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
    public static float horizontalMove(float x, float y, int border, float hspeed,
                                       ArrayList<Character[]> room, char[] obstacles)
    {
        float xsave = x;
        x += hspeed;
        char block = checkAllCollision(x,y,border,room,obstacles);
        //pass doors
        if (block == '^' || block == 'v' || block == '<' || block == '>') {
            if (block == '>') {
                block = '`';
                if (hspeed < 0 && quadrantCheck(x, y, border, room, 1) == ' ') {
                    if (quadrantCheck(xsave, y, border, room, 3) == ' ') {
                        block = '#';
                    } else {
                        x -= hspeed;
                    }
                }
            }
            else if (block == '<') {
                block = '`';
                if (hspeed > 0 && quadrantCheck(x, y, border, room, 3) == ' ') {
                    if (quadrantCheck(xsave, y, border, room, 1) == ' ') {
                        block = '#';
                    } else {
                        x += hspeed;
                    }
                }
            }
            else
            {
                block = '`';
            }
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
        char block = checkAllCollision(x,y,border,room,obstacles);
        //pass one way doors
        if (block == '^' || block == 'v' || block == '<' || block == '>')
        {
            if (block == '^')
            {
                //set block to open
                block = '`';
                //check for moving in wrong direction and placement to see where avatar is in relation to block
                if (vspeed > 0 && quadrantCheck(x, y, border, room, 2) == ' ')
                {
                    if (quadrantCheck(x, ysave, border, room, 0) == ' ')
                    {
                        //if on outside use wall
                        block = '#';
                    }
                    else
                    {
                        //if on inside stop backwards movement once past threshhold
                        y -= vspeed;
                    }
                }
            }
            else if (block == 'v')
            {
                block = '`';
                if (vspeed < 0 && quadrantCheck(x, y, border, room, 0) == ' ') {
                    if (quadrantCheck(x, ysave, border, room, 2) == ' ') {
                        block = '#';
                    } else {
                        y += vspeed;
                    }
                }
            }
            else
            {
                block = '`';
            }
        }
        /*
        if (block == '^')
        {
            block = '`';
            if (vspeed > 0 && quadrantCheck(x,y,border,room,2) == ' ')
            {
                if (quadrantCheck(x,ysave,border,room,0) == ' ')
                {
                    block = '#';
                }
                else {
                    y -= vspeed;
                }
            }
        }
        */
        //snap to collision contact
        if (block != '`')
        {
            y = 50 * Math.round(ysave/50);
            y += 15.001 * Util.getSign(-vspeed);
        }
        return y - ysave;
    }

    //returns the object in the specified quadrant
    public static char quadrantCheck(float x, float y, int border, ArrayList<Character[]> room, int quadrant)
    {
        int xpos = 0;
        int ypos = 0;
        switch (quadrant)
        {
            case 0:
                xpos = (int)((x - border)/50);
                ypos = (int)((y + border)/50);
                break;
            case 1:
                xpos = (int)((x + border) / 50);
                ypos = (int)((y + border) / 50);
                break;
            case 2:
                xpos = (int)((x + border) / 50);
                ypos = (int)((y - border) / 50);
                break;
            case 3:
                xpos = (int)((x - border) / 50);
                ypos = (int)((y - border) / 50);
                break;
        }
        return room.get(ypos)[xpos];
    }
}
/*
        if (block == '^' || block == 'v' || block == '<' || block == '>')
        {
            block = '`';
            if (block == '^') {
                if (vspeed > 0 && quadrantCheck(x, y, border, room, 2) == ' ') {
                    if (quadrantCheck(x, ysave, border, room, 0) == ' ') {
                        block = '#';
                    } else {
                        y -= vspeed;
                    }
                }
            }
            else if (block == 'v')
            {
                if (vspeed < 0 && quadrantCheck(x, y, border, room, 0) == ' ') {
                    if (quadrantCheck(x, ysave, border, room, 2) == ' ') {
                        block = '#';
                    } else {
                        y += vspeed;
                    }
                }
            }
        }
         */