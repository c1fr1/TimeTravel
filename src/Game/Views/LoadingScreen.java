package Game.Views;

import Game.Buttons.SpriteButton;
import engine.EnigView;
import engine.OpenGL.*;

public class LoadingScreen extends EnigView {
    SpriteButton load;
    float aspectRatio;
    public static SpriteButton texturePath;

    public LoadingScreen(EnigWindow window){

        super(window, false);
    }

    @Override
    public boolean loop() {
        FBO.prepareDefaultRender();
        SpriteButton.shader.enable();

        SpriteButton.shader.shaders[0].uniforms[0].set(aspectRatio);
        load.render(window.cursorXFloat, window.cursorYFloat);
        return true;
    }

    @Override
    public void setup() {
        aspectRatio = (float)window.getHeight()/(float)window.getWidth();
        load = texturePath;
        SpriteButton.shader = new ShaderProgram("buttonShader");
    }

    @Override
    public String getName() {
        return "Loading...";
    }
}
