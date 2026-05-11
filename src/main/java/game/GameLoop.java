package game;

import game.constants.GameConstants;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Manages the game loop with fixed timestep and optimized rendering
 */
public class GameLoop implements ActionListener {
    private boolean isRunning;
    private final Board board;
    private GamePanel gamePanel;
    
    // Fixed timestep variables for consistent FPS
    private static final int TARGET_FPS = 60;
    private static final long FRAME_TIME = 1000 / TARGET_FPS; // milliseconds per frame
    private long lastFrameTime = 0;
    
    // Frame rate control variables
    private volatile boolean shouldRender = false;
    
    public GameLoop(Board board) {
        this.board = board;
        this.isRunning = false;
    }
    
    /**
     * Sets the GamePanel reference for repaint notifications
     */
    public void setGamePanel(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }
    
    /**
     * Starts the game loop with fixed timestep
     */
    public void start() {
        System.out.println("Starting GameLoop with fixed timestep...");
        if (!isRunning) {
            // Use swing timer to maintain consistent frame rate
            Timer gameTimer = new Timer((int) FRAME_TIME, this);
            gameTimer.start();
            isRunning = true;
        }
    }
    
    /**
     * Stops the game loop
     */
    public void stop() {
        System.out.println("Stopping GameLoop...");
        isRunning = false;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        // Fixed timestep game loop
        long currentTime = System.currentTimeMillis();
        long deltaTime = currentTime - lastFrameTime;
        
        if (deltaTime >= FRAME_TIME) {
            // Update game state at fixed interval
            update();
            
            // Mark that we should render this frame
            shouldRender = true;
            
            lastFrameTime = currentTime;
        }
        
        // Only repaint if we have a new frame to display
        if (shouldRender && gamePanel != null) {
            // Trigger repaint only once per frame
            SwingUtilities.invokeLater(() -> {
                gamePanel.repaint();
            });
            shouldRender = false; // Reset render flag after painting
        }
    }
    
    /**
     * Updates game state at fixed intervals
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
        // Speed adjustment is handled by the fixed timestep approach
        // which maintains consistent frame rate regardless of game speed
    }
    
    /**
     * Gets the board instance
     */
    public Board getBoard() {
        return board;
    }
}
    
    /**
     * Sets the GamePanel reference for repaint notifications
     */
    public void setGamePanel(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }
    
    /**
     * Starts the game loop
     */
    public void start() {
        System.out.println("Starting GameLoop...");
        if (!isRunning) {
            gameTimer.start();
            isRunning = true;
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
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        // Game loop logic - update game state
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
            // Trigger repaint after update
            if (gamePanel != null) {
                // Use SwingUtilities.invokeLater to ensure thread safety
                SwingUtilities.invokeLater(() -> {
                    gamePanel.repaint(); // Explicitly trigger repaint
                });
            }
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