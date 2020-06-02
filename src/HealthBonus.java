import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public class HealthBonus extends Sprite implements Serializable {
    public int value;
    public HealthBonus(String id, int x, int y, ImageIcon image) {
        super(id,x,y,image);
        this.value = 1;
        super.hitBox = new Rectangle(x+10,y+10,75-20,75-20);
    }
    public void init() { super.setInstance(this); }
    public int getValue() { return this.value; }
    public void setValue(int value) { this.value = value; }
}
