/*
Edit.java
Sat Arora
File that allows the user to make/add levels to their game.
 */
import java.awt.event.*;
import java.io.IOException;
import javax.swing.*;
import javax.swing.Timer;
public class Level extends JFrame implements ActionListener {
    Timer myTimer;
    LevelPanel lvl;
    public Level() throws IOException, ClassNotFoundException {
        super("Gamestar Mechanic");
        setSize(1200 ,825);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myTimer = new javax.swing.Timer(10,this);
        lvl = new LevelPanel(this);
        add(lvl);
        setResizable(false);
        setVisible(true);
        setIconImage(new ImageIcon("richman.jpeg").getImage());
        //System.out.println(this.getWidth());
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        new Level();
    }
    public void start() {myTimer.start();}

    @Override
    public void actionPerformed(ActionEvent e) {
        if (lvl != null) { lvl.repaint(); }
    }
}
