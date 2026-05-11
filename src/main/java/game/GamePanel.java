package game;

import game.constants.GameConstants;
import javax.swing.*;
import java.awt.*;

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
    private JButton helpButton;
    
    public GamePanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(
            BOARD_WIDTH * BLOCK_SIZE + SIDE_PANEL_WIDTH, // Increased space for next piece preview and UI
            BOARD_HEIGHT * BLOCK_SIZE
        ));
        setBackground(new Color(30, 30, 30)); // Darker background
        setFocusable(true);
        setOpaque(true);
        
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
        
        // Create and setup help button
        helpButton = new JButton("Help");
        helpButton.addActionListener(e -> showHelpDialog());
        helpButton.setFocusable(false);
        helpButton.setPreferredSize(new Dimension(SIDE_PANEL_WIDTH - 20, 30));
        helpButton.setBackground(Color.DARK_GRAY);
        helpButton.setForeground(Color.BLACK);
        
        // Add help button to a side panel at bottom using BorderLayout for proper positioning
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BorderLayout());
        sidePanel.setBackground(new Color(30, 30, 30));
        sidePanel.setPreferredSize(new Dimension(SIDE_PANEL_WIDTH, BOARD_HEIGHT * BLOCK_SIZE));
        sidePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        // Create bottom panel for help button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(new Color(30, 30, 30));
        bottomPanel.add(helpButton);
        
        // Add help button panel to the bottom of the side panel
        sidePanel.add(bottomPanel, BorderLayout.SOUTH);
        
        // Add side panel to the right side
        add(sidePanel, BorderLayout.EAST);
        
        // Set opaque false to prevent conflicts with custom painting
        setOpaque(true);
        
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
        
        // Draw next piece preview
        drawNextPiece(g);
        
        // Draw game info at top-left
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
     * Draws game information (score, level, etc.) at top-left
     */
    private void drawGameInfo(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 12));
        g.drawString("TETRIS", 10, 20);
        
        // Draw score and level
        g.drawString("Score: " + board.getScore(), 10, 40);
        g.drawString("Level: " + board.getLevel(), 10, 60);
        g.drawString("Lines: " + board.getLinesCleared(), 10, 80);
    }
    
    /**
     * Draws the next piece preview in the side panel
     */
    private void drawNextPiece(Graphics g) {
        Tetromino nextTetromino = board.getNextTetromino();
        if (nextTetromino != null) {
            // Draw preview panel background (make sure this area is not covered by swing components)
            g.setColor(new Color(30, 30, 30));
            g.fillRect(BOARD_WIDTH * BLOCK_SIZE + 10, 100, 140, 60);
            
            // Draw preview border
            g.setColor(Color.GRAY);
            g.drawRect(BOARD_WIDTH * BLOCK_SIZE + 10, 100, 140, 60);
            
            // Draw next piece label
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 12));
            g.drawString("Next:", BOARD_WIDTH * BLOCK_SIZE + 15, 115);
            
            // Draw next piece centered in preview area
            int[][] shape = nextTetromino.getShape();
            Color color = nextTetromino.getColor();
            
            // Calculate centering for preview
            int previewX = BOARD_WIDTH * BLOCK_SIZE + 10;
            int previewY = 115;
            
            // Draw the piece using its shape
            for (int row = 0; row < shape.length; row++) {
                for (int col = 0; col < shape[row].length; col++) {
                    if (shape[row][col] != 0) {
                        // Adjust coordinates for preview area - ensure it's within bounds
                        int x = previewX + 30 + col * BLOCK_SIZE;
                        int y = previewY - 15 + row * BLOCK_SIZE;
                        
                        // Draw block
                        g.setColor(color);
                        g.fillRect(x, y, BLOCK_SIZE, BLOCK_SIZE);
                        
                        // Draw block outline
                        g.setColor(Color.WHITE);
                        g.drawRect(x, y, BLOCK_SIZE, BLOCK_SIZE);
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
        g.drawString("Press [R] to Play Again", BOARD_WIDTH * BLOCK_SIZE / 2 - 100, BOARD_HEIGHT * BLOCK_SIZE / 2 + 50);
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