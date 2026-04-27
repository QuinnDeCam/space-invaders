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


# Prompt 10
# Results:

# Prompt 11
# Results:
 
 