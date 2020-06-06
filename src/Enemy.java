import javax.swing.*;
import java.awt.*;

public class Enemy extends Sprite  {
    private static final long serialVersionUID = -8694421890318300463L;
    public static final int RIGHT = 0, UP = 1, LEFT = 2, DOWN = 3;
    private String id;
    private int speed;
    private int health;
    private int maxHealth;
    private int direction;
    private boolean stationary;
    private int bulletSpeed;
    private int damage;
    private double step;
    public ImageIcon[] rightSprites, upSprites, leftSprites, downSprites;
    public ImageIcon[][] sprites;
    private int drawHealthBar;
    private int bulletPeriod,bulletTimer;
    public Enemy(String id, int x, int y, ImageIcon image, int speed, int health) {
        super(id,x,y,image);
        if (EditPanel.find(EditPanel.enemyTopDownStrings,id) != -1)
            super.setImg(new ImageIcon(new ImageIcon(String.format("Top-Down/%sL0.png",id)).getImage().getScaledInstance(75,75,Image.SCALE_SMOOTH)));
        else
            super.setImg(new ImageIcon(new ImageIcon(String.format("Platform/%sL0.png",id)).getImage().getScaledInstance(75,75,Image.SCALE_SMOOTH)));
        this.id = id;
        this.speed = speed;
        this.health = health;
        this.maxHealth = this.health;
        this.direction = LEFT;
        this.stationary = false;
        super.hitBox = new Rectangle(x+5,y+5,75-10,75-10);
        this.bulletSpeed = 5;
        this.damage = 1;
        this.step = 0;
        this.drawHealthBar = 0;
        if (EditPanel.find(EditPanel.enemyTopDownStrings,this.id) != -1) {
            rightSprites = new ImageIcon[3];
            upSprites = new ImageIcon[3];
            leftSprites = new ImageIcon[3];
            downSprites = new ImageIcon[3];
            for (int i = 0; i < 3; i++) {
                rightSprites[i] = new ImageIcon(new ImageIcon(String.format("Top-Down/%sR%d.png",this.id,i)).getImage().getScaledInstance(75,75, Image.SCALE_SMOOTH));
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
            upSprites = null;
            downSprites = null;
            for (int i = 0; i < 3; i++) {
                rightSprites[i] = new ImageIcon(new ImageIcon(String.format("Platform/%sR%d.png",this.id,i)).getImage().getScaledInstance(75,75, Image.SCALE_SMOOTH));
                leftSprites[i] = new ImageIcon(new ImageIcon(String.format("Platform/%sL%d.png",this.id,i)).getImage().getScaledInstance(75,75, Image.SCALE_SMOOTH));
            }
            rightSprites[3] = new ImageIcon(new ImageIcon(String.format("Platform/%sRU.png",this.id)).getImage().getScaledInstance(75,75, Image.SCALE_SMOOTH));
            rightSprites[4] = new ImageIcon(new ImageIcon(String.format("Platform/%sRD.png",this.id)).getImage().getScaledInstance(75,75, Image.SCALE_SMOOTH));
            leftSprites[3] = new ImageIcon(new ImageIcon(String.format("Platform/%sLU.png",this.id)).getImage().getScaledInstance(75,75, Image.SCALE_SMOOTH));
            leftSprites[4] = new ImageIcon(new ImageIcon(String.format("Platform/%sLD.png",this.id)).getImage().getScaledInstance(75,75, Image.SCALE_SMOOTH));
            sprites = new ImageIcon[4][5];
            sprites[0] = rightSprites;
            sprites[1] = null; sprites[3] = null;
            sprites[2] = leftSprites;
        }

    }
    public void init() {super.setInstance(this);}
    public int getHealth() {return this.health;}
    public int getMaxHealth() { return this.maxHealth; }
    public int getSpeed() {return this.speed; }
    public String getId() {return this.id;}

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public boolean isStationary() {
        return stationary;
    }

    public void setStationary(boolean stationary) {
        this.stationary = stationary;
    }

    public void setHealth(int h) {
        this.health = h;
        this.maxHealth = h;
    }
    public void setCurHealth(int h) {
        this.health = h;
    }
    public void setSpeed(int s) {this.speed = s;}
    public void setId(String i) {this.id = i;}
    public Sprite getSprite() { return super.getSprite(); }
    public int getBulletSpeed() { return this.bulletSpeed; }
    public void setBulletSpeed(int bulletSpeed) { this.bulletSpeed = bulletSpeed; }
    public void setDamage(int damage) { this.damage = damage; }
    public int getDamage() { return this.damage; }
    public void increaseStep() {
        this.step += 0.05;
        if (this.step >= 4) this.step -= 4;
    }
    public double getStep() { return this.step; }
    public void decrementHealhtBar() {
        this.drawHealthBar = Math.max(this.drawHealthBar-1,0);
    }
    public void setDrawHealthBar(int drawHealthBar) { this.drawHealthBar = drawHealthBar; }
    public int getDrawHealthBar() { return this.drawHealthBar; }

    public int getBulletPeriod() {
        return bulletPeriod;
    }

    public void setBulletPeriod(int bulletPeriod) {
        this.bulletPeriod = bulletPeriod;
    }
    public void incrementTimer() {
        bulletTimer++;
        bulletTimer %= 100;
    }
    public int getBulletTimer() { return this.bulletTimer; }

}
