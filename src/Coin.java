import java.awt.*;
public class Coin extends Sprite {
    private boolean editable;
    private int pts;
    public Coin(String id, int x, int y, Image image, boolean editable) {
        super(id,x,y,image);
        this.editable = editable;
        if (editable) this.pts = 3;
        else this.pts = 1;

    }
    public void init() { super.setInstance(this); }
    public boolean isEditable() { return this.editable; }
    public int getPts() { return this.pts; }
    public void setPts(int pts) { this.pts = pts; }
}
