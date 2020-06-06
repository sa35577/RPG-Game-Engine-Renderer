/*
Block.java
Sat Arora
Block class that stores the data for the blocks, which are used for walls or aesthetics, so
they don't require much data besides the requirements for the super class
 */

//importing packages
import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public class Block extends Sprite implements Serializable {
    //constructor
    public Block(String id, int x, int y, ImageIcon image) {
        super(id,x,y,image);
        super.hitBox = new Rectangle(x+5,y,75-10,75);
    }
    public void init() {super.setInstance(this);} //method used to link the super object to this one


}
