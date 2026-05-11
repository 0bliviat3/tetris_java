package game;

import game.constants.GameConstants;

import javax.swing.*;
import java.awt.*;

import static game.constants.GameConstants.PANEL_WIDTH;

public class GamePanel extends JPanel {
    private static final int BOARD_WIDTH = GameConstants.BOARD_WIDTH;
    private static final int BOARD_HEIGHT = GameConstants.BOARD_HEIGHT;
    private static final int BLOCK_SIZE = GameConstants.BLOCK_SIZE;
    
    private Board board;
    private GameLoop gameLoop;
    private SidePanel sidePanel;
    
    public GamePanel(Board board) {
        this.board = board;
        
        // Initialize game loop
        this.gameLoop = new GameLoop(board);
        
        // Enable double buffering for smoother rendering
        setDoubleBuffered(true);
        
        // Set layout manager to BorderLayout
        setLayout(new BorderLayout());
        
        // Initialize side panel
        sidePanel = new SidePanel(board);
        add(sidePanel, BorderLayout.EAST);
        
        // Setup panel properties
        setPreferredSize(new Dimension(
                BOARD_WIDTH * BLOCK_SIZE + PANEL_WIDTH,
                BOARD_HEIGHT * BLOCK_SIZE
        ));
        setBackground(Color.BLACK);
        
        // Setup input handling
        InputHandler inputHandler = new InputHandler(this);
        addKeyListener(inputHandler);
        setFocusable(true);
        requestFocusInWindow();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        drawBoard(g);
    }
    
    /**
     * Draws the game board and all elements
     */
    private void drawBoard(Graphics g) {
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

        Color color = getColorForBlock(current.getType());
        g.setColor(color);
        int tetrominoRow = current.getRow();
        int tetrominoCol = current.getCol();
        
        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[row].length; col++) {
                if (shape[row][col] != 0) {
                    int x = (col + tetrominoCol) * BLOCK_SIZE;
                    int y = (row + tetrominoRow) * BLOCK_SIZE;
                    g.fillRect(x, y, BLOCK_SIZE, BLOCK_SIZE);
                    
                    g.setColor(Color.BLACK);
                    g.drawRect(x, y, BLOCK_SIZE, BLOCK_SIZE);
                    g.setColor(color);
                }
            }
        }
    }
    
    /**
     * Gets the color for a block based on its ID
     */
    private Color getColorForBlock(int blockId) {
        // Map block IDs to colors using the same definitions as Tetromino.COLORS
        switch (blockId) {
            case 1: return Color.CYAN;    // I piece
            case 2: return Color.YELLOW;  // O piece
            case 3: return Color.MAGENTA; // T piece
            case 4: return Color.GREEN;   // S piece
            case 5: return Color.RED;     // Z piece
            case 6: return Color.BLUE;    // J piece
            case 7: return Color.ORANGE;  // L piece
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