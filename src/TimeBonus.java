/*
TimeBonus.java
Sat Arora
Time Bonus class that acts similar to the Coin, but is used to add time until the game is over.
 */

//importing packages
import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public class TimeBonus extends Sprite implements Serializable {
    public int value; //the amount of time (in seconeds) the TimeBonus adds
    //constructor
    public TimeBonus(String id, int x, int y, ImageIcon image) {
        super(id,x,y,image);
        this.value = 10;
        super.hitBox = new Rectangle(x+5,y+5,75-15,75-15);
    }
    public void init() { super.setInstance(this); } //linking Sprite object to TimeBonus object
    //getter and setter
    public int getValue() { return this.value; }
    public void setValue(int value) { this.value = value; }
}
