import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public class Goal extends Sprite implements Serializable {
    //handles the "cement","cloud","dirt","glass","gold","grass";
    private String id;
    private ImageIcon mask;
    private int pointsToOpen;
    private String maskID;

    public Goal(String id, int x, int y, ImageIcon image, String maskID) {
        super(id,x,y,image);
        this.id = id;
        this.maskID = maskID;
        this.mask = new ImageIcon(new ImageIcon(String.format("Block/%s.png",maskID)).getImage().getScaledInstance(75,75, Image.SCALE_SMOOTH));
        this.pointsToOpen = 0;
        super.hitBox = new Rectangle(x,y,75,75);
    }
    public void init() {super.setInstance(this);}
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public ImageIcon getMask() { return this.mask; }
    public String getMaskID() { return this.maskID; }
    public void setMaskID(String maskID) {
        this.maskID = maskID;
        this.setMask(new ImageIcon(new ImageIcon(String.format("Block/%s.png",maskID)).getImage().getScaledInstance(75,75,Image.SCALE_SMOOTH)));
    }
    public void setMask(ImageIcon mask) { this.mask = mask; }
    public int getPointsToOpen() { return pointsToOpen; }
    public void setPointsToOpen(int pointsToOpen) { this.pointsToOpen = pointsToOpen; }


}
