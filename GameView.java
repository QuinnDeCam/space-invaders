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
        String gameState = model.getGameState();
        
        if (gameState.equals(GameModel.STATE_START_SCREEN)) {
            drawStartScreen(g);
        } else if (gameState.equals(GameModel.STATE_PLAYING)) {
            drawPlayingState(g);
        } else if (gameState.equals(GameModel.STATE_GAME_OVER)) {
            drawGameOverScreen(g);
        }
    }
    
    private void drawStartScreen(Graphics2D g) {
        // Draw title in big green letters
        g.setColor(Color.GREEN);
        g.setFont(new Font("Arial", Font.BOLD, 80));
        String title = "SPACE INVADERS";
        int titleWidth = g.getFontMetrics().stringWidth(title);
        g.drawString(title,
                     (model.getGameWidth() - titleWidth) / 2,
                     model.getGameHeight() / 2 - 100);
        
        // Draw "Press S to Start" in white
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        String startText = "Press S to Start";
        int startWidth = g.getFontMetrics().stringWidth(startText);
        g.drawString(startText,
                     (model.getGameWidth() - startWidth) / 2,
                     model.getGameHeight() / 2 + 100);
    }
    
    private void drawPlayingState(Graphics2D g) {
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
    }
    
    private void drawGameOverScreen(Graphics2D g) {
        // Draw game state first (for aliens/bullets visual context)
        drawPlayer(g);
        drawShields(g);
        drawAliens(g);
        drawPlayerBullet(g);
        drawAlienBullets(g);
        drawHUD(g);
        
        // Draw overlay and end screen
        if (model.isGameWon()) {
            drawWinScreen(g);
        } else {
            drawLoseScreen(g);
        }
    }
    
