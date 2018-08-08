package Game;

import Game.Views.MainView;
import engine.OpenGL.EnigWindow;
import engine.OpenGL.ShaderProgram;
import engine.OpenGL.Texture;
import engine.OpenGL.VAO;
import org.joml.Matrix4f;

import java.util.ArrayList;

import static Game.Views.MainView.aspectRatio;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class TTOGUI {
	public int selectedTZ;
	public Texture[] leftSide = new Texture[3];//open, on, closed
	public Texture[] middle = new Texture[3];
	public Texture[] rightSide = new Texture[3];
	public Texture arrow;
	public VAO upVAO;
	public VAO downVAO;
	public ShaderProgram program;
	public TTOGUI() {
		leftSide[0] = new Texture("res/timeline/left-open.png");
		leftSide[1] = new Texture("res/timeline/left-selected.png");
		leftSide[2] = new Texture("res/timeline/left-closed.png");
		middle[0] = new Texture("res/timeline/center-open.png");
		middle[1] = new Texture("res/timeline/center-selected.png");
		middle[2] = new Texture("res/timeline/center-closed.png");
		rightSide[0] = new Texture("res/timeline/right-open.png");
		rightSide[1] = new Texture("res/timeline/right-selected.png");
		rightSide[2] = new Texture("res/timeline/right-closed.png");
		arrow = new Texture("res/timeline/timeSelector.png");
		upVAO = new VAO(-0.15f, 0.35f, 0.3f, 0.3f);
		downVAO = new VAO(-0.075f, -0.95f, 0.15f, 0.15f);
		program = new ShaderProgram("ttoGUIShader");
	}
	public int render(int ttoInd, boolean enabled) {
		int tzCount = MainView.currentLevel.levelseries.size();
		if (tzCount == 1) {
			return -1;
		}
		VAO vao = null;
		float width = 0.3f;
		Boolean[] possibilities;
		if (enabled) {
			possibilities = MainView.currentLevel.timeZonePossibilities.get(ttoInd);
			vao = upVAO;
		}else {
			width = 0.15f;
			vao = downVAO;
			possibilities = new Boolean[tzCount];
			for (int i = 0; i < possibilities.length; ++i) {
				possibilities[i] = true;
			}
		}
		int ret = -1;
		int currentTZ = MainView.currentLevel.currentTZ;
		float translation = -(float) (tzCount - 1) * width * aspectRatio * 0.5f;
		float[] translations = new float[tzCount];
		for (int i = 0; i < translations.length;++i) {
			translations[i] = translation + ((float) i) * width * aspectRatio;
		}
		float yOff = EnigWindow.mainWindow.cursorYFloat - 0.5f;
		float ys = (yOff) * (yOff);
		if (UserControls.leftArrowPress(EnigWindow.mainWindow)) {
			--selectedTZ;
			if (selectedTZ < 0) {
				selectedTZ = 0;
			}
		}
		if (UserControls.rightArrowPress(EnigWindow.mainWindow)) {
			++selectedTZ;
			if (selectedTZ >= tzCount) {
				selectedTZ = tzCount - 1;
			}
		}
		for (int i = 0; i < translations.length;++i) {
			float xOff = translations[i] - EnigWindow.mainWindow.cursorXFloat;
			if (ys + xOff * xOff < 0.01f) {
				selectedTZ = i;
				if (EnigWindow.mainWindow.mouseButtons[GLFW_MOUSE_BUTTON_LEFT] > 0) {
					if (possibilities[i]) {
						ret = i;
					}
				}
			}
		}
		if (UserControls.enter(EnigWindow.mainWindow)) {
			if (possibilities[selectedTZ]) {
				ret = selectedTZ;
			}
		}
		program.enable();
		program.shaders[0].uniforms[0].set(aspectRatio);
		program.shaders[0].uniforms[1].set(translation);
		vao.prepareRender();
		if (currentTZ == 0) {
			leftSide[1].bind();
		}else if (!possibilities[0]) {
			leftSide[2].bind();
		}else {
			leftSide[0].bind();
		}
		vao.drawTriangles();
		for (int i = 1; i < tzCount - 1; ++i) {
			program.shaders[0].uniforms[1].set(translations[i]);
			if (currentTZ == i) {
				middle[1].bind();
			}else if (!possibilities[i]) {
				middle[2].bind();
			}else {
				middle[0].bind();
			}
			vao.drawTriangles();
		}
		program.shaders[0].uniforms[1].set(translations[tzCount - 1]);
		if (currentTZ == tzCount - 1) {
			rightSide[1].bind();
		}else if (!possibilities[tzCount - 1]) {
			rightSide[2].bind();
		}else {
			rightSide[0].bind();
		}
		vao.drawTriangles();
		if (enabled) {
			if (possibilities[selectedTZ]) {
				program.shaders[0].uniforms[1].set(translations[selectedTZ]);
				arrow.bind();
				vao.drawTriangles();
				vao.unbind();
				if (ret > -1) {
					return ret;
				}
			}
		}
		return currentTZ;
	}
}
