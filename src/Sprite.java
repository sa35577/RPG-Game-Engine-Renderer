import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.*;


public class Sprite<T> implements Serializable {
    private static final long serialVersionUID = 2552520455099101002L;
    private static HashMap<Integer,Sprite> spriteHashMap = new HashMap<>();
    protected int locX,locY,key;
    protected ImageIcon img;
    protected String id;
    protected T instance;
    protected Rectangle hitBox;
    public Sprite(String id, int x, int y, ImageIcon image) {
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
    public ImageIcon getImg() {return this.img;}
    public void setImg(ImageIcon img) { this.img = img; }
    public int getKey() {return this.key;}
    public String getId() {return this.id;}
    public static HashMap<Integer,Sprite> getSpriteHashMap() {return spriteHashMap; }
    protected void setInstance(T instance) {
        this.instance = instance;
    }
    public static void clear() {
        Iterator<Map.Entry<Integer, Sprite>> it = spriteHashMap.entrySet().iterator();
        ArrayList<Integer> deletions = new ArrayList<>();
        while (it.hasNext()) {
            Map.Entry<Integer, Sprite> pair = it.next();
            Sprite sprite = pair.getValue();
            int k = pair.getKey();
            if (sprite.instance instanceof Avatar || sprite.instance instanceof Enemy) {
                deletions.add(new Integer(k));
            }
        }
        for (Integer k : deletions) {
            spriteHashMap.remove(k);
        }
    }
    public Sprite getSprite() { return this; }
    public void translate(int dx, int dy) {
        this.locX += dx;
        this.locY += dy;
        this.hitBox.translate(dx,dy);
    }



}
