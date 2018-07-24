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

    public static float getMoveX(float x, float y, float hSpeed, float vSpeed, ArrayList<Character[]> room){
        Vector2f speedVector = new Vector2f(hSpeed, vSpeed);
        Vector2f positionVector = new Vector2f(x,y);
        Vector2f totalVector = speedVector.add(positionVector);

        while(room.get((int)(totalVector.x/50f))[(int)(totalVector.y/50f)] == '#'){
            totalVector.set((float)Math.floor(totalVector.x-(hSpeed/16f)), totalVector.y);
        }
        return totalVector.x;

    }

    public static float getMoveY(float x, float y, float hSpeed, float vSpeed, ArrayList<Character[]> room){
        Vector2f speedVector = new Vector2f(hSpeed, vSpeed);
        Vector2f positionVector = new Vector2f(x,y);
        Vector2f totalVector = speedVector.add(positionVector);

        while(room.get((int)(totalVector.x/50f))[(int)(totalVector.y/50f)] == '#'){
            totalVector.set(totalVector.x, (float)Math.floor(totalVector.y-(vSpeed/16f)));
        }
        return totalVector.y;


    }
}
