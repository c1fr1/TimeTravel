package Game;

import engine.EnigView;
import engine.OpenGL.*;
import org.lwjglx.debug.org.eclipse.jetty.server.Authentication;

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
        title = new SpriteButton(-1f,.6f,1.6f,0.2f,"res/sprites/Button.png", aspectRatio);
        start = new SpriteButton(-1f,.4f,1.6f,0.2f,"res/sprites/playButton-1.png", aspectRatio);
        levelSelect = new SpriteButton(-1f,.2f,1.6f,0.2f,"res/sprites/continueButton.png", aspectRatio);
        options = new SpriteButton(-1f,0f,1.6f,0.2f,"res/sprites/optionsButton-1.png", aspectRatio);
        quit = new SpriteButton(-1f,-.2f,1.6f,0.2f,"res/sprites/quitButton-1.png", aspectRatio);
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
