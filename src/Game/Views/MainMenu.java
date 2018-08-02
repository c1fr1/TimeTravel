package Game.Views;

import Game.MainView;
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


    public MainMenu(EnigWindow window){
        super(window, false);
    }

    float aspectRatio;

    @Override
    public void setup() {
        aspectRatio =  (float)window.getHeight()/(float)window.getWidth();
        title = new SpriteButton(-1.6f,.6f,1.6f,0.2f,"res/sprites/Button.png", aspectRatio);
        start = new SpriteButton(-1.6f,0f,1f,0.125f,"res/menu/playButton-1.png", aspectRatio);
        levelSelect = new SpriteButton(-1.6f,-.2f,1f,0.125f,"res/menu/selectorButton-1.png", aspectRatio);
        options = new SpriteButton(-1.6f,-.4f,1f,0.125f,"res/menu/optionsButton-1.png", aspectRatio);
        quit = new SpriteButton(-1.6f,-.6f,1f,0.125f,"res/menu/quitButton-1.png", aspectRatio);
        SpriteButton.shader = new ShaderProgram("buttonShader");


    }

    @Override
    public boolean loop() {

        FBO.prepareDefaultRender();
        SpriteButton.shader.enable();

        SpriteButton.shader.shaders[0].uniforms[0].set(aspectRatio);
        title.render(window.cursorXFloat, window.cursorYFloat);
        start.render(window.cursorXFloat, window.cursorYFloat);
        levelSelect.render(window.cursorXFloat, window.cursorYFloat);
        options.render(window.cursorXFloat, window.cursorYFloat);
        quit.render(window.cursorXFloat, window.cursorYFloat);
        if(start.hoverCheck(window.cursorXFloat, window.cursorYFloat) && UserControls.leftMB(window)){
            //System.out.println("yeah");
            return true;
        }
        if(levelSelect.hoverCheck(window.cursorXFloat, window.cursorYFloat) && UserControls.leftMB(window)){
            new LevelSelect(window);
        }
        if(quit.hoverCheck(window.cursorXFloat, window.cursorYFloat) && UserControls.leftMB(window)){
            MainView.quit = true;
            return true;
        }
        EnigWindow.checkGLError();
        return false;
    }

    @Override
    public String getName() {
        return "Paradox Pulverizer";
    }
}
