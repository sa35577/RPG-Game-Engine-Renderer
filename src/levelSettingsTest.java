import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;
public class levelSettingsTest extends JFrame implements ActionListener {
    Timer myTimer;
    levelSettingsTestPanel thingy;
    public levelSettingsTest() {
        super("Level Settings Test");
        setSize(1920 ,1080);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myTimer = new javax.swing.Timer(10,this);
        thingy = new levelSettingsTestPanel(this);
        add(thingy);
        setResizable(false);
        setVisible(true);
    }

    public void start() {myTimer.start();}
    public static void main(String[] args) {new levelSettingsTest();}

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}

class levelSettingsTestPanel extends JPanel implements MouseListener {

    private levelSettingsTest mainFrame;
    private Point mouse;
    private int gravity;
    private boolean topDown;
    
    public levelSettingsTestPanel(levelSettingsTest m) {
        mainFrame = m;

    }

    public void addNotify() {
        super.addNotify();
        requestFocus();
        mainFrame.start();
    }

    public void update() {
        mouse =MouseInfo.getPointerInfo().getLocation();
        Point offset = getLocationOnScreen();

        mouse.translate(-offset.x, -offset.y);
    }
    public void paintComponent(Graphics g) {

    }


    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}