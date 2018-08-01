package Game;

import engine.OpenGL.ShaderProgram;
import engine.OpenGL.Texture;
import engine.OpenGL.VAO;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class SpriteButton
{
    public float xpos;
    public float ypos;
    public float width;
    public float height;
    public Texture sprite;
    public VAO vao;
    public static ShaderProgram shader;


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
    
    public boolean render(float cursorX, float cursorY) {
    	boolean ret = false;
    	shader.enable();
    	if (hoverCheck(cursorX, cursorY)) {
    		ret = true;
    		shader.shaders[2].uniforms[0].set(new Vector4f(1f, 1f, 0f, 1f));
		}else {
			shader.shaders[2].uniforms[0].set(new Vector4f(0f, 0f, 0f, 0f));
		}
    	sprite.bind();
    	vao.fullRender();
    	return ret;
	}

    public boolean render(float cursorX, float cursorY, float aspectRatio) {
        boolean ret = false;
        shader.enable();
        shader.shaders[0].uniforms[0].set(aspectRatio);
        if (hoverCheck(cursorX, cursorY)) {
            ret = true;
            shader.shaders[2].uniforms[0].set(new Vector4f(1f, 1f, 0f, 1f));
        }else {
            shader.shaders[2].uniforms[0].set(new Vector4f(0f, 0f, 0f, 0f));
        }
        sprite.bind();
        vao.fullRender();
        return ret;
    }
}
