# space-invaders
Space Invaders game

# Space Invaders (Java Swing MVC)

This project is a the classic Space Invaders game built in Java using Swing. The player controls a ship that can move left and right and shoot aliens. A formation of aliens moves across the screen, descends over time, and fires bullets back at the player. The game ends when the player loses all lives or when the aliens reach the bottom of the screen.

The game is structured using the Model-View-Controller (MVC) design pattern. This separation helps keep the code organized by dividing responsibilities into three distinct parts: game logic, rendering, and input handling.

The GameModel class is responsible for all game state and rules. It stores the positions of the player and aliens, manages bullets, tracks the score and remaining lives, and handles all collision detection. It also contains logic for movement, such as moving the alien formation across the screen and downward when it reaches the edge. Importantly, the Model contains no Swing imports and does not handle any drawing or user input. For example, methods like registering a player hit or tracking hit timing belong in the Model because they represent changes in game state rather than visual output.

The GameView class is responsible for everything the player sees. It reads data from the Model and draws the player, aliens, bullets, score, and lives using Swing graphics. It also displays the game-over screen when the game ends. The View does not modify any game state and does not contain logic for how the game behaves; it only visualizes what the Model provides.

The GameController class connects the Model and View. It handles keyboard input for moving the player and firing bullets, and it runs the main game loop using a Swing Timer. Each tick of the loop updates the Model and then tells the View to redraw. The Controller is also responsible for stopping the game when a game-over condition is reached.

Several extensions were added to improve the game. The player and aliens are drawn using pixel-art sprites created from 2D arrays instead of simple rectangles. Some shields were added, which have 5 "lives", that change color when hit by aliens or the player. 

Overall, this project shows the use of MVC method in game development. It  separates responsibilities so that the Model handles logic, the View handles rendering, and the Controller handles input and game flow. This structure makes the code easier to understand, extend, and debug.