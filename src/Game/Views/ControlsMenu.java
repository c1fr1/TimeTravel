package Game.Views;

import Game.ControlButton;
import Game.UserControls;
import engine.EnigView;
import engine.OpenGL.EnigWindow;
import engine.OpenGL.FBO;
import org.lwjgl.glfw.GLFW;
import org.lwjglx.debug.org.eclipse.jetty.server.Authentication;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class ControlsMenu extends EnigView {
    public ControlsMenu(EnigWindow window){
        super(window, false);
    }

    ControlButton[] keys;



    boolean keyPress = false;
    int keyPressValue = -1;
    boolean alt = false;

    public static EnigView controlMenu;

    public static boolean escapeRes = false;

    @Override
    public void setup() {

        controlMenu = this;


        window.callbackView = controlMenu;

        String[] controlList = getControls();
        keys = new ControlButton[controlList.length];
        for(int i = 0; i < controlList.length; i++){
            int yPos = window.getHeight() - (i * 100);
            int k = GLFW.GLFW_KEY_B;

            String controlName = controlList[i].substring(0, controlList[i].indexOf(':'));
            String[] controls = controlList[i].substring(controlList[i].indexOf(':') + 1).split(",");
            if(controls.length == 2){
                keys[i] = new ControlButton(yPos, Integer.parseInt(controls[0]), Integer.parseInt(controls[1]), controlName);
            } else {
                keys[i] = new ControlButton(yPos, Integer.parseInt(controls[0]), controlName);
            }
        }



    }



    @Override
    public boolean loop() {
        FBO.prepareDefaultRender();
        for(int i = 0; i < keys.length; i++){
            keys[i].render(window);
            if(keys[i].hoverCheck(window) && UserControls.leftMBPress(window)){
                escapeRes = true;
                keyPress = true;
                keyPressValue = i;
                alt = false;
            }
            if(keys[i].hoverCheckAlt(window) && UserControls.leftMBPress(window)) {
                escapeRes = true;
                keyPress = true;
                keyPressValue = i;
                alt = true;
            }
        }
        if(UserControls.pausePress(window) && !keyPress && !escapeRes){
            window.callbackView = null;
            OptionsMenu.escapeRes = true;
            return true;
        } else if(escapeRes && UserControls.pausePress(window)){
            escapeRes = false;
        }
        return false;
    }

    @Override
    public String getName() {
        return null;
    }


    @Override
    public void keyCallback(int key, int state){
        if(key == 256){
            keyPressValue = -1;
            keyPress = false;
            alt = false;
            return;
        }
        if(keyPress){
            if(state != 0){

                keys[keyPressValue].writeToFile(key, alt);

                if(!alt) {
                    keys[keyPressValue].currentButton = key;
                    keys[keyPressValue].writeToFile(key, false);
                } else {
                    keys[keyPressValue].currentButtonAlt = key;
                    keys[keyPressValue].writeToFile(key, true);
                }

                keyPressValue = -1;
                keyPress = false;
                alt = false;
            }
        }
    }

    public static String[] getControls(){
        Scanner s = null;
        try {
            s = new Scanner(new File("res/controls.txt"));

            ArrayList<String> controls = new ArrayList<>();
            while(s.hasNextLine()){
                controls.add(s.nextLine());
            }
            String[] controlsList = new String[controls.size()];
            for(int i = 0; i < controls.size(); i++){
                controlsList[i] = controls.get(i);
            }
            return controlsList;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
