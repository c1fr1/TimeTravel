package Game.Views;

import Game.*;
import Game.Views.LevelSelect;
import engine.EnigView;
import engine.OpenGL.*;

public class MainMenu extends EnigView {

    SpriteButton title;
    SpriteButton start;
    SpriteButton levelSelect;
    SpriteButton options;
    SpriteButton quit;

    public static boolean mainMenuQuit = false;


    public MainMenu(EnigWindow window){
        super(window, false);
    }

    float aspectRatio;

    @Override
    public void setup() {
        aspectRatio = (float)window.getHeight()/(float)window.getWidth();
        title = new DoubleTextureButton(-1.6f,.5f,1.2f,0.4f,"res/menu/titleImage.png", "res/menu/titleImage.png", aspectRatio);
        start = new DoubleTextureButton(-1.6f,0f,1f,0.125f,"res/menu/playButton-1.png","res/menu/playButton-0.png", aspectRatio);
        levelSelect = new DoubleTextureButton(-1.6f,-.2f,1f,0.125f,"res/menu/selectorButton-1.png","res/menu/selectorButton-0.png", aspectRatio);
        options = new DoubleTextureButton(-1.6f,-.4f,1f,0.125f,"res/menu/optionsButton-1.png","res/menu/optionsButton-0.png", aspectRatio);
        quit = new DoubleTextureButton(-1.6f,-.6f,1f,0.125f,"res/menu/quitButton-1.png", "res/menu/quitButton-1.png", aspectRatio);
        SpriteButton.shader = new ShaderProgram("buttonShader");


    }

    @Override
    public boolean loop() {
        if(!mainMenuQuit) {
            FBO.prepareDefaultRender();

            title.render(window.cursorXFloat, window.cursorYFloat, aspectRatio);
            start.render(window.cursorXFloat, window.cursorYFloat, aspectRatio);
            levelSelect.render(window.cursorXFloat, window.cursorYFloat, aspectRatio);
            options.render(window.cursorXFloat, window.cursorYFloat, aspectRatio);
            quit.render(window.cursorXFloat, window.cursorYFloat, aspectRatio);
            if (start.hoverCheck(window.cursorXFloat, window.cursorYFloat) && UserControls.leftMB(window)) {
                //System.out.println("yeah");
                return true;
            }
            if (levelSelect.hoverCheck(window.cursorXFloat, window.cursorYFloat) && UserControls.leftMB(window)) {
                new LevelSelect(window);
            }
            if (quit.hoverCheck(window.cursorXFloat, window.cursorYFloat) && UserControls.leftMB(window)) {
                MainView.quit = true;
                return true;
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
