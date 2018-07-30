package Game;

import sun.applet.Main;

public class Entity
{
    public static float x;
    public static float y;
    public static int border;
    public float hspeed;
    public float vspeed;

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
