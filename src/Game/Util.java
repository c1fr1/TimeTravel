package Game;

public class Util {

    public static int getSign(float thing){
        if(thing > 0){
            return 1;
        } else if(thing < 0){
            return -1;
        } else {
            return 0;
        }
    }

    public static boolean isNumericValue(char character) {
        int val = Character.getNumericValue(character);
        return val >= 0 && val < 10;
    }

    public static int numVal(char character) {
        int val = Character.getNumericValue(character);
        if (val >= 10) {
            return -1;
        }
        return val;
    }
}
