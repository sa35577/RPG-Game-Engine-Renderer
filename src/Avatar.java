import javax.swing.*;
import java.awt.*;

public class Avatar extends Sprite  {
    public static final int RIGHT = 0, UP = 1, LEFT = 2, DOWN = 3;
    private String id;
    private int speed;
    private int health;
    private int bulletSpeed;
    public ImageIcon[] rightSprites, upSprites, leftSprites, downSprites;
    public ImageIcon[][] sprites;
    private double step;
    public Avatar(String id, int x, int y, ImageIcon image) {
        super(id,x,y,image);
        this.id = id;
        this.speed = 5;
        this.health = 3;
        super.hitBox = new Rectangle(x+5,y+5,75-10,75-10);
        this.bulletSpeed = 3;
        if (EditPanel.find(EditPanel.playerTopDownStrings,this.id) != -1) {
            rightSprites = new ImageIcon[3];
            upSprites = new ImageIcon[3];
            leftSprites = new ImageIcon[3];
            downSprites = new ImageIcon[3];
            for (int i = 0; i < 3; i++) {
                rightSprites[i] = new ImageIcon(String.format("Top-Down/%sR%d.png",this.id,i));
                upSprites[i] = new ImageIcon(new ImageIcon(String.format("Top-Down/%sU%d.png",this.id,i)).getImage().getScaledInstance(75,75,Image.SCALE_SMOOTH));
                downSprites[i] = new ImageIcon(new ImageIcon(String.format("Top-Down/%sD%d.png",this.id,i)).getImage().getScaledInstance(75,75,Image.SCALE_SMOOTH));
                leftSprites[i] = new ImageIcon(new ImageIcon(String.format("Top-Down/%sL%d.png",this.id,i)).getImage().getScaledInstance(75,75,Image.SCALE_SMOOTH));
            }
            sprites = new ImageIcon[4][3];
            sprites[RIGHT] = rightSprites;
            sprites[UP] = upSprites;
            sprites[LEFT] = leftSprites;
            sprites[DOWN] = downSprites;
        }
        else {
            rightSprites = new ImageIcon[5];
            leftSprites = new ImageIcon[5];
            for (int i = 0; i < 3; i++) {
                rightSprites[i] = new ImageIcon(new ImageIcon(String.format("Platform/%sR%d.png",this.id,i)).getImage().getScaledInstance(75,75, Image.SCALE_SMOOTH));
                leftSprites[i] = new ImageIcon(new ImageIcon(String.format("Platform/%sL%d.png",this.id,i)).getImage().getScaledInstance(75,75, Image.SCALE_SMOOTH));
            }
            rightSprites[3] = new ImageIcon(new ImageIcon(String.format("Platform/%sRU.png",this.id)).getImage().getScaledInstance(75,75, Image.SCALE_SMOOTH));
            rightSprites[4] = new ImageIcon(new ImageIcon(String.format("Platform/%sRD.png",this.id)).getImage().getScaledInstance(75,75, Image.SCALE_SMOOTH));
            leftSprites[3] = new ImageIcon(new ImageIcon(String.format("Platform/%sLU.png",this.id)).getImage().getScaledInstance(75,75, Image.SCALE_SMOOTH));
            leftSprites[4] = new ImageIcon(new ImageIcon(String.format("Platform/%sLD.png",this.id)).getImage().getScaledInstance(75,75, Image.SCALE_SMOOTH));
            upSprites = rightSprites;
            downSprites = leftSprites;
            sprites = new ImageIcon[4][5];
            sprites[0] = rightSprites;
            sprites[1] = rightSprites; sprites[3] = leftSprites;
            sprites[2] = leftSprites;
        }
    }

    public void init() {super.setInstance(this);}
    public int getHealth() {return this.health;}
    public int getSpeed() {return this.speed; }
    public void setSpeed(int speed) { this.speed = speed; }
    public String getId() {return this.id;}
    public void setId(String id) { this.id = id; }
    public void setHealth(int h) {this.health = h;}
    public int getBulletSpeed() { return this.bulletSpeed; }
    public void setBulletSpeed(int bulletSpeed) { this.bulletSpeed = bulletSpeed; }






}
