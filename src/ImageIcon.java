import java.awt.*;
import java.net.URL;

public class ImageIcon extends javax.swing.ImageIcon {
    private static final long serialVersionUID = -962022720109015502L;

    public ImageIcon(String fileName) {
        super(fileName);
    }
    public ImageIcon(String fileName, String description) {
        super(fileName,description);
    }
    public ImageIcon(URL location, String description) {
        super(location,description);
    }
    public ImageIcon(Image image) {
        super(image);
    }

}

