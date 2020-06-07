/*
Enemy.java
Sat Arora
Enemy class that holds the data for the enemies in the game.
 */

import javax.swing.*;
import java.awt.*;

public class Enemy extends Sprite  {
    private static final long serialVersionUID = -8694421890318300463L; //long for serialization id (writing object to text file)
    public static final int RIGHT = 0, UP = 1, LEFT = 2, DOWN = 3;
    private String id; //enemy string name
    private int speed; //speed of the enemy while moving
    private int health; //amount of health the enemy has
    private int maxHealth; //max health the enemy can have (the initial health)
    private int direction; //direction of enemy
    private boolean stationary; //boolean holding whether the enemy is stationary
    private int bulletSpeed; //speed of released bullets
    private int damage; //damage done by bullets
    private double step; //step for the current sprite to display in motion
    public ImageIcon[] rightSprites, upSprites, leftSprites, downSprites; //storing sprites for enemy
    public ImageIcon[][] sprites; //stores all the sprites in a 2d array
    private int drawHealthBar; //acts as a timer for drawing the health bar when hit
    private int bulletPeriod,bulletTimer; //bullet period for frequency of bullets, and timer determining when the enemy can shoot
    private int velocityY;
    private int initialVelocityY;
    private boolean onGround;
    private int jumpPeriod;
    private int jumpTimer;
    //constructor
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
        if (EditPanel.find(EditPanel.enemyTopDownStrings,this.id) != -1) { //loading sprites for top down, if id is found in top down array
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
        else { //load sprites for platform
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
    public void init() {super.setInstance(this);} //linking Sprite and Enemy
    //getters and setters
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
    //sets health for the edit panel, meaning both the current and meax health are the value passed in
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
    //increasing step for sprite drawing
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
    //incrementing timer for being able to release a bullet periodically
    public void incrementTimer() {
        bulletTimer++;
        bulletTimer %= 100;
    }
    public int getBulletTimer() { return this.bulletTimer; }

    public int getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(int velocityY) {
        this.velocityY = velocityY;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    public int getJumpPeriod() {
        return jumpPeriod;
    }

    public void setJumpPeriod(int jumpPeriod) {
        this.jumpPeriod = jumpPeriod;
    }

    public int getJumpTimer() {
        return jumpTimer;
    }
    public void setJumpTimer(int jumpTimer) {
        this.jumpTimer = jumpTimer;
    }

    public boolean incJumpTimer() {
        this.jumpTimer++;
        this.jumpTimer %= this.jumpPeriod;
        return this.jumpTimer == 0;
    }

    public int getInitialVelocityY() {
        return initialVelocityY;
    }

    public void setInitialVelocityY(int initialVelocityY) {
        this.initialVelocityY = initialVelocityY;
    }
}
