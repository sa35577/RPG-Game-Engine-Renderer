import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public class TimeBonus extends Sprite implements Serializable {
    public int value;
    public TimeBonus(String id, int x, int y, ImageIcon image) {
        super(id,x,y,image);
        this.value = 10;
        super.hitBox = new Rectangle(x+5,y+5,75-15,75-15);
    }
    public void init() { super.setInstance(this); }
    public int getValue() { return this.value; }
    public void setValue(int value) { this.value = value; }
}
