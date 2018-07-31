package Game;

import engine.OpenGL.EnigWindow;

import static org.lwjgl.glfw.GLFW.*;

public class UserControls {
	public static int[] forward = new int[] {GLFW_KEY_W};
	public static int[] backward = new int[] {GLFW_KEY_S};
	public static int[] left = new int[] {GLFW_KEY_A};
	public static int[] right = new int[] {GLFW_KEY_D};
	public static int[] leftArrow = new int[] {GLFW_KEY_LEFT, GLFW_KEY_Q};
	public static int[] rightArrow = new int[] {GLFW_KEY_RIGHT, GLFW_KEY_E};
	public static int[] down = new int[] {GLFW_KEY_LEFT_SHIFT};
	public static int[] up = new int[] {GLFW_KEY_SPACE};
	public static int[] pause = new int[] {GLFW_KEY_ESCAPE};
	public static int[] enter = new int[] {GLFW_KEY_ENTER, GLFW_KEY_R};
	public static int[] rightMB = new int[] {GLFW_MOUSE_BUTTON_RIGHT};
	public static int[] leftMB = new int[] {GLFW_MOUSE_BUTTON_LEFT};
	public static int[] levelAdvance = new int[] {GLFW_KEY_N};
	public static int[] levelBack = new int[] {GLFW_KEY_B};
	public static int[] ohYknow = new int[] {GLFW_KEY_RIGHT_SHIFT};

	public static float sensitivity = 1f/500f;
	
	public static boolean forward(EnigWindow window) {
		for (int i:forward) {
			if (window.keys[i] > 0) {
				return true;
			}
		}
		return false;
	}
	public static boolean backward(EnigWindow window) {
		for (int i:backward) {
			if (window.keys[i] > 0) {
				return true;
			}
		}
		return false;
	}
	public static boolean left(EnigWindow window) {
		for (int i:left) {
			if (window.keys[i] > 0) {
				return true;
			}
		}
		return false;
	}
	public static boolean right(EnigWindow window) {
		for (int i:right) {
			if (window.keys[i] > 0) {
				return true;
			}
		}
		return false;
	}
	public static boolean leftArrow(EnigWindow window){
		for (int i:leftArrow) {
			if (window.keys[i] > 0) {
				return true;
			}
		}
		return false;
	}
	public static boolean leftArrowPress(EnigWindow window){
		for (int i:leftArrow) {
			if (window.keys[i] == 1) {
				return true;
			}
		}
		return false;
	}
	public static boolean rightArrow(EnigWindow window){
		for (int i:rightArrow) {
			if (window.keys[i] > 0) {
				return true;
			}
		}
		return false;
	}
	public static boolean rightArrowPress(EnigWindow window){
		for (int i:rightArrow) {
			if (window.keys[i] == 1) {
				return true;
			}
		}
		return false;
	}
	public static boolean down(EnigWindow window) {
		for (int i:down) {
			if (window.keys[i] > 0) {
				return true;
			}
		}
		return false;
	}
	public static boolean up(EnigWindow window) {
		for (int i:up) {
			if (window.keys[i] > 0) {
				return true;
			}
		}
		return false;
	}
	public static boolean pause(EnigWindow window) {
		for (int i:pause) {
			if (window.keys[i] > 0) {
				return true;
			}
		}
		return false;
	}
	public static boolean enter(EnigWindow window) {
		for (int i:enter) {
			if (window.keys[i] > 0) {
				return true;
			}
		}
		return false;
	}
	public static boolean rightMB(EnigWindow window) {
		for (int i:rightMB) {
			if (window.mouseButtons[i] > 0) {
				return true;
			}
		}
		return false;
	}
	public static boolean leftMB(EnigWindow window) {
		for (int i:leftMB) {
			if (window.mouseButtons[i] > 0) {
				return true;
			}
		}
		return false;
	}
	public static boolean levelAdvance(EnigWindow window) {
		for (int i:levelAdvance) {
			if (window.keys[i] > 0) {
				return true;
			}
		}
		return false;
	}
	public static boolean levelBack(EnigWindow window) {
		for (int i:levelBack) {
			if (window.keys[i] > 0) {
				return true;
			}
		}
		return false;
	}
	public static boolean ohYknow(EnigWindow window) {
		for (int i:ohYknow) {
			if (window.keys[i] > 0) {
				return true;
			}
		}
		return false;
	}
	
	
	
/*
	if commonCamera is true, the camera rotation for most 3d games will be used, if it is false, this alternate method is used:

	every frame the new rotation matrix is set to the rotation matrix from relative mouse position changes (from the last frame),
	will be multiplied by the old rotation matrix.
	R(xnew-xold)*R(ynew-yold)*R(znew-zold)*oldRotationMatrix.
	
	oh btw
	this variable has been moved from this file, to the Camera file, so you can have different cameras with different types of cameras
																																		 */
}
