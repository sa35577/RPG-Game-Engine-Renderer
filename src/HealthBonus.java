import java.awt.*;
public class HealthBonus extends Sprite {
    public int value;
    public HealthBonus(String id, int x, int y, Image image) {
        super(id,x,y,image);
        this.value = 1;
    }
    public void init() { super.setInstance(this); }
    public int getValue() { return this.value; }
    public void setValue(int value) { this.value = value; }
}