private void drawPlayer(Graphics2D g) {

    int[][] player = {        
        {0,0,0,0,1,1,0,0,0,0},
        {0,0,0,1,1,1,1,0,0,0},
        {1,1,1,1,1,1,1,1,1,1},
        {1,1,1,1,1,1,1,1,1,1},
        {1,1,1,1,1,1,1,1,1,1},
        {0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,0}
    };

    int x = model.getPlayerX();
    int y = model.getPlayerY();

    int w = model.getPlayerWidth();
    int h = model.getPlayerHeight();

    int pixelW = w / 10;
    int pixelH = h / 8;

    Color playerColor = new Color(0, 149, 194);
    g.setColor(playerColor);

    for (int row = 0; row < player.length; row++) {
        for (int col = 0; col < player[row].length; col++) {

            if (player[row][col] == 1) {
                g.fillRect(
                    x + col * pixelW,
                    y + row * pixelH,
                    pixelW,
                    pixelH
                );
            }
        }
    }

    // Draw glow effect when player is hit
    if (model.getPlayerHitCounter() > 0) {
        // Create pulsing glow intensity based on hit counter
        float alpha = (model.getPlayerHitCounter() / 15.0f) * 0.6f; // Fades from 0.6 to 0 over 15 frames
        Color glowColor = new Color(1.0f, 0.0f, 0.0f, alpha); // Red glow with transparency
        g.setColor(glowColor);
        g.fillRect(x, y, w, h);
    }

    // outline (keeps retro feel)
    g.setColor(Color.BLACK);
    g.setStroke(new java.awt.BasicStroke(2));
    g.drawRect(x, y, w, h);
}
    
    private void drawShields(Graphics2D g) {
        java.util.List<GameModel.Shield> shields = model.getShields();
        
        for (GameModel.Shield shield : shields) {
            // Color based on health: green (5) → yellow-green (4) → pale (3) → orange (2) → red (1)
            Color shieldColor;
            int health = shield.getHealth();
            
            if (health == 5) {
                shieldColor = new Color(68, 206, 27); // Bright green
            } else if (health == 4) {
                shieldColor = new Color(187, 219, 68); // Light green
            } else if (health == 3) {
                shieldColor = new Color(247, 227, 121); // Yellow-green/pale
            } else if (health == 2) {
                shieldColor = new Color(242, 161, 52); // Orange
            } else {
                // health == 1
                shieldColor = new Color(229, 31, 31); // Red
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
        // Alien pixel art pattern: 9x7
        int[][] alienPattern = {
            {0,0,1,0,0,0,1,0,0},
            {0,1,1,1,0,1,1,1,0},
            {1,1,1,1,1,1,1,1,1},
            {1,0,1,1,1,1,1,0,1},
            {1,1,1,0,1,0,1,1,1},
            {0,1,0,0,0,0,0,1,0},
            {1,0,0,1,0,1,0,0,1}
        };
        
        GameModel.Alien[][] alienGrid = model.getAlienGrid();
        g.setColor(Color.RED);
        
        for (int row = 0; row < alienGrid.length; row++) {
            for (int col = 0; col < alienGrid[row].length; col++) {
                GameModel.Alien alien = alienGrid[row][col];
                if (alien != null && alien.isAlive()) {
                    drawAlienPixelArt(g, alien, alienPattern);
                }
            }
        }
    }
    
    private void drawAlienPixelArt(Graphics2D g, GameModel.Alien alien, int[][] pattern) {
        int alienWidth = model.getAlienWidth();
        int alienHeight = model.getAlienHeight();
        int patternWidth = 9;
        int patternHeight = 7;
        
        // Calculate pixel size
        float pixelWidth = (float) alienWidth / patternWidth;
        float pixelHeight = (float) alienHeight / patternHeight;
        
        g.setColor(Color.RED);
        
        // Draw each pixel in the pattern
        for (int row = 0; row < patternHeight; row++) {
            for (int col = 0; col < patternWidth; col++) {
                if (pattern[row][col] == 1) {
                    int x = alien.getX() + (int)(col * pixelWidth);
                    int y = alien.getY() + (int)(row * pixelHeight);
                    int w = (int)pixelWidth;
                    int h = (int)pixelHeight;
                    g.fillRect(x, y, w, h);
                }
            }
        }
        
        // Draw border around entire alien
        g.setColor(new Color(0, 0, 0)); // Black so it blends in
        g.setStroke(new java.awt.BasicStroke(1));
        g.drawRect(alien.getX(), alien.getY(), alienWidth, alienHeight);
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
    
    private void drawLoseScreen(Graphics2D g) {
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
                     model.getGameHeight() / 2 - 100);
        
        // Final score
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        String scoreText = "Final Score: " + model.getScore();
        textWidth = g.getFontMetrics().stringWidth(scoreText);
        g.drawString(scoreText,
                     (model.getGameWidth() - textWidth) / 2,
                     model.getGameHeight() / 2 - 20);
        
        // Restart options
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        String restartText1 = "Press S to Restart";
        textWidth = g.getFontMetrics().stringWidth(restartText1);
        g.drawString(restartText1,
                     (model.getGameWidth() - textWidth) / 2,
                     model.getGameHeight() / 2 + 60);
        
        String restartText2 = "Press A for Start Screen";
        textWidth = g.getFontMetrics().stringWidth(restartText2);
        g.drawString(restartText2,
                     (model.getGameWidth() - textWidth) / 2,
                     model.getGameHeight() / 2 + 90);
    }
    
    private void drawWinScreen(Graphics2D g) {
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
                     model.getGameHeight() / 2 - 100);
        
        // Final score
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        String scoreText = "Final Score: " + model.getScore();
        textWidth = g.getFontMetrics().stringWidth(scoreText);
        g.drawString(scoreText,
                     (model.getGameWidth() - textWidth) / 2,
                     model.getGameHeight() / 2 - 20);
        
        // Restart options
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        String restartText1 = "Press S to Restart";
        textWidth = g.getFontMetrics().stringWidth(restartText1);
        g.drawString(restartText1,
                     (model.getGameWidth() - textWidth) / 2,
                     model.getGameHeight() / 2 + 60);
        
        String restartText2 = "Press A for Start Screen";
        textWidth = g.getFontMetrics().stringWidth(restartText2);
        g.drawString(restartText2,
                     (model.getGameWidth() - textWidth) / 2,
                     model.getGameHeight() / 2 + 90);
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