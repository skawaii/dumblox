import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Dumblox extends JFrame implements WindowListener, DumbloxConstants {
    private DumbloxPanel dumblox_panel;
    
    /**
     * Setup the JFrame
     * @param period
     */
    public Dumblox(int period) {
        super("Dumblox");
        makeGUI(period);
        
        addWindowListener(this);
        pack();
        setResizable(false);
        setVisible(true);
    }
    
    /**
     * Keep setting up the JFrame
     * @param loop_period
     */
    private void makeGUI(int loop_period) {
        Container c = getContentPane();
        
        dumblox_panel = new DumbloxPanel(this, loop_period);
        c.add(dumblox_panel, "Center");
    }
    
    // ----------------- window listener methods -------------
    
    public void windowActivated(WindowEvent e) {
        dumblox_panel.resumeGame();
        // TODO not sure if this should resume the game...kind of annoying
    }
    
    public void windowDeactivated(WindowEvent e) {
        dumblox_panel.pauseGame();
    }
        
    public void windowDeiconified(WindowEvent e) {
        dumblox_panel.resumeGame();
    }
    
    public void windowIconified(WindowEvent e) {
        dumblox_panel.pauseGame();
    }
        
    public void windowClosing(WindowEvent e) {
        dumblox_panel.stopGame();
    }
    
    public void windowClosed(WindowEvent e) {}
    
    public void windowOpened(WindowEvent e) {}
    
    // ----------------------------------------------------
    
    public static void main(String args[]) {
        int fps = DEFAULT_FPS;
        if (args.length != 0)
            fps = Integer.parseInt(args[0]);
        
        // TODO what's going on here?
        int loop_period = (int) 1000.0/fps;
        
        new Dumblox(loop_period);
    }
}
