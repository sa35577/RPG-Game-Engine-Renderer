/*
PointTotal.java
Sat Arora
Optional point total object added to game enforces a rule that the goal can
only be accessed if all coins are collected.
 */

//importing package
import java.io.Serializable;

public class PointTotal implements Serializable {
    private static final long serialVersionUID = -7622257347763473994L;
    private int total; //total of all coins
    private int cur;
    //constructor, default no points required to open
    public PointTotal() {
        this.total = 0;
    }
    //constructor, given the amount of points put down in the EditPanel currently
    public PointTotal(int total) {
        this.total = total;
    }

    public int getTotal() { return this.total; }
    //increase point total (added coin, increased coin value)
    public void increase(int val) { this.total += val; }
    //decrease point total (deleted coin, decreased coin value)
    public void decrease(int val) { this.total -= val; }
    public void setCur(int cur) { this.cur = cur; }
    public int getCur() { return this.cur; }
}
