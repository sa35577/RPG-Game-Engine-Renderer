import java.awt.*;
import java.util.*;
public class Item extends Sprite {

    public Item(String id, int x, int y, Image image) {
        super(id, x, y, image);
    }

    public void init() {super.setInstance(this);}
}
