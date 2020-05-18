import java.awt.*;

public class Avatar extends Sprite {
    private String id;
    private int speed;
    private int health;
    public Avatar(String id, int x, int y, Image image) {
        super(id,x,y,image);
        this.id = id;
        this.speed = 5;
        this.health = 3;
    }

    public void init() {super.setInstance(this);}
    public int getHealth() {return this.health;}
    public int getSpeed() {return this.speed; }
    public String getId() {return this.id;}
    public void setHealth(int h) {this.health = h;}
    public void setSpeed(int s) {this.speed = s;}




}
