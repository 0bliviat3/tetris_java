package game;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import static game.constants.GameConstants.*;

/**
 * Handles keyboard input for the game
 */
public class InputHandler extends KeyAdapter {
    private final GamePanel gamePanel;
    
    public InputHandler(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        // Add debugging to see actual key events
        
        // Handle key press events
        switch(e.getKeyCode()) {
            case KEY_P:
                // Toggle pause
                togglePause();
                break;
            case KEY_R:
                // Restart game
                restartGame();
                break;
            case KEY_LEFT:
                // Move left
                moveLeft();
                break;
            case KEY_RIGHT:
                // Move right
                moveRight();
                break;
            case KEY_DOWN:
                // Soft drop
                softDrop();
                break;
            case KEY_UP:
                // Rotate
                rotate();
                break;
            case KEY_SPACE:
                // Hard drop
                hardDrop();
                break;
            default:
                break;
        }
    }
    
    /**
     * Toggle game pause state
     */
    private void togglePause() {
        Board board = gamePanel.getBoard();
        board.setPaused(!board.isPaused());
    }
    
    /**
     * Restart the game
     */
    private void restartGame() {
        // Reset the board to initial state by calling the dedicated restart method
        Board board = gamePanel.getBoard();
        board.reset();
    }
    
    /**
     * Move tetromino left
     */
    private void moveLeft() {
        Board board = gamePanel.getBoard();
        if (!board.isGameOver() && !board.isPaused()) {
            boolean moved = board.moveLeft();
        }
    }
    
    /**
     * Move tetromino right
     */
    private void moveRight() {
        Board board = gamePanel.getBoard();
        if (!board.isGameOver() && !board.isPaused()) {
            boolean moved = board.moveRight();
        }
    }
    
    /**
     * Soft drop (move down faster)
     */
    private void softDrop() {
        Board board = gamePanel.getBoard();
        if (!board.isGameOver() && !board.isPaused()) {
            boolean moved = board.moveDown();
        }
    }
    
    /**
     * Rotate tetromino
     */
    private void rotate() {
        Board board = gamePanel.getBoard();
        if (!board.isGameOver() && !board.isPaused()) {
            boolean rotated = board.rotate();
        }
    }
    
    /**
     * Hard drop (instantly drop tetromino)
     */
    private void hardDrop() {
        Board board = gamePanel.getBoard();
        if (!board.isGameOver() && !board.isPaused()) {
            board.hardDrop();
        }
    }
}