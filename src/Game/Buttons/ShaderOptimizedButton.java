package Game.Buttons;

import engine.OpenGL.Texture;
import engine.OpenGL.VAO;
import org.joml.Vector4f;

public class ShaderOptimizedButton extends SpriteButton {
	public Texture sprite;
	public VAO vao;
	public ShaderOptimizedButton(float x, float y, float width, float height, String path) {
		xpos = x;
		ypos = y;
		this.width = width;
		this.height = height;
		sprite = new Texture(path);
		vao = new VAO(x, y, width, height);
	}
	
	public ShaderOptimizedButton(float x, float y, float width, float height, String path, float aspectRatio) {
		xpos = x * aspectRatio;
		ypos = y;
		this.width = width * aspectRatio;
		this.height = height;
		sprite = new Texture(path);
		vao = new VAO(x, y, width, height);
	}
	
	@Override
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
	
	@Override
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
