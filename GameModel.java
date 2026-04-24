import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * GameModel - Contains all game logic and state.
 * This class handles game rules, entity positions, collision detection,
 * scoring, and any other game mechanics without any Swing dependencies.
 */
public class GameModel {
    // Game dimensions
    private static final int GAME_WIDTH = 800;
    private static final int GAME_HEIGHT = 600;
    
    // Player state
    private int playerX;
    private int playerY;
    private static final int PLAYER_WIDTH = 40;
    private static final int PLAYER_HEIGHT = 40;
    private static final int PLAYER_SPEED = 5;
    
    // Alien grid
    private Alien[][] alienGrid;
    private static final int ALIEN_ROWS = 5;
    private static final int ALIEN_COLS = 11;
    private static final int ALIEN_WIDTH = 30;
    private static final int ALIEN_HEIGHT = 30;
    private static final int ALIEN_PADDING = 20;
    private int alienFormationX;
    private int alienFormationY;
    private int alienDirection; // 1 for right, -1 for left
    private static final int ALIEN_SPEED = 2;
    
    // Bullets
    private PlayerBullet playerBullet;
    private List<AlienBullet> alienBullets;
    private static final int BULLET_SPEED = 7;
    private static final int ALIEN_BULLET_SPEED = 4;
    
    // Game state
    private int score;
    private int lives;
    private Random random;
    private int alienFireCounter;
    private static final int ALIEN_FIRE_INTERVAL = 30; // Fire every 30 ticks on average
    
    // Helper classes
    public static class Alien {
        private int x, y;
        private boolean alive;
        
        Alien(int x, int y) {
            this.x = x;
            this.y = y;
            this.alive = true;
        }
        
        public int getX() { return x; }
        public int getY() { return y; }
        public boolean isAlive() { return alive; }
    }
    
    public static class PlayerBullet {
        private int x, y;
        
        PlayerBullet(int x, int y) {
            this.x = x;
            this.y = y;
        }
        
        public int getX() { return x; }
        public int getY() { return y; }
    }
    
    public static class AlienBullet {
        private int x, y;
        
        AlienBullet(int x, int y) {
            this.x = x;
            this.y = y;
        }
        
        public int getX() { return x; }
        public int getY() { return y; }
    }
    
    public GameModel() {
        random = new Random();
        
        // Initialize player
        playerX = GAME_WIDTH / 2 - PLAYER_WIDTH / 2;
        playerY = GAME_HEIGHT - 60;
        
        // Initialize alien grid
        alienGrid = new Alien[ALIEN_ROWS][ALIEN_COLS];
        alienFormationX = 50;
        alienFormationY = 30;
        alienDirection = 1; // Start moving right
        initializeAliens();
        
        // Initialize bullets
        alienBullets = new ArrayList<>();
        playerBullet = null;
        
        // Initialize game state
        score = 0;
        lives = 3;
        alienFireCounter = 0;
    }
    
    private void initializeAliens() {
        for (int row = 0; row < ALIEN_ROWS; row++) {
            for (int col = 0; col < ALIEN_COLS; col++) {
                int x = alienFormationX + col * (ALIEN_WIDTH + ALIEN_PADDING);
                int y = alienFormationY + row * (ALIEN_HEIGHT + ALIEN_PADDING);
                alienGrid[row][col] = new Alien(x, y);
            }
        }
    }
    
    // Move player left
    public void movePlayerLeft() {
        playerX = Math.max(0, playerX - PLAYER_SPEED);
    }
    
    // Move player right
    public void movePlayerRight() {
        playerX = Math.min(GAME_WIDTH - PLAYER_WIDTH, playerX + PLAYER_SPEED);
    }
    
    // Fire player bullet
    public void firePlayerBullet() {
        if (playerBullet == null) {
            int bulletX = playerX + PLAYER_WIDTH / 2 - 2;
            int bulletY = playerY;
            playerBullet = new PlayerBullet(bulletX, bulletY);
        }
    }
    
    // Update game state each tick
    public void update() {
        // Update player bullet
        if (playerBullet != null) {
            playerBullet.y -= BULLET_SPEED;
            if (playerBullet.y < 0) {
                playerBullet = null;
            }
        }
        
        // Update alien bullets
        Iterator<AlienBullet> bulletIter = alienBullets.iterator();
        while (bulletIter.hasNext()) {
            AlienBullet bullet = bulletIter.next();
            bullet.y += ALIEN_BULLET_SPEED;
            if (bullet.y > GAME_HEIGHT) {
                bulletIter.remove();
            }
        }
        
        // Move alien formation
        moveAlienFormation();
        
        // Fire alien bullets
        fireAlienBullets();
        
        // Detect collisions
        detectCollisions();
    }
    
