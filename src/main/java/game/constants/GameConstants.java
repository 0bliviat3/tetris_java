package game.constants;

/**
 * Game configuration constants
 */
public class GameConstants {
    // Board dimensions
    public static final int BOARD_WIDTH = 10;
    public static final int BOARD_HEIGHT = 20;
    
    // Block sizes
    public static final int BLOCK_SIZE = 30;
    
    // Game speeds (in milliseconds)
    public static final int INITIAL_DROP_SPEED = 800;
    public static final int SPEED_DECREMENT_PER_LEVEL = 100;
    public static final int MIN_DROP_SPEED = 100;
    
    // Level progression
    public static final int LINES_PER_LEVEL = 10;
    
    // Key mappings
    public static final int KEY_LEFT = java.awt.event.KeyEvent.VK_LEFT;
    public static final int KEY_RIGHT = java.awt.event.KeyEvent.VK_RIGHT;
    public static final int KEY_DOWN = java.awt.event.KeyEvent.VK_DOWN;
    public static final int KEY_UP = java.awt.event.KeyEvent.VK_UP;
    public static final int KEY_SPACE = java.awt.event.KeyEvent.VK_SPACE;
    public static final int KEY_P = java.awt.event.KeyEvent.VK_P;
    public static final int KEY_R = java.awt.event.KeyEvent.VK_R;
}