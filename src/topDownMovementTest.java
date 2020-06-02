
import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;
public class topDownMovementTest extends JFrame implements ActionListener {
    Timer myTimer;
    topDownMovementTestPanel thingy;
    public topDownMovementTest() {
        super("Top Down Movement Test");
        setSize(1920 ,1080);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myTimer = new javax.swing.Timer(10,this);
        thingy = new topDownMovementTestPanel(this);
        add(thingy);

        setResizable(false);
        setVisible(true);

    }

    public void start() {myTimer.start();}

    public static void main(String[] args) {
        new topDownMovementTest();
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (thingy != null) thingy.repaint();
    }
}
class topDownMovementTestPanel extends JPanel implements KeyListener {
    private topDownMovementTest mainFrame;
    private boolean[] keys;
    private Image[] rightSprites, upSprites, leftSprites, downSprites;
    private Image[][] sprites;
    private String inPlay;
    private double x,y;
    private int direction;
    private boolean inMotion, initialShot;
    private Image scroll = new ImageIcon("scroll.png").getImage().getScaledInstance(1920,1080,Image.SCALE_SMOOTH);
    private double step;
    private ArrayList<Bullet> bullets;
    public static final int RIGHT = 0, UP = 1, LEFT = 2, DOWN = 3;
    public static final int INITIAL = 0;
    private int rapidFire;


    public topDownMovementTestPanel(topDownMovementTest t) {
        mainFrame = t;
        keys = new boolean[KeyEvent.KEY_LAST+1];
        addKeyListener(this);
        inPlay = "tank";
        rightSprites = new Image[3];
        upSprites = new Image[3];
        leftSprites = new Image[3];
        downSprites = new Image[3];
        sprites = new Image[4][4];
        for (int i = 0; i < 3; i++) {
            rightSprites[i] = new ImageIcon(String.format("Top-Down/%sR%d.png",inPlay,i)).getImage().getScaledInstance(75,75, Image.SCALE_SMOOTH);
            upSprites[i] = new ImageIcon(String.format("Top-Down/%sU%d.png",inPlay,i)).getImage().getScaledInstance(75,75,Image.SCALE_SMOOTH);
            downSprites[i] = new ImageIcon(String.format("Top-Down/%sD%d.png",inPlay,i)).getImage().getScaledInstance(75,75,Image.SCALE_SMOOTH);
            leftSprites[i] = new ImageIcon(String.format("Top-Down/%sL%d.png",inPlay,i)).getImage().getScaledInstance(75,75,Image.SCALE_SMOOTH);
        }
        sprites[RIGHT] = rightSprites;
        sprites[UP] = upSprites;
        sprites[LEFT] = leftSprites;
        sprites[DOWN] = downSprites;
        direction = INITIAL;
        step = 0;
        inMotion = false;
        initialShot = false;
        bullets = new ArrayList<>();
        rapidFire = 0;
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

        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {direction = RIGHT; inMotion = true;}
        else if (e.getKeyCode() == KeyEvent.VK_UP) {direction = UP; inMotion = true; }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT) {direction = LEFT; inMotion = true; }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN) {direction = DOWN; inMotion = true; }
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (rapidFire == 0) {
                if (direction == RIGHT) {
                    bullets.add(new Bullet(x+50,y+25,direction));
                }
                else if (direction == UP) {
                    bullets.add(new Bullet(x+25,y,direction));
                }
                else if (direction == LEFT) {
                    bullets.add(new Bullet(x,y+25,direction));
                }
                else if (direction == DOWN) {
                    bullets.add(new Bullet(x+25,y+50,direction));
                }
                initialShot = true;
            }
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
        if (inMotion && !(keys[KeyEvent.VK_RIGHT] || keys[KeyEvent.VK_UP] || keys[KeyEvent.VK_LEFT] || keys[KeyEvent.VK_DOWN])) {
            inMotion = false;
            step = 0;
        }
    }
    public void move(){
        if (direction == RIGHT) x += 1;
        if (direction == UP) y -= 1;
        if (direction == LEFT) x -= 1;
        if (direction == DOWN) y += 1;
    }
    public void moveBullets() {
        for (Bullet b : bullets) {
            if (b.getX() < 0 || b.getX() > mainFrame.getWidth() || b.getY() < 0 || b.getY() > mainFrame.getHeight()) bullets.remove(b);
            else {
                if (b.getDir() == RIGHT) b.setX((int) b.getX() + 10);
                else if (b.getDir() == UP) b.setY((int) b.getY() - 10);
                else if (b.getDir() == LEFT) b.setX((int) b.getX() - 10);
                else if (b.getDir() == DOWN) b.setY((int) b.getY() + 10);
            }
        }
    }
    public void paintComponent(Graphics g) {
        if (inMotion) move();
        moveBullets();
        g.drawImage(scroll, 0, 0, null);
        if (rapidFire != 0 || initialShot) {rapidFire = (rapidFire+1) % 25; initialShot = false;}
        g.setColor(Color.blue);
        for (Bullet b : bullets) {
            g.fillOval((int)b.getX()-5,(int)b.getY()-5,10,10);
        }
        if (inMotion) {
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
        g.drawString(String.format("%d",bullets.size()),800,800);
        g.drawRect((int)x+5,(int)y,75-10,75);
    }
}