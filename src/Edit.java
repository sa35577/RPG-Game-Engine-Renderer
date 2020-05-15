/*
Edit.java
Sat Arora
File that allows the user to make/add levels to their game.
 */
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;
public class Edit extends JFrame implements ActionListener {
    Timer myTimer;
    EditPanel editor;
    public Edit() {
        super("Gamestar Mechanic");
        setSize(1920 ,1080);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myTimer = new javax.swing.Timer(10,this);
        editor = new EditPanel(this);
        add(editor);
        setResizable(false);
        setVisible(true);
        //System.out.println(this.getWidth());
    }

    public static void main(String[] args) {
        String s = "sf";
        System.out.println();
        new Edit();
    }
    public void start() {myTimer.start();}

    @Override
    public void actionPerformed(ActionEvent e) {
        if (editor != null) { editor.repaint(); }
    }
}
