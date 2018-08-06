package Game.Views;

import Game.*;
import Game.Buttons.DoubleTextureButton;
import Game.Buttons.SpriteButton;
import engine.EnigView;
import engine.OpenGL.*;
import org.lwjglx.debug.org.eclipse.jetty.server.Authentication;

public class MainMenu extends EnigView {

    DoubleTextureButton title;
    DoubleTextureButton start;
    DoubleTextureButton levelSelect;
    DoubleTextureButton options;
    DoubleTextureButton quit;

    public static boolean mainMenuQuit = false;

   int mainMenuSelector;


    public MainMenu(EnigWindow window){
        super(window, false);
    }

    float aspectRatio;

    @Override
    public void setup() {
        aspectRatio = (float)window.getHeight()/(float)window.getWidth();//0.56222546
		float border = -0.9f / aspectRatio;
        title = new DoubleTextureButton(border,.5f,1.2f,0.4f,"res/menu/titleImage.png", "res/menu/titleImage.png", aspectRatio);
        start = new DoubleTextureButton(border,0f,1f,0.125f,"res/menu/playButton-0.png","res/menu/playButton-1.png", aspectRatio);
        levelSelect = new DoubleTextureButton(border,-.2f,1f,0.125f,"res/menu/selectorButton-0.png","res/menu/selectorButton-1.png", aspectRatio);
        options = new DoubleTextureButton(border,-.4f,1f,0.125f,"res/menu/optionsButton-0.png","res/menu/optionsButton-1.png", aspectRatio);
        quit = new DoubleTextureButton(border,-.6f,1f,0.125f,"res/menu/quitButton-0.png", "res/menu/quitButton-1.png", aspectRatio);
        SpriteButton.shader = new ShaderProgram("buttonShader");

        mainMenuSelector = -1;


    }

    @Override
    public boolean loop() {
        if(!mainMenuQuit) {
            //System.out.println(mainMenuSelector);
            if(mainMenuSelector > 0) {
                if (UserControls.upArrowPress(window)) {
                    //System.out.println("up Press");
                    mainMenuSelector--;
                }
            }
            if(mainMenuSelector < 3){
                if(UserControls.downArrowPress(window)){
                    //System.out.println("down Press");
                    mainMenuSelector++;
                }
            }
            if(mainMenuSelector == -1){
                if(UserControls.upArrowPress(window)){
                    mainMenuSelector = 3;
                }
            }
            FBO.prepareDefaultRender();
            title.render(window.cursorXFloat, window.cursorYFloat, aspectRatio);


            if(!start.hoverCheck(window.cursorXFloat, window.cursorYFloat) && !levelSelect.hoverCheck(window.cursorXFloat, window.cursorYFloat) &&
                    !options.hoverCheck(window.cursorXFloat, window.cursorYFloat) && !quit.hoverCheck(window.cursorXFloat, window.cursorYFloat)){
                if(mainMenuSelector == 0){
                    start.render(window.cursorXFloat, window.cursorYFloat, aspectRatio, true);
                    if(UserControls.enter(window)){
                        mainMenuSelector = 0;
                        return true;
                    }
                }
                if(mainMenuSelector == 1){
                    levelSelect.render(window.cursorXFloat, window.cursorYFloat, aspectRatio,  true);
                    if(UserControls.enter(window)){
                        mainMenuSelector = 0;
                        new LevelSelect(window);
                    }
                }
                if(mainMenuSelector == 2){
                    options.render(window.cursorXFloat, window.cursorYFloat, aspectRatio,  true);
                    if(UserControls.enter(window)){
                        mainMenuSelector = 0;
                        new OptionsMenu(window);
                    }
                }
                if(mainMenuSelector == 3){
                    quit.render(window.cursorXFloat, window.cursorYFloat, aspectRatio,  true);
                    if(UserControls.enter(window)){
                        mainMenuSelector = 0;
                        MainView.quit = true;
                        return true;
                    }
                }
            }

            if (start.render(window.cursorXFloat, window.cursorYFloat, aspectRatio)) {
                mainMenuSelector = 0;
                if(UserControls.leftMB(window) ){
                    //System.out.println("yeah");
                    return true;
                }
            }
            if (levelSelect.render(window.cursorXFloat, window.cursorYFloat, aspectRatio) ) {
                mainMenuSelector = 1;
                if(UserControls.leftMB(window)) {
                    new LevelSelect(window);
                }
            }
            if(options.render(window.cursorXFloat, window.cursorYFloat, aspectRatio)){
                mainMenuSelector = 2;
                if( UserControls.leftMB(window)) {
                    new OptionsMenu(window);
                }
            }
            if (quit.render(window.cursorXFloat, window.cursorYFloat, aspectRatio)) {
                mainMenuSelector = 3;
                if(UserControls.leftMB(window)) {
                    MainView.quit = true;
                    return true;
                }
            }


            EnigWindow.checkGLError();
            return false;
        } else if(mainMenuQuit){
            return true;
        }
        return false;
    }

    @Override
    public String getName() {
        return "Paradox Pulverizer";
    }
}
