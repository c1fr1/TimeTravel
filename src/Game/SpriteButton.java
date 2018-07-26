package Game;

import engine.OpenGL.ShaderProgram;
import engine.OpenGL.Texture;
import engine.OpenGL.VAO;

public class SpriteButton
{
    public float xpos;
    public float ypos;
    public float width;
    public float height;
    public Texture sprite;
    public VAO vao;
    public ShaderProgram shader;


    public SpriteButton(float x, float y, float width, float height, String path)
    {
        xpos = x;
        ypos = y;
        this.width = width;
        this.height = height;
        sprite = new Texture(path);
        vao = new VAO(x, y, width, height);
    }
	
	public SpriteButton(float x, float y, float width, float height, String path, float aspectRatio)
	{
		xpos = x * aspectRatio;
		ypos = y;
		this.width = width * aspectRatio;
		this.height = height;
		sprite = new Texture(path);
		vao = new VAO(x, y, width, height);
	}
    
    public boolean hoverCheck(float xmouse, float ymouse)
    {
        if (xmouse <= xpos + width && xmouse >= xpos)
        {
            if (ymouse >= ypos && ymouse <= ypos + height)
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

    public float getYpos() {
        return ypos;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
    
    public void render() {
    	sprite.bind();
    	vao.fullRender();
	}

	public void setPath(String path)
    {
        sprite = new Texture(path);
    }
}
