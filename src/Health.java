/*
Health.java
Sat Arora
Health class that holds the avatar's current health, and enforces another way to lose the game.
 */

//importing packages
import java.io.Serializable;

public class Health implements Serializable {
    private int value; //max value of health
    private int cur; //current value of health
    //constructor
    public Health() {
        this.value = 3;
        this.cur = 3;
    }
    //getters and setters
    public int getValue() { return this.value; }
    public void setValue(int value) { this.value = value; }
    public int getCur() { return this.cur; }
    public void setCur(int cur) { this.cur = cur; }
    //decrementing the current health value by 1
    public void decrement() { cur--; }
    //overloaded decrement by a specified value
    public void decrement(int val) { cur = Math.max(cur-val,0); }
}
