package game;

import game.constants.GameConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
    private SidePanel sidePanel;
    
    public GamePanel() {
        // Initialize the game board
        board = new Board();
        inputHandler = new InputHandler(this);
        gameLoop = new GameLoop(board);
        
        // Set up the main panel
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(
            BOARD_WIDTH * BLOCK_SIZE + SIDE_PANEL_WIDTH,
            BOARD_HEIGHT * BLOCK_SIZE
        ));
        setBackground(new Color(30, 30, 30));
        setFocusable(true);
        setOpaque(true);
        
        // Add focus listener to ensure GamePanel gets focus
        addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                // Ensure GamePanel gets focus when needed
                if (!GamePanel.this.hasFocus()) {
                    GamePanel.this.requestFocusInWindow();
                }
            }
        });
        
        // Initialize game timer for rendering
        gameTimer = new Timer(16, e -> repaint()); // ~60 FPS
        gameTimer.start();
        
        // Start the game loop
        gameLoop.start();
        
        // Create dedicated side panel for UI elements
        sidePanel = new SidePanel(board);
        add(sidePanel, BorderLayout.EAST);
        
        // Add key bindings and ensure proper focus
        setFocusTraversalKeysEnabled(false);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Clear the panel to avoid residual rendering
        g.clearRect(0, 0, getWidth(), getHeight());
        // Draw the game board (excluding UI elements)
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
        
        // Draw current tetromino if exists
        drawCurrentTetromino(g);
        
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
                    g.fillRect(col * BLOCK_SIZE, row * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                    
                    // Draw block border
                    g.setColor(Color.BLACK);
                    g.drawRect(col * BLOCK_SIZE, row * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                }
            }
        }
    }
    
    /**
     * Gets the color for a given block type
     */
    private Color getBlockColor(int blockType) {
        switch (blockType) {
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
     * Draws the current falling tetromino
     */
    private void drawCurrentTetromino(Graphics g) {
        // Get current tetromino from board
        Tetromino current = board.getCurrentTetromino();
        if (current == null) return;
        
        // Draw the tetromino blocks
        g.setColor(current.getColor());
        int tetrominoRow = current.getRow();
        int tetrominoCol = current.getCol();
        for (int[] pos : current.getShape()) {
            int x = (pos[1] + tetrominoCol) * BLOCK_SIZE;
            int y = (pos[0] + tetrominoRow) * BLOCK_SIZE;
            g.fillRect(x, y, BLOCK_SIZE, BLOCK_SIZE);
            
            // Draw block border
            g.setColor(Color.BLACK);
            g.drawRect(x, y, BLOCK_SIZE, BLOCK_SIZE);
        }
    }
    
    /**
     * Draws the game over screen
     */
    private void drawGameOver(Graphics g) {
        // Draw semi-transparent overlay
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, BOARD_WIDTH * BLOCK_SIZE, BOARD_HEIGHT * BLOCK_SIZE);
        
        // Draw game over text
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        FontMetrics fm = g.getFontMetrics();
        String gameOverText = "GAME OVER";
        int x = (BOARD_WIDTH * BLOCK_SIZE - fm.stringWidth(gameOverText)) / 2;
        int y = (BOARD_HEIGHT * BLOCK_SIZE + fm.getAscent()) / 2;
        g.drawString(gameOverText, x, y);
    }
    
    /**
     * Shows the help dialog
     */
    private void showHelpDialog() {
        JOptionPane.showMessageDialog(this,
            "Tetris Game Help:\n" +
            "- Arrow keys: Move tetromino\n" +
            "- Space: Hard drop\n" +
            "- P: Pause game\n" +
            "- R: Restart game",
            "Help",
            JOptionPane.INFORMATION_MESSAGE);
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