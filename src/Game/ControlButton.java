package Game;

import engine.OpenGL.EnigWindow;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

public class ControlButton {
    StringRenderer text;
    StringRenderer control;
    StringRenderer controlAlt;
    String controlFunction;
    int currentButton;
    int newButton;
    int currentButtonAlt = 0;
    int newButtonAlt = 0;

    public ControlButton(int yPos, int control, String controlFunction){
        text = new StringRenderer(90, -1800, yPos);
        text.centered = false;

        this.control = new StringRenderer(90, 400, yPos);
        this.control.centered = false;
        this.control.unselectedColor = new Vector4f(.5f, .5f,1f,1f);

        this.controlAlt = new StringRenderer(90, 600, yPos);
        this.controlAlt.centered = false;
        this.controlAlt.unselectedColor = new Vector4f(.5f, .5f,1f,1f);

        currentButton = control;

        this.controlFunction = controlFunction;
    }

    public ControlButton(int yPos, int control, int controlAlt, String controlFunction){
        text = new StringRenderer(90, -1800, yPos);
        text.centered = false;

        this.control = new StringRenderer(90, 400, yPos);
        this.control.centered = false;
        this.control.unselectedColor = new Vector4f(.5f, .5f,1f,1f);

        this.controlAlt = new StringRenderer(90, 600, yPos);
        this.controlAlt.centered = false;
        this.controlAlt.unselectedColor = new Vector4f(.5f, .5f,1f,1f);

        currentButton = control;

        currentButtonAlt = controlAlt;

        this.controlFunction = controlFunction;
    }

    public void render(EnigWindow window){
        text.renderStr(controlFunction);
        control.hoverCheck(Character.toString((char)currentButton),window.cursorXFloat, window.cursorYFloat, new Vector4f(.7f,.7f,1f,1f));
        control.renderStr(Character.toString((char)currentButton));


        if(currentButtonAlt == 0){
            controlAlt.hoverCheck("null",window.cursorXFloat, window.cursorYFloat, new Vector4f(.7f,.7f,1f,1f));
            controlAlt.renderStr("null");
        } else {
            controlAlt.hoverCheck(Character.toString((char)currentButtonAlt),window.cursorXFloat, window.cursorYFloat, new Vector4f(.7f,.7f,1f,1f));
            controlAlt.renderStr(Character.toString((char)currentButtonAlt));
        }
    }
}
