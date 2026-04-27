/**
 * ModelTester - Unit tests for GameModel behavior.
 * Tests five key game mechanics without using any testing libraries.
 * Prints PASS or FAIL for each test.
 */
public class ModelTester {
    
    private static int passCount = 0;
    private static int failCount = 0;
    
    public static void main(String[] args) {
        System.out.println("=== GameModel Unit Tests ===\n");
        
        testPlayerCannotMoveLeftPastEdge();
        testPlayerCannotMoveRightPastEdge();
        testFiringWhileBulletInFlightDoesNothing();
        testBulletRemovedWhenReachingTop();
        testDestroyingAlienIncreasesScore();
        testLosingAllLivesTriggersGameOver();
        testShieldsCreatedAtStart();
        testShieldsTakeDamageFromBullets();
        testShieldsRemovedWhenHealthZero();
        
        System.out.println("\n=== Test Summary ===");
        System.out.println("PASSED: " + passCount);
        System.out.println("FAILED: " + failCount);
        System.out.println("TOTAL:  " + (passCount + failCount));
    }
    
    private static void testPlayerCannotMoveLeftPastEdge() {
        System.out.print("Test 1: Player cannot move left past edge... ");
        GameModel model = new GameModel();
        int initialX = model.getPlayerX();
        
        // Move left many times
        for (int i = 0; i < 100; i++) {
            model.movePlayerLeft();
        }
        
        // Player should be at x=0
        if (model.getPlayerX() == 0 && model.getPlayerX() < initialX) {
            System.out.println("PASS");
            passCount++;
        } else {
            System.out.println("FAIL (x=" + model.getPlayerX() + ")");
            failCount++;
        }
    }
    
    private static void testPlayerCannotMoveRightPastEdge() {
        System.out.print("Test 2: Player cannot move right past edge... ");
        GameModel model = new GameModel();
        int initialX = model.getPlayerX();
        
        // Move right many times
        for (int i = 0; i < 100; i++) {
            model.movePlayerRight();
        }
        
        // Player should not exceed game width
        int rightBound = model.getGameWidth() - model.getPlayerWidth();
        if (model.getPlayerX() == rightBound && model.getPlayerX() > initialX) {
            System.out.println("PASS");
            passCount++;
        } else {
            System.out.println("FAIL (x=" + model.getPlayerX() + ", expected=" + rightBound + ")");
            failCount++;
        }
    }
    
    private static void testFiringWhileBulletInFlightDoesNothing() {
        System.out.print("Test 3: Firing while bullet in flight does nothing... ");
        GameModel model = new GameModel();
        
        // Fire first bullet
        model.firePlayerBullet();
        GameModel.PlayerBullet firstBullet = model.getPlayerBullet();
        
        if (firstBullet == null) {
            System.out.println("FAIL (first bullet is null)");
            failCount++;
            return;
        }
        
        int firstBulletY = firstBullet.getY();
        
        // Try to fire second bullet
        model.firePlayerBullet();
        GameModel.PlayerBullet secondBullet = model.getPlayerBullet();
        
        // Should still have the first bullet, not a new one
        if (secondBullet == firstBullet && secondBullet.getY() == firstBulletY) {
            System.out.println("PASS");
            passCount++;
        } else {
            System.out.println("FAIL (bullet changed or was replaced)");
            failCount++;
        }
    }
    
    private static void testBulletRemovedWhenReachingTop() {
        System.out.print("Test 4: Bullet removed when reaching top... ");
        GameModel model = new GameModel();
        
        // Fire a bullet
        model.firePlayerBullet();
        GameModel.PlayerBullet bullet = model.getPlayerBullet();
        
        if (bullet == null) {
            System.out.println("FAIL (bullet is null)");
            failCount++;
            return;
        }
        
        int bulletY = bullet.getY();
        
        // Update model many times to move bullet off screen
        for (int i = 0; i < 200; i++) {
            model.update();
        }
        
        // Bullet should be removed (null)
        if (model.getPlayerBullet() == null) {
            System.out.println("PASS");
            passCount++;
        } else {
            System.out.println("FAIL (bullet still exists at y=" + model.getPlayerBullet().getY() + ")");
            failCount++;
        }
    }
    
    private static void testDestroyingAlienIncreasesScore() {
        System.out.print("Test 5: Destroying alien increases score... ");
        GameModel model = new GameModel();
        int initialScore = model.getScore();
        
        // Get the first alien
        GameModel.Alien[][] grid = model.getAlienGrid();
        GameModel.Alien targetAlien = grid[0][5]; // Middle alien in top row
        
        // Align player directly under the alien
        // The player bullet fires from playerX + playerWidth/2 - 2
        // Alien is at alien.x to alien.x + 30
        // We need: playerX + 20 - 2 to overlap with targetAlien.x to targetAlien.x + 30
        
        int targetPlayerX = targetAlien.getX() + 10; // Center roughly under alien
        
        // Move player to alignment position
        while (model.getPlayerX() < targetPlayerX) {
            model.movePlayerRight();
        }
        while (model.getPlayerX() > targetPlayerX) {
            model.movePlayerLeft();
        }
        
        // Fire bullet
        model.firePlayerBullet();
        
        // Update until bullet hits or is removed
        int updates = 0;
        while (model.getPlayerBullet() != null && updates < 1000) {
            model.update();
            updates++;
        }
        
        // Check if score increased (at least 10 points for one alien)
        if (model.getScore() > initialScore) {
            System.out.println("PASS (score: " + initialScore + " -> " + model.getScore() + ")");
            passCount++;
        } else {
            System.out.println("FAIL (score unchanged at " + model.getScore() + ")");
            failCount++;
        }
    }
    
