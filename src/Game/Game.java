package Game;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Game {

    public static boolean restart = false;

    public static void main(String[] args){
        setup();
    }

    public static void setup(){
        MainView.createTextFolder();
        try {
            Scanner s = new Scanner(new File("res/options.txt"));

            String fullscreen = s.nextLine();
            String res = s.nextLine();
            String backgroundMove = s.nextLine();

            if (fullscreen.replace("fullscreen:", "").equals("t")) {
                MainView.fullScreenBool = true;
                MainView.main = new MainView();

            }else {
                MainView.fullScreenBool = false;
                String[] dim = res.replace("res:", "").split(",");
                int width = Integer.parseInt(dim[0]);
                int height = Integer.parseInt(dim[1]);
                if(width < 200) width = 200;
                if(height < 200) height = 200;
                MainView.main = new MainView(width, height);
            }
            if(backgroundMove.replace("backgroundmove:", "").equals("t")){
                MainView.backgroundMoveBool = true;
            } else {
                MainView.backgroundMoveBool = false;
            }
        } catch (FileNotFoundException e) {
            MainView.main = new MainView();
        }

        if(restart){
            restart = false;
            setup();
        }


    }
}
