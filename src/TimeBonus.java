import java.awt.*;
public class TimeBonus extends Sprite {
    public int value;
    public TimeBonus(String id, int x, int y, Image image) {
        super(id,x,y,image);
        this.value = 10;
    }
    public void init() { super.setInstance(this); }
    public int getValue() { return this.value; }
    public void setValue(int value) { this.value = value; }
}
