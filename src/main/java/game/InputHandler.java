package game;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

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
        // Handle key press events
        switch(e.getKeyCode()) {
            case KeyEvent.VK_P:
                // Toggle pause
                togglePause();
                break;
            case KeyEvent.VK_R:
                // Restart game
                restartGame();
                break;
            case KeyEvent.VK_LEFT:
                // Move left
                moveLeft();
                break;
            case KeyEvent.VK_RIGHT:
                // Move right
                moveRight();
                break;
            case KeyEvent.VK_DOWN:
                // Soft drop
                softDrop();
                break;
            case KeyEvent.VK_UP:
                // Rotate
                rotate();
                break;
            case KeyEvent.VK_SPACE:
                // Hard drop
                hardDrop();
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
            board.moveLeft();
        }
    }
    
    /**
     * Move tetromino right
     */
    private void moveRight() {
        Board board = gamePanel.getBoard();
        if (!board.isGameOver() && !board.isPaused()) {
            board.moveRight();
        }
    }
    
    /**
     * Soft drop (move down faster)
     */
    private void softDrop() {
        Board board = gamePanel.getBoard();
        if (!board.isGameOver() && !board.isPaused()) {
            board.moveDown();
        }
    }
    
    /**
     * Rotate tetromino
     */
    private void rotate() {
        Board board = gamePanel.getBoard();
        if (!board.isGameOver() && !board.isPaused()) {
            board.rotate();
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