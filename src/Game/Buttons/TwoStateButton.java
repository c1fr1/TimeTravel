package Game.Buttons;

import engine.OpenGL.ShaderProgram;
import engine.OpenGL.Texture;
import engine.OpenGL.VAO;
import org.joml.Vector4f;

public class TwoStateButton extends SpriteButton {
    public Texture textureA;
    public Texture textureB;
    public VAO vao;
    public boolean state;
    public static ShaderProgram dtexShader;
    public TwoStateButton(float x, float y, float width, float height, String pathA, String pathB, boolean state) {
        xpos = x;
        ypos = y;
        this.width = width;
        this.height = height;
        textureA = new Texture(pathA);
        textureB = new Texture(pathB);
        this.state = state;
        vao = new VAO(x, y, width, height);
    }

    public TwoStateButton(float x, float y, float width, float height, String pathA, String pathB, boolean state, float aspectRatio) {
        xpos = x * aspectRatio;
        ypos = y;
        this.width = width * aspectRatio;
        this.height = height;
        textureA = new Texture(pathA);
        textureB = new Texture(pathB);
        this.state = state;
        vao = new VAO(x, y, width, height);
    }

    @Override
    public boolean render(float cursorX, float cursorY) {
        boolean ret = hoverCheck(cursorX, cursorY);
        shader.enable();
        if(state) {
            if (ret) {
                shader.shaders[2].uniforms[0].set(new Vector4f(1f, 1f, 0f, 1f));
            } else {
                shader.shaders[2].uniforms[0].set(new Vector4f(0f, 0f, 0f, 0f));
            }
            textureA.bind();
        } else {
            if (ret) {
                shader.shaders[2].uniforms[0].set(new Vector4f(1f, 1f, 0f, 1f));
            } else {
                shader.shaders[2].uniforms[0].set(new Vector4f(0f, 0f, 0f, 0f));
            }
            textureB.bind();
        }
        vao.fullRender();
        return ret;
    }

    @Override
    public boolean render(float cursorX, float cursorY, float aspectRatio) {
        boolean ret = hoverCheck(cursorX, cursorY);
        shader.enable();
        shader.shaders[0].uniforms[0].set(aspectRatio);
        if(state) {
            if (ret) {
                shader.shaders[2].uniforms[0].set(new Vector4f(1f, 1f, 0f, 1f));
            } else {
                shader.shaders[2].uniforms[0].set(new Vector4f(0f, 0f, 0f, 0f));
            }
            textureA.bind();
        } else {
            if (ret) {
                shader.shaders[2].uniforms[0].set(new Vector4f(1f, 1f, 0f, 1f));
            } else {
                shader.shaders[2].uniforms[0].set(new Vector4f(0f, 0f, 0f, 0f));
            }
            textureB.bind();
        }
        vao.fullRender();
        return ret;
    }
}
