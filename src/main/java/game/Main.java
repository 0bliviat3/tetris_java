package game;

import javax.swing.*;
import java.awt.*;

/**
 * Main entry point for the Tetris game
 */
public class Main {
    public static void main(String[] args) {
        // Set look and feel to system default
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeel());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Schedule GUI creation on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            createAndShowGUI();
        });
    }
    
    /**
     * Creates and shows the main game window
     */
    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Tetris");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        
        // Add game panel
        GamePanel gamePanel = new GamePanel();
        frame.add(gamePanel);
        
        // Pack and center the window
        frame.pack();
        frame.setLocationRelativeTo(null);
        
        // Make frame visible
        frame.setVisible(true);
    }
}