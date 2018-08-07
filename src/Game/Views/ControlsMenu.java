package Game.Views;

import Game.ControlButton;
import Game.UserControls;
import engine.EnigView;
import engine.OpenGL.EnigWindow;
import engine.OpenGL.FBO;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class ControlsMenu extends EnigView {
    public ControlsMenu(EnigWindow window){
        super(window, false);
    }

    ControlButton[] keys;

    public static EnigView controlMenu;

    @Override
    public void setup() {

        controlMenu = this;


        window.callbackView = controlMenu;

        String[] controlList = getControls();
        keys = new ControlButton[controlList.length];
        for(int i = 0; i < controlList.length; i++){
            int yPos = window.getHeight() - (i * 100);

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
        for(ControlButton i: keys){
            i.render(window);
        }
        return false;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void keyCallback(int key, int state){

    }

    public String[] getControls(){
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
