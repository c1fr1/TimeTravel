package Game;

import engine.EnigView;
import engine.OpenGL.EnigWindow;

import java.io.*;
import java.util.Scanner;

public class LevelSelect extends EnigView {

    static int[] levelState = new int[new File("res/Levels").listFiles().length];
    //0 = not cleared
    //1 = clear
    //2 = locked

    public LevelSelect(EnigWindow window){
        super(window, false);
    }


    @Override
    public boolean loop() {
        return false;
    }

    @Override
    public void setup() {
        //reads file
        Scanner reader = new Scanner("res/levelComplete.txt");
        String read = reader.nextLine();
        String[] readArray = read.split(",");
        int index = -1;
        for(int i = 0; i < readArray.length; i++){
            if(readArray[i].equals('c')){
                levelState[i] = 1;
            } else if(readArray[i].equals('n') && index == -1){
                levelState[i] = 0;
                index = i;
            } else if(readArray[i].equals('n') && index > -1){
                levelState[i] = 2;
            }
        }
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
