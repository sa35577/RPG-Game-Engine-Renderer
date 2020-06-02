import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public class Block extends Sprite implements Serializable {
    //handles the "cement","cloud","dirt","glass","gold","grass","teleport";

    public Block(String id, int x, int y, ImageIcon image) {
        super(id,x,y,image);
        super.hitBox = new Rectangle(x+5,y,75-10,75);
    }
    public void init() {super.setInstance(this);}


}
