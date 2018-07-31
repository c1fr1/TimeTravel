package Game;

import engine.EnigView;
import engine.OpenGL.*;
import org.lwjglx.debug.org.eclipse.jetty.server.Authentication;

public class MainMenu extends EnigView {

    SpriteButton title;
    SpriteButton start;
    SpriteButton controls;
    SpriteButton quit;


    public MainMenu(EnigWindow window){
        super(window, false);
    }

    float aspectRatio;

    @Override
    public void setup() {
        aspectRatio =  (float)window.getHeight()/(float)window.getWidth();
        title = new SpriteButton(-0.5f,.5f,1f,0.25f,"res/sprites/Button.png", aspectRatio);
        start = new SpriteButton(-0.5f,0f,1f,0.25f,"res/sprites/continueButton.png", aspectRatio);
        controls = new SpriteButton(-0.5f,-.5f,1f,0.25f,"res/sprites/continueButton.png", aspectRatio);
        quit = new SpriteButton(-0.5f,-1f,1f,0.25f,"res/sprites/ButtonDoor.png", aspectRatio);
        SpriteButton.shader = new ShaderProgram("buttonShader");


    }

    @Override
    public boolean loop() {

        FBO.prepareDefaultRender();
        SpriteButton.shader.enable();

        SpriteButton.shader.shaders[0].uniforms[0].set(aspectRatio);
        title.render(window.cursorXFloat, window.cursorYFloat);
        start.render(window.cursorXFloat, window.cursorYFloat);
        controls.render(window.cursorXFloat, window.cursorYFloat);
        quit.render(window.cursorXFloat, window.cursorYFloat);
        if(start.hoverCheck(window.cursorXFloat, window.cursorYFloat) && UserControls.leftMB(window)){
            System.out.println("yeah");
            return true;
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
        return "OhYknow";
    }
}
