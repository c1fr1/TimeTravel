package Game;

import engine.OpenGL.Texture;

public class SpriteButton
{
    int xpos;
    int ypos;
    int width;
    int height;
    Texture sprite;


    public SpriteButton(int x, int y, int width, int height, String path)
    {
        xpos = x;
        ypos = y;
        this.width = width;
        this.height = height;
        sprite = new Texture(path);
    }

    public boolean hoverCheck(float xmouse, float ymouse)
    {
        if (xmouse <= xpos + width && xmouse >= xpos)
        {
            if (ymouse <= ypos && ymouse >= ypos - height)
            {
                return true;
            }
        }
        return false;
    }

    public int getXpos()
    {
        return xpos;
    }

    public void setXpos(int xpos)
    {
        this.xpos = xpos;
    }

    public int getYpos() {
        return ypos;
    }

    public void setYpos(int ypos) {
        this.ypos = ypos;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Texture getSprite() {
        return sprite;
    }

    public void setSprite(Texture sprite) {
        this.sprite = sprite;
    }
}
