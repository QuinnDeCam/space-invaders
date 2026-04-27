import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.RenderingHints;
import java.util.List;

/**
 * GameView - Handles all rendering and UI display.
 * This class extends JPanel and is hosted in a JFrame.
 * It receives game state from GameModel and renders it to the screen.
 * This class only reads from the model — it never modifies game state.
 */
public class GameView extends JPanel {
    
    private GameModel model;
    
    public GameView(GameModel model) {
        this.model = model;
        setBackground(Color.BLACK);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        if (model != null) {
            drawGame(g2d);
        }
    }
    
    private void drawGame(Graphics2D g) {
        // Draw player
        drawPlayer(g);
        
        // Draw shields
        drawShields(g);
        
        // Draw aliens
        drawAliens(g);
        
        // Draw bullets
        drawPlayerBullet(g);
        drawAlienBullets(g);
        
        // Draw HUD (score and lives)
        drawHUD(g);
        
        // Draw game over or game won message
        if (model.isGameOver()) {
            drawGameOver(g);
        } else if (model.isGameWon()) {
            drawGameWon(g);
        }
    }
    
    private void drawPlayer(Graphics2D g) {
        g.setColor(Color.GREEN);
        g.fillRect(model.getPlayerX(), model.getPlayerY(), 
                   model.getPlayerWidth(), model.getPlayerHeight());
        g.setColor(Color.LIGHT_GRAY);
        g.setStroke(new java.awt.BasicStroke(2));
        g.drawRect(model.getPlayerX(), model.getPlayerY(),
                   model.getPlayerWidth(), model.getPlayerHeight());
    }
    
    private void drawShields(Graphics2D g) {
        java.util.List<GameModel.Shield> shields = model.getShields();
        
        for (GameModel.Shield shield : shields) {
            // Color based on health: full green (3) to dim red (1)
            Color shieldColor;
            int health = shield.getHealth();
            
            if (health == 3) {
                shieldColor = Color.GREEN;
            } else if (health == 2) {
                // Transition from green to yellow
                shieldColor = new Color(128, 128, 0); // Olive (greenish-yellow)
            } else {
                // Red (health == 1)
                shieldColor = new Color(139, 0, 0); // Dark red
            }
            
            g.setColor(shieldColor);
            g.fillRect(shield.getX(), shield.getY(),
                       model.getShieldWidth(), model.getShieldHeight());
            g.setColor(Color.LIGHT_GRAY);
            g.setStroke(new java.awt.BasicStroke(1));
            g.drawRect(shield.getX(), shield.getY(),
                       model.getShieldWidth(), model.getShieldHeight());
        }
    }
    
    private void drawAliens(Graphics2D g) {
        GameModel.Alien[][] alienGrid = model.getAlienGrid();
        g.setColor(Color.RED);
        
        for (int row = 0; row < alienGrid.length; row++) {
            for (int col = 0; col < alienGrid[row].length; col++) {
                GameModel.Alien alien = alienGrid[row][col];
                if (alien != null && alien.isAlive()) {
                    g.fillRect(alien.getX(), alien.getY(),
                               model.getAlienWidth(), model.getAlienHeight());
                    g.setColor(new Color(139, 0, 0)); // Dark red
                    g.setStroke(new java.awt.BasicStroke(1));
                    g.drawRect(alien.getX(), alien.getY(),
                               model.getAlienWidth(), model.getAlienHeight());
                    g.setColor(Color.RED);
                }
            }
        }
    }
    
    private void drawPlayerBullet(Graphics2D g) {
        GameModel.PlayerBullet bullet = model.getPlayerBullet();
        if (bullet != null) {
            g.setColor(Color.YELLOW);
            g.fillRect(bullet.getX(), bullet.getY(), 4, 8);
        }
    }
    
    private void drawAlienBullets(Graphics2D g) {
        List<GameModel.AlienBullet> bullets = model.getAlienBullets();
        g.setColor(Color.CYAN);
        
        for (GameModel.AlienBullet bullet : bullets) {
            g.fillRect(bullet.getX(), bullet.getY(), 4, 8);
        }
    }
    
    private void drawHUD(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        
        // Draw score
        g.drawString("Score: " + model.getScore(), 20, 30);
        
        // Draw lives
        g.drawString("Lives: " + model.getLives(), model.getGameWidth() - 200, 30);
    }
    
    private void drawGameOver(Graphics2D g) {
        // Semi-transparent overlay
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, model.getGameWidth(), model.getGameHeight());
        
        // Game Over text
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 60));
        String gameOverText = "GAME OVER";
        int textWidth = g.getFontMetrics().stringWidth(gameOverText);
        g.drawString(gameOverText,
                     (model.getGameWidth() - textWidth) / 2,
                     model.getGameHeight() / 2 - 50);
        
        // Final score
        g.setFont(new Font("Arial", Font.BOLD, 30));
        String scoreText = "Final Score: " + model.getScore();
        textWidth = g.getFontMetrics().stringWidth(scoreText);
        g.drawString(scoreText,
                     (model.getGameWidth() - textWidth) / 2,
                     model.getGameHeight() / 2 + 20);
    }
    
    private void drawGameWon(Graphics2D g) {
        // Semi-transparent overlay
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, model.getGameWidth(), model.getGameHeight());
        
        // Victory text
        g.setColor(Color.GREEN);
        g.setFont(new Font("Arial", Font.BOLD, 60));
        String winText = "YOU WIN!";
        int textWidth = g.getFontMetrics().stringWidth(winText);
        g.drawString(winText,
                     (model.getGameWidth() - textWidth) / 2,
                     model.getGameHeight() / 2 - 50);
        
        // Final score
        g.setFont(new Font("Arial", Font.BOLD, 30));
        String scoreText = "Final Score: " + model.getScore();
        textWidth = g.getFontMetrics().stringWidth(scoreText);
        g.drawString(scoreText,
                     (model.getGameWidth() - textWidth) / 2,
                     model.getGameHeight() / 2 + 20);
    }
    
    public static void main(String[] args) {
        // Create and configure the JFrame to host this panel
        JFrame frame = new JFrame("Space Invaders");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        
        GameView view = new GameView(null);
        frame.add(view);
        
        frame.setVisible(true);
    }
}