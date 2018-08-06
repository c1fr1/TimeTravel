package Game.Views;

import Game.*;
import Game.Buttons.DoubleTextureButton;
import Game.Buttons.SpriteButton;
import engine.EnigView;
import engine.OpenGL.*;
import org.joml.Vector2f;
import org.lwjglx.debug.org.eclipse.jetty.server.Authentication;

import static Game.MainView.screenVAO;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_REPEAT;

public class MainMenu extends EnigView {

    private DoubleTextureButton title;
    private DoubleTextureButton start;
    private DoubleTextureButton levelSelect;
    private DoubleTextureButton options;
    private DoubleTextureButton quit;

    public ShaderProgram backgroundShader;
    public Texture starBackground;
    public Texture frontStars;
    
    public Vector2f backgroundOffset;
    
    public static boolean mainMenuQuit = false;

   	int mainMenuSelector;

    public MainMenu(EnigWindow window){
        super(window, false);
    }

    float aspectRatio;

    @Override
    public void setup() {
		glDisable(GL_DEPTH_TEST);
        aspectRatio = (float)window.getHeight()/(float)window.getWidth();//0.56222546
		float border = -0.9f / aspectRatio;
        title = new DoubleTextureButton(border,.5f,1.2f,0.4f,"res/menu/titleImage.png", "res/menu/titleImage.png", aspectRatio);
        start = new DoubleTextureButton(border,0f,1f,0.125f,"res/menu/playButton-0.png","res/menu/playButton-1.png", aspectRatio);
        levelSelect = new DoubleTextureButton(border,-.2f,1f,0.125f,"res/menu/selectorButton-0.png","res/menu/selectorButton-1.png", aspectRatio);
        options = new DoubleTextureButton(border,-.4f,1f,0.125f,"res/menu/optionsButton-0.png","res/menu/optionsButton-1.png", aspectRatio);
        quit = new DoubleTextureButton(border,-.6f,1f,0.125f,"res/menu/quitButton-0.png", "res/menu/quitButton-1.png", aspectRatio);
        SpriteButton.shader = new ShaderProgram("buttonShader");
        backgroundShader = new ShaderProgram("backgroundShader");
		starBackground = new Texture("res/sprites/stars.png");
		frontStars = new Texture("res/sprites/frontstars.png");
		screenVAO = new VAO(-1f, -1f, 2f, 2f);
		backgroundOffset = new Vector2f(0f, 0f);
	
		frontStars.bind();
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		Texture.unbind();
		starBackground.bind();
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		Texture.unbind();

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
			renderBackground();
			backgroundOffset.x += 0.001f;
			
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
    
    public void renderBackground() {
		backgroundShader.enable();
		backgroundShader.shaders[2].uniforms[0].set(backgroundOffset.mul(0.6f, new Vector2f()));
		starBackground.bind();
		screenVAO.prepareRender();
		screenVAO.drawTriangles();
		frontStars.bind();
		backgroundShader.shaders[2].uniforms[0].set(backgroundOffset);
		screenVAO.drawTriangles();
		screenVAO.unbind();
	}

    @Override
    public String getName() {
        return "Paradox Pulverizer";
    }
}
