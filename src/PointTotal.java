import java.io.Serializable;
public class PointTotal implements Serializable {
    private int total;
    public PointTotal() {
        this.total = 0;
    }
    public PointTotal(int total) {
        this.total = total;
    }
    public int getTotal() { return this.total; }
    public void increase(int val) { this.total += val; }
    public void decrease(int val) { this.total -= val; }
}
