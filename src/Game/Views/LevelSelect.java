package Game.Views;

import Game.MainView;
import Game.ShaderOptimizedButton;
import Game.SpriteButton;
import Game.UserControls;
import engine.EnigView;
import engine.OpenGL.*;
import sun.tools.jar.Main;

import java.io.*;
import java.util.Scanner;

public class LevelSelect extends EnigView {

    public SpriteButton[] levels;

    public static int[] levelState = new int[new File("res/Levels").listFiles().length];
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
            //System.out.println(j);
        }
        levels = new SpriteButton[LevelSelect.levelState.length];
        int columnNumber = (int)(window.getHeight()/100f);
        int rowNumber = (int)(window.getWidth()/200f);
        for(int i = 0; i < LevelSelect.levelState.length; i++){

            float thingY = (0.4f + (i / rowNumber)*0.4f);
            float thingX = ((i%rowNumber)*0.4f + 0.4f) - 1;

            int thingXIndex = i % rowNumber;
            int thingYIndex = i / rowNumber;

            float thingWidth = 100f/window.getWidth()/aspectRatio;
            float thingHeight = 100f/window.getHeight();

            float thingXPos = (float)thingXIndex/(float)rowNumber*2f-1f;
            float thingYPos = ((float)thingYIndex/(float)columnNumber*-2f+1f) - thingHeight*4;




            //System.out.println(thingXPos + " " + thingYPos);
            //System.out.println(thingWidth + " " + thingHeight);

            if(levelState[i] == 1){
                levels[i] = new ShaderOptimizedButton(thingXPos, thingYPos,thingWidth,thingHeight, "res/sprites/levelSelectComplete.png");
            } else if(levelState[i] == 2){
                levels[i] = new ShaderOptimizedButton(thingXPos, thingYPos,thingWidth,thingHeight, "res/sprites/levelSelectLock.png");
            } else {
                levels[i] = new ShaderOptimizedButton(thingXPos, thingYPos, thingWidth, thingHeight, "res/sprites/levelSelect.png");
            }
        }
    }


    public boolean loop() {
        FBO.prepareDefaultRender();

        for (int i = 0; i < levels.length; i++) {
            levels[i].render(window.cursorXFloat, window.cursorYFloat, aspectRatio);
            if (levels[i].hoverCheck(window.cursorXFloat, window.cursorYFloat) && UserControls.leftMBPress(window) && levelState[i] != 2) {
                MainView.currentLevelNum = i;
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
                //System.out.println("1");
                levelState[i] = 1;
            } else if(readArray[i].equals("n") && index == -1){
                //System.out.println("0");
                levelState[i] = 0;
                index = i;
            } else if(readArray[i].equals("n") && index > -1){
                //System.out.println("2");
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
                for(int i = 1; i < new File("res/Levels").listFiles().length; i++){
                    format += ",n";
                }
                writer.println(format);
                writer.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }
}
