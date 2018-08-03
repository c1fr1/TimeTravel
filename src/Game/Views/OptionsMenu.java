package Game.Views;

import Game.Buttons.NoChangeButton;
import Game.Buttons.ShaderOptimizedButton;
import Game.Buttons.TwoStateButton;
import Game.MainView;
import Game.UserControls;
import engine.EnigView;
import engine.OpenGL.EnigWindow;
import engine.OpenGL.FBO;
import org.omg.PortableServer.CurrentPackage.NoContextHelper;
import sun.applet.Main;

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
            MainView.fullScreenBool = !MainView.fullScreenBool;
        }
        if(resolutionOption.hoverCheck(window.cursorXFloat, window.cursorYFloat) && UserControls.leftMBPress(window)){
            resolutionOption.state = !resolutionOption.state;
        }
        if(backgroundMoveOption.hoverCheck(window.cursorXFloat, window.cursorYFloat) && UserControls.leftMBPress(window)){
            backgroundMoveOption.state = !backgroundMoveOption.state;
            MainView.backgroundMoveBool = !MainView.backgroundMoveBool;
        }
        if(controlMenu.hoverCheck(window.cursorXFloat, window.cursorYFloat) && UserControls.leftMBPress(window)){
            //Controls menu
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
}
