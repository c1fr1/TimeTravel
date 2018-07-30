package Game;

import sun.applet.Main;

public class Entity
{
    //This array holds a whole bunch of x and y coordinates regarding the entity's position. To get the position in the nth timezone, the indexes are n*2-2 and n*2-1
    //positions[0], positions[1] is the location of the entity in the first timezone. In the 4th timezone, positions[6] and positions[7] are the relevant indexes.
    public static float[] positions;
    public static float x;
    public static float y;
    public static int border;
    public float hspeed;
    public float vspeed;

    public Entity(float startX, float startY, int amountOfTimezones) {
        positions = new float[amountOfTimezones];
        //Fill array with starting positions
        for (int i = 0; i < amountOfTimezones*2; i +=2) {
            positions[i]=startX;
            positions[i+1]=startY;
        }
    }

    public static boolean entityCollision(float xSelf, float ySelf, int borderSelf, float xOther, float yOther, int borderOther)
    {
        boolean overlap = false;

        if ((xSelf + borderSelf >= xOther - borderOther && xSelf - borderSelf <= xOther - borderOther) ||
                (xSelf - borderSelf <= xOther + borderOther && xSelf + borderSelf >= xOther + borderOther))
        {
            if ((ySelf + borderSelf >= yOther - borderOther && ySelf - borderSelf <= yOther - borderOther) ||
                    (ySelf - borderSelf <= yOther + borderOther && ySelf + borderSelf >= yOther + borderOther))
            {
                overlap = true;
            }
        }
        return overlap;
    }

    public static void getEntityX(float vspeed, float hspeed, float xOther, float yOther)
    {
        if (entityCollision(x,y,border,xOther,yOther,15))
        {
            if ((x + border >= xOther - 15 && x - border <= xOther - 15))
            {
                if (hspeed < 0)
                {
                    float xstep = (float)Math.sqrt(vspeed*vspeed + hspeed*hspeed);
                    while (entityCollision(x,y,border,xOther,yOther,15))
                    {
                        x -= xstep;
                    }
                    x += hspeed;
                }
            }
        }
    }

}
