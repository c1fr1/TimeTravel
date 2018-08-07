package Game.Views;

import Game.Buttons.NoChangeButton;
import Game.Buttons.ShaderOptimizedButton;
import Game.Buttons.TwoStateButton;
import Game.Game;
import Game.UserControls;
import Game.StringRenderer;
import engine.EnigView;
import engine.OpenGL.EnigWindow;
import engine.OpenGL.FBO;
import org.joml.Vector4f;
import sun.applet.Main;

import javax.xml.soap.Text;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class OptionsMenu extends EnigView {
    public OptionsMenu(EnigWindow window){
        super(window, false);
    }

    public static OptionsMenu optionsView;

    StringRenderer fullScreen;
    StringRenderer fullScreenOption;
    StringRenderer resolution;
    StringRenderer resolutionOption1;
    StringRenderer resolutionOption2;
    StringRenderer backgroundMove;
    StringRenderer backgroundMoveOption;
    ShaderOptimizedButton controlMenu;
    ShaderOptimizedButton restart;

    String[] options;

    float aspectRatio;

    boolean keyInput = false;

    int xRes;
    int yRes;

    int keyCallbackState;

    @Override
    public void setup() {
        optionsView = this;


        window.callbackView = optionsView;

        xRes = window.getWidth();
        yRes = window.getHeight();
        aspectRatio = (float)window.getHeight()/(float)window.getWidth();

        fullScreen = new StringRenderer(180, -1700, 900);
        fullScreen.centered = false;

        fullScreenOption = new StringRenderer(180, 400, 900);
        fullScreenOption.centered = false;
        fullScreenOption.unselectedColor = new Vector4f(1f,0f,0f,1f);

        resolution = new StringRenderer(180, -1700, 700);
        resolution.centered = false;

        resolutionOption1 = new StringRenderer(180, 400, 700);
        resolutionOption1.centered = false;
        resolutionOption1.unselectedColor = new Vector4f(1f,0f,0f,1f);

        resolutionOption2 = new StringRenderer(180, 900, 700);
        resolutionOption2.centered = false;
        resolutionOption2.unselectedColor = new Vector4f(1f,0f,0f,1f);

        backgroundMove = new StringRenderer(180, -1700, 500);
        backgroundMove.centered = false;

        backgroundMoveOption = new StringRenderer(180, 400, 500);
        backgroundMoveOption.centered = false;
        backgroundMoveOption.unselectedColor = new Vector4f(1f,0f,0f,1f);

        controlMenu = new ShaderOptimizedButton(-1f, -.1f, 2f, .2f, "res/sprites/testex.png", aspectRatio);
        restart = new ShaderOptimizedButton(-1, -.4f, 2f, .2f, "res/menu/restart.png", aspectRatio);

        options = getOptionsString().split("\n");

        keyCallbackState = 0;

    }

    @Override
    public boolean loop() {

        FBO.prepareDefaultRender();

        fullScreen.renderStr("Fullscreen");
        String fullScreenOptionText;
        if(MainView.fullScreenBool){
            fullScreenOptionText = "Enabled";
        } else {
            fullScreenOptionText = "Disabled";
        }
        fullScreenOption.renderStr(fullScreenOptionText);
        resolution.renderStr("Resolution");


        resolutionOption1.renderStr(Integer.toString(xRes));
        resolutionOption2.renderStr(Integer.toString(yRes));


        backgroundMove.renderStr("Background Move");
        String backGroundMoveOptionText;
        if(MainView.backgroundMoveBool){
            backGroundMoveOptionText = "Enabled";
        } else {
            backGroundMoveOptionText = "Disabled";
        }
        backgroundMoveOption.renderStr(backGroundMoveOptionText);
        controlMenu.render(window.cursorXFloat, window.cursorYFloat, aspectRatio);


        if(fullScreenOption.hoverCheck(fullScreenOptionText, window.cursorXFloat, window.cursorYFloat, new Vector4f(.5f,.5f,1f,1f)) && UserControls.leftMBPress(window)){
            if(options[0].equals("fullscreen:t")){
                options[0] =  "fullscreen:f";
            } else {
                options[0] = "fullscreen:t";
            }
            MainView.fullScreenBool = !MainView.fullScreenBool;
        }
        if(resolutionOption1.hoverCheck(Integer.toString(xRes), window.cursorXFloat, window.cursorYFloat, new Vector4f(1f,1f,1f,1f)) && UserControls.leftMBPress(window)){

            if(keyCallbackState == 0) {
                System.out.println("yeah1");
                keyInput = true;
                keyCallbackState = 1;
            }

        }
        if(resolutionOption2.hoverCheck(Integer.toString(yRes), window.cursorXFloat, window.cursorYFloat, new Vector4f(1f,1f,1f,1f)) && UserControls.leftMBPress(window)){

            if(keyCallbackState == 0) {
                System.out.println("yeah");
                keyInput = true;
                keyCallbackState = 2;
            }

        }
        if(backgroundMoveOption.hoverCheck(backGroundMoveOptionText, window.cursorXFloat, window.cursorYFloat, new Vector4f(.5f, .5f, 1f, 1f)) && UserControls.leftMBPress(window)){
            if(options[2].equals("backgroundmove:t")){
                options[2] =  "backgroundmove:f";
            } else {
                options[2] = "backgroundmove:t";
            }
            MainView.backgroundMoveBool = !MainView.backgroundMoveBool;
        }
        if(controlMenu.hoverCheck(window.cursorXFloat, window.cursorYFloat) && UserControls.leftMBPress(window)){
            //Controls menu
        }

        if(!(options[0] + "\n" + options[1] + "\n" + options[2] + "\n").equals(getOptionsString())){
            restart.render(window.cursorXFloat, window.cursorYFloat, aspectRatio);
        }

        String[] currentOptions = getOptionsString().split("\n");

        boolean restartCheck = false;
        if(!options[0].equals(currentOptions[0])) restartCheck = true;
        if(!options[1].equals(currentOptions[1])) restartCheck = true;

        if(restartCheck && restart.hoverCheck(window.cursorXFloat, window.cursorYFloat) && UserControls.leftMBPress(window)){
            writeToOptions(options);
            MainView.quit = true;
            MainMenu.mainMenuQuit = true;
            Game.restart = true;
            return true;
        }

        if(UserControls.pause(window)){
            return true;
        }
        return false;
    }

    @Override
    public String getName() {
        return "options";
    }


    public String getOptionsString(){
        try {
            Scanner s = new Scanner(new File("res/options.txt"));
            String options = "";
            while(s.hasNextLine()){
                options += s.nextLine() + "\n";
            }
            return options;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void writeToOptions(String[] write){
        try {
            PrintWriter w = new PrintWriter(new File("res/options.txt"));
            for (String i: write){
                w.println(i);
            }
            w.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    int tempRes = 0;
    int callbackCounter = 0;

    @Override
    public void keyCallback(int key, int state) {
        if (state != 0) {
            //System.out.println("callback");
            if (keyInput) {
                if (key == UserControls.enterSetting || key == UserControls.enterSetting2) {
                    keyInput = false;
                    if (tempRes < 200) tempRes = 200;
                    if (keyCallbackState == 1) {
                        xRes = tempRes;
                    } else if (keyCallbackState == 2) {
                        yRes = tempRes;
                    }
                    tempRes = 0;
                    callbackCounter = 0;
                    keyCallbackState = 0;
                    System.out.println("enter");
                    return;
                } //TODO make this actually write and try to add support for numpad stuff
                if (key > 47 && key < 58) {
                    tempRes = tempRes * 10;
                    tempRes += key - 48;
                    callbackCounter++;
                    System.out.println(key);
                }
                if (callbackCounter == 4) {
                    keyInput = false;
                    if (tempRes < 200) tempRes = 200;
                    if (keyCallbackState == 1) {
                        xRes = tempRes;
                    } else if (keyCallbackState == 2) {
                        yRes = tempRes;
                    }
                    tempRes = 0;
                    callbackCounter = 0;
                    keyCallbackState = 0;
                    System.out.println("limit");
                    return;
                }


            }
        }
    }
}

