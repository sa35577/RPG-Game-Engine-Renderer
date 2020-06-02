import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public class Coin extends Sprite implements Serializable {
    private boolean editable;
    private int pts;
    public Coin(String id, int x, int y, ImageIcon image, boolean editable) {
        super(id,x,y,image);
        this.editable = editable;
        if (editable) {
            this.pts = 3;
            super.hitBox = new Rectangle(x+18,y+18,75-40,75-40);
        }
        else {
            this.pts = 1;
            super.hitBox = new Rectangle(x+25,y+25,75-50,75-50);
        }

    }
    public void init() { super.setInstance(this); }
    public boolean isEditable() { return this.editable; }
    public int getPts() { return this.pts; }
    public void setPts(int pts) { this.pts = pts; }
}
