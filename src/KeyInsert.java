/*
KeyInsert.java
Sat Arora
Key Insert class used to open key holes that come in the avatar's way from reaching the goal
 */

//importing packages
import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public class KeyInsert extends Sprite implements Serializable {
    public static final int GREEN = 0, RED = 1; //color ints
    private int value; //how much the key increases the total key count
    private int color; //color of key
    //constructor
    public KeyInsert(String id, int x, int y, ImageIcon image, int color) {
        super(id,x,y,image);
        this.value = 1;
        this.color = color - 4;
        super.hitBox = new Rectangle(x,y,75,75);
    }
    public void init() {super.setInstance(this);} //linking Sprite object to KeyInsert object
    //getters and setters
    public int getValue() { return value; }
    public void setValue(int value) { this.value = value; }
    public int getColor() { return color; }
    public void setColor(int color) { this.color = color; }
}
