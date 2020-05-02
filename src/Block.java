import java.awt.*;
import java.util.*;
public class Block extends Sprite {
    private int dmg;
    public Block(String id, int x, int y, Image image) {
        super(id,x,y,image);
        this.dmg = 1;
    }
    public void init() {super.setInstance(this);}
    public int getDmg() {return dmg;}
    public void setDmg(int v) {this.dmg = v;}
}
