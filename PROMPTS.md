# Prompt 1
I'm building Space Invaders in Java using Swing, split into three files: GameModel.java, GameView.java, and GameController.java. GameView should extend JPanel and be hosted in a JFrame. GameController should have the main method and wire the three classes together. GameModel must have no Swing imports. For now, just create the three class shells with placeholder comments describing what each class will do. The program should compile and open a blank window.
# Results:
AI created 3 files, and three classes, with descriptions:
GameModel.java —— Pure game logic — no Swing imports. Handles state, collisions, scoring
GameView.java —— Extends JPanel, hosted in JFrame. Renders game state
GameController.java	—— Contains main(), wires model + view together
All three files open a blank 800x600 window when run

# Prompt 2
Fill in GameModel.java. The model should track: the player's horizontal position, the alien formation (5 rows of 11), the player's bullet (one at a time), alien bullets, the score, and lives remaining (start with 3). Add logic to: move the player left and right, fire a player bullet if one isn't already in flight, advance the player's bullet each tick, move the alien formation right until the edge then down and reverse, fire alien bullets at random intervals, and detect collisions between bullets and aliens or the player. No Swing imports.
# Results:
Impletmented tracking of states, like Player position, alien formation, one player bullet (fires only if none in flight), list of alien bulletsand score and lives (starts at 3). It also contained game logic, like player movemnt contrainsts, firePlayerBullet(), which creates bullet from player if slot is free, update(), which is the main game tick that advances bullets, moves aliens, fires alien bullets, detects collisions, checks for game over versus you won. 

# Prompt 3
Fill in GameView.java. It should take a reference to the model and draw everything the player sees: the player, the alien formation, both sets of bullets, the score, and remaining lives. Show a centered game-over message when the game ends. The view should only read from the model — it must never change game state.
# Results:
Set up the visuals for each concept. Player is a green rectangle with light gray border,Aliens are red rectangles with dark red borders, player bullets are yellow rectangles while alien bullets are cyan rectangles, and score and lives in white text. Also contiained the information on the game state, like game over and winning screen. 

# Prompt 4
Fill in GameController.java. Add keyboard controls so the player can move left and right with the arrow keys and fire with the spacebar. Add a game loop using a Swing timer that updates the model each tick and redraws the view. Stop the loop when the game is over.
# Results:
implemented keyboard controls, (with spacebar firing bullet), the game loop, and now put everything together. with each tick, it handles key input, updates model, redraws view.
When run, successfully creates a testable start to the space invaders game

# Prompt 5
Create a separate file called ModelTester.java with a main method. It should create a GameModel, call its methods directly, and print PASS or FAIL for each check. Write tests for at least five behaviors: the player cannot move past the left or right edge, firing while a bullet is already in flight does nothing, a bullet that reaches the top is removed, destroying an alien increases the score, and losing all lives triggers the game-over state. No testing libraries — just plain Java.
# Results:
Tests implemented:
Player cannot move left past edge — verifies x = 0 boundary
Player cannot move right past edge — verifies x = gameWidth - playerWidth boundary
Firing while bullet in flight does nothing — confirms second firePlayerBullet() is ignored
Bullet removed when reaching top — bullet is null after 200 updates
Destroying alien increases score — aligns player under alien, fires, confirms score +10
Losing all lives triggers game-over — simulates damage until isGameOver() returns true
––––––––– All tests passed ––––––––

# Prompt 6
In GameModel.java, add a list of shield rectangles positioned between the player and the alien formation. Reduce a shield's health when hit by a bullet from either side. Remove the shield when health reaches zero. No Swing imports.
# Results:
Changed all main files. Decided to add shield functionality to GameModel, then updated GameView to render the shields, and even updated ModelTester to verify shield behavior. The AI folowed the constraints, no Swing imports, but in GameView shields are pure game logic in GameModelrenders shields with color fading (bright blue → dark blue → cornflower blue) as health decreases.

