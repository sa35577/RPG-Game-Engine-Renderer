import java.awt.*;

public class Block extends Sprite {
    //handles the "cement","cloud","dirt","glass","gold","grass","teleport";

    public Block(String id, int x, int y, Image image) {
        super(id,x,y,image);
    }
    public void init() {super.setInstance(this);}


}
