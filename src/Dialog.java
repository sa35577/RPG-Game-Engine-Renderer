
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;


public class Dialog extends JFrame implements ActionListener {
    DialogPanel d;
    Timer myTimer;
    JTextArea jta;
    public Dialog() {
        super("Dialog Test");
        //this.setLayout(null);
        setSize(800,600);/*
        jta = new JTextArea();
        jta.setVisible(true);
        jta.setEditable(true);
        jta.setLineWrap(true);
        jta.setBounds(0,0,200,200);
        jta.setBackground(Color.red);*/
        //add(jta);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myTimer = new javax.swing.Timer(10,this);
        d = new DialogPanel(this);
        add(d);
        //add(jta);
        setVisible(true);

    }

    public static void main(String[] args) {
        new Dialog();
    }
    public void start() {myTimer.start();}
    public void actionPerformed(ActionEvent e) {

        d.repaint();

    }

}
class DialogPanel extends JPanel implements KeyListener,MouseListener {
    private Dialog mainFrame;
    private boolean[] keys;
    private boolean startedTyping=false;
    private ArrayList<String> arr;
    public JTextArea j;
    //private JDialog dia;
    public String txt;
    public DialogPanel(Dialog d) {
        this.setLayout(null);
        mainFrame = d;
        keys = new boolean[KeyEvent.KEY_LAST+1];
        arr = new ArrayList<>();
        addKeyListener(this);


        //d.setLocation(100,100);
        j = new JTextArea();
        j.setFont(new Font("System San Francisco Display Regular.ttf",Font.BOLD,15));
        //j.setOpaque(false);
       // j.setBackground(new Color(0,0,152,255));
        j.setBackground(new Color(255,0,0));
/*
        JScrollPane scrollPane = new JScrollPane(j);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setOpaque(false);
*/
        j.setBounds(0,0,200,200);
        j.setLineWrap(true);
        j.setVisible(true);
        j.setEditable(true);
        add(j);



    }
    public void addNotify() {
        super.addNotify();
        requestFocus();
        mainFrame.start();
    }
    public void update() {
        /*if (j.getText().length() >= 50) {
            j.setText(j.getText().substring(0,50));
        }*/
        System.out.println("update");
    }

    public void paintComponent(Graphics g) {
        System.out.println(345);
        g.setColor(Color.BLACK);
        g.fillRect(0,0,mainFrame.getWidth(),mainFrame.getHeight());
        g.setColor(new Color(207,185,151));
        g.fillRect(100,100,600,400);

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
