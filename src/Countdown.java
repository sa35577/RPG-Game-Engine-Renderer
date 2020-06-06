/*
Countdown.java
Sat Arora
Countdown class holds the data for the timer
 */
//importing packages
import java.io.Serializable;
public class Countdown implements Serializable {
    private static final long serialVersionUID = -7314552173772899865L; //long for serialization id (writing object to text file)
    private int timeLeft; //holding the time left in seconds
    //constructor
    public Countdown() {
        this.timeLeft = 60;
    }

    public int getTime() { return this.timeLeft; }
    public String getStrTime() { //getting the time left in a displayable format
        String out = "";
        int timeLeft = this.timeLeft;

        //hours portion of string
        if (timeLeft/3600 < 10) out += "0";
        out += timeLeft/3600 + ":";
        timeLeft %= 3600;

        //minutes portion of string
        if (timeLeft/60 < 10) out += "0";
        out += timeLeft/60 + ":";
        timeLeft %= 60;

        //seconds portion of string
        if (timeLeft < 10) out += "0";
        out += timeLeft;

        return out;
    }
    //setting time left (in seconds) given the string, using a bunch of string concatenation
    public void setTimeLeft(String timeLeft) { this.timeLeft = Integer.parseInt(timeLeft.substring(0,2))*3600+Integer.parseInt(timeLeft.substring(3,5))*60+Integer.parseInt(timeLeft.substring(6)); }
    //decrementing time left
    public void decrement() { this.timeLeft--; }
    //increasing time left (done with collision with bonus)
    public void increease(int val) { this.timeLeft += val; }

}
