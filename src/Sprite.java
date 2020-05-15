import java.awt.*;
import java.util.*;

public class Sprite<T> {
    private static HashMap<Integer,Sprite> spriteHashMap = new HashMap<>();
    protected int locX,locY,key;
    protected Image img;
    protected String id;
    protected T instance;

    public Sprite(String id, int x, int y, Image image) {
        this.id = id;
        this.locX = x;
        this.locY = y;
        this.key = this.locX*200000+this.locY;
        this.img = image;

        if (spriteHashMap.containsKey(this.key)) {
            spriteHashMap.remove(this.key);
        }
        spriteHashMap.put(this.key,this);

    }
    public static void delete(int x, int y) {
        if (spriteHashMap.containsKey(x*200000+y)) spriteHashMap.remove(x*200000+y);


    }

    public int getX() {return this.locX;}
    public int getY() {return this.locY;}
    public Image getImg() {return this.img;}
    public int getKey() {return this.key;}
    public String getId() {return this.id;}
    public static HashMap<Integer,Sprite> getSpriteHashMap() {return spriteHashMap; }
    protected void setInstance(T instance) {
        this.instance = instance;
    }


}
