package game;

import game.constants.GameConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import static game.constants.GameConstants.PANEL_WIDTH;

public class GamePanel extends JPanel {
    private static final int BOARD_WIDTH = GameConstants.BOARD_WIDTH;
    private static final int BOARD_HEIGHT = GameConstants.BOARD_HEIGHT;
    private static final int BLOCK_SIZE = GameConstants.BLOCK_SIZE;

    private Board board;
    private GameLoop gameLoop;
    private SidePanel sidePanel;

    // Double buffering for improved rendering
    private BufferedImage doubleBuffer;
    private Graphics2D bufferGraphics;

    // Fixed timestep variables
    private static final int TARGET_FPS = 60;
    private static final long FRAME_TIME = 1000 / TARGET_FPS; // milliseconds per frame

    public GamePanel(Board board) {
        this.board = board;

        // Initialize game loop
        this.gameLoop = new GameLoop(board);

        // Enable double buffering for smoother rendering
        setDoubleBuffered(false); // We'll handle it manually for better control

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

        // Enable focus for keyboard input
        setFocusable(true);
        requestFocusInWindow();

        // Initialize double buffering
        initializeDoubleBuffer();
    }

    /**
     * Initialize double buffering with BufferedImage
     */
    private void initializeDoubleBuffer() {
        doubleBuffer = new BufferedImage(
            BOARD_WIDTH * BLOCK_SIZE,
            BOARD_HEIGHT * BLOCK_SIZE,
            BufferedImage.TYPE_INT_ARGB
        );
        bufferGraphics = doubleBuffer.createGraphics();
        bufferGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        bufferGraphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Render to double buffer first
        renderToBuffer();

        // Draw double buffer to screen
        g.drawImage(doubleBuffer, 0, 0, null);
    }

    /**
     * Renders everything to the double buffer
     */
    private void renderToBuffer() {
        // Clear buffer with transparent background
        bufferGraphics.setComposite(AlphaComposite.Clear);
        bufferGraphics.fillRect(0, 0, doubleBuffer.getWidth(), doubleBuffer.getHeight());
        bufferGraphics.setComposite(AlphaComposite.Src);

        // Draw scene to buffer
        drawBoard(bufferGraphics);

        // Apply any additional effects here (anti-aliasing, filters, etc.)
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

        // Draw game over message if game is over
        if (board.isGameOver()) {
            drawGameOverMessage(g);
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
     * Draws the game over message centered on screen
     */
    private void drawGameOverMessage(Graphics g) {
        // Set up graphics for text rendering
        Graphics2D g2d = (Graphics2D) g.create();

        // Enable anti-aliasing for better text rendering
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Set text properties
        g2d.setColor(Color.RED); // Red text for high visibility
        Font font = new Font("Arial", Font.BOLD, 40); // Large bold font
        g2d.setFont(font);

        // Calculate text dimensions
        String message = "GAME OVER";
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(message);
        int textHeight = fm.getAscent();

        // Center the text on the screen - using panel width and height
        int x = (getWidth() - textWidth) / 2 - 40;
        int y = (getHeight() - textHeight) / 2 + textHeight;

        // Draw the text
        g2d.drawString(message, x, y);

        // Clean up
        g2d.dispose();
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