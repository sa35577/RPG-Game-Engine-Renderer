/*
Coin.java
Sat Arora
Coin class that holds the data for collecting coins throughout the level
 */

//importing packages
import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public class Coin extends Sprite implements Serializable {
    private boolean editable; //boolean holding whether the coin can be edited or not
    private int pts; //holding the value of the coin
    //constructor
    public Coin(String id, int x, int y, ImageIcon image, boolean editable) {
        super(id,x,y,image); //passing data to super class
        this.editable = editable;
        if (editable) {
            this.pts = 3;
            super.hitBox = new Rectangle(x+18,y+18,75-40,75-40); //slightly smaller image for 1 point coins
        }
        else {
            this.pts = 1;
            super.hitBox = new Rectangle(x+25,y+25,75-50,75-50); //slightly smaller image for variable-point coins
        }

    }
    public void init() { super.setInstance(this); } //linking the Sprite with the Coin object
    //getters and setters
    public boolean isEditable() { return this.editable; }
    public int getPts() { return this.pts; }
    public void setPts(int pts) { this.pts = pts; }
}
