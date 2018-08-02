package Game.Views;

import Game.MainView;
import Game.ShaderOptimizedButton;
import Game.SpriteButton;
import Game.UserControls;
import Game.Views.LevelSelect;
import engine.EnigView;
import engine.OpenGL.*;

public class MainMenu extends EnigView {

    SpriteButton title;
    SpriteButton start;
    SpriteButton levelSelect;
    SpriteButton options;
    SpriteButton quit;

    static boolean mainMenuQuit = false;


    public MainMenu(EnigWindow window){
        super(window, false);
    }

    float aspectRatio;

    @Override
    public void setup() {
        aspectRatio =  (float)window.getHeight()/(float)window.getWidth();
        title = new ShaderOptimizedButton(-1.6f,.5f,1.2f,0.4f,"res/menu/titleImage.png", aspectRatio);
        start = new ShaderOptimizedButton(-1.6f,0f,1f,0.125f,"res/menu/playButton-1.png", aspectRatio);
        levelSelect = new ShaderOptimizedButton(-1.6f,-.2f,1f,0.125f,"res/menu/selectorButton-1.png", aspectRatio);
        options = new ShaderOptimizedButton(-1.6f,-.4f,1f,0.125f,"res/menu/optionsButton-1.png", aspectRatio);
        quit = new ShaderOptimizedButton(-1.6f,-.6f,1f,0.125f,"res/menu/quitButton-1.png", aspectRatio);
        SpriteButton.shader = new ShaderProgram("buttonShader");


    }

    @Override
    public boolean loop() {
        if(!mainMenuQuit) {
            FBO.prepareDefaultRender();
            SpriteButton.shader.enable();

            SpriteButton.shader.shaders[0].uniforms[0].set(aspectRatio);
            title.render(window.cursorXFloat, window.cursorYFloat);
            start.render(window.cursorXFloat, window.cursorYFloat);
            levelSelect.render(window.cursorXFloat, window.cursorYFloat);
            options.render(window.cursorXFloat, window.cursorYFloat);
            quit.render(window.cursorXFloat, window.cursorYFloat);
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
