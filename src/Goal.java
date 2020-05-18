import java.awt.*;

public class Goal extends Sprite {
    //handles the "cement","cloud","dirt","glass","gold","grass";
    private String id;
    private Image mask;
    private int pointsToOpen;

    public Goal(String id, int x, int y, Image image, Image mask) {
        super(id,x,y,image);
        this.id = id;
        this.mask = mask;
        this.pointsToOpen = 0;
    }
    public void init() {super.setInstance(this);}
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Image getMask() { return mask; }
    public void setMask(Image mask) { this.mask = mask; }
    public int getPointsToOpen() { return pointsToOpen; }
    public void setPointsToOpen(int pointsToOpen) { this.pointsToOpen = pointsToOpen; }


}
