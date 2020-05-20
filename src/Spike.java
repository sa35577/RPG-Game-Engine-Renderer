import java.awt.*;
public class Spike extends Sprite {
    private int dmg;
    public Spike(String id, int x, int y, Image image) {
        super(id,x,y,image);
        this.dmg = dmg;
    }
    public void init() { super.setInstance(this); }
    public int getDmg() { return this.dmg; }
    public void setDmg(int dmg) { this.dmg = dmg; }

}
