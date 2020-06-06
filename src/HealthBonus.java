/*
HealthBonus.java
Sat Arora
Health Bonus class that acts similar to the Coin, but is used to add health to the avatar.
 */

//importing packages
import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public class HealthBonus extends Sprite implements Serializable {
    public int value; //how much the avatar's health will increase
    //constructor
    public HealthBonus(String id, int x, int y, ImageIcon image) {
        super(id,x,y,image);
        this.value = 1;
        super.hitBox = new Rectangle(x+10,y+10,75-20,75-20);
    }
    public void init() { super.setInstance(this); } //linking Sprite object to HealthBonus object
    //getter and setter
    public int getValue() { return this.value; }
    public void setValue(int value) { this.value = value; }
}
