package Game.Buttons;

import engine.OpenGL.Texture;
import engine.OpenGL.VAO;
import org.joml.Vector4f;

public class NoChangeButton extends SpriteButton{
    public Texture sprite;
    public VAO vao;
    public NoChangeButton(float x, float y, float width, float height, String path) {
        xpos = x;
        ypos = y;
        this.width = width;
        this.height = height;
        sprite = new Texture(path);
        vao = new VAO(x, y, width, height);
    }

    public NoChangeButton(float x, float y, float width, float height, String path, float aspectRatio) {
        xpos = x * aspectRatio;
        ypos = y;
        this.width = width * aspectRatio;
        this.height = height;
        sprite = new Texture(path);
        vao = new VAO(x, y, width, height);
    }

    @Override
    public boolean render(float cursorX, float cursorY) {
        boolean ret = hoverCheck(cursorX, cursorY);
        shader.enable();
        shader.shaders[2].uniforms[0].set(new Vector4f(0f, 0f, 0f, 0f));
        sprite.bind();
        vao.fullRender();
        return ret;
    }

    @Override
    public boolean render(float cursorX, float cursorY, float aspectRatio) {
        boolean ret = hoverCheck(cursorX, cursorY);
        shader.enable();
        shader.shaders[2].uniforms[0].set(new Vector4f(0f, 0f, 0f, 0f));
        sprite.bind();
        vao.fullRender();
        return ret;
    }
}
