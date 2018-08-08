package Game;

import Game.Views.ControlsMenu;
import engine.OpenGL.EnigWindow;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class ControlButton {
    public StringRenderer text;
    public StringRenderer control;
    public StringRenderer controlAlt;
    public String controlFunction;
    public int currentButton;
    public int newButton;
    public int currentButtonAlt = 0;
    public int newButtonAlt = 0;

    public ControlButton(int yPos, int control, String controlFunction){
        text = new StringRenderer(90, -1800, yPos);
        text.centered = false;

        this.control = new StringRenderer(90, 200, yPos);
        this.control.centered = false;
        this.control.unselectedColor = new Vector4f(.5f, .5f,1f,1f);

        this.controlAlt = new StringRenderer(90, 1200, yPos);
        this.controlAlt.centered = false;
        this.controlAlt.unselectedColor = new Vector4f(.5f, .5f,1f,1f);

        currentButton = control;

        this.controlFunction = controlFunction;
    }

    public ControlButton(int yPos, int control, int controlAlt, String controlFunction){
        text = new StringRenderer(90, -1800, yPos);
        text.centered = false;

        this.control = new StringRenderer(90, 200, yPos);
        this.control.centered = false;
        this.control.unselectedColor = new Vector4f(.5f, .5f,1f,1f);

        this.controlAlt = new StringRenderer(90, 1200, yPos);
        this.controlAlt.centered = false;
        this.controlAlt.unselectedColor = new Vector4f(.5f, .5f,1f,1f);

        currentButton = control;

        currentButtonAlt = controlAlt;

        this.controlFunction = controlFunction;
    }

    public void render(EnigWindow window){
        text.renderStr(controlFunction);

        //control.hoverCheck(IHATEGLFW.getKeyName(currentButton),window.cursorXFloat, window.cursorYFloat, new Vector4f(.7f,.7f,1f,1f));
        control.renderStr(IHATEGLFW.getKeyName(currentButton));


        if(currentButtonAlt == 0){
            //controlAlt.hoverCheck("null",window.cursorXFloat, window.cursorYFloat, new Vector4f(.7f,.7f,1f,1f));
            controlAlt.renderStr("null");
        } else {
            //controlAlt.hoverCheck(IHATEGLFW.getKeyName(currentButtonAlt),window.cursorXFloat, window.cursorYFloat, new Vector4f(.7f,.7f,1f,1f));
            controlAlt.renderStr(IHATEGLFW.getKeyName(currentButtonAlt));
        }
    }


    public boolean hoverCheck(EnigWindow window){
        if(control.hoverCheck(IHATEGLFW.getKeyName(currentButton),window.cursorXFloat, window.cursorYFloat, new Vector4f(.7f,.7f,1f,1f))){
            return true;
        }
        return false;
    }

    public boolean hoverCheckAlt(EnigWindow window){
        if(currentButtonAlt == 0){
            if(controlAlt.hoverCheck("null",window.cursorXFloat, window.cursorYFloat, new Vector4f(.7f,.7f,1f,1f))) {
                return true;
            }
        } else {
            if(controlAlt.hoverCheck(IHATEGLFW.getKeyName(currentButtonAlt),window.cursorXFloat, window.cursorYFloat, new Vector4f(.7f,.7f,1f,1f))) {
                return true;
            }
        }
        return false;
    }

    public void writeToFile(int key, boolean alt){
            String[] controls = ControlsMenu.getControls();
            for (int i = 0; i < controls.length; i++) {
                if (controls[i].startsWith(controlFunction)) {
                    if (!alt) {
                        String thing = controlFunction + ":" + Integer.toString(key);
                        if (currentButtonAlt != 0) {
                            thing += "," + currentButtonAlt;
                        }
                        controls[i] = thing;
                    } else {
                        String thing = controlFunction + ":" + Integer.toString(currentButton);
                        thing += "," + Integer.toString(key);
                        controls[i] = thing;
                    }
                }
            }

            try {
                PrintWriter w = new PrintWriter(new File("res/controls.txt"));
                for (String i : controls) {
                    w.println(i);
                }
                w.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            UserControls.getControls();
            UserControls.intit();
        }

}
