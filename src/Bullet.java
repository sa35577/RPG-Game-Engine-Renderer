/*
Bullet.java
Sat Arora
Bullet class for shooting bullets to enemies or the avatar.
 */

//importing packages
import java.awt.*;
public class Bullet {
    private int x,y; //current location, x & y coords
    private int dir; //direction of bullet
    private Rectangle hitBox; //hitBox used for rectangle collisions with other objects
    private int speed; //speed of bullet
    private boolean avatarBullet; //boolean holding whether the bullet was initiated by the avatar
    private static int avatarBulletPeriod; //time interval for seperation between bullets
    private int damage; //damage done by each bullet
    //constructor
    public Bullet(int m, int n, int dir, boolean avatarBullet, int speed, int damage) {
        x = m;
        y = n;
        this.dir = dir;
        this.hitBox = new Rectangle(x-10,y-10,20,20);
        this.avatarBullet = avatarBullet;
        this.speed = speed+3;
        this.damage = damage;
    }
    //getters and setters
    public int getX() {return x;}
    public int getY() {return y;}
    public int getDir() {return dir;}
    public void setX(int m) {x = m;}
    public void setY(int n) {y = n;}
    public void setDir(int dir) {this.dir = dir;}
    public Rectangle getHitBox() {
        return hitBox;
    }
    public void setHitBox(Rectangle hitBox) {
        this.hitBox = hitBox;
    }
    public boolean isAvatarBullet() {
        return avatarBullet;
    }
    public void setAvatarBullet(boolean avatarBullet) {
        this.avatarBullet = avatarBullet;
    }
    public int getSpeed() {
        return speed;
    }
    public void setSpeed(int speed) {
        this.speed = speed;
    }
    public int getDamage() {
        return damage;
    }
    public void setDamage(int damage) {
        this.damage = damage;
    }



    public static void decrementAvatarTime() {
        avatarBulletPeriod = Math.max(avatarBulletPeriod-1,0);
    }
    public static boolean avatarReadyToShoot() {return avatarBulletPeriod == 0; }
    public static void setAvatarTime() { avatarBulletPeriod = 30; }
    public void translate(int dx, int dy) {
        this.x += dx;
        this.y += dy;
        this.hitBox.translate(dx,dy);
    }


    @Override
    public int hashCode() {return x*2000+y;}

}

