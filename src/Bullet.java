/*
Bullet.java
Sat Arora
Pair class that will be used to store locations of objects.
 */

class Bullet {
    private double x,y;
    private int dir;
    public Bullet(double m, double n, int dir) {
        x = m;
        y = n;
        this.dir = dir;
    }
    public double getX() {return x;}
    public double getY() {return y;}
    public int getDir() {return dir;}
    public void setX(int m) {x = m;}
    public void setY(int n) {y = n;}
    public void setDir(int dir) {this.dir = dir;}

    @Override
    public int hashCode() {return (int)x*2000+(int)y;}

}

