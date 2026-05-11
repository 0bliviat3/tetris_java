package game;

import game.constants.GameConstants;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class GamePanel extends JPanel {
    private static final int BOARD_WIDTH = GameConstants.BOARD_WIDTH;
    private static final int BOARD_HEIGHT = GameConstants.BOARD_HEIGHT;
    private static final int BLOCK_SIZE = GameConstants.BLOCK_SIZE;
    
    private Board board;
    private GameLoop gameLoop;
    
    public GamePanel(Board board) {
        this.board = board;
        
        // Enable double buffering for smoother rendering
        setDoubleBuffered(true);
        
        // Initialize the board
        this.board = board;
        
        // Setup panel properties
        setPreferredSize(new Dimension(BOARD_WIDTH * BLOCK_SIZE, BOARD_HEIGHT * BLOCK_SIZE));
        setBackground(Color.BLACK);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Draw placed blocks
        drawPlacedBlocks(g);
        
        // Draw current tetromino
        drawCurrentTetromino(g);
    }
    
    /**
     * Draws the game board and all elements
     */
    private void drawBoard(Graphics g) {
        System.out.println("Drawing board");
        // Draw dark background
        g.setColor(new Color(30, 30, 30));
        g.fillRect(0, 0, BOARD_WIDTH * BLOCK_SIZE, BOARD_HEIGHT * BLOCK_SIZE);
        
        // Draw grid lines with darker color
        g.setColor(new Color(50, 50, 50));
        for (int x = 0; x <= BOARD_WIDTH; x++) {
            g.drawLine(x * BLOCK_SIZE, 0, x * BLOCK_SIZE, BOARD_HEIGHT * BLOCK_SIZE);
        }
        for (int y = 0; y <= BOARD_HEIGHT; y++) {
            g.drawLine(0, y * BLOCK_SIZE, BOARD_WIDTH * BLOCK_SIZE, y * BLOCK_SIZE);
        }
        
        // Draw placed blocks
        drawPlacedBlocks(g);
        
        // Draw current tetromino
        drawCurrentTetromino(g);
    }
    
    /**
     * Draws the placed blocks on the board
     */
    private void drawPlacedBlocks(Graphics g) {
        int[][] grid = board.getGrid();
        for (int row = 0; row < BOARD_HEIGHT; row++) {
            for (int col = 0; col < BOARD_WIDTH; col++) {
                if (grid[row][col] != 0) {
                    Color color = getColorForBlock(grid[row][col]);
                    g.setColor(color);
                    g.fillRect(col * BLOCK_SIZE, row * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                    
                    g.setColor(Color.BLACK);
                    g.drawRect(col * BLOCK_SIZE, row * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                }
            }
        }
    }
    
    /**
     * Draws the current falling tetromino
     */
    private void drawCurrentTetromino(Graphics g) {
        Tetromino current = board.getCurrentTetromino();
        if (current == null) {
            return;
        }
        
        int[][] shape = current.getShape();
        System.out.println("Current tetromino shape:");
        for (int[] row : shape) {
            System.out.println(Arrays.toString(row));
        }
        
        g.setColor(current.getColor());
        int tetrominoRow = current.getRow();
        int tetrominoCol = current.getCol();
        System.out.println("Drawing tetromino blocks - row: " + tetrominoRow + ", col: " + tetrominoCol);
        
        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[row].length; col++) {
                if (shape[row][col] != 0) {
                    int x = (col + tetrominoCol) * BLOCK_SIZE;
                    int y = (row + tetrominoRow) * BLOCK_SIZE;
                    System.out.println("Drawing block at x: " + x + ", y: " + y);
                    g.fillRect(x, y, BLOCK_SIZE, BLOCK_SIZE);
                    
                    g.setColor(Color.BLACK);
                    g.drawRect(x, y, BLOCK_SIZE, BLOCK_SIZE);
                    g.setColor(current.getColor());
                }
            }
        }
    }
    
    /**
     * Gets the color for a block based on its ID
     */
    private Color getColorForBlock(int blockId) {
        switch (blockId) {
            case 1: return Color.CYAN;
            case 2: return Color.BLUE;
            case 3: return Color.ORANGE;
            case 4: return Color.YELLOW;
            case 5: return Color.GREEN;
            case 6: return Color.MAGENTA;
            case 7: return Color.RED;
            default: return Color.GRAY;
        }
    }
    
    /**
     * Starts the game loop
     */
    public void startGame() {
        gameLoop.start();
    }
    
    /**
     * Stops the game loop
     */
    public void stopGame() {
        gameLoop.stop();
    }
    
    /**
     * Gets the board instance
     */
    public Board getBoard() {
        return board;
    }
}