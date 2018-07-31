package Game;

import engine.EnigView;
import engine.OpenGL.*;
import org.joml.Vector4f;

public class LoadingScreen extends EnigView {
    SpriteButton load;


    public LoadingScreen(EnigWindow window){
        super(window, false);
    }

    @Override
    public boolean loop() {
        FBO.prepareDefaultRender();
        SpriteButton.shader.enable();

        SpriteButton.shader.shaders[0].uniforms[0].set((float)window.getHeight()/(float)window.getWidth());
        load.render(window.cursorXFloat, window.cursorYFloat);
        return true;
    }

    @Override
    public void setup() {
        load = new SpriteButton(-1,-1,2f,1f,"lib/ohYknow.jpg", (float)window.getHeight()/(float)window.getWidth());
        SpriteButton.shader = new ShaderProgram("buttonShader");
    }

    @Override
    public String getName() {
        return null;
    }
}
