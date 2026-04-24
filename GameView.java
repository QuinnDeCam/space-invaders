import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.Graphics;

/**
 * GameView - Handles all rendering and UI display.
 * This class extends JPanel and is hosted in a JFrame.
 * It receives game state from GameModel and renders it to the screen.
 */
public class GameView extends JPanel {
    
    // Reference to the game model for accessing state
    
    // Constructor that takes a GameModel reference
    
    // Override paintComponent to render game entities
    
    // Methods for updating the display
    
    // Methods for handling visual effects (explosions, animations)
    
    public static void main(String[] args) {
        // Create and configure the JFrame to host this panel
        JFrame frame = new JFrame("Space Invaders");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        
        GameView view = new GameView();
        frame.add(view);
        
        frame.setVisible(true);
    }
}