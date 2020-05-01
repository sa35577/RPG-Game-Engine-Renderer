public class Avatar {
    private String id;
    private int speed;
    private int health;
    public static int maxSpeed = 10, maxHealth = 5;
    public Avatar(String id) {
        this.id = id;
        this.speed = 3;
        this.health = 5;
    }
    public int getHealth() {return this.health;}
    public int getSpeed() {return this.speed; }
    public String getId() {return this.id;}
    public void setHealth(int h) {this.health = h;}
    public void setSpeed(int s) {this.speed = s;}
    public void setId(String i) {this.id = i;}
}
