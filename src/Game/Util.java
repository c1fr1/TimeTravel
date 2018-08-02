package Game;

public class Util {

    /**
     *
     * @param thing
     * @return returns sign of float
     */
    public static int getSign(float thing){
        if(thing > 0){
            return 1;
        } else if(thing < 0){
            return -1;
        } else {
            return 0;
        }
    }

    /**
     *
     * @param thing
     * @return returns sign of an int
     */
    public static int getSign(int thing){
        if(thing > 0){
            return 1;
        } else if(thing < 0){
            return -1;
        } else {
            return 0;
        }
    }

    /**
     *
     * @param character
     * @return if the char is a numeric value
     */
    public static boolean isNumericValue(char character) {
        int val = Character.getNumericValue(character);
        return val >= 0 && val < 10;
    }

    /**
     *
     * @param character
     * @return if number then returns number as int otherwise, -1
     */
    public static int numVal(char character) {
        int val = Character.getNumericValue(character);
        if (val >= 10) {
            return -1;
        }
        return val;
    }
    
    public static float absMin(float a, float b) {
    	if (Math.abs(a) < Math.abs(b)) {
    		return a;
		}
		return b;
	}
}
