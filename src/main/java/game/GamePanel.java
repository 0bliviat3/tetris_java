package game;

import game.constants.GameConstants;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionEvent;

/**
 * Main game panel that handles rendering and user interaction
 */
public class GamePanel extends JPanel {
    private static final int BOARD_WIDTH = GameConstants.BOARD_WIDTH;
    private static final int BOARD_HEIGHT = GameConstants.BOARD_HEIGHT;
    private static final int BLOCK_SIZE = GameConstants.BLOCK_SIZE;
    private static final int SIDE_PANEL_WIDTH = 180; // Increased from 150 to better accommodate all UI elements
    
    private final Timer gameTimer;
    private final InputHandler inputHandler;
    private final Board board;
    private final GameLoop gameLoop;
    
    public GamePanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(
            BOARD_WIDTH * BLOCK_SIZE + SIDE_PANEL_WIDTH, // Increased space for next piece preview and UI
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
        
        // Ensure we get focus on startup
        SwingUtilities.invokeLater(() -> {
            requestFocusInWindow();
        });
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Clear the panel to avoid residual rendering
        g.clearRect(0, 0, getWidth(), getHeight());
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
        
        // Draw next piece preview
        drawNextPiece(g);
        
        // Draw game info
        drawGameInfo(g);
        
        // Draw game over screen if game is over
        if (board.isGameOver()) {
            drawGameOver(g);
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Clear the panel to avoid residual rendering
        g.clearRect(0, 0, getWidth(), getHeight());
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
        
        // Draw next piece preview
        drawNextPiece(g);
        
        // Draw game info
        drawGameInfo(g);
        
        // Draw game over screen if game is over
        if (board.isGameOver()) {
            drawGameOver(g);
        }
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
        
        // Draw score and level
        g.drawString("Score: " + board.getScore(), 10, 40);
        g.drawString("Level: " + board.getLevel(), 10, 60);
        g.drawString("Lines: " + board.getLinesCleared(), 10, 80);
    }
    
    /**
     * Draws the next piece preview
     */
    private void drawNextPiece(Graphics g) {
        Tetromino nextTetromino = board.getNextTetromino();
        if (nextTetromino != null) {
            // Draw preview panel background
            g.setColor(Color.DARK_GRAY);
            g.fillRect(BOARD_WIDTH * BLOCK_SIZE + 10, 10, 140, 140);
            
            // Draw preview border
            g.setColor(Color.LIGHT_GRAY);
            g.drawRect(BOARD_WIDTH * BLOCK_SIZE + 10, 10, 140, 140);
            
            // Draw next piece
            g.setColor(Color.WHITE);
            g.drawString("Next:", BOARD_WIDTH * BLOCK_SIZE + 15, 25);
            
            // Get the shape and draw it
            int[][] shape = nextTetromino.getShape();
            Color color = nextTetromino.getColor();
            
            // Calculate dimensions needed for the preview
            int maxRows = shape.length;
            int maxCols = 0;
            for (int[] row : shape) {
                if (row.length > maxCols) {
                    maxCols = row.length;
                }
            }
            
            // Calculate centering offsets - improved version
            int previewWidth = 140;
            int previewHeight = 140;
            int blockWidth = BLOCK_SIZE;
            int blockHeight = BLOCK_SIZE;
            
            // Find min/max positions to determine actual bounding box
            int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;
            int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;
            
            // Find bounding box of the shape
            for (int row = 0; row < shape.length; row++) {
                for (int col = 0; col < shape[row].length; col++) {
                    if (shape[row][col] != 0) {
                        minX = Math.min(minX, col);
                        minY = Math.min(minY, row);
                        maxX = Math.max(maxX, col);
                        maxY = Math.max(maxY, row);
                    }
                }
            }
            
            // Calculate dimensions of actual shape
            int shapeWidth = maxX - minX + 1;
            int shapeHeight = maxY - minY + 1;
            
            // Calculate centering offsets
            int centerX = BOARD_WIDTH * BLOCK_SIZE + 10 + (previewWidth / 2) - ((shapeWidth * blockWidth) / 2);
            int centerY = 10 + (previewHeight / 2) - ((shapeHeight * blockHeight) / 2);
            
            // Offset by minimum position to center correctly
            centerX -= minX * blockWidth;
            centerY -= minY * blockHeight;
            
            // Draw the actual shape
            g.setColor(color);
            for (int row = 0; row < shape.length; row++) {
                for (int col = 0; col < shape[row].length; col++) {
                    if (shape[row][col] != 0) {
                        int x = centerX + (col * blockWidth);
                        int y = centerY + (row * blockHeight);
                        g.fillRect(x, y, blockWidth, blockHeight);
                        
                        // Draw block border
                        g.setColor(Color.DARK_GRAY);
                        g.drawRect(x, y, blockWidth, blockHeight);
                        g.setColor(color);
                    }
                }
            }
        }
    }
    
    /**
     * Draws game over screen
     */
    private void drawGameOver(Graphics g) {
        // Draw semi-transparent overlay
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, BOARD_WIDTH * BLOCK_SIZE, BOARD_HEIGHT * BLOCK_SIZE);
        
        // Draw game over text
        g.setColor(Color.RED);
        Font font = new Font("Arial", Font.BOLD, 32);
        g.setFont(font);
        g.drawString("GAME OVER", BOARD_WIDTH * BLOCK_SIZE / 2 - 100, BOARD_HEIGHT * BLOCK_SIZE / 2 - 30);
        
        // Draw score
        g.setColor(Color.WHITE);
        font = new Font("Arial", Font.BOLD, 20);
        g.setFont(font);
        g.drawString("Score: " + board.getScore(), BOARD_WIDTH * BLOCK_SIZE / 2 - 60, BOARD_HEIGHT * BLOCK_SIZE / 2 + 10);
        
        // Draw restart button
        g.setColor(Color.GREEN);
        g.drawString("Click Restart Button to Play Again", BOARD_WIDTH * BLOCK_SIZE / 2 - 130, BOARD_HEIGHT * BLOCK_SIZE / 2 + 50);
    }
    
    /**
     * Shows help dialog
     */
    private void showHelpDialog() {
        String helpText = """
            ← : Left Move
            → : Right Move
            ↓ : Soft Drop  
            ↑ : Rotate
            Space : Hard Drop
            P : Pause
            R : Restart""";
        
        JOptionPane.showMessageDialog(this, helpText, "Controls", JOptionPane.INFORMATION_MESSAGE);
        // Restore focus to GamePanel after help dialog is closed
        SwingUtilities.invokeLater(() -> {
            requestFocusInWindow();
        });
    }
    
    /**
     * Restarts the game completely
     */
    public void restartGame() {
        // Stop the game loop and timer
        gameLoop.stop();
        gameTimer.stop();
        
        // Reset the board completely
        board.reset();
        
        // Restart timers
        gameTimer.start();
        gameLoop.start();
        
        // Request focus to ensure keyboard input works
        requestFocusInWindow();
        
        // Force repaint to refresh UI
        repaint();
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