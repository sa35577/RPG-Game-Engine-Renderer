
import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public class Teleport extends Sprite implements Serializable {
    private Teleport exitTeleport,entryTeleport;
    public Teleport(String id, int x, int y, ImageIcon image) {
         super(id,x,y,image);
         this.exitTeleport = null;
         this.entryTeleport = null;
         super.hitBox = new Rectangle(x,y,75,75);
    }
    public void init() {super.setInstance(this);}
    public void setExit(Teleport t) {
        this.exitTeleport = t;
    }
    public void setEntry(Teleport t) {
        this.entryTeleport = t;
    }
    public int getPartnerX() {
        if (this.entryTeleport != null) {
            return this.entryTeleport.getX();
        }
        else {
            return this.exitTeleport.getX();
        }
    }
    public int getPartnerY() {
        if (this.entryTeleport != null) {
            return this.entryTeleport.getY();
        }
        else {
            return this.exitTeleport.getY();
        }
    }
    public boolean isEntry() {
        return exitTeleport == null;
    }






}
