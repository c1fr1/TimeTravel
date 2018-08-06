package Game;

import engine.OpenGL.EnigWindow;
import engine.OpenGL.ShaderProgram;
import engine.OpenGL.Texture;
import engine.OpenGL.VAO;
import org.joml.Matrix4f;

public class StringRenderer {
	public static Texture[] textures = new Texture[62];
	public static VAO shape;//9*17
	public static ShaderProgram shader;
	public Matrix4f mat;
	public float fontSize;
	public float x;
	public float y;
	public boolean centered = true;
	public StringRenderer(int width, int height, int fontSize, float x, float y) {
		if (shape == null) {
			for (int i = 0; i < 10; ++i) {
				textures[i] = new Texture("res/numbers/num-" + i + ".png");
			}
			for (int i = 0; i < 26; ++i) {
				char letter = (char) (i + 'a');
				textures[i + 10] = new Texture("res/letters/lower-" + letter + ".png");
			}
			for (int i = 0; i < 26; ++i) {
				char letter = (char) (i + 'a');
				textures[i + 36] = new Texture("res/letters/upper-" + letter + ".png");
			}
			shape = new VAO(-0.26470588235f, -0.5f, 0.5294117647f, 1f);
			shader = new ShaderProgram("numberShader");
		}
		mat = new Matrix4f().ortho(-width/2, width/2, -height/2, height/2, 0, 1);
		this.fontSize = 1080f * fontSize / (float) height;
		this.x = 1920 * x / (float) width;
		this.y = 1080 * y / (float) height;
	}
	public void renderNum(String str) {
		shader.enable();
		shape.prepareRender();
		float offset = x;
		if (centered) {
			offset -= (float) (str.length() - 1) * 0.5294117647f * fontSize/2;
		}
		for (int i = 0; i < str.length(); ++i) {
			char ch = str.charAt(i);
			if (ch != ' ') {
				if (ch < ':') {
					textures[Character.getNumericValue(str.charAt(i))].bind();
				} else if (ch < '[') {
					textures[ch - '7'].bind();
				} else if (ch < '{') {
					textures[ch - '='].bind();
				}
				Matrix4f temp = new Matrix4f(mat);
				shader.shaders[0].uniforms[0].set(temp.translate(offset + ((float) i) * 0.5294117647f * fontSize, y, 0f).scale(fontSize));
				shape.drawTriangles();
			}
		}
		shape.unbind();
	}
}