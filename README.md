# Java-Minesweeper
Assignment 6 in COSC121
This is my code solution to the Minesweeper game. This is made with Java and the JavaFX media package.

The game works making a grid of buttons that need to be clicked to reveal their contents.
They are a number between 0 and 8 inclusive or a mine.
The idea is to expose all numbers and avoid mines. 
You can place a flag in a tile where you think there is a mine.
  
Right click  and left click event handlers were placed in each button.
right click exposes a tile and left click places a flag.
Placing a flag only happens if the tile is not exposed and there is no flag in it. 
If there is already a flag, then the right click will remove it.
  
If the user right clicks a exposed tile with the correct number of flagged mines around it will expose every tile around it.
It recursively exposes everything around a zero tile.
  
The first click of the game will always be in a zero tile, so its always safe
  
A menu bar was added with three difficulties. Easy, normal and Hard. 
The easy has 10 Mines and a 8X8 grid
The normal has 40 Mines and a 16X16 grid
The easy has 99 Mines and a 32X16 grid
  
Clicking the smiley face will create a new game saving the game preference selected.

A custom button class was made. The MineCell extends the javaFX button and adds a little more functionality
storing coordinates, values and states.
If all non bomb tiles are uncovered, the face turns into the sunglasses one but if
a mine tile was clicked, then it changes to the dead face.	
 
The timeline object created will keep track of the seconds and will display them every second in the screen.
  
The seconds are stored on each new game and then compared with previous ones. 
If the player has a relatively low amount of seconds compare to the rest of gameplays, then a couple of pop-up windows will be displayed
asking for a name and then displaying the top 3 best pplayers and their time.
 Enjoy!
   
