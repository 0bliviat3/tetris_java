package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import game.Board;
import game.Tetromino;

import static game.constants.GameConstants.PANEL_HEIGHT;
import static game.constants.GameConstants.PANEL_WIDTH;

public class SidePanel extends JPanel {
    
    private Board board;
    private JButton helpButton;
    
    public SidePanel(Board board) {
        this.board = board;
        
        // Set preferred size
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        
        // Set background color
        setBackground(new Color(30, 30, 30));
        
        // Initialize help button
        initializeHelpButton();
    }
    
    /**
     * Initializes the help button
     */
    private void initializeHelpButton() {
        helpButton = new JButton("Help");
        helpButton.setPreferredSize(new Dimension(80, 30));
        helpButton.setToolTipText("Show Controls Help");
        helpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showHelpPopup();
            }
        });
        
        // Add help button to panel
        setLayout(new BorderLayout());
        add(helpButton, BorderLayout.SOUTH);
    }
    
    /**
     * Shows the help popup with game controls
     */
    private void showHelpPopup() {
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        HelpDialog helpDialog = new HelpDialog(parentFrame);
        helpDialog.setVisible(true);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Draw score and level information
        drawGameInfo(g);
        
        // Draw next piece preview
        drawNextPiece(g);
    }
    
    /**
     * Draws game info (score, level) on the side panel
     */
    private void drawGameInfo(Graphics g) {
        // Get current score and level from the board
        int score = board.getScore();
        int level = board.getLevel();
        
        // Set font and color for text rendering
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.setColor(Color.WHITE);
        
        // Draw score label and value
        g.drawString("Score: " + score, 10, 30);
        
        // Draw level label and value
        g.drawString("Level: " + level, 10, 50);
    }
    
    /**
     * Draws the preview piece in the side panel
     */
    private void drawNextPiece(Graphics g) {
        // Get next tetromino from board
        Tetromino nextPiece = board.getNextTetromino();
        
        if (nextPiece == null) return;
        
        // Calculate position for preview
        int previewX = 10;
        int previewY = 70;
        
        // Draw preview box
        g.setColor(new Color(50, 50, 50));
        g.fillRect(previewX - 2, previewY - 2, 100, 100);
        
        // Get the shape of the next piece
        int[][] shape = nextPiece.getShape();
        
        // Calculate dimensions of the shape
        int shapeHeight = shape.length;
        int shapeWidth = 0;
        for (int[] row : shape) {
            if (row.length > shapeWidth) {
                shapeWidth = row.length;
            }
        }
        
        // Calculate center offset for perfect centering
        int shapePixelWidth = shapeWidth * 20;
        int shapePixelHeight = shapeHeight * 20;
        int offsetX = (100 - shapePixelWidth) / 2;
        int offsetY = (100 - shapePixelHeight) / 2;
        
        // Draw the preview tetromino blocks with proper alignment
        Color pieceColor = nextPiece.getColor();
        g.setColor(pieceColor);
        
        // Draw each block in the shape
        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[row].length; col++) {
                if (shape[row][col] != 0) {
                    int x = previewX + offsetX + (col * 20);
                    int y = previewY + offsetY + (row * 20);
                    g.fillRect(x, y, 20, 20);
                    
                    // Draw block border
                    g.setColor(Color.BLACK);
                    g.drawRect(x, y, 20, 20);
                    
                    // Restore original color
                    g.setColor(pieceColor);
                }
            }
        }
    }
}