/*
Spike.java
Sat Arora
Spike class that acts as a block that does damage to the avatar
if their hit boxes collide.
 */

//importing packages
import javax.swing.*;
import java.awt.*;

public class Spike extends Sprite {
    private int dmg; //damage done by spike
    //constructor
    public Spike(String id, int x, int y, ImageIcon image) {
        super(id,x,y,image);
        this.dmg = 1;
        super.hitBox = new Rectangle(x,y,75,75);
    }
    public void init() { super.setInstance(this); } //linking Sprite object to Spike object
    //getter and setter
    public int getDmg() { return this.dmg; }
    public void setDmg(int dmg) { this.dmg = dmg; }

}
