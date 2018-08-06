package Game.Views;

import Game.Buttons.NoChangeButton;
import Game.Buttons.ShaderOptimizedButton;
import Game.Buttons.TwoStateButton;
import Game.Game;
import Game.UserControls;
import engine.EnigView;
import engine.OpenGL.EnigWindow;
import engine.OpenGL.FBO;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class OptionsMenu extends EnigView {
    public OptionsMenu(EnigWindow window){
        super(window, false);
    }

    NoChangeButton fullScreen;
    TwoStateButton fullScreenOption;
    NoChangeButton resolution;
    TwoStateButton resolutionOption;
    NoChangeButton backgroundMove;
    TwoStateButton backgroundMoveOption;
    ShaderOptimizedButton controlMenu;
    ShaderOptimizedButton restart;

    String[] options;

    float aspectRatio;

    @Override
    public void setup() {
        aspectRatio = (float)window.getHeight()/(float)window.getWidth();

        fullScreen = new NoChangeButton(-1f, .8f, 1f, .2f, "res/sprites/crateEntity.png", aspectRatio);
        fullScreenOption = new TwoStateButton(0, .8f, 1f, .2f, "res/sprites/crateEntity.png", "res/sprites/ButtonDoor.png", MainView.fullScreenBool, aspectRatio);
        resolution = new NoChangeButton(-1f, .5f, 1f, .2f, "res/sprites/crateEntity.png", aspectRatio);
        resolutionOption = new TwoStateButton(0, .5f, 1f, .2f, "res/sprites/crateEntity.png", "res/sprites/ButtonDoor.png", true, aspectRatio);
        backgroundMove = new NoChangeButton(-1f, .2f, 1f, .2f, "res/sprites/crateEntity.png", aspectRatio);
        backgroundMoveOption = new TwoStateButton(0, .2f, 1f, .2f, "res/sprites/crateEntity.png", "res/sprites/ButtonDoor.png", MainView.backgroundMoveBool, aspectRatio);
        controlMenu = new ShaderOptimizedButton(-1f, -.1f, 2f, .2f, "res/sprites/testex.png", aspectRatio);
        restart = new ShaderOptimizedButton(-1, -.4f, 2f, .2f, "res/menu/restart.png", aspectRatio);

        options = getOptionsString().split("\n");
    }

    @Override
    public boolean loop() {
        FBO.prepareDefaultRender();

        fullScreen.render(window.cursorXFloat, window.cursorYFloat, aspectRatio);
        fullScreenOption.render(window.cursorXFloat, window.cursorYFloat, aspectRatio);
        resolution.render(window.cursorXFloat, window.cursorYFloat, aspectRatio);
        resolutionOption.render(window.cursorXFloat, window.cursorYFloat, aspectRatio);
        backgroundMove.render(window.cursorXFloat, window.cursorYFloat, aspectRatio);
        backgroundMoveOption.render(window.cursorXFloat, window.cursorYFloat, aspectRatio);
        controlMenu.render(window.cursorXFloat, window.cursorYFloat, aspectRatio);



        if(fullScreenOption.hoverCheck(window.cursorXFloat, window.cursorYFloat) && UserControls.leftMBPress(window)){
            fullScreenOption.state = !fullScreenOption.state;
            if(options[0].equals("fullscreen:t")){
                options[0] =  "fullscreen:f";
            } else {
                options[0] = "fullscreen:t";
            }
            MainView.fullScreenBool = !MainView.fullScreenBool;
        }
        if(resolutionOption.hoverCheck(window.cursorXFloat, window.cursorYFloat) && UserControls.leftMBPress(window)){
            resolutionOption.state = !resolutionOption.state;
        }
        if(backgroundMoveOption.hoverCheck(window.cursorXFloat, window.cursorYFloat) && UserControls.leftMBPress(window)){
            backgroundMoveOption.state = !backgroundMoveOption.state;
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
}
