import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public class KeyInsert extends Sprite implements Serializable {
    public static final int GREEN = 0, RED = 1;
    private int value;
    private int color;
    public KeyInsert(String id, int x, int y, ImageIcon image, int color) {
        super(id,x,y,image);
        this.value = 1;
        this.color = color - 4;
        super.hitBox = new Rectangle(x,y,75,75);
    }
    public void init() {super.setInstance(this);}
    public int getValue() { return value; }
    public void setValue(int value) { this.value = value; }
    public int getColor() { return color; }
    public void setColor(int color) { this.color = color; }
}
