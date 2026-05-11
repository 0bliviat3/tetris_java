package game;

import javax.swing.*;
import java.awt.*;

/**
 * Side panel for rendering game UI elements
 * This class handles all UI rendering separately from GamePanel
 * to prevent z-order conflicts between custom graphics and Swing components
 */
public class SidePanel extends JPanel {
    private final Board board;
    private static final int SIDE_PANEL_WIDTH = 180;
    
    public SidePanel(Board board) {
        this.board = board;
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(SIDE_PANEL_WIDTH, 0)); // Will be set by parent
        setBackground(new Color(30, 30, 30));
        setOpaque(true);
        
        // Create bottom panel for help button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(new Color(30, 30, 30));
        
        JButton helpButton = new JButton("Help");
        helpButton.addActionListener(e -> showHelpDialog());
        helpButton.setFocusable(false);
        helpButton.setPreferredSize(new Dimension(SIDE_PANEL_WIDTH - 20, 30));
        helpButton.setBackground(Color.DARK_GRAY);
        helpButton.setForeground(Color.WHITE);
        
        bottomPanel.add(helpButton);
        
        // Add help button panel to the bottom
        add(bottomPanel, BorderLayout.SOUTH);
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
        
        // Draw preview grid lines
        g.setColor(new Color(70, 70, 70));
        for (int x = 0; x <= 4; x++) {
            g.drawLine(previewX + x * 20, previewY, previewX + x * 20, previewY + 80);
        }
        for (int y = 0; y <= 4; y++) {
            g.drawLine(previewX, previewY + y * 20, previewX + 80, previewY + y * 20);
        }
        
        // Draw the preview tetromino blocks
        g.setColor(nextPiece.getColor());
        for (int[] pos : nextPiece.getShape()) {
            int x = previewX + (pos[0] + 1) * 20;
            int y = previewY + (pos[1] + 1) * 20;
            g.fillRect(x, y, 20, 20);
        }
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
}