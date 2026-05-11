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
        // Add debugging to see actual key events
        System.out.println("KEY PRESSED: " + e.getKeyCode() + " - " + KeyEvent.getKeyText(e.getKeyCode()));
        
        // Handle key press events
        switch(e.getKeyCode()) {
            case KeyEvent.VK_P:
                // Toggle pause
                System.out.println("P pressed - toggling pause");
                togglePause();
                break;
            case KeyEvent.VK_R:
                // Restart game
                System.out.println("R pressed - restarting game");
                restartGame();
                break;
            case KeyEvent.VK_LEFT:
                // Move left
                System.out.println("LEFT pressed - moving left");
                moveLeft();
                break;
            case KeyEvent.VK_RIGHT:
                // Move right
                System.out.println("RIGHT pressed - moving right");
                moveRight();
                break;
            case KeyEvent.VK_DOWN:
                // Soft drop
                System.out.println("DOWN pressed - soft drop");
                softDrop();
                break;
            case KeyEvent.VK_UP:
                // Rotate
                System.out.println("UP pressed - rotating");
                rotate();
                break;
            case KeyEvent.VK_SPACE:
                // Hard drop
                System.out.println("SPACE pressed - hard drop");
                hardDrop();
                break;
            default:
                System.out.println("Unhandled key: " + e.getKeyCode());
        }
    }
    
    /**
     * Toggle game pause state
     */
    private void togglePause() {
        Board board = gamePanel.getBoard();
        board.setPaused(!board.isPaused());
        System.out.println("Pause toggled: " + board.isPaused());
    }
    
    /**
     * Restart the game
     */
    private void restartGame() {
        // Reset the board to initial state by calling the dedicated restart method
        Board board = gamePanel.getBoard();
        board.reset();
        System.out.println("Game restarted");
    }
    
    /**
     * Move tetromino left
     */
    private void moveLeft() {
        Board board = gamePanel.getBoard();
        if (!board.isGameOver() && !board.isPaused()) {
            boolean moved = board.moveLeft();
            System.out.println("Move left: " + moved);
        }
    }
    
    /**
     * Move tetromino right
     */
    private void moveRight() {
        Board board = gamePanel.getBoard();
        if (!board.isGameOver() && !board.isPaused()) {
            boolean moved = board.moveRight();
            System.out.println("Move right: " + moved);
        }
    }
    
    /**
     * Soft drop (move down faster)
     */
    private void softDrop() {
        Board board = gamePanel.getBoard();
        if (!board.isGameOver() && !board.isPaused()) {
            boolean moved = board.moveDown();
            System.out.println("Soft drop: " + moved);
        }
    }
    
    /**
     * Rotate tetromino
     */
    private void rotate() {
        Board board = gamePanel.getBoard();
        if (!board.isGameOver() && !board.isPaused()) {
            boolean rotated = board.rotate();
            System.out.println("Rotate: " + rotated);
        }
    }
    
    /**
     * Hard drop (instantly drop tetromino)
     */
    private void hardDrop() {
        Board board = gamePanel.getBoard();
        if (!board.isGameOver() && !board.isPaused()) {
            board.hardDrop();
            System.out.println("Hard drop performed");
        }
    }
}