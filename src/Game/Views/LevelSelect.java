package Game.Views;

import Game.Buttons.ShaderOptimizedButton;
import Game.Buttons.SpriteButton;
import Game.StringRenderer;
import Game.UserControls;
import engine.EnigView;
import engine.OpenGL.*;
import org.joml.Vector4f;

import java.io.*;
import java.util.Scanner;

public class LevelSelect extends EnigView {

    public SpriteButton[] levels;
    public StringRenderer[] levelNumbers;

    public static int[] levelState;
    //0 = not cleared
    //1 = clear
    //2 = locked

    float aspectRatio;

    Texture select;
    VAO selectVAO;

    public LevelSelect(EnigWindow window){
        super(window, false);
    }

    public void LevelSelectButtons(EnigWindow window){

        for(int j: levelState){
        }
        levels = new SpriteButton[LevelSelect.levelState.length];
        levelNumbers =  new StringRenderer[LevelSelect.levelState.length];


        int columnNumber = (int)(((window.getHeight())/100f)/MainView.scale);
        int rowNumber = (int)(((window.getWidth())/100f)/MainView.scale) - 1;
        for(int i = 0; i < LevelSelect.levelState.length; i++){

            int thingXIndex = i % rowNumber;
            int thingYIndex = i / rowNumber;

            float thingWidth = 100f/window.getWidth()/aspectRatio;
            float thingHeight = 100f/window.getHeight();

            float thingXPos = ((float)thingXIndex/(float)rowNumber*2f-1f)/aspectRatio + thingWidth*MainView.scale;
            float thingYPos = ((float)thingYIndex/(float)columnNumber*-2f+1f) - thingHeight*MainView.scale*2;

            if(levelState[i] == 1){
                levels[i] = new ShaderOptimizedButton(thingXPos, thingYPos,thingWidth * MainView.scale,thingHeight * MainView.scale, "res/sprites/levelSelectComplete.png", aspectRatio);
            } else if(levelState[i] == 2){
                levels[i] = new ShaderOptimizedButton(thingXPos, thingYPos,thingWidth * MainView.scale,thingHeight * MainView.scale, "res/sprites/levelSelectLock.png", aspectRatio);
            } else {
                levels[i] = new ShaderOptimizedButton(thingXPos, thingYPos, thingWidth * MainView.scale, thingHeight * MainView.scale, "res/sprites/levelSelect.png", aspectRatio);
            }

            levelNumbers[i] = new StringRenderer(100*MainView.scale, (thingXPos*1920) * aspectRatio + 85, (thingYPos *1080) - 70);
        }
    }


    public boolean loop() {
        FBO.prepareDefaultRender();

        for (int i = 0; i < levels.length; i++) {
            levels[i].render(window.cursorXFloat, window.cursorYFloat, aspectRatio);
            levelNumbers[i].renderStr(Integer.toString(i));
            if (((levels[i].hoverCheck(window.cursorXFloat, window.cursorYFloat) && UserControls.leftMBPress(window)) ||
                    (levelNumbers[i].hoverCheck(Integer.toString(i), window.cursorXFloat, window.cursorYFloat, new Vector4f(.7f, .7f, 1f, 1f)) && UserControls.leftMBPress(window))) && levelState[i] != 2) {
                MainView.currentLevelNum = 0;
                MainView.main.nextLevel(i);
                //MainView.main.nextLevel(i);
                MainMenu.mainMenuQuit = true;
                return true;
            }
        }


        if(UserControls.pause(window)){
            return true;
        }
        return false;
    }

    @Override
    public void setup() {

        aspectRatio = (float)window.getHeight()/(float)window.getWidth();
        SpriteButton.shader = new ShaderProgram("buttonShader");

        //reads file
        Scanner reader = null;
        try {
            reader = new Scanner(new File("res/levelComplete.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String read = reader.nextLine();
        reader.close();
        String[] readArray = read.split(",");
        int index = -1;
        for(int i = 0; i < readArray.length; i++){
            if(readArray[i].equals("c")){
                levelState[i] = 1;
            } else if(readArray[i].equals("n") && index == -1){
                levelState[i] = 0;
                index = i;
            } else if(readArray[i].equals("n") && index > -1){
                levelState[i] = 2;
            }
        }
        LevelSelectButtons(window);
    }

    @Override
    public String getName() {
        return null;
    }

    public static void updateLevelTextDoc(){
        String out = "";
        for(int i = 0; i < levelState.length; i++){
            if(i != 0){
                out += ",";
            }
            if(levelState[i] == 0 || levelState[i] == 2){
                out += "n";
            } else if(levelState[i] == 1){
                out += "c";
            }
        }

        try {
            PrintWriter writer = new PrintWriter("res/levelComplete.txt", "UTF-8");
            writer.write(out);
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


    }

    public static void createTextFolder(){
        boolean check = false;
        for(File i:new File("res/").listFiles()){
            if(i.getName().equals("levelComplete.txt")){
                check = true;
            }
        }
        if(!check){
            try {
                PrintWriter writer = new PrintWriter("res/levelComplete.txt", "UTF-8");
                String format = "n";
                for(int i = 1; i < new File("res/levels").listFiles().length; i++){
                    if(new File("res/levels").listFiles()[i].getName().contains(".txt")) {
                        format += ",n";
                    }
                }
                format = format.substring(0, format.length()-2);
                writer.println(format);
                writer.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        int co = 0;
        for(File i: new File("res/levels").listFiles()){
            if(i.getName().contains(".txt")){
                co++;
            }
        }
        levelState = new int[co];
    }
}
