package Game;

import engine.OpenGL.EnigWindow;
import engine.OpenGL.ShaderProgram;
import engine.OpenGL.Texture;
import engine.OpenGL.VAO;
import org.joml.Matrix4f;
import org.joml.Vector4f;

public class StringRenderer {
	public static Texture[] textures = new Texture[62];
	public static VAO shape;//9*17
	public static ShaderProgram shader;
	public Matrix4f mat;
	public float fontSize;
	public float x;
	public float y;
	public boolean centered = true;
	public Vector4f color = new Vector4f(1f, 1f, 1f, 1f);
	public Vector4f selectColor = new Vector4f(.2f, .2f, 1f, 1f);
	public Vector4f unselectedColor =  new Vector4f(1f,1f,1f,1f);
	public StringRenderer(int fontSize, float x, float y) {
		if (shape == null) {
			for (int i = 0; i < 10; ++i) {
				textures[i] = new Texture("res/numbers/num-" + i + ".png");
			}
			for (int i = 0; i < 26; ++i) {
				char letter = (char) (i + 'a');
				textures[i + 10] = new Texture("res/letters/upper-" + letter + ".png");
			}
			for (int i = 0; i < 26; ++i) {
				char letter = (char) (i + 'a');
				textures[i + 36] = new Texture("res/letters/lower-" + letter + ".png");
			}
			shape = new VAO(-0.26470588235f, -0.5f, 0.5294117647f, 1f);
			shader = new ShaderProgram("numberShader");
		}
		float semiWidth = ((float) EnigWindow.mainWindow.getWidth()) / 2f;
		float semiHeight = ((float) EnigWindow.mainWindow.getHeight()) / 2f;
		mat = new Matrix4f().ortho(-semiWidth, semiWidth, -semiHeight, semiHeight, 0, 1);
		this.fontSize = semiHeight * fontSize / 1080f;
		this.x = semiWidth * x / 1920f;
		this.y = semiHeight * y / 1080f;
	}
	public void renderStr(String str) {
		shader.enable();
		shader.shaders[2].uniforms[0].set(color);
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


	public boolean hoverCheck(String str, float mouseX, float mouseY, Vector4f select){
		selectColor = select;
		//System.out.println(x + " " + y);
		float width = (0.5294117647f * fontSize)/(EnigWindow.mainWindow.getWidth()/2) * str.length();
		float height = (fontSize)/(EnigWindow.mainWindow.getHeight()/2);
		float xPos = x/(EnigWindow.mainWindow.getWidth()/2);
		if(!centered){
			xPos += ((str.length() - 1) * 0.5294117647f * fontSize/2) / (EnigWindow.mainWindow.getWidth()/2);
		}
		float yPos = y/(EnigWindow.mainWindow.getHeight()/2);

		//System.out.println( xPos + " " + yPos);
		//System.out.println(mouseX + " " + mouseY);
		color = unselectedColor;
		if (mouseX <= xPos + width/2 && mouseX >= xPos - width/2) {
			if (mouseY >= yPos - height/2 && mouseY <= yPos + height/2) {
				color = selectColor;
				//System.out.println("yeah");
				return true;

			}
		}

		return false;
	}
}
