import java.awt.*;
import java.util.*;

public class Sprite {
    private static HashMap<Integer,Sprite> spriteHashMap = new HashMap<>();
    private int locX,locY,key;
    private Image img;
    private String id;


    public Sprite(String id, int x, int y, Image image) {
        this.id = id;
        this.locX = x;
        this.locY = y;
        this.key = this.locX*200000+this.locY;
        this.img = image;

        if (spriteHashMap.containsKey(this.key)) {
            spriteHashMap.remove(new Integer(this.key));
        }
        spriteHashMap.put(new Integer(this.key),this);

    }
    public static void delete(int x, int y) {
        if (spriteHashMap.containsKey(x*200000+y)) spriteHashMap.remove(new Integer(x*200000+y));
    }

    public int getX() {return this.locX;}
    public int getY() {return this.locY;}
    public Image getImg() {return this.img;}
    public String getId() {return this.id;}
    public int getKey() {return this.key;}
    public static HashMap<Integer,Sprite> getSpriteHashMap() {return spriteHashMap; }

}
