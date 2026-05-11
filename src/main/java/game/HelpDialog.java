package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class HelpDialog extends JDialog {
    
    public HelpDialog(JFrame parent) {
        super(parent, "Game Controls Help", true); // true = modal
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(parent);
        
        // Create and setup the help content
        setupHelpContent();
        
        // Add key listener for ESC key to close
        addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    dispose();
                }
            }
            
            @Override
            public void keyTyped(KeyEvent e) {}
            
            @Override
            public void keyReleased(KeyEvent e) {}
        });
        
        // Make dialog focusable
        setFocusable(true);
        requestFocusInWindow();
    }
    
    /**
     * Sets up the help content with game controls
     */
    private void setupHelpContent() {
        setLayout(new BorderLayout());
        
        // Create main panel for content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPanel.setBackground(new Color(30, 30, 30));
        
        // Title
        JLabel titleLabel = new JLabel("Game Controls");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Controls list
        String[] controls = {
            "Move Left:   ← Arrow Key",
            "Move Right:  → Arrow Key", 
            "Move Down:   ↓ Arrow Key",
            "Rotate:      ↑ Arrow Key",
            "Hard Drop:   Spacebar",
            "Pause Game:  P Key",
            "Close Help:  ESC Key"
        };
        
        for (String control : controls) {
            JLabel controlLabel = new JLabel(control);
            controlLabel.setForeground(Color.LIGHT_GRAY);
            controlLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            controlLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            contentPanel.add(controlLabel);
        }
        
        contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Close button
        JButton closeButton = new JButton("Close");
        closeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        closeButton.addActionListener(e -> dispose());
        contentPanel.add(closeButton);
        
        add(contentPanel, BorderLayout.CENTER);
        
        // Add a small padding at the bottom
        add(Box.createVerticalStrut(10), BorderLayout.SOUTH);
    }
}