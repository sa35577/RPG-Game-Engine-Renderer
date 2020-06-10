/*
App.java
Sat Arora
Main Frame class that runs 3 panels with a card layout.
 */

//importing packages
import com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.ObjectStreamClass;

public class App extends JFrame {
    // string constants
    public static final String EDITPANEL = "edit";
    public static final String LEVELPANEL = "level";
    public static final String SELECTPANEL = "select";
    //panels used
    private EditPanel edit;
    private LevelPanel level;
    private SelectPanel select;
    //help manage the cardlayout
    private JPanel panelManager;
    private String activePanel;
    private Timer myTimer; // Timer to call the game functions each frame
    public App() throws IOException, ClassNotFoundException {
        super("Gamestar Mechanic");
        myTimer = new Timer(10, new TickListener());// trigger every 10 ms
        select = new SelectPanel(this);
        panelManager = new JPanel(new CardLayout());

        // Setting up the CardLayout in panelManager
        panelManager.add(select, SELECTPANEL);
        switchPanel(SELECTPANEL);
        setResizable(false);
        setLocationRelativeTo(null);
        add(panelManager);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Image icon = ImageIO.read(new File("richman.jpeg"));
        setIconImage(icon);
        setVisible(true);
    }
    //switching the panels throughout the app, taking in the target string that is linked with the panel
    public void switchPanel(String targetPanel) throws IOException, ClassNotFoundException {
        //the edit or level panels need to be the most updated versions, so they are deleted (if they exist) and then re-added to the panel manager every time
        Component[] cpts = panelManager.getComponents();
        boolean containsLevel = false, containsEdit = false;
        for (Component cpt : cpts) {
            if (cpt instanceof LevelPanel) containsLevel = true;
            else if (cpt instanceof EditPanel) containsEdit = true;
        }
        if (targetPanel.equals("edit")) {
            setSize(1920,1080);
            if (containsEdit) {
                panelManager.remove(edit); //removing old edit, outdated
            }
            if (select.getFile() != null)
                edit = new EditPanel(this,select.getFile()); //getting the new edit panel
            else
                edit = new EditPanel(this);
            panelManager.add(edit,EDITPANEL); //putting new edit panel into the panel manager
        }
        else if (targetPanel.equals("level")) {
            setSize(1200,900);
            if (containsLevel) {
                panelManager.remove(level); //removing old level, outdated
            }
            level = new LevelPanel(this,select.getFile()); //getting the new level panel
            panelManager.add(level,LEVELPANEL); //putting new level panel into the panel manage
        }
        else {
            setSize(1200, 900);
        }
        CardLayout cardLayout = (CardLayout) panelManager.getLayout();
        cardLayout.show(panelManager, targetPanel);

        activePanel = targetPanel;
        addNotify(); // Getting the focus of the current panel
    }
    // TickListener Class
    class TickListener implements ActionListener {
        public void actionPerformed(ActionEvent evt){
            switch(activePanel){
                case EDITPANEL:
                    edit.repaint();
                    break;
                case LEVELPANEL:
                    try {
                        level.update();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    level.repaint();
                    break;
                case SELECTPANEL:
                    select.repaint();
                    break;
            }
        }
    }
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        //System.setProperty("sun.java2d.opengl", "True"); //used to make panel manager slightly faster (was advised by a friend to do this)
        long serialVersionID = ObjectStreamClass.lookup(new Boolean(true).getClass()).getSerialVersionUID();
        System.out.println(serialVersionID);
        App game = new App();

    }
    public void start() {
        myTimer.start();
    }
}
//-962022720109015502 --> ImageIcon