/*
KeyHole.java
Sat Arora
Data for key hole object, which is kept as a block until enough keys of the
key hole's color are collected.
 */

//importing packages
import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public class KeyHole extends Sprite implements Serializable {
    public static final int GREEN = 0, RED = 1; //color ints
    private int unlockRequirement; //the amount of keys of the key hole's color required to open the key hole
    private int color; //color of the key hole
    //constructor
    public KeyHole(String id, int x, int y, ImageIcon image, int color) {
        super(id,x,y,image);
        this.unlockRequirement = 1;
        this.color = color - 9; //-9 was for the index passed in the EditPanel (green was 9, red was 10)
        super.hitBox = new Rectangle(x,y,75,75);
    }
    public void init() {super.setInstance(this);} //Linking Sprite object to KeyHole object
    //getters and setters
    public int getUnlockRequirement() { return unlockRequirement; }
    public void setUnlockRequirement(int unlockRequirement) { this.unlockRequirement = unlockRequirement; }
    public int getColor() { return color; }
    public void setColor(int color) { this.color = color; }
}
