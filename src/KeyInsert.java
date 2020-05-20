import java.awt.*;
public class KeyInsert extends Sprite {
    public static final int GREEN = 0, RED = 1;
    private int value;
    private int color;
    public KeyInsert(String id, int x, int y, Image image, int color) {
        super(id,x,y,image);
        this.value = 1;
        this.color = color - 4;
    }
    public void init() {super.setInstance(this);}
    public int getValue() { return value; }
    public void setValue(int value) { this.value = value; }
    public int getColor() { return color; }
    public void setColor(int color) { this.color = color; }
}
