package Game;

import engine.OpenGL.EnigWindow;
import org.lwjgl.glfw.GLFW;

import static org.lwjgl.glfw.GLFW.*;

public class UserControls {


	public static String forwardSettingString = "";
	public static String backwardSettingString;
	public static String leftSettingString;
	public static String rightSettingString;
	public static String leftArrowSettingString;
	public static String rightArrowSettingString;
	public static String downSettingString;
	public static String upSettingString;
	public static String pauseSettingString;
	public static String enterSettingString;
	public static String levelAdvanceSettingString;
	public static String levelBackSettingString;
	public static String ohYknowSettingString;

	public static int forwardSetting;
	public static int backwardSetting;
	public static int leftSetting;
	public static int rightSetting;
	public static int leftArrowSetting;
	public static int rightArrowSetting;
	public static int downSetting;
	public static int upSetting;
	public static int pauseSetting;
	public static int enterSetting;
	public static int levelAdvanceSetting;
	public static int levelBackSetting;
	public static int ohYknowSetting;

	public static int forwardSetting2 = 0;
	public static int backwardSetting2 = 0;
	public static int leftSetting2 = 0;
	public static int rightSetting2 = 0;
	public static int leftArrowSetting2 = 0;
	public static int rightArrowSetting2 = 0;
	public static int downSetting2 = 0;
	public static int upSetting2 = 0;
	public static int pauseSetting2 = 0;
	public static int enterSetting2 = 0;
	public static int levelAdvanceSetting2 = 0;
	public static int levelBackSetting2 = 0;
	public static int ohYknowSetting2 = 0;

	public static int[] forward;
	public static int[] backward;
	public static int[] left;
	public static int[] right;
	public static int[] leftArrow;
	public static int[] rightArrow;
	public static int[] down;
	public static int[] up;
	public static int[] pause;
	public static int[] enter;
	public static int[] rightMB = new int[] {GLFW_MOUSE_BUTTON_RIGHT};
	public static int[] leftMB = new int[] {GLFW_MOUSE_BUTTON_LEFT};
	public static int[] levelAdvance;
	public static int[] levelBack;
	public static int[] ohYknow;

	public static float sensitivity = 1f/500f;

	public static void intit(){
		forward  =  new int[] {forwardSetting, forwardSetting2};
		backward =  new int[] {backwardSetting, backwardSetting2};
		left =  new int[] {leftSetting, leftSetting2};
		right =  new int[] {rightSetting, rightSetting2};
		leftArrow =  new int[] {leftArrowSetting, leftArrowSetting2};
		rightArrow =  new int[] {rightArrowSetting, rightArrowSetting2};
		down =  new int[] {downSetting, downSetting2};
		up =  new int[] {upSetting, upSetting2};
		pause =  new int[] {pauseSetting, pauseSetting2};
		enter =  new int[] {enterSetting, enterSetting2};
		levelAdvance =  new int[] {levelAdvanceSetting, levelAdvanceSetting2};
		levelBack =  new int[] {levelBackSetting, levelBackSetting2};
		ohYknow =  new int[] {ohYknowSetting, ohYknowSetting2};
	}
	
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
	public static boolean leftMBPress(EnigWindow window) {
		for (int i:leftMB) {
			if (window.mouseButtons[i] == 1) {
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
