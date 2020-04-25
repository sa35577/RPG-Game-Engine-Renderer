
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.Border;

public class Dialog extends JFrame implements ActionListener {
    DialogPanel d;
    Timer myTimer;



    public Dialog() {
        super("Dialog Test");
        setSize(800,600);
        System.out.println(this.getLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myTimer = new javax.swing.Timer(10,this);
        d = new DialogPanel(this);



        add(d);


        setVisible(true);
    }

    public static void main(String[] args) {
        new Dialog();
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (d != null) {
            d.update();
            d.repaint();
        }
    }

}
class DialogPanel extends JPanel implements KeyListener {
    private Dialog mainFrame;
    private boolean[] keys;
    private boolean startedTyping=false;
    private ArrayList<String> arr;
    private JTextArea j;
    //private JDialog dia;


    public DialogPanel(Dialog d) {
        mainFrame = d;
        keys = new boolean[KeyEvent.KEY_LAST+1];
        arr = new ArrayList<>();
        d.setLocation(100,100);
        j = new JTextArea();

        j.setPreferredSize(new Dimension(200,200));
        j.setLineWrap(true);

        add(j);
    }
    public void addNotify() {
        super.addNotify();
        requestFocus();
    }
    public void update() {

    }

    public void paintComponent(Graphics g) {
        g.setColor(new Color(207,185,151));
        g.fillRect(100,100,600,400);
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }


    @Override
    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
    }
}
