import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;
public class platformMovementTest extends JFrame implements ActionListener {
    Timer myTimer;
    platformMovementTestPanel thingy;
    public platformMovementTest() {
        super("Platform Movement Test");
        setSize(1920 ,1080);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myTimer = new javax.swing.Timer(10,this);
        thingy = new platformMovementTestPanel(this);
        add(thingy);
        setResizable(false);
        setVisible(true);
    }



    public void start() {myTimer.start();}

    public static void main(String[] args) {
        new platformMovementTest();

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (thingy != null) thingy.repaint();
    }
}

class platformMovementTestPanel extends JPanel implements KeyListener {

    private platformMovementTest mainFrame;
    private boolean[] keys;
    private Image[] leftSprites, rightSprites;
    private Image[][] sprites;
    private String inPlay;
    private double x=500,y=500;
    private boolean inXMotion, inYMotion;
    private Image scroll = new ImageIcon("scroll.png").getImage().getScaledInstance(1920,1080,Image.SCALE_SMOOTH);
    private double step;
    private static final int RIGHT = 0, UP = 1, LEFT = 2, DOWN = 3;
    private int INITIAL = 0, direction;
    private double gravity = 0, velocityX = 0, velocityY = 0, accelerationX = 0.2, accelerationY = 0.5, maxVelocityX = 3;



    public platformMovementTestPanel(platformMovementTest p) {
        mainFrame = p;
        keys = new boolean[KeyEvent.KEY_LAST+1];
        addKeyListener(this);
        inPlay = "hero";
        rightSprites = new Image[5];
        leftSprites = new Image[5];
        for (int i = 0; i < 3; i++) {
            rightSprites[i] = new ImageIcon(String.format("Platform/%sR%d.png",inPlay,i)).getImage().getScaledInstance(50,50, Image.SCALE_SMOOTH);
            leftSprites[i] = new ImageIcon(String.format("Platform/%sL%d.png",inPlay,i)).getImage().getScaledInstance(50,50, Image.SCALE_SMOOTH);
        }
        rightSprites[3] = new ImageIcon(String.format("Platform/%sRU.png",inPlay)).getImage().getScaledInstance(50,50, Image.SCALE_SMOOTH);
        rightSprites[4] = new ImageIcon(String.format("Platform/%sRD.png",inPlay)).getImage().getScaledInstance(50,50, Image.SCALE_SMOOTH);
        leftSprites[3] = new ImageIcon(String.format("Platform/%sLU.png",inPlay)).getImage().getScaledInstance(50,50, Image.SCALE_SMOOTH);
        leftSprites[4] = new ImageIcon(String.format("Platform/%sLD.png",inPlay)).getImage().getScaledInstance(50,50, Image.SCALE_SMOOTH);
        sprites = new Image[4][5];
        sprites[0] = rightSprites;
        sprites[1] = null; sprites[3] = null;
        sprites[2] = leftSprites;
        direction = INITIAL;
        step = 0;
        inXMotion = false; inYMotion = false;

    }
    public void addNotify() {
        super.addNotify();
        requestFocus();
        mainFrame.start();
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {direction = RIGHT; inXMotion = true;}
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {direction = LEFT; inXMotion = true; }

        if (e.getKeyCode() == KeyEvent.VK_UP && !inYMotion) {inYMotion = true; velocityY = -20;}
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
        if (inXMotion && !(keys[KeyEvent.VK_RIGHT] || keys[KeyEvent.VK_LEFT])) {
            inXMotion = false;
            step = 0;
            velocityX = 0;
        }
    }
    public void moveX() {
        if (direction == RIGHT) x += velocityX;
        if (direction == LEFT) x -= velocityX;
        velocityX = Math.min(velocityX+accelerationX,maxVelocityX);

    }
    public void moveY() {
        if (Math.ceil(y) >= 500 && velocityY > 0) {
            y = 500;
            inYMotion = false;
            velocityY = 0;
            return;
        }
        y += velocityY;
        velocityY += accelerationY;


    }


    public void paintComponent(Graphics g) {
        if (inXMotion) moveX();
        if (inYMotion) moveY();

        g.drawImage(scroll, 0, 0, null);
        g.setColor(Color.blue);
        if (inXMotion && !inYMotion) {
            if ((int)step % 2 == 0) {
                g.drawImage(sprites[direction][0], (int) x, (int) y, null);
                step += 0.05;
            } else {
                g.drawImage(sprites[direction][((int)step + 1) / 2], (int) x, (int) y, null);
                step += 0.05;
            }
            if (step >= 4) step -= 4;
        }
        else {
            g.drawImage(sprites[direction][0],(int)x,(int)y,null);
        }
        if (inYMotion) {
            System.out.println("hello");
            if (velocityY <= 0) g.drawImage(sprites[direction][3],(int)x,(int)y,null);
            else g.drawImage(sprites[direction][4],(int)x,(int)y,null);
        }


    }

}