# Prompt 7
In GameView.java's paintComponent method only, draw the shields from the model's shield list. Use the shield's health value to choose a color from full green to dim red. Do not call any model mutating methods.
# Results:
Pretty much just changed the shields color gradient when dying to three 'lives', and full green, an olive yellow, then a dark red before dissapearing.
**don't like this feature at all, may remove?**
Attempted to edit the colors and added 5 lives, however it stop showing up. 

# Prompt 8
In GameView.java's paintComponent method only, draw the shields again from the model's shield list. Use the shield's health value, which should be 5, to choose a color from full green to dim red, which should be: (68,206,27), (187,219,68), (247,227,121), (242,161,52), then (229,31,31). Do not call any model mutating methods.
# Results:
Fixed the issue, as well as succeessfully colored the sheilds with the correct number of lives. 
 
# Prompt 9
In GameModel.java, trigger game over when any aliens have left the boundries of the screen. No Swing imports.
# Results:
Updated the GameModel.java and game triggers game over when aliens can no longer be shot at. Created a checkAliensOutOfBounds() method — checks if any alive alien has reached or passed the bottom of the screen (y + ALIEN_HEIGHT >= GAME_HEIGHT)
Triggered during update() — called after alien movement each tick
It also added a test in ModelTester.java where aliens leave screen triggers game-over, which passes. The ModelTester says there are 3 failed tests, supposedly fails to increase score when alien is destroyed, losing all lives triggers game-over, and sheilds are removed when health is zero. Actually running the program shows these tests do actually pass.  

# Prompt 10
In GameView.java, change how the aliens look, where the current square they are becomes a 9x7 pixels, where only some are filled. The shape should look like this:  
int[][] alien = {
        {0,0,1,0,0,0,1,0,0},
        {0,1,1,1,0,1,1,1,0},
        {1,1,1,1,1,1,1,1,1},
        {1,0,1,1,1,1,1,0,1},
        {1,1,1,0,1,0,1,1,1},
        {0,1,0,0,0,0,0,1,0},
        {1,0,0,1,0,1,0,0,1}
    };
    Where the "1"s represent where a pixel should be filled in with the same red. 
# Results:
Successfully changed the aliens to a pixel sprite!!! There is a still an red outlined square, but I was able to just turn that black so it wouldn't be visible, but it behaving as a hitbox is still there. 


# Prompt 11
Create a start screen that is a state in GameModel.java, but is only drawn in the GameView.java file with the name of the program in big green letters. When the game is over, (by winning or by losing), have a way of either starting the game again, which is triggered by a pressing the S key in GameController.java, or going back to the start screen. Show it as text in the GameView.java in white.
# Results:
Sucessfully created a startscreen, and a way of restarting game, also with a way of going to startscreen. 

# Prompt 12 (Continuation of prompt above)
In GameView.java, in the lose screen and the win screen, make the two options, (pressing S or pressing A), seperate lines.
# Results
Formatted the game over and win screen the way I intended!
 
# Prompt 13
In GameView.java, edit the look of the player just like we did for the aliens. This time, the player will become a 10x10 pixel sprite with a shape like this:
int[][] player = {
{0,0,0,0,0,0,0,0,0,0},
{0,0,0,0,1,1,0,0,0,0},
{0,0,0,1,1,1,1,0,0,0},
{1,1,1,1,1,1,1,1,1,1},
{1,1,1,1,1,1,1,1,1,1},
{1,1,1,1,1,1,1,1,1,1},
{1,1,1,1,1,1,1,1,1,1},
{0,0,0,0,0,0,0,0,0,0},
};
With the 1's having a color of (0, 149, 194)
# Results (Little note here, AI i was using ran out and it switched to a different one :(
))
Successfully turned player into a better sprite!! I did edit the 2D array a little bit after since it wasn't what I was expecting, so I made it thinner by putting more empty space on the bottom.  

# Prompt 14
Can you add a glow effect to the player in GameView.java when the player is hit
# Results
Flashes a little red box over player when hit

# Prompt 15
# Results