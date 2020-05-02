import java.awt.*;

public class Enemy extends Sprite {
    private String id;
    private int speed;
    private int health;
    public Enemy(String id, int x, int y, Image image, int speed, int health) {
        super(id, x, y, image);
        this.id = id;
        this.speed = speed;
        this.health = health;
    }
    public void init() {super.setInstance(this);}
    public int getHealth() {return this.health;}
    public int getSpeed() {return this.speed; }
    public String getId() {return this.id;}
    public void setHealth(int h) {this.health = h;}
    public void setSpeed(int s) {this.speed = s;}
    public void setId(String i) {this.id = i;}




}
