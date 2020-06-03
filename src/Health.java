import java.io.Serializable;

public class Health implements Serializable {
    private int value,cur;
    public Health() {
        this.value = 3;
        this.cur = 3;
    }
    public int getValue() { return this.value; }
    public void setValue(int value) { this.value = value; }
    public int getCur() { return this.cur; }
    public void setCur(int cur) { this.cur = cur; }
    public void decrement() { cur--; }
    public void decrement(int val) { cur = Math.max(cur-val,0); }
}