    private static void testLosingAllLivesTriggersGameOver() {
        System.out.print("Test 6: Losing all lives triggers game-over... ");
        GameModel model = new GameModel();
        
        if (model.isGameOver()) {
            System.out.println("FAIL (game already over at start)");
            failCount++;
            return;
        }
        
        // Get initial lives
        int lives = model.getLives();
        
        // Simulate losing all lives by directly calling collision logic
        // We'll do this by firing alien bullets into the player
        for (int i = 0; i < lives; i++) {
            // Find an alien and have it fire
            GameModel.Alien[][] grid = model.getAlienGrid();
            if (grid[0][0].isAlive()) {
                // Manually update to trigger alien fire multiple times
                for (int j = 0; j < 500; j++) {
                    model.update();
                    
                    // Check if an alien bullet hit the player
                    if (model.getLives() < lives - i) {
                        break;
                    }
                }
            }
        }
        
        // Game should be over now
        if (model.isGameOver()) {
            System.out.println("PASS (lives: " + model.getLives() + ")");
            passCount++;
        } else {
            System.out.println("FAIL (game not over, lives: " + model.getLives() + ")");
            failCount++;
        }
    }
    
    private static void testShieldsCreatedAtStart() {
        System.out.print("Test 7: Shields created at start... ");
        GameModel model = new GameModel();
        
        if (model.getShields().size() == 4) {
            System.out.println("PASS (4 shields)");
            passCount++;
        } else {
            System.out.println("FAIL (shields count: " + model.getShields().size() + ")");
            failCount++;
        }
    }
    
    private static void testShieldsTakeDamageFromBullets() {
        System.out.print("Test 8: Shields take damage from bullets... ");
        GameModel model = new GameModel();
        
        java.util.List<GameModel.Shield> shields = model.getShields();
        if (shields.isEmpty()) {
            System.out.println("FAIL (no shields)");
            failCount++;
            return;
        }
        
        GameModel.Shield testShield = shields.get(0);
        int initialHealth = testShield.getHealth();
        
        // Move player to align with first shield
        int targetX = testShield.getX() + 10;
        while (model.getPlayerX() < targetX) {
            model.movePlayerRight();
        }
        while (model.getPlayerX() > targetX) {
            model.movePlayerLeft();
        }
        
        // Fire bullet and update until it hits shield
        model.firePlayerBullet();
        for (int i = 0; i < 500; i++) {
            model.update();
            if (model.getPlayerBullet() == null) {
                break; // Bullet was used
            }
        }
        
        // Check if shield took damage
        if (testShield.getHealth() < initialHealth) {
            System.out.println("PASS (health: " + initialHealth + " -> " + testShield.getHealth() + ")");
            passCount++;
        } else {
            System.out.println("FAIL (shield health unchanged)");
            failCount++;
        }
    }
    
    private static void testShieldsRemovedWhenHealthZero() {
        System.out.print("Test 9: Shields removed when health zero... ");
        GameModel model = new GameModel();
        
        java.util.List<GameModel.Shield> shields = model.getShields();
        if (shields.isEmpty()) {
            System.out.println("FAIL (no shields)");
            failCount++;
            return;
        }
        
        GameModel.Shield testShield = shields.get(1); // Use second shield
        int initialCount = shields.size();
        
        // Move player to align with this shield
        int targetX = testShield.getX() + 10;
        while (model.getPlayerX() < targetX) {
            model.movePlayerRight();
        }
        while (model.getPlayerX() > targetX) {
            model.movePlayerLeft();
        }
        
        // Fire 3 bullets to destroy shield (health = 3)
        for (int shot = 0; shot < 3; shot++) {
            model.firePlayerBullet();
            for (int i = 0; i < 500; i++) {
                model.update();
                if (model.getPlayerBullet() == null) {
                    break;
                }
            }
        }
        
        // Refresh shields list (it may have been modified)
        java.util.List<GameModel.Shield> currentShields = model.getShields();
        
        // Check if shield was removed
        if (currentShields.size() < initialCount) {
            System.out.println("PASS (shields: " + initialCount + " -> " + currentShields.size() + ")");
            passCount++;
        } else {
            System.out.println("FAIL (shield count unchanged)");
            failCount++;
        }
    }
}
