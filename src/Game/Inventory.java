package Game;

import java.util.ArrayList;

public class Inventory {



    public ArrayList<Character> inventory;

    public Inventory(){
        inventory = new ArrayList();
    }

    /**
     *
     * @return returns inventory
     */
    public ArrayList getInventory() {
        return inventory;
    }

    /**
     *
     * @param inventory set it to external inventory
     */
    public void setInventory(ArrayList inventory) {
        this.inventory = inventory;
    }

    /**
     *
     * @param item item to remove
     * @return true if succ false if not
     */
    public boolean getAndRemove(char item){
        for(int i = 0; i < inventory.size(); i++){
            if(inventory.get(i) == item){
                inventory.remove(i);
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param item item to check if in
     * @return true if yes false if no
     */
    public boolean check(char item){
        for(Character i: inventory){
            if(i == item){
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param ad thing to add to inventory (at the end)
     */
    public void add(char ad){
        inventory.add(ad);
    }

    /**
     *
     * @return the size of the inventory
     */
    public int getSize(){
        return inventory.size();
    }

    /**
     *
     * @param index the index to get the char from
     * @return char in inventory at index
     */
    public char get(int index){
        return inventory.get(index);
    }

    /**
     *
     * @param ch counts the instances of this char
     * @return returns instances of
     */
    public int instancesOf(char ch){
        int count = 0;
        for (Character i: inventory){
            if(i == ch){
                count++;
            }
        }
        return count;
    }

    /**
     *
     * @param ch finds location of ch
     * @return returns all indexes of ch
     * */
    public int[] locationOf(char ch){
        ArrayList<Integer> indexes = new ArrayList<Integer>();
        for(int i = 0; i < inventory.size(); i++){
            if(inventory.get(i) == ch){
                indexes.add(i);
            }
        }
        int[] intIndexes = new int[indexes.size()];
        for(int i = 0; i < indexes.size(); i++){
            intIndexes[i] = indexes.get(i);
        }
        return intIndexes;
    }

    public void reset(){
        inventory = new ArrayList();
    }
}
