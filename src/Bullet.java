/*
Bullet.java
Sat Arora
Pair class that will be used to store locations of objects.
 */

import java.awt.*;

class Bullet {
    private int x,y;
    private int dir;
    private Rectangle hitBox;
    private int speed;
    private boolean avatarBullet;
    private static int avatarBulletPeriod = 0;
    private int damage;


    public Bullet(int m, int n, int dir, boolean avatarBullet, int speed, int damage) {
        x = m;
        y = n;
        this.dir = dir;
        this.hitBox = new Rectangle(x-10,y-10,20,20);
        this.avatarBullet = avatarBullet;
        this.speed = speed+3;
        this.damage = damage;
    }
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

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
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

