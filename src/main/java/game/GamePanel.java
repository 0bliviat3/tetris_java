package game;

import game.constants.GameConstants;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Main game panel that handles rendering and user interaction
 */
public class GamePanel extends JPanel {
    private static final int BOARD_WIDTH = GameConstants.BOARD_WIDTH;
    private static final int BOARD_HEIGHT = GameConstants.BOARD_HEIGHT;
    private static final int BLOCK_SIZE = GameConstants.BLOCK_SIZE;
    
    private final Timer gameTimer;
    private final InputHandler inputHandler;
    private final Board board;
    private final GameLoop gameLoop;
    
    public GamePanel() {
        setPreferredSize(new Dimension(
            BOARD_WIDTH * BLOCK_SIZE,
            BOARD_HEIGHT * BLOCK_SIZE
        ));
        setBackground(Color.BLACK);
        setFocusable(true);
        
        // Initialize game components
        board = new Board();
        inputHandler = new InputHandler(this);
        gameLoop = new GameLoop(board);
        
        addKeyListener(inputHandler);
        
        // Initialize game timer for rendering
        gameTimer = new Timer(16, e -> repaint()); // ~60 FPS
        gameTimer.start();
        
        // Start the game loop
        gameLoop.start();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw the game board
        drawBoard(g);
    }
    
    /**
     * Draws the game board and all elements
     */
    private void drawBoard(Graphics g) {
        // Draw grid lines
        g.setColor(Color.GRAY);
        for (int x = 0; x <= BOARD_WIDTH; x++) {
            g.drawLine(x * BLOCK_SIZE, 0, x * BLOCK_SIZE, BOARD_HEIGHT * BLOCK_SIZE);
        }
        for (int y = 0; y <= BOARD_HEIGHT; y++) {
            g.drawLine(0, y * BLOCK_SIZE, BOARD_WIDTH * BLOCK_SIZE, y * BLOCK_SIZE);
        }
        
        // Draw placed blocks
        drawPlacedBlocks(g);
        
        // Draw current tetromino if exists
        drawCurrentTetromino(g);
        
        // Draw game info
        drawGameInfo(g);
    }
    
    /**
     * Draws the placed blocks on the board
     */
    private void drawPlacedBlocks(Graphics g) {
        int[][] grid = board.getGrid();
        for (int row = 0; row < BOARD_HEIGHT; row++) {
            for (int col = 0; col < BOARD_WIDTH; col++) {
                if (grid[row][col] != 0) {
                    // Draw colored block
                    Color color = getBlockColor(grid[row][col]);
                    g.setColor(color);
                    g.fillRect(col * BLOCK_SIZE, row * BLOCK_SIZE, 
                              BLOCK_SIZE, BLOCK_SIZE);
                    
                    // Draw block border
                    g.setColor(Color.DARK_GRAY);
                    g.drawRect(col * BLOCK_SIZE, row * BLOCK_SIZE, 
                              BLOCK_SIZE, BLOCK_SIZE);
                }
            }
        }
    }
    
    /**
     * Draws the current falling tetromino
     */
    private void drawCurrentTetromino(Graphics g) {
        Tetromino currentTetromino = board.getCurrentTetromino();
        if (currentTetromino != null) {
            int[][] shape = currentTetromino.getShape();
            Color color = currentTetromino.getColor();
            
            g.setColor(color);
            for (int row = 0; row < shape.length; row++) {
                for (int col = 0; col < shape[row].length; col++) {
                    if (shape[row][col] != 0) {
                        int x = (currentTetromino.getCol() + col) * BLOCK_SIZE;
                        int y = (currentTetromino.getRow() + row) * BLOCK_SIZE;
                        g.fillRect(x, y, BLOCK_SIZE, BLOCK_SIZE);
                        
                        // Draw block border
                        g.setColor(Color.DARK_GRAY);
                        g.drawRect(x, y, BLOCK_SIZE, BLOCK_SIZE);
                        g.setColor(color);
                    }
                }
            }
        }
    }
    
    /**
     * Gets the color for a block based on its type
     */
    private Color getBlockColor(int type) {
        switch(type) {
            case 1: return Color.CYAN;    // I
            case 2: return Color.YELLOW;  // O
            case 3: return Color.MAGENTA; // T
            case 4: return Color.GREEN;   // S
            case 5: return Color.RED;     // Z
            case 6: return Color.BLUE;    // J
            case 7: return Color.ORANGE;  // L
            default: return Color.WHITE;
        }
    }
    
    /**
     * Draws game information (score, level, etc.)
     */
    private void drawGameInfo(Graphics g) {
        g.setColor(Color.WHITE);
        g.drawString("TETRIS", 10, 20);
    }
    
    /**
     * Starts the game loop
     */
    public void startGame() {
        if (!gameTimer.isRunning()) {
            gameTimer.start();
        }
        gameLoop.start();
    }
    
    /**
     * Stops the game loop
     */
    public void stopGame() {
        if (gameTimer.isRunning()) {
            gameTimer.stop();
        }
        gameLoop.stop();
    }
    
    /**
     * Gets the board instance
     */
    public Board getBoard() {
        return board;
    }
}