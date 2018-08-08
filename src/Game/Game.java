package Game;

import Game.Views.MainMenu;
import Game.Views.MainView;
import com.sun.org.apache.xerces.internal.util.SymbolTable;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Game {

    public static boolean restart = false;



    public static void main(String[] args){
        setup();
    }

    public static void setup(){
        MainView.quit = false;
        MainMenu.mainMenuQuit = false;
        restart = false;
        MainView.createTextFolder();
        try {
            Scanner s = new Scanner(new File("res/options.txt"));

            int width = 200;
            int height = 200;

            String fullscreen = s.nextLine();
            String res = s.nextLine();
            String backgroundMove = s.nextLine();
            String texLock = s.nextLine();
            String timer = s.nextLine();
            MainView.inv = new Inventory();
            if (fullscreen.replace("fullscreen:", "").equals("t")) {
                MainView.fullScreenBool = true;
            } else {
                MainView.fullScreenBool = false;
                String[] dim = res.replace("res:", "").split(",");
                width = Integer.parseInt(dim[0]);
                height = Integer.parseInt(dim[1]);
                if(width < 200) width = 200;
                if(height < 200) height = 200;
            }
            if(backgroundMove.replace("backgroundmove:", "").equals("t")){
                MainView.backgroundMoveBool = true;
            }
            if(texLock.replace("texLock:", "").equals("t")){
                LevelBase.lockedTextures = true;
            }
            if(timer.replace("timer:", "").equals("t")){
                MainView.timerBool = true;
            }
            if(MainView.fullScreenBool){
                MainView.main = new MainView();
            } else {
                MainView.main = new MainView(width, height);

            }
        } catch (FileNotFoundException e) {
            MainView.main = new MainView();
        }

    }
}
