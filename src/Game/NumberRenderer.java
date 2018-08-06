package Game;

import engine.OpenGL.EnigWindow;
import engine.OpenGL.ShaderProgram;
import engine.OpenGL.Texture;
import engine.OpenGL.VAO;
import org.joml.Matrix4f;
import org.joml.Vector2f;

public class NumberRenderer {
	public static Texture[] textures = new Texture[10];
	public static VAO shape;//9*17
	public static ShaderProgram shader;
	public Matrix4f mat;
	public float fontSize;
	public float x;
	public float y;
	public boolean centered = true;
	public NumberRenderer(int width, int height, int fontSize, float x, float y) {
		if (shape == null) {
			for (int i = 0; i < 10; ++i) {
				textures[i] = new Texture("res/numbers/num-" + i + ".png");
			}
			shape = new VAO(-0.26470588235f, -0.5f, 0.5294117647f, 1f);
			shader = new ShaderProgram("numberShader");
		}
		mat = new Matrix4f().ortho(-width/2, width/2, -height/2, height/2, 0, 1);
		this.fontSize = 1080f * fontSize/(float) EnigWindow.mainWindow.getHeight();
		this.x = x;
		this.y = y;
	}
	public void renderNum(int num) {
		shader.enable();
		String str = "" + num;
		shape.prepareRender();
		float offset = x;
		if (centered) {
			offset -= (float) (str.length() - 1) * 0.5294117647f * fontSize/2;
		}
		for (int i = 0; i < str.length(); ++i) {
			textures[Character.getNumericValue(str.charAt(i))].bind();
			Matrix4f temp = new Matrix4f(mat);
			shader.shaders[0].uniforms[0].set(temp.translate(offset + ((float) i) * 0.5294117647f * fontSize, y, 0f).scale(fontSize));
			shape.drawTriangles();
		}
		shape.unbind();
	}
}
