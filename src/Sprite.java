/*
Sprite.java
Sat Arora
Sprite class acts as a super class to the specified versions of each sprite, but holds
the general data that is common to every sprite.
 */

//importing packages
import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.*;


public class Sprite<T> implements Serializable {
    private static final long serialVersionUID = 2552520455099101002L; //long for serialization id (writing object to text file)
    private static HashMap<Integer,Sprite> spriteHashMap = new HashMap<>(); //static HashMap used for accessing sprites in O(1) time
    public int locX,locY; //the x and y coordinates of the sprite
    public int key; //the key value used in the hashmap
    public ImageIcon img; //image of sprite itself for drawing, stored as ImageIcon instead of image due to the Image class not being serializable
    public String id; //name of Sprite, stored in the program
    public T instance; //the generic instance, or specific type of sprite
    public Rectangle hitBox; //collision of the sprite is handled with the hit box rectangles
    private boolean visible; //holds whether the sprite is currently visible
    //constructor
    public Sprite(String id, int x, int y, ImageIcon image) {
        this.id = id;
        this.locX = x;
        this.locY = y;
        this.key = this.locX*200000+this.locY; //making a key that is unique to the sprite
        this.img = image;


        if (spriteHashMap.containsKey(this.key)) { //if there was a sprite at the current x,y then it will be removed
            spriteHashMap.remove(this.key);
        }
        spriteHashMap.put(this.key,this);

    }
    //delete method removes from the panel (by removing from the hash map)
    public static void delete(int x, int y) {
        if (spriteHashMap.containsKey(x*200000+y)) spriteHashMap.remove(x*200000+y);
    }
    //getters and setters
    public int getX() {return this.locX;}
    public int getY() {return this.locY;}
    public ImageIcon getImg() {return this.img;}
    public void setImg(ImageIcon img) { this.img = img; }
    public int getKey() {return this.key;}
    public String getId() {return this.id;}
    public static HashMap<Integer,Sprite> getSpriteHashMap() {return spriteHashMap; }
    //linking the Sprite object to the specific sprite object
    public void setInstance(T instance) {
        this.instance = instance;
    }
    //method that clears all enemies and avatars (switching from top-down to platform, or vice versa)
    public static void clear() {
        Iterator<Map.Entry<Integer, Sprite>> it = spriteHashMap.entrySet().iterator(); //iterator of HashMap
        ArrayList<Integer> deletions = new ArrayList<>();  //ArrayList holding all keys
        while (it.hasNext()) { //runs until all keys are checked
            Map.Entry<Integer, Sprite> pair = it.next(); //storing the entry pair
            Sprite sprite = pair.getValue();
            int k = pair.getKey();
            if (sprite.instance instanceof Avatar || sprite.instance instanceof Enemy) { //checking if the specific instance is from the Avatar or Enemy class
                deletions.add(new Integer(k));
            }
        }
        for (Integer k : deletions) { //removing with the keys stored in the deletions ArrayList
            spriteHashMap.remove(k);
        }
    }
    public Sprite getSprite() { return this; }
    //moving the sprite requires the x,y coordinates, as well as the hitBox to be moved
    public void translate(int dx, int dy) {
        this.locX += dx;
        this.locY += dy;
        this.hitBox.translate(dx,dy);
    }
    public void setVisible(boolean visible) { this.visible = visible; }
    public boolean getVisible() { return this.visible; }
    //add from saved file
    public static void put(Sprite sprite) {
        spriteHashMap.put(sprite.locX*200000+sprite.locY,sprite);
    }

}
