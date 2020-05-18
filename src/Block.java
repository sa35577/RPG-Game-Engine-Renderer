import java.awt.*;

public class Block extends Sprite {
    //handles the "cement","cloud","dirt","glass","gold","grass";
    private String id;

    public Block(String id, int x, int y, Image image) {
        super(id,x,y,image);
        this.id = id;
    }
    public void init() {super.setInstance(this);}


}
