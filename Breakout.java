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

	private static final int WAIT_BETWEEN_BALL_MOVES=15;

	/** Paddle color */
	private static final Color PADDLE_COLOR=Color.black;

	/**Ball color */
	private static final Color BALL_COLOR=Color.black;


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

	/**Instance variable of paddle */
	private GRect paddle =createPaddle();

	/**Instance variable of paddle */
	private GOval ball =createBall();

	/**Instance variables of ball velocity on x and y axis */
	private double vx, vy;

	/**Intialize Random Generator */
	RandomGenerator rgen = RandomGenerator.getInstance();
	
	/** Tracks if ball is on the board **/
	private boolean ballInPlay=false;

	public void run() {
		setupGame();
		addMouseListeners();
		for (int life=1; life<=NTURNS; life++) {
			playTurn(life);
			if (anyBrickStillPresent()==false) {
				break;
			}
		}
		/*Will not allow mouse click to release new ball*/
		ballInPlay=true;
		gameOverMessage();
	}
	/**Keeps ball moving, changes direction of ball if hit wall or brick as long as ball did not hit bottom */

	private void playTurn(int life) {
		while (ball.getY()+BALL_RADIUS*2<HEIGHT) {
			ball.move(vx,vy);
			adjustForWallCollision();
			adjustForPaddleCollision();
			adjustForBrickCollision();
			if (anyBrickStillPresent()==false) {
				break;
			}
			pause(WAIT_BETWEEN_BALL_MOVES);
		}
		remove(ball);
		ballInPlay=false;
		ball=createBall();
	}

	/** Prints Game is Over **/
	private void gameOverMessage() {
		GLabel message= new GLabel("Game is Over");
		message.setFont("SansSerif-36");
		message.setColor(Color.RED);
		double messageX=WIDTH/2-message.getWidth()/2;
		double messageY=BRICK_HEIGHT*NBRICK_ROWS+BRICK_Y_OFFSET+(HEIGHT-PADDLE_Y_OFFSET-BRICK_HEIGHT*NBRICK_ROWS-BRICK_Y_OFFSET)/2-message.getDescent()/2;
		message.setLocation(messageX,messageY);
		add(message);
	}
	/** Adjusts ball trajectory if it hit any of the walls- except the bottom one **/
	private void adjustForWallCollision() {
		if (ball.getX() +ball.getWidth() >= WIDTH) {
			vx=-1*Math.abs(vx);
		}else{
			if (ball.getX()<=0) {
				vx=Math.abs(vx);
			}else{
				if (ball.getY()<=0) {
					vy=Math.abs(vy);
				}
			}
		}
	}

	private void adjustForPaddleCollision() {
		ball.sendToBack();
		if (ballIsClear()==paddle) {
			vy=-1*Math.abs(vy);
		}
	}

	private void adjustForBrickCollision() {
		if (ballIsClear() != ball && ballIsClear() != paddle) {
			int collisionPoint=pointOfBallCollision();
			remove(ballIsClear());
			if (collisionPoint==1) {
				vy=Math.abs(vy);
			}else{
				if (collisionPoint==2) {
					vx=-1*Math.abs(vx);
				}else{
					if (collisionPoint==3) {
						vy=-1*Math.abs(vy);
					}else{
						vx=Math.abs(vx);
					}
				}
			}
		}
	}

	private GObject ballIsClear() {
		double checkX=ball.getX();
		double checkY=ball.getY();
		ball.sendToBack();
		if (getElementAt(checkX,checkY) != null) return getElementAt(checkX,checkY);
		checkX= checkX+ ball.getWidth();
		if (getElementAt(checkX,checkY) != null) return getElementAt(checkX,checkY);
		checkY= checkY+ball.getHeight();
		if (getElementAt(checkX,checkY) != null) return getElementAt(checkX,checkY);
		checkX=checkX-ball.getWidth();
		if (getElementAt(checkX,checkY) != null) return getElementAt(checkX,checkY);
		return ball;
	}
	private boolean anyBrickStillPresent() {
		for (int row=1; row<=NBRICKS_PER_ROW; row++) {
			for (int column=1; column<=NBRICK_ROWS; column++) {
				if (checkBrickAbsent(row,column)==false) return true;
			}
		}
		return false;
	}
	private boolean checkBrickAbsent (int row, int column) {
		double topBrickX= (WIDTH-BRICK_WIDTH*NBRICKS_PER_ROW)/2;
		double topBrickY= BRICK_Y_OFFSET;
		double currentBrickX= topBrickX+(column-1)*BRICK_WIDTH;
		double currentBrickY= topBrickY+(row-1)*BRICK_HEIGHT;
		if (getElementAt(currentBrickX,currentBrickY) != null && getElementAt(currentBrickX,currentBrickY) != ball) {
			return false;
		}
		return true;
	}
	private int pointOfBallCollision() {
		double checkX=ball.getX()+BALL_RADIUS;
		double checkY=ball.getY();
		if (getElementAt(checkX,checkY) != null) return 1;
		checkX=checkX+BALL_RADIUS;
		checkY= checkY+ BALL_RADIUS;
		if (getElementAt(checkX,checkY) != null) return 2;
		checkX=checkX-BALL_RADIUS;
		checkY= checkY+BALL_RADIUS;
		if (getElementAt(checkX,checkY) != null) return 3;
		checkX=checkX-BALL_RADIUS;
		checkY= checkY-BALL_RADIUS;
		if (getElementAt(checkX,checkY) != null) return 4;
		return 0;
	}

	/** Makes paddle track mouse */
	public void mouseMoved(MouseEvent e) {
		double paddleLocationX=e.getX()-paddle.getWidth() / 2.0;
		if ((paddleLocationX+(paddle.getWidth()))>WIDTH) {
			paddleLocationX=WIDTH-paddle.getWidth();
		}else{
			if (paddleLocationX<0){
				paddleLocationX=0;
			}
		}

		paddle.setLocation(paddleLocationX,
				paddle.getY());
	}

	/**drops ball if beginning of turn and mouse clicked*/
	public void mouseClicked(MouseEvent e) {
			if (ballInPlay==false) {
				vx=rgen.nextDouble(1,3);
				if (rgen.nextBoolean(0.5)) vx = -vx;
				vy=3.0;
				createBall();
				add(ball);
				ballInPlay=true;
		}
	}

	/** Sets up bricks in game */
	private void setupGame() {
		for (int row=1; row<=NBRICKS_PER_ROW; row++) {
			for (int column=1; column<=NBRICK_ROWS; column++) {
				createBrick(row,column);
			}
		}
	}

	/** Creates ball and does not add to screen*/
	private GOval createBall() {
		double ballStartX= WIDTH/2-BALL_RADIUS;
		double lowestBrickY= NBRICK_ROWS*BRICK_HEIGHT+BRICK_Y_OFFSET;
		double heightBetweenPaddleLowestBrick= lowestBrickY-PADDLE_HEIGHT+PADDLE_Y_OFFSET;
		double ballStartY= lowestBrickY+heightBetweenPaddleLowestBrick;
		GOval newBall= new GOval(ballStartX,ballStartY,2*BALL_RADIUS,2*BALL_RADIUS);
		newBall.setFilled(true);
		newBall.setFillColor(BALL_COLOR);
		return newBall;
	}

	/** Creates paddle and adds to screen*/
	private GRect createPaddle() {
		double paddleStartX= (WIDTH- PADDLE_WIDTH)/2;
		double paddleStartY=HEIGHT-PADDLE_Y_OFFSET-PADDLE_HEIGHT;
		GRect newPaddle= new GRect(paddleStartX,paddleStartY,PADDLE_WIDTH,PADDLE_HEIGHT);
		newPaddle.setFilled(true);
		newPaddle.setFillColor(PADDLE_COLOR);
		add(newPaddle);
		return newPaddle;
	}

	/** Creates individual brick given row and column */

	private void createBrick(int row, int column) {
		double topBrickX= (WIDTH-BRICK_WIDTH*NBRICKS_PER_ROW)/2;
		double topBrickY= BRICK_Y_OFFSET;
		double newBrickX= topBrickX+(column-1)*BRICK_WIDTH;
		double newBrickY= topBrickY+(row-1)*BRICK_HEIGHT;
		GRect brick= new GRect (newBrickX,newBrickY,BRICK_WIDTH, BRICK_HEIGHT);
		brick.setFilled(true);
		Color brickColor= whatIsColorGivenRow(row);
		brick.setFillColor(brickColor);
		add(brick);

	}

	/** Returns color of a brick given it's row 
	 * Could have used switch method here, but already coded it
	 * when you sent out the tips-- next time!*/

	private Color whatIsColorGivenRow(int row) {
		if (row<=2){
			return(FIRST_TWO_ROWS_COLOR);
		}else{
			if (row<=4) {
				return(SECOND_TWO_ROWS_COLOR);
			}else{
				if (row<=6) {
					return(THIRD_TWO_ROWS_COLOR);
				}else{
					if (row<=8) {
						return(FOURTH_TWO_ROWS_COLOR);
					}
				}
			}
		}
		return(FIFTH_TWO_ROWS_COLOR);
	}

}
