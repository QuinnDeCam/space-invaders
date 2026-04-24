import javax.swing.JFrame;

/**
 * GameController - Wires together GameModel and GameView.
 * This class contains the main method and orchestrates the game loop,
 * input handling, and communication between model and view.
 */
public class GameController {
    
    private GameModel model;
    private GameView view;
    
    // Constructor that initializes and wires the model and view
    
    // Main method - entry point for the application
    
    // Game loop that updates model and refreshes view
    
    // Input handling methods (keyboard listeners)
    
    // Methods for starting, pausing, and stopping the game
    
    public static void main(String[] args) {
        // Create the controller which sets up model and view
        GameController controller = new GameController();
        
        // Get the view's frame and make it visible
        JFrame frame = new JFrame("Space Invaders");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        
        GameView view = new GameView();
        frame.add(view);
        
        frame.setVisible(true);
    }
}