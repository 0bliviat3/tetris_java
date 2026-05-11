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

    private long lastDropTime = 0;
    
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
        long currentTime = System.currentTimeMillis();

        if (!board.isGameOver() && !board.isPaused()) {

            int level = board.getLevel();

            // 속도 계산 (GameConstants 기반)
            int dropSpeed = GameConstants.INITIAL_DROP_SPEED
                    - (level * GameConstants.SPEED_DECREMENT_PER_LEVEL);

            // 최소 속도 제한
            dropSpeed = Math.max(GameConstants.MIN_DROP_SPEED, dropSpeed);

            // 시간 기반 낙하
            if (currentTime - lastDropTime >= dropSpeed) {
                board.moveDown();
                lastDropTime = currentTime;
            }
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
    
