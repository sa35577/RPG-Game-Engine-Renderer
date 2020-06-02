import javax.swing.*;
import java.awt.*;

public class Spike extends Sprite {
    private int dmg;
    public Spike(String id, int x, int y, ImageIcon image) {
        super(id,x,y,image);
        this.dmg = 1;
        super.hitBox = new Rectangle(x,y,75,75);
    }
    public void init() { super.setInstance(this); }
    public int getDmg() { return this.dmg; }
    public void setDmg(int dmg) { this.dmg = dmg; }

}
