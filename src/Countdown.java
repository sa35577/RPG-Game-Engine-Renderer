public class Countdown {

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
}
