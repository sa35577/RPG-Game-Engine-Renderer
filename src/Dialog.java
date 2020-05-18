
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
    public void start() {myTimer.start();}
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
    public JTextArea j;
    //private JDialog dia;
    public String txt;

    public DialogPanel(Dialog d) {
        mainFrame = d;
        keys = new boolean[KeyEvent.KEY_LAST+1];
        arr = new ArrayList<>();
        d.setLocation(100,100);
        j = new JTextArea();
        j.setFont(new Font("System San Francisco Display Regular.ttf",Font.BOLD,15));
        j.setOpaque(false);
        j.setBackground(new Color(0,0,0,0));/*
        j.setBackground(new Color(1,1,1,1));

        JScrollPane scrollPane = new JScrollPane(j);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setOpaque(false);*/

        j.setPreferredSize(new Dimension(200,200));
        j.setLineWrap(true);

        add(j);

    }
    public void addNotify() {
        super.addNotify();
        requestFocus();
        mainFrame.start();
    }
    public void update() {
        if (j.getText().length() >= 50) {
            j.setText(j.getText().substring(0,50));
        }
    }

    public void paintComponent(Graphics g) {
        g.setColor(Color.white);
        g.fillRect(0,0,this.getWidth(),this.getHeight());
        g.setColor(new Color(207,185,151));
        g.fillRect(100,100,600,400);
        System.out.println(j.getText());

    }


    @Override
    public void keyTyped(KeyEvent e) { }


    @Override
    public void keyPressed(KeyEvent e) {

        keys[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
    }
}
