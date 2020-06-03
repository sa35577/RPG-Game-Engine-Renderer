import java.io.Serializable;
public class Countdown implements Serializable {

    private int timeLeft;
    public Countdown() {
        this.timeLeft = 60;
    }
    public int getTime() { return this.timeLeft; }
    public String getStrTime() {
        String s = "";
        int t = this.timeLeft;

        if (t/3600 < 10) s += "0";
        s += t/3600 + ":";

        t %= 3600;

        if (t/60 < 10) s += "0";
        s += t/60 + ":";

        t %= 60;

        if (t < 10) s += "0";
        s += t;

        return s;
    }
    public void setTimeLeft(String timeLeft) { this.timeLeft = Integer.parseInt(timeLeft.substring(0,2))*3600+Integer.parseInt(timeLeft.substring(3,5))*60+Integer.parseInt(timeLeft.substring(6)); }
    public void decrement() { this.timeLeft--; }

}
