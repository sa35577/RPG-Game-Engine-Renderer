/*
Goal.java
Sat Arora
Goal class handling the goals (where the avatar needs to reach)
 */

//importing packages
import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public class Goal extends Sprite implements Serializable {
    private static final long serialVersionUID = -2823045921933857867L; //long used for serialization (writing objects to text file)
    private String id;
    private ImageIcon mask; //masking the goal allows for it to be hidden unless it comes in contact with the avatar
    private int pointsToOpen; //required points to open goal
    private String maskID; //id for the mask to access its image
    //constructor
    public Goal(String id, int x, int y, ImageIcon image, String maskID) {
        super(id,x,y,image);
        this.id = id;
        this.maskID = maskID;
        this.mask = new ImageIcon(new ImageIcon(String.format("Block/%s.png",maskID)).getImage().getScaledInstance(75,75, Image.SCALE_SMOOTH));
        this.pointsToOpen = 0;
        super.hitBox = new Rectangle(x,y,75,75);
    }
    public void init() {super.setInstance(this);} //linking Sprite object to this (Goal) object
    //getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public ImageIcon getMask() { return this.mask; }
    public String getMaskID() { return this.maskID; }
    //changing the mask id will automatically change the image as well
    public void setMaskID(String maskID) {
        this.maskID = maskID;
        this.setMask(new ImageIcon(new ImageIcon(String.format("Block/%s.png",maskID)).getImage().getScaledInstance(75,75,Image.SCALE_SMOOTH)));
    }
    public void setMask(ImageIcon mask) { this.mask = mask; }
    public int getPointsToOpen() { return pointsToOpen; }
    public void setPointsToOpen(int pointsToOpen) { this.pointsToOpen = pointsToOpen; }

}
