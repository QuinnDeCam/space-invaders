import javax.swing.JFrame;
import javax.swing.Timer;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;
import java.util.HashSet;

/**
 * GameController - Wires together GameModel and GameView.
 * This class contains the main method and orchestrates the game loop,
 * input handling, and communication between model and view.
 */
public class GameController extends JFrame implements KeyListener {
    
    private GameModel model;
    private GameView view;
    private Timer gameLoopTimer;
    private Set<Integer> keysPressed; // Track which keys are currently pressed
    private static final int FRAME_DELAY = 30; // ~33 FPS
    
    public GameController() {
        // Initialize model and view
        model = new GameModel();
        view = new GameView(model);
        keysPressed = new HashSet<>();
        
        // Setup frame
        setTitle("Space Invaders");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setResizable(false);
        setLocationRelativeTo(null);
        
        // Add view to frame
        add(view);
        
        // Setup keyboard listener
        view.addKeyListener(this);
        view.setFocusable(true);
        view.requestFocus();
        
        // Initialize and start game loop
        startGameLoop();
        
        setVisible(true);
    }
    
    private void startGameLoop() {
        gameLoopTimer = new Timer(FRAME_DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateGame();
            }
        });
        gameLoopTimer.start();
    }
    
    private void updateGame() {
        // Handle continuous key inputs
        if (keysPressed.contains(KeyEvent.VK_LEFT)) {
            model.movePlayerLeft();
        }
        if (keysPressed.contains(KeyEvent.VK_RIGHT)) {
            model.movePlayerRight();
        }
        
        // Update game model
        model.update();
        
        // Redraw view
        view.repaint();
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        keysPressed.add(e.getKeyCode());
        
        // Fire on spacebar
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            model.firePlayerBullet();
        }
        
        // Start game on S key
        if (e.getKeyCode() == KeyEvent.VK_S) {
            if (model.getGameState().equals(GameModel.STATE_START_SCREEN)) {
                model.startGame();
            } else if (model.getGameState().equals(GameModel.STATE_GAME_OVER)) {
                model.restartGame();
            }
        }
        
        // Back to start screen on A key
        if (e.getKeyCode() == KeyEvent.VK_A) {
            if (model.getGameState().equals(GameModel.STATE_GAME_OVER)) {
                model.backToStartScreen();
            }
        }
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        keysPressed.remove(e.getKeyCode());
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
        // Not used
    }
    
    public static void main(String[] args) {
        // Create the controller which sets up model, view, and game loop
        new GameController();
    }
}