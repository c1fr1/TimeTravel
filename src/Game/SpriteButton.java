package Game;

import engine.OpenGL.Texture;

public class SpriteButton
{
    float xpos;
    float ypos;
    float width;
    float height;
    Texture sprite;


    public SpriteButton(float x, float y, float width, float height, String path)
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

    public float getXpos()
    {
        return xpos;
    }

    public void setXpos(int xpos)
    {
        this.xpos = xpos;
    }

    public float getYpos() {
        return ypos;
    }

    public void setYpos(int ypos) {
        this.ypos = ypos;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public float getHeight() {
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
