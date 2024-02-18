/**COSC 121 Lab8 
 * @author Andres Laverde 300340209
 * 
 * This is part3 of a MineSweeper game.
 * In this project we will be using javaFX to run the game in a separate window,
 * 	to separate the sections of that window according to the various layouts permitted by the HBox, VBox and GridPane objects.
 * 
 * The game works making a grid of buttons that need to be clicked to reveal their contents.
 * They are a number between 0 and 8 inclusive or a mine.
 * The idea is to expose all numbers and avoid mines. 
 * You can place a flag in a tile where you think there is a mine.
 * 
 * Right click  and left click event handlers were placed in each button.
 * 	right click exposes a tile and left click places a flag.
 * Placing a flag only happens if the tile is not exposed and there is no flag in it. 
 * 	If there is already a flag, then the right click will remove it.
 * 
 * If the user right clicks a exposed tile with the correct number of flagged mines around it will expose every tile around it.
 * 	It recursively exposes everything around a zero tile.
 * 
 * The first click of the game will always be in a zero tile, so its always safe
 * 
 * A menu bar was added with three difficulties. Easy, normal and Hard. 
 * 	The easy has 10 Mines and a 8X8 grid
 * 	The normal has 40 Mines and a 16X16 grid
 * 	The easy has 99 Mines and a 32X16 grid
 * 
 * Clicking the smiley face will create a new game saving the game preference selected.
 * 
 * A custom button class was made. The MineCell extends the javaFX button and adds a little more functionality
 *  storing coordinates, values and states.
 *If all non bomb tiles are uncovered, the face turns into the sunglasses one but if
 *	a mine tile was clicked, then it changes to the dead face.	
 *
 * The timeline object created will keep track of the seconds and will display them every second in the screen.
 * 
 * The seconds are stored on each new game and then compared with previous ones. 
 * If the player has a relatively low amount of seconds compare to the rest of gameplays, then a couple of pop-up windows will be displayed
 * 	asking for a name and then displaying the top 3 best pplayers and their time.
 * 
 * Enjoy!
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;


public class Minesweeper extends Application{
	static int mineCount;
	static int flagNum;
	static int ROWS; // Horizontal Dimension of the board
	static int COLS; // Vertical Dimension of the board
	static String level;
	static Timer timer;
	static Timeline tl;
	static int seconds;
	static MineCell [][] board; //We create a button grid with the specified dimensions

	//We create an int grid for values
	static int [][] valueBoard; 

	//MenuBar declaration
	MenuBar menubar;
	Menu difficultyMenu;
	MenuItem easy;
	MenuItem normal;
	MenuItem hard;


	// Mine count Labels
	Label singleMine;
	Label tenMine;
	Label hundredMine;


	// Time Box labels
	Label sec;
	Label tenSec;
	Label min;

	//GridPane where we will put our buttons
	GridPane mineField;

	//The top section where the mine counter, face and time are
	Button emoji;
	HBox minesLeft;
	HBox time;

	//Main containers
	HBox top;
	VBox gameBoard;
	Scene scene;

	//Conditionals for game difficulty and the last one for the safe first click
	boolean isEasy, isNormal, isHard, isGameStarted;

	public void start(Stage primaryStage) {  //JavaFX required method to start

		//We instantiate all of our previously declared Objects and variables
		isEasy = false; isNormal = false; isHard = false; isGameStarted = false;

		level ="";
		sec = new Label();
		tenSec = new Label();
		min = new Label();

		singleMine = new Label();
		tenMine = new Label();
		hundredMine = new Label();

		menubar = new MenuBar();
		difficultyMenu = new Menu("Select Difficulty");
		easy = new MenuItem("Easy mode");
		normal = new MenuItem("Normal mode");
		hard = new MenuItem("Hard mode");

		easy.setOnAction(e -> {
			tl.stop();
			level = "Easy";
			seconds = 0;
			ROWS = 8;
			COLS = 8;
			mineCount = 10;
			System.out.println("Easy Mode selected Selected\nGrid size 8 by 8");
			mineField= new GridPane();
			mineField.setAlignment(Pos.CENTER);
			valueBoard = new int[ROWS][COLS];
			board = new MineCell[ROWS][COLS];
			initializeTilesAndEventHandlers();
			drawMineCounter();
			isGameStarted = false;
			isEasy = true;
			isNormal = false;
			isHard = false;
			gameBoard = new VBox(menubar,top, mineField);
			gameBoard.setAlignment(Pos.TOP_CENTER);

			tl = new Timeline(new KeyFrame(Duration.millis(1000), (c) -> {
				drawTime();
			}));

			tl.setCycleCount(Timeline.INDEFINITE);
			tl.play();

			if(gameBoard != null ) {
				scene = new Scene(gameBoard);
				primaryStage.setTitle("Mr Green MineSweeper baby"); // Set the stage title
				primaryStage.setScene(scene); // Place the scene in the stage
				primaryStage.show(); // Display the stage
			}
		});
		normal.setOnAction(e -> {
			tl.stop();
			level ="Normal";
			seconds = 0;
			ROWS = 16;
			COLS = 16;
			mineCount = 40;
			System.out.println("Normal Mode selected Selected\nGrid size 16 by 16");
			mineField= new GridPane();
			mineField.setAlignment(Pos.CENTER);
			valueBoard = new int[ROWS][COLS];
			board = new MineCell[ROWS][COLS];
			getValuesForGrid(valueBoard);
			initializeTilesAndEventHandlers();
			drawMineCounter();
			isGameStarted = false;
			isNormal = true;
			isEasy = false;
			isHard = false;
			gameBoard = new VBox(menubar,top, mineField);
			gameBoard.setAlignment(Pos.TOP_CENTER);

			tl = new Timeline(new KeyFrame(Duration.millis(1000), (b) -> {
				drawTime();
			}));

			tl.setCycleCount(Timeline.INDEFINITE);
			tl.play();

			if(gameBoard != null ) {
				scene = new Scene(gameBoard);
				primaryStage.setTitle("Mr Green MineSweeper baby"); // Set the stage title
				primaryStage.setScene(scene); // Place the scene in the stage
				primaryStage.show(); // Display the stage
			}
		});
		hard.setOnAction(e -> {
			tl.stop();
			level ="Hard";
			seconds = 0;
			ROWS = 16;
			COLS = 32;
			mineCount = 99;
			System.out.println("Hard Mode selected Selected\nGrid size 32 by 16");
			mineField= new GridPane();
			mineField.setAlignment(Pos.CENTER);
			valueBoard = new int[ROWS][COLS];
			board = new MineCell[ROWS][COLS];
			initializeTilesAndEventHandlers();
			isGameStarted = false;
			isHard = true;
			isEasy = false;
			isNormal = false;
			gameBoard = new VBox(menubar,top, mineField);
			gameBoard.setAlignment(Pos.TOP_CENTER);
			drawMineCounter();

			tl = new Timeline(new KeyFrame(Duration.millis(1000), (a) -> {
				drawTime();
			}));

			tl.setCycleCount(Timeline.INDEFINITE);
			tl.play();

			if(gameBoard != null ) {
				scene = new Scene(gameBoard);
				primaryStage.setTitle("Mr Green MineSweeper baby"); // Set the stage title
				primaryStage.setScene(scene); // Place the scene in the stage
				primaryStage.show(); // Display the stage
			}

		});
		menubar.getMenus().add(difficultyMenu);
		difficultyMenu.getItems().add(easy);
		difficultyMenu.getItems().add(normal);
		difficultyMenu.getItems().add(hard);
		//*************************************SMILEY FACE (EMOJI)***********************************************************************************

		emoji  = new Button();
		emoji.setGraphic(new ImageView(new Image("file:images/faces/face-smile.png")));
		emoji.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				createNewGame();
			}

			private void createNewGame() {
				tl.stop();
				if(isEasy) {
					ROWS = 8;
					COLS = 8;
					mineCount = 10;
					level ="Easy";
				}
				else if(isNormal) {
					ROWS = 16;
					COLS = 16;
					mineCount = 40;
					level ="Normal";
				}
				else if(isHard) {
					ROWS = 16;
					COLS = 32;
					mineCount = 99;
					level ="Hard";
				}
				seconds = 0;
				isGameStarted = false;
				mineField= new GridPane();
				mineField.setAlignment(Pos.CENTER);
				valueBoard = new int[ROWS][COLS];
				board = new MineCell[ROWS][COLS];
				initializeTilesAndEventHandlers();
				gameBoard = new VBox(menubar,top, mineField);
				gameBoard.setAlignment(Pos.TOP_CENTER);
				drawMineCounter();
				emoji.setGraphic(new ImageView(new Image("file:images/faces/face-smile.png")));

				tl = new Timeline(new KeyFrame(Duration.millis(1000), (e) -> {
					drawTime();
				}));

				tl.setCycleCount(Timeline.INDEFINITE);
				tl.play();

				if(gameBoard != null ) {
					scene = new Scene(gameBoard);
					primaryStage.setTitle("Mr Green MineSweeper baby"); // Set the stage title
					primaryStage.setScene(scene); // Place the scene in the stage
					primaryStage.show(); // Display the stage
				}
			}
		});	

		// We set the styling and layout of the game
		drawMineCounter();
		minesLeft = new HBox(hundredMine, tenMine, singleMine);
		minesLeft.setAlignment(Pos.CENTER);

		drawTime();
		time = new HBox(min, tenSec, sec);
		time.setAlignment(Pos.CENTER);

		//The HBox top is the header where the smiley faces and clocks will be
		top = new HBox(minesLeft, emoji, time);
		top.setAlignment(Pos.CENTER); //We set the alignment of the HBox elements
		top.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

		//************************************* TIMELINE INIT ***************************************************************
		tl = new Timeline(new KeyFrame(Duration.millis(1000), (e) -> {
			drawTime();
		}));

		tl.setCycleCount(Timeline.INDEFINITE);
		//********************************************************************************************************************
		mineField= new GridPane();
		mineField.setAlignment(Pos.CENTER);
		valueBoard = new int[ROWS][COLS];
		board = new MineCell[ROWS][COLS];
		initializeTilesAndEventHandlers();

		gameBoard = new VBox(menubar,top, mineField);
		gameBoard.setAlignment(Pos.TOP_CENTER);

		if(gameBoard != null ) {
			scene = new Scene(gameBoard);
			primaryStage.setTitle("Mr Green MineSweeper baby"); // Set the stage title
			primaryStage.setScene(scene); // Place the scene in the stage
			primaryStage.show(); // Display the stage
			System.out.println("MineCount:  " + mineCount);
		}
	}

	//Main method to launch JavaFX
	public static void main(String[] args) {
		launch(args);
	}


	private boolean checkWin(MineCell[][] mc) {
		for(int row = 0; row < mc.length; row++) {
			for(int col = 0; col < mc[row].length; col++) {
				if(mc[row][col].value != -1) {
					if(mc[row][col].isExposed == false)
						return false;
				}
			}
		}
		return true;
	}

	//This method initializes the button array and sets event handlers on them
	public void initializeTilesAndEventHandlers(){
		for (int x = 0; x<ROWS; x++) {
			for (int y =0; y<COLS; y++) {
				// We instantiate each of the custom buttons adding the value on the same index of the board of values
				board[x][y] = new MineCell(x, y, valueBoard[x][y]);
				board[x][y].setGraphic(new ImageView(new Image("file:images/cover.png"))); // On each instantiated button we place the cover graphic
				final int xx = x;
				final int yy = y;

				board[x][y].setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent arg0) {
						MouseButton mb = arg0.getButton();
						if(mb.name().equals("PRIMARY")) {
							if(isGameStarted == false) {
								isGameStarted = true;
								firstClick(valueBoard, xx, yy);
								getValuesForGrid(valueBoard);
								initializeTilesAndEventHandlers();
							}
							if(board[xx][yy].value == 0) {
								exposeAdjacent(board[xx][yy]);
								if(checkWin(board)) {
									emoji.setGraphic(new ImageView(new Image("file:images/faces/face-win.png")));
									revealAll();
									top3Players(seconds);
								}
							}
							System.out.println("left-click");
							exposeCell();
						}
						else if(mb.name().equals("SECONDARY")) {
							System.out.println("right-click");
							if(board[xx][yy].isExposed == false) {
								placeFlag();
							}
							else {
								countFlags(board[xx][yy]);
								if(checkWin(board)) {
									emoji.setGraphic(new ImageView(new Image("file:images/faces/face-win.png")));
									revealAll();
									top3Players(seconds);
								}
							}
						}
					}
					private void placeFlag() {
						if(board[xx][yy].hasFlag == false && board[xx][yy].isExposed == false) {
							board[xx][yy].setGraphic(new ImageView(new Image("file:images/flag.png")));
							board[xx][yy].hasFlag = true;
							mineCount--;
						}
						else {
							board[xx][yy].setGraphic(new ImageView(new Image("file:images/cover.png")));
							board[xx][yy].hasFlag = false;
							mineCount++;
						}
						drawMineCounter();
					}
					private void exposeCell() {

						if(board[xx][yy].hasFlag == false && board[xx][yy].isExposed == false) {
							board[xx][yy].isExposed = true;
							if(board[xx][yy].value == -1) {
								board[xx][yy].setGraphic(new ImageView(new Image("file:images/mine-red.png")));
								emoji.setGraphic(new ImageView(new Image("file:images/faces/face-dead.png")));
								revealAll();
							}
							else if(checkWin(board)) {
								emoji.setGraphic(new ImageView(new Image("file:images/faces/face-win.png")));
								revealAll();
								top3Players(seconds);
							}

							else{
								board[xx][yy].setGraphic(new ImageView(new Image("file:images/numbers/" + board[xx][yy].value + ".png")));
							}
						}
					}

				});
				board[x][y].setOnMousePressed(new EventHandler<MouseEvent>() {

					@Override
					public void handle(MouseEvent arg0) {
						MouseButton mb = arg0.getButton();
						if(mb.name().equals("PRIMARY")) {

							if(board[xx][yy].isExposed == false && board[xx][yy].hasFlag== false) {
								emoji.setGraphic(new ImageView(new Image("file:images/faces/face5.png")));
								board[xx][yy].setGraphic(new ImageView(new Image("file:images/gray_cell.png")));
							}
						}
					}
				});
				board[x][y].setOnMouseReleased(new EventHandler<MouseEvent>() {

					@Override
					public void handle(MouseEvent arg0) {

						if(board[xx][yy].isExposed == false && board[xx][yy].hasFlag== false) {
							emoji.setGraphic(new ImageView(new Image("file:images/faces/face-smile.png")));
							board[xx][yy].setGraphic(new ImageView(new Image("file:images/cover.png")));
						}
					}

				});
			}
		}
		for (int x = 0; x<ROWS; x++) {
			for (int y=0; y<COLS; y++) {
				mineField.add(board[x][y], y, x);
			}
		}
		System.out.println("size="+mineField.getChildren().size());
	}


	// This method will expose the cells adjacent to an exposed tile that has all of the bomb tiles correctly flagged
	public void countFlags(MineCell mc) {

		// We first count the number of flags on every direction
		int count = 0;
		if(mc.col-1 >= 0 && board[mc.row][mc.col-1].value == -1 && board[mc.row][mc.col-1].hasFlag)
			count++;
		if(mc.col+1 < board[mc.row].length && board[mc.row][mc.col+1].value == -1 && board[mc.row][mc.col+1].hasFlag)
			count++;
		if(mc.row-1 >= 0 && board[mc.row-1][mc.col].value == -1 && board[mc.row-1][mc.col].hasFlag)
			count++;
		if(mc.row+1 <board.length && board[mc.row+1][mc.col].value == -1 && board[mc.row+1][mc.col].hasFlag)
			count++;
		if((mc.row-1 >= 0 && mc.col-1 >= 0) && (board[mc.row-1][mc.col-1].value == -1) && board[mc.row-1][mc.col-1].hasFlag)
			count++;
		if((mc.row-1 >= 0 && mc.col+1 < board[mc.row].length) && (board[mc.row-1][mc.col+1].value == -1) && board[mc.row-1][mc.col+1].hasFlag)
			count++;
		if((mc.row+1 <board.length && mc.col+1 < board[mc.row].length) && (board[mc.row+1][mc.col+1].value == -1) && board[mc.row+1][mc.col+1].hasFlag)
			count++;
		if((mc.row+1 <board.length && mc.col-1 >=0 ) && (board[mc.row+1][mc.col-1].value == -1) && board[mc.row+1][mc.col-1].hasFlag)
			count++;

		// If the flagged cells count is the same as the value then we reveal everything that is not flagged and is adjacent the cell
		if(mc.value == count) {
			exposeAdjacent(mc);
		}

	}

	//Method to expose tiles recursively on each direction
	public void exposeAdjacent(MineCell mc) {

		//We check each direction. If its valid, not a flag and it is not exposed then we reveal it.
		if(mc.col-1 >= 0 && board[mc.row][mc.col-1].value != -1 && board[mc.row][mc.col-1].hasFlag == false && board[mc.row][mc.col-1].isExposed == false) {
			board[mc.row][mc.col-1].setGraphic(new ImageView(new Image("file:images/numbers/" + board[mc.row][mc.col-1].value + ".png")));
			board[mc.row][mc.col-1].isExposed = true;
			// If that tile has no bombs attached then we call the method on this button
			if(board[mc.row][mc.col-1].value == 0) { exposeAdjacent(board[mc.row][mc.col-1]);}
		}
		if(mc.col+1 < board[mc.row].length && board[mc.row][mc.col+1].value != -1 && board[mc.row][mc.col+1].hasFlag == false && board[mc.row][mc.col+1].isExposed == false) {
			board[mc.row][mc.col+1].setGraphic(new ImageView(new Image("file:images/numbers/" + board[mc.row][mc.col+1].value + ".png")));
			board[mc.row][mc.col+1].isExposed = true;
			if(board[mc.row][mc.col+1].value == 0) { exposeAdjacent(board[mc.row][mc.col+1]);}
		}

		if(mc.row-1 >= 0 && board[mc.row-1][mc.col].value != -1 && board[mc.row-1][mc.col].hasFlag == false && board[mc.row-1][mc.col].isExposed == false) {
			board[mc.row-1][mc.col].setGraphic(new ImageView(new Image("file:images/numbers/" + board[mc.row-1][mc.col].value + ".png")));
			board[mc.row-1][mc.col].isExposed = true;
			if(board[mc.row-1][mc.col].value == 0) { exposeAdjacent(board[mc.row-1][mc.col]);}
		}

		if(mc.row+1 <board.length && board[mc.row+1][mc.col].value != -1 && board[mc.row+1][mc.col].hasFlag == false && board[mc.row+1][mc.col].isExposed == false) {
			board[mc.row+1][mc.col].setGraphic(new ImageView(new Image("file:images/numbers/" + board[mc.row+1][mc.col].value + ".png")));
			board[mc.row+1][mc.col].isExposed = true;
			if(board[mc.row+1][mc.col].value == 0) { exposeAdjacent(board[mc.row+1][mc.col]);}
		}

		if((mc.row-1 >= 0 && mc.col-1 >= 0) && (board[mc.row-1][mc.col-1].value != -1) && board[mc.row-1][mc.col-1].hasFlag == false && board[mc.row-1][mc.col-1].isExposed == false) {
			board[mc.row-1][mc.col-1].setGraphic(new ImageView(new Image("file:images/numbers/" + board[mc.row-1][mc.col-1].value + ".png")));
			board[mc.row-1][mc.col-1].isExposed = true;
			if(board[mc.row-1][mc.col-1].value == 0) { exposeAdjacent(board[mc.row-1][mc.col-1]);}
		}

		if((mc.row-1 >= 0 && mc.col+1 < board[mc.row].length) && (board[mc.row-1][mc.col+1].value != -1) && board[mc.row-1][mc.col+1].hasFlag== false && board[mc.row-1][mc.col+1].isExposed == false) {
			board[mc.row-1][mc.col+1].setGraphic(new ImageView(new Image("file:images/numbers/" + board[mc.row-1][mc.col+1].value + ".png")));
			board[mc.row-1][mc.col+1].isExposed = true;
			if(board[mc.row-1][mc.col+1].value == 0) { exposeAdjacent(board[mc.row-1][mc.col+1]);}
		}

		if((mc.row+1 <board.length && mc.col+1 < board[mc.row].length) && (board[mc.row+1][mc.col+1].value != -1) && board[mc.row+1][mc.col+1].hasFlag==false && board[mc.row+1][mc.col+1].isExposed == false) {
			board[mc.row+1][mc.col+1].setGraphic(new ImageView(new Image("file:images/numbers/" + board[mc.row+1][mc.col+1].value + ".png")));
			board[mc.row+1][mc.col+1].isExposed = true;
			if(board[mc.row+1][mc.col+1].value == 0) { exposeAdjacent(board[mc.row+1][mc.col+1]);}
		}

		if((mc.row+1 <board.length && mc.col-1 >=0 ) && (board[mc.row+1][mc.col-1].value != -1) && board[mc.row+1][mc.col-1].hasFlag == false && board[mc.row+1][mc.col-1].isExposed == false) {
			board[mc.row+1][mc.col-1].setGraphic(new ImageView(new Image("file:images/numbers/" + board[mc.row+1][mc.col-1].value + ".png")));
			board[mc.row+1][mc.col-1].isExposed = true;
			if(board[mc.row+1][mc.col-1].value == 0) { exposeAdjacent(board[mc.row+1][mc.col-1]);}
		}


	}

	//method to expose all the tiles once 
	public void revealAll() {
		for (int x = 0; x<ROWS; x++) {
			for (int y =0; y<COLS; y++) {

				//If the tile is a bomb, unflagged and unexposed
				if(board[x][y].value == -1 && board[x][y].isExposed== false ) {
					board[x][y].setGraphic(new ImageView(new Image("file:images/mine-grey.png")));
				}
				//If the tile is not a bomb and is covered
				if(board[x][y].value != -1 && board[x][y].isExposed== false) {
					board[x][y].setGraphic(new ImageView(new Image("file:images/numbers/" + board[x][y].value + ".png")));
				}
				//If the tile is a misflagged mine
				if(board[x][y].hasFlag && board[x][y].value != -1) {
					board[x][y].setGraphic(new ImageView(new Image("file:images/mine-misflagged.png")));
				}
				board[x][y].isExposed = true;
			}
		}
		tl.stop();
	}


	//This method places the bombs
	public static void setBombs(int[][] n) {
		//we will loop until all the mines have been placed
		int BOMBCount = mineCount;
		while(BOMBCount>0) {
			for(int i = 0; i<n.length; i++) {
				for(int j = 0; j<n[i].length; j++) {
					// a random index is generated
					int randIndex = (int) (Math.random()*COLS);
					// and if the current index equals the random one then we place a mine
					// we also make sure that a mine wasn't placed there already
					if(j == randIndex && n[i][j]!= -1 && n[i][j]!= -2 &&  BOMBCount>0) {
						n[i][j] = -1;
						//System.out.println("mine in row: "+ i + " column: " + j);
						//once placed, the amount of mines-to-place decreases
						BOMBCount--;
					}
				}
			}
		}
	}

	//This method will make sure the first click is a safe click
	public static void firstClick(int[][] n, int x, int y) {
		//first we set fake bombs inside the indeces of the first click, so that when we populate the bombs they ignore this places
		n[x][y] =-2;
		if(x+1 < n.length) {n[x+1][y] =-2;}
		if(x-1 >= 0) {n[x-1][y] =-2;}
		if(y+1 < n[0].length) {n[x][y+1] =-2;}
		if(y-1 >= 0) {n[x][y-1] =-2;}
		if(x+1 < n.length && y+1 < n[0].length) {n[x+1][y+1] =-2;}
		if(x+1 < n.length && y-1 >= 0) {n[x+1][y-1] =-2;}
		if(x-1 >= 0 && y+1 < n[0].length) {n[x-1][y+1] =-2;}
		if(x-1 >= 0 && y-1 >= 0) {n[x-1][y-1] =-2;}

		//Then we place the bombs
		setBombs(n);

		//Finally we remove the fake bombs
		for(int i = 0; i<n.length; i++) {
			for(int j = 0; j<n[i].length; j++) {
				if(n[i][j] == -1) { continue;}
				else {n[i][j] = 0;}
			}
		}
	}

	//This method will give us an array of values for our minesweeper game. 
	public static void getValuesForGrid(int[][] n) {

		// nested for loop to generate values in the grid according to the mines placed
		for(int i = 0; i<n.length; i++) {
			for(int j = 0; j<n[i].length; j++) {
				//if the cell is a mine then we look at the next cell
				if(n[i][j] == -1) {
					continue;
				}
				//if its not then we look on every possible direction checking first if its not out of bounds
				else {
					//We make a counter to count the amount of mines adjacent
					int count = 0;
					if(j-1 >= 0 && n[i][j-1] == -1) {count++;}	
					if(j+1 < n[i].length && n[i][j+1] == -1) {count++;}
					if(i-1 >= 0 && n[i-1][j] == -1) {count++;}	
					if(i+1 <n.length && n[i+1][j] == -1) {count++;}	
					if((i-1 >= 0 && j-1 >= 0) && (n[i-1][j-1] == -1)) {count++;}	
					if((i-1 >= 0 && j+1 < n[i].length) && (n[i-1][j+1] == -1)) {count++;}	
					if((i+1 <n.length && j+1 < n[i].length) && (n[i+1][j+1] == -1)) {count++;}
					if((i+1 <n.length && j-1 >=0 ) && (n[i+1][j-1] == -1)) {count++;}
					//at the end we give the counter value to the cell
					n[i][j]=count;
				}
			}
		}
	}
	public void drawTime() {
		sec.setGraphic(new ImageView(new Image("file:images/clockDigits/"+(seconds%60)%10 + ".png")));
		tenSec.setGraphic(new ImageView(new Image("file:images/clockDigits/"+(seconds%60)/10 + ".png")));
		min.setGraphic(new ImageView(new Image("file:images/clockDigits/"+ (seconds/60)+ ".png")));
		seconds++;
	}

	public void drawMineCounter() {
		singleMine.setGraphic(new ImageView(new Image("file:images/clockDigits/"+ mineCount%10 +".png")));
		tenMine.setGraphic(new ImageView(new Image("file:images/clockDigits/" + (mineCount%100)/10 +".png")));
		hundredMine.setGraphic(new ImageView(new Image("file:images/clockDigits/" + mineCount/100 +".png")));
	}


	public void deleteGridTiles() {
		for(int  i= 0; i<valueBoard.length; i++) {
			for(int  j= 0; j<valueBoard[i].length; j++) {
				valueBoard[i][j] = 0;
			}
		}

		for(int x = 0; x<board.length; x++) {
			for(int y = 0; y<board[x].length; y++) {
				board[x][y] = null;
			}
		}
		for (int x = 0; x<ROWS; x++) {
			for (int y=0; y<COLS; y++) {
				mineField.getChildren().remove(board[x][y]);
			}
		}
	}
	
	//This method uses the Payer class and prompts a new window to ask for the player name if he/she made it to the top players list
	public static void top3Players(int sec) {
		//We first create an array list of Strings
		ArrayList<String> topPlayers = new ArrayList<>();

		// Create the file where we will store the data
		File top3 = new File("top3Players.txt");

		//We create the other 2 popup windows
		Stage popup = new Stage();
		Stage top3Popup = new Stage();
		Label HSMessage = new Label("You are a speedster, you enter the top 3 players\nPlease enter your name\n\n");
		TextField name = new TextField();
		Button submit = new Button("Submit");
		//The isntructions given to the buttons are simple, just hide and show the other window
		submit.setOnMouseClicked(e -> {
			MouseButton mb = e.getButton();
			if(mb.name().equals("PRIMARY")) {
				popup.hide();
				top3Popup.show();
			}
		});
		VBox namePrompt = new VBox(HSMessage, name, submit);
		Scene newScene = new Scene(namePrompt);
		popup.setScene(newScene);
		
		
		VBox HSPDisplay = new VBox();
		Label highScorePlayers = new Label();
		Button close = new Button("Close");
		close.setOnMouseClicked(l -> {
			MouseButton mb = l.getButton();
			if(mb.name().equals("PRIMARY")) {
				top3Popup.hide();
			}
		});
		
		Scanner sc = null;
		try {
			sc = new Scanner(top3);			
		} catch (FileNotFoundException fnfe) {
			System.out.println("File not found");
		}
		while(sc.hasNext()) {
			String HSPlayer = sc.nextLine();
			topPlayers.add(HSPlayer);
		}
		//If the file is new then we don't bother doing nothing else and we add the player
		if(topPlayers.size() == 0) {
			popup.show();
			String playerName = name.getText();
			Player p1 = new Player(playerName, seconds, level);
			topPlayers.add(p1.toString());
			highScorePlayers.setText(topPlayers.get(0));
			HSPDisplay.getChildren().addAll(highScorePlayers, close);
			Scene HSScene = new Scene(HSPDisplay);
			top3Popup.setScene(HSScene);
			
		}
		//If there are some other entries then we compare tteh time and we sort them putting the lowest time on top
		else {
			int otherTime = Integer.MAX_VALUE;
			for(int i = 0; i<topPlayers.size(); i++) {
				int start = topPlayers.get(i).indexOf(" ");
				int end = topPlayers.get(i).indexOf(" ", start+1);
				otherTime = Integer.parseInt(topPlayers.get(i).substring(start+1, end));
				if(sec < otherTime) {
					popup.show();
					String playerName = name.getText();
					Player p1 = new Player(playerName, seconds, level);
					topPlayers.add(i, p1.toString());
					String players = "";
					for(String s : topPlayers) {
						players += s +"\n";
					}
					highScorePlayers.setText(players);
					HSPDisplay.getChildren().addAll(highScorePlayers, close);
					Scene HSScene = new Scene(HSPDisplay);
					top3Popup.setScene(HSScene);
					break;
				}
			}
		}
		sc.close();
	}
}


//We create a custom button class with the basic button information
class MineCell extends Button{

	// Button coordinates and value
	int row, col, value;
	// Button states
	boolean isExposed, hasFlag;

	public MineCell(int r, int c, int v) {
		row = r;
		col = c;
		value = v;
	}
}
//We need a player object to store the name, difficulty level and time taken
class Player implements Comparable {
	private String name;
	private int timeTaken;
	private String level;

	public Player(String name, int timeTaken, String level) {
		this.name = name;
		this.timeTaken = timeTaken;
		this.level = level;
	}

	// Getters
	public String getPlayerName() {
		return name;
	}
	public int getPlayerScore() {
		return timeTaken;
	}

	//Setters
	public void setPlayerName(String n) {
		name = n;
	}
	public void setPlayerScore(int s) {
		timeTaken = s;
	}

	//Overloaded toString method
	public String toString() {
		return level + ":\t\t\t " + timeTaken + " seconds" + " \t" +  name;
	}


	@Override
	public int compareTo(Object o) {

		return 0;
	}
}

