package Game.Buttons;

import engine.OpenGL.ShaderProgram;
import engine.OpenGL.Texture;
import engine.OpenGL.VAO;

public class DoubleTextureButton extends SpriteButton {
	public Texture textureA;
	public Texture textureB;
	public VAO vao;
	public static ShaderProgram dtexShader;
	public DoubleTextureButton(float x, float y, float width, float height, String pathA, String pathB) {
		xpos = x;
		ypos = y;
		this.width = width;
		this.height = height;
		textureA = new Texture(pathA);
		textureB = new Texture(pathB);
		vao = new VAO(x, y, width, height);
	}
	
	public DoubleTextureButton(float x, float y, float width, float height, String pathA, String pathB, float aspectRatio) {
		xpos = x * aspectRatio;
		ypos = y;
		this.width = width * aspectRatio;
		this.height = height;
		textureA = new Texture(pathA);
		textureB = new Texture(pathB);
		vao = new VAO(x, y, width, height);
	}
	
	@Override
	public boolean render(float cursorX, float cursorY) {
		boolean ret = hoverCheck(cursorX, cursorY);
		dtexShader.enable();
		if (ret) {
			textureB.bind();
		}else {
			textureA.bind();
		}
		vao.fullRender();
		return ret;
	}
	
	@Override
	public boolean render(float cursorX, float cursorY, float aspectRatio) {
		boolean ret = hoverCheck(cursorX, cursorY);
		dtexShader.enable();
		dtexShader.shaders[0].uniforms[0].set(aspectRatio);
		if (ret) {
			textureB.bind();
		}else {
			textureA.bind();
		}
		vao.fullRender();
		return ret;
	}

	public boolean render(float cursorX, float cursorY, boolean manualOverride) {
		boolean ret = hoverCheck(cursorX, cursorY);
		dtexShader.enable();
		if (manualOverride) {
			textureB.bind();
		}else {
			textureA.bind();
		}
		vao.fullRender();
		return ret;
	}

	public boolean render(float cursorX, float cursorY, float aspectRatio, boolean manualOverride) {
		boolean ret = hoverCheck(cursorX, cursorY);
		dtexShader.enable();
		dtexShader.shaders[0].uniforms[0].set(aspectRatio);
		if (manualOverride) {
			textureB.bind();
		}else {
			textureA.bind();
		}
		vao.fullRender();
		return ret;
	}
}
