package Game;

import engine.Entities.Camera;
import org.joml.Vector2f;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.logging.Level;

public class CamCollision {

    public static char checkCollision(float x, float y, float hSpeed, float vSpeed, ArrayList<Character[]> room){
        Vector2f speedVector = new Vector2f(hSpeed, vSpeed);
        Vector2f positionVector = new Vector2f(x,y);
        Vector2f totalVector = speedVector.add(positionVector);
        totalVector.set(totalVector.x/50f, totalVector.y/50f);
        return room.get((int)totalVector.y)[(int)totalVector.x];
    }

    public static float getMoveX(float x, float y, float hSpeed, float vSpeed, ArrayList<Character[]> room, char[] jumpChar){
        Vector2f speedVector = new Vector2f(hSpeed, vSpeed);
        Vector2f positionVector = new Vector2f(x,y);
        Vector2f totalVector = positionVector;

        boolean go = true;
        int count = 16;
        while (go && count > 1) {
            for(char i: jumpChar) {
                if(room.get((int) (totalVector.y / 50f))[(int) (totalVector.x / 50f)] == i){
                    go = false;
                    break;
                }
            }
            if(go) {
                totalVector.add(new Vector2f(speedVector.x / 16f, speedVector.y / 16f));
                count--;
            }
        }
        return totalVector.x;
    }

    public static float getMoveY(float x, float y, float hSpeed, float vSpeed, ArrayList<Character[]> room, char[] jumpChar){
        Vector2f speedVector = new Vector2f(hSpeed, vSpeed);
        Vector2f positionVector = new Vector2f(x,y);
        Vector2f totalVector = positionVector;

        boolean go = true;
        int count = 16;
        while (go && count > 1) {
            for(char i: jumpChar) {
                if(room.get((int) (totalVector.y / 50f))[(int) (totalVector.x / 50f)] == i){
                    go = false;
                    break;
                }
            }
            if(go) {
                totalVector.add(new Vector2f(speedVector.x / 16f, speedVector.y / 16f));
                count--;
            }
        }
        return totalVector.y;
    }

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
}
