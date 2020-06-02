import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public class KeyHole extends Sprite implements Serializable {
    public static final int GREEN = 0, RED = 1;
    private int unlockRequirement;
    private int color;
    public KeyHole(String id, int x, int y, ImageIcon image, int color) {
        super(id,x,y,image);
        this.unlockRequirement = 1;
        this.color = color - 9;
        super.hitBox = new Rectangle(x,y,75,75);
    }
    public void init() {super.setInstance(this);}
    public int getUnlockRequirement() { return unlockRequirement; }
    public void setUnlockRequirement(int unlockRequirement) { this.unlockRequirement = unlockRequirement; }
    public int getColor() { return color; }
    public void setColor(int color) { this.color = color; }
}
