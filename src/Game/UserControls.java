package Game;

import engine.OpenGL.EnigWindow;

import static org.lwjgl.glfw.GLFW.*;

public class UserControls {
	public static int[] forward = new int[] {GLFW_KEY_W,GLFW_KEY_UP};
	public static int[] backward = new int[] {GLFW_KEY_S,GLFW_KEY_DOWN};
	public static int[] left = new int[] {GLFW_KEY_A,GLFW_KEY_LEFT};
	public static int[] right = new int[] {GLFW_KEY_D,GLFW_KEY_RIGHT};
	public static int[] down = new int[] {GLFW_KEY_LEFT_SHIFT};
	public static int[] up = new int[] {GLFW_KEY_SPACE};
	public static int[] pause = new int[] {GLFW_KEY_ESCAPE};
	public static int[] rightMB = new int[] {GLFW_MOUSE_BUTTON_RIGHT};
	public static int[] leftMB = new int[] {GLFW_MOUSE_BUTTON_LEFT};
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
	
	
	
/*
	if commonCamera is true, the camera rotation for most 3d games will be used, if it is false, this alternate method is used:

	every frame the new rotation matrix is set to the rotation matrix from relative mouse position changes (from the last frame),
	will be multiplied by the old rotation matrix.
	R(xnew-xold)*R(ynew-yold)*R(znew-zold)*oldRotationMatrix.
	
	oh btw
	this variable has been moved from this file, to the Camera file, so you can have different cameras with different types of cameras
																																		 */
}
