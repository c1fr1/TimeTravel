package Game;

import engine.EnigView;
import engine.OpenGL.*;
import org.joml.Vector4f;

public class LoadingScreen extends EnigView {
    SpriteButton load;
    float aspectRatio;

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
        load = new SpriteButton(-1f / aspectRatio,-1f,2f / aspectRatio,2f,"res/sprites/Loading.png");
        SpriteButton.shader = new ShaderProgram("buttonShader");
    }

    @Override
    public String getName() {
        return "Loading...";
    }
}
