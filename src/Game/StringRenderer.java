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
		float semiWidth = ((float) width) / 2f;
		float semiHeight = ((float) height) / 2f;
		mat = new Matrix4f().ortho(-semiWidth, semiWidth, -semiHeight, semiHeight, 0, 1);
		this.fontSize = 1080f * fontSize / semiHeight;
		this.x = /*1920f * */x / semiWidth;
		this.y = /*1080f * */y / semiHeight;
		System.out.println(this.fontSize);
		System.out.println(this.x);
		System.out.println(this.y);
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
