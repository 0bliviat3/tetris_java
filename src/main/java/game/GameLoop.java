package game;

import game.constants.GameConstants;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Manages the game loop using Swing Timer
 */
public class GameLoop implements ActionListener {
    private final Timer gameTimer;
    private boolean isRunning;
    private final Board board;
    private static final int DEFAULT_GAME_SPEED = 800; // milliseconds
    
    public GameLoop(Board board) {
        this.board = board;
        this.gameTimer = new Timer(DEFAULT_GAME_SPEED, this);
        this.isRunning = false;
        System.out.println("GameLoop created with timer: " + gameTimer);
    }
    
    /**
     * Starts the game loop
     */
    public void start() {
        System.out.println("Starting GameLoop...");
        if (!isRunning) {
            gameTimer.start();
            isRunning = true;
            System.out.println("GameLoop started successfully");
        }
    }
    
    /**
     * Stops the game loop
     */
    public void stop() {
        System.out.println("Stopping GameLoop...");
        if (isRunning) {
            gameTimer.stop();
            isRunning = false;
            System.out.println("GameLoop stopped successfully");
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        // Game loop logic - update game state
        System.out.println("GameLoop tick - isRunning: " + isRunning);
        if (isRunning) {
            update();
        }
    }
    
    /**
     * Updates game state
     */
    private void update() {
        // Move the current tetromino down one row
        if (!board.isGameOver() && !board.isPaused()) {
            board.moveDown();
        }
    }
    
    /**
     * Updates the game speed based on level
     */
    public void setSpeed(int level) {
        int speed = Math.max(GameConstants.MIN_DROP_SPEED, 
                           GameConstants.INITIAL_DROP_SPEED - 
                           (level * GameConstants.SPEED_DECREMENT_PER_LEVEL));
        gameTimer.setDelay(speed);
    }
    
    /**
     * Gets the board instance
     */
    public Board getBoard() {
        return board;
    }
}