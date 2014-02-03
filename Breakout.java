/*
 * File: Breakout.java
 * -------------------
 * Name:
 * Section Leader:
 * 
 * This file will eventually implement the game of Breakout.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class Breakout extends GraphicsProgram {

	/** Width and height of application window in pixels */
	public static final int APPLICATION_WIDTH = 400;
	public static final int APPLICATION_HEIGHT = 600;

	/** Dimensions of game board (usually the same) */
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT;

	/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;

	/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 30;

	/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 10;

	/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 10;

	/** Separation between bricks */
	private static final int BRICK_SEP = 4;

	/** Width of a brick */
	private static final int BRICK_WIDTH =
		(WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;

	/** Height of a brick */
	private static final int BRICK_HEIGHT = 8;

	/** Radius of the ball in pixels */
	private static final int BALL_RADIUS = 10;

	/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;

	/** Number of turns */
	private static final int NTURNS = 3;

	/** Brick color of first two rows */
	private static final Color FIRST_TWO_ROWS_COLOR=Color.RED;

	/** Brick color of second two rows */
	private static final Color SECOND_TWO_ROWS_COLOR=Color.ORANGE;

	/** Brick color of third two rows */
	private static final Color THIRD_TWO_ROWS_COLOR=Color.YELLOW;

	/** Brick color of fourth two rows */
	private static final Color FOURTH_TWO_ROWS_COLOR=Color.GREEN;

	/** Brick color of fifth two rows */
	private static final Color FIFTH_TWO_ROWS_COLOR=Color.CYAN;
	public void run() {
		/* You fill this in, along with any subsidiary methods */
		setupGame();
		/*playGame(); */
	}

	/** Sets up bricks in game */

	private void setupGame() {
		for (int row=0; row<NBRICKS_PER_ROW; row++) {
			for (int column=0; column<NBRICK_ROWS; column++) {
				createBrick(row,column);
			}
		}
	}

	/** Creates individual brick given row and column */

	private void createBrick(int row, int column) {
		double topBrickX= (APPLICATION_WIDTH-BRICK_WIDTH*NBRICKS_PER_ROW)/2;
		double topBrickY= BRICK_Y_OFFSET;
		double newBrickX= topBrickX+column*BRICK_WIDTH;
		double newBrickY= topBrickY+row*BRICK_HEIGHT;
		GRect brick= new GRect (newBrickX,newBrickY,BRICK_WIDTH, BRICK_HEIGHT);
		brick.setFilled(true);
		Color brickColor= whatIsColorGivenRow(row);
		brick.setFillColor(brickColor);
		add(brick);

	}

	/** Returns color of a brick given it's row */

	private Color whatIsColorGivenRow(int row) {
		if (row<3){
			return(FIRST_TWO_ROWS_COLOR);
		}else{
			if (row<5) {
				return(SECOND_TWO_ROWS_COLOR);
			}else{
				if (row<7) {
					return(THIRD_TWO_ROWS_COLOR);
				}else{
					if (row<9) {
						return(FOURTH_TWO_ROWS_COLOR);
					}
				}
			}
		}
		return(FIFTH_TWO_ROWS_COLOR);
	}


}