    private void moveAlienFormation() {
        // Check if formation needs to move down and reverse
        boolean shouldMoveDown = false;
        
        for (int row = 0; row < ALIEN_ROWS; row++) {
            for (int col = 0; col < ALIEN_COLS; col++) {
                Alien alien = alienGrid[row][col];
                if (alien.alive) {
                    alien.x += alienDirection * ALIEN_SPEED;
                    
                    // Check boundaries
                    if (alien.x <= 0 || alien.x + ALIEN_WIDTH >= GAME_WIDTH) {
                        shouldMoveDown = true;
                    }
                }
            }
        }
        
        // Move down and reverse direction
        if (shouldMoveDown) {
            for (int row = 0; row < ALIEN_ROWS; row++) {
                for (int col = 0; col < ALIEN_COLS; col++) {
                    Alien alien = alienGrid[row][col];
                    if (alien.alive) {
                        alien.y += 30; // Move down
                    }
                }
            }
            alienDirection *= -1; // Reverse direction
        }
    }
    
    private void fireAlienBullets() {
        alienFireCounter++;
        
        if (alienFireCounter >= ALIEN_FIRE_INTERVAL) {
            // Pick a random alive alien to fire
            List<Alien> aliveAliens = new ArrayList<>();
            for (int row = 0; row < ALIEN_ROWS; row++) {
                for (int col = 0; col < ALIEN_COLS; col++) {
                    if (alienGrid[row][col].alive) {
                        aliveAliens.add(alienGrid[row][col]);
                    }
                }
            }
            
            if (!aliveAliens.isEmpty()) {
                Alien shooter = aliveAliens.get(random.nextInt(aliveAliens.size()));
                int bulletX = shooter.x + ALIEN_WIDTH / 2 - 2;
                int bulletY = shooter.y + ALIEN_HEIGHT;
                alienBullets.add(new AlienBullet(bulletX, bulletY));
            }
            
            alienFireCounter = 0;
        }
    }
    
    private void detectCollisions() {
        // Check player bullet collisions with aliens
        if (playerBullet != null) {
            for (int row = 0; row < ALIEN_ROWS; row++) {
                for (int col = 0; col < ALIEN_COLS; col++) {
                    Alien alien = alienGrid[row][col];
                    if (alien.alive && isColliding(playerBullet.x, playerBullet.y, 4, 8,
                            alien.x, alien.y, ALIEN_WIDTH, ALIEN_HEIGHT)) {
                        alien.alive = false;
                        playerBullet = null;
                        score += 10;
                        return; // Only one collision per tick
                    }
                }
            }
        }
        
        // Check alien bullet collisions with player
        Iterator<AlienBullet> bulletIter = alienBullets.iterator();
        while (bulletIter.hasNext()) {
            AlienBullet bullet = bulletIter.next();
            if (isColliding(bullet.x, bullet.y, 4, 8,
                    playerX, playerY, PLAYER_WIDTH, PLAYER_HEIGHT)) {
                bulletIter.remove();
                lives--;
                return; // Only one collision per tick
            }
        }
    }
    
    private boolean isColliding(int x1, int y1, int w1, int h1,
                                int x2, int y2, int w2, int h2) {
        return x1 < x2 + w2 && x1 + w1 > x2 &&
               y1 < y2 + h2 && y1 + h1 > y2;
    }
    
    // Getters
    public int getPlayerX() { return playerX; }
    public int getPlayerY() { return playerY; }
    public int getPlayerWidth() { return PLAYER_WIDTH; }
    public int getPlayerHeight() { return PLAYER_HEIGHT; }
    
    public Alien[][] getAlienGrid() { return alienGrid; }
    public int getAlienWidth() { return ALIEN_WIDTH; }
    public int getAlienHeight() { return ALIEN_HEIGHT; }
    
    public PlayerBullet getPlayerBullet() { return playerBullet; }
    public List<AlienBullet> getAlienBullets() { return alienBullets; }
    
    public int getScore() { return score; }
    public int getLives() { return lives; }
    public int getGameWidth() { return GAME_WIDTH; }
    public int getGameHeight() { return GAME_HEIGHT; }
    
    public boolean isGameOver() { return lives <= 0; }
    public boolean isGameWon() {
        for (int row = 0; row < ALIEN_ROWS; row++) {
            for (int col = 0; col < ALIEN_COLS; col++) {
                if (alienGrid[row][col].alive) {
                    return false;
                }
            }
        }
        return true;
    }
}