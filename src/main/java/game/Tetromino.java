package game;

import game.constants.GameConstants;

import java.awt.*;
import java.util.Arrays;

/**
 * Represents a Tetromino (game block) with its shape, color, and position
 */
public class Tetromino {
    // Tetromino types
    public static final int TYPE_I = 1;
    public static final int TYPE_O = 2;
    public static final int TYPE_T = 3;
    public static final int TYPE_S = 4;
    public static final int TYPE_Z = 5;
    public static final int TYPE_J = 6;
    public static final int TYPE_L = 7;
    
    // Shape definitions for each tetromino type
    private static final int[][][] SHAPES = {
        // I shape
        {
            {0, 0, 0, 0},
            {1, 1, 1, 1},
            {0, 0, 0, 0},
            {0, 0, 0, 0}
        },
        // O shape
        {
            {1, 1},
            {1, 1}
        },
        // T shape
        {
            {0, 1, 0},
            {1, 1, 1},
            {0, 0, 0}
        },
        // S shape
        {
            {0, 1, 1},
            {1, 1, 0},
            {0, 0, 0}
        },
        // Z shape
        {
            {1, 1, 0},
            {0, 1, 1},
            {0, 0, 0}
        },
        // J shape
        {
            {1, 0, 0},
            {1, 1, 1},
            {0, 0, 0}
        },
        // L shape
        {
            {0, 0, 1},
            {1, 1, 1},
            {0, 0, 0}
        }
    };
    
    // Rotated shapes for each tetromino type (for 4 rotations)
    private static final int[][][][] ROTATED_SHAPES = {
        // I shape rotations
        {
            {{0, 0, 0, 0}, {1, 1, 1, 1}, {0, 0, 0, 0}, {0, 0, 0, 0}},  // 0°
            {{0, 0, 1, 0}, {0, 0, 1, 0}, {0, 0, 1, 0}, {0, 0, 1, 0}},  // 90°
            {{0, 0, 0, 0}, {0, 0, 0, 0}, {1, 1, 1, 1}, {0, 0, 0, 0}},  // 180°
            {{0, 1, 0, 0}, {0, 1, 0, 0}, {0, 1, 0, 0}, {0, 1, 0, 0}}   // 270°
        },
        // O shape - same in all rotations
        {
            {{1, 1}, {1, 1}},  // All rotations the same
            {{1, 1}, {1, 1}},
            {{1, 1}, {1, 1}},
            {{1, 1}, {1, 1}}
        },
        // T shape rotations
        {
            {{0, 1, 0}, {1, 1, 1}, {0, 0, 0}},  // 0°
            {{0, 1, 0}, {0, 1, 1}, {0, 1, 0}},  // 90°
            {{0, 0, 0}, {1, 1, 1}, {0, 1, 0}},  // 180°
            {{0, 1, 0}, {1, 1, 0}, {0, 1, 0}}   // 270°
        },
        // S shape rotations
        {
            {{0, 1, 1}, {1, 1, 0}, {0, 0, 0}},  // 0°
            {{0, 1, 0}, {0, 1, 1}, {0, 0, 1}},  // 90°
            {{0, 0, 0}, {0, 1, 1}, {1, 1, 0}},  // 180°
            {{1, 0, 0}, {1, 1, 0}, {0, 1, 0}}   // 270°
        },
        // Z shape rotations
        {
            {{1, 1, 0}, {0, 1, 1}, {0, 0, 0}},  // 0°
            {{0, 0, 1}, {0, 1, 1}, {0, 1, 0}},  // 90°
            {{0, 0, 0}, {1, 1, 0}, {1, 1, 0}},  // 180°
            {{0, 1, 0}, {1, 1, 0}, {1, 0, 0}}   // 270°
        },
        // J shape rotations
        {
            {{1, 0, 0}, {1, 1, 1}, {0, 0, 0}},  // 0°
            {{0, 1, 1}, {0, 1, 0}, {0, 1, 0}},  // 90°
            {{0, 0, 0}, {1, 1, 1}, {0, 0, 1}},  // 180°
            {{0, 1, 0}, {0, 1, 0}, {1, 1, 0}}   // 270°
        },
        // L shape rotations
        {
            {{0, 0, 1}, {1, 1, 1}, {0, 0, 0}},  // 0°
            {{0, 1, 0}, {0, 1, 0}, {0, 1, 1}},  // 90°
            {{0, 0, 0}, {1, 1, 1}, {1, 0, 0}},  // 180°
            {{1, 1, 0}, {1, 0, 0}, {1, 0, 0}}   // 270°
        }
    };
    
    // Colors for each tetromino type
    private static final Color[] COLORS = {
        Color.BLACK,   // Placeholder for index 0
        Color.CYAN,    // I
        Color.YELLOW,  // O
        Color.MAGENTA, // T
        Color.GREEN,   // S
        Color.RED,     // Z
        Color.BLUE,    // J
        Color.ORANGE   // L
    };
    
    private int[][] shape;
    private int row;
    private int col;
    private int type;
    private int rotation;
    
    public Tetromino() {
        // For now, create a default tetromino
        this.type = TYPE_I;  // Default to I piece
        this.shape = SHAPES[TYPE_I - 1];  // Adjust for 0-based indexing
        this.row = 0;
        this.col = 3;  // Start near center
        this.rotation = 0;
    }
    
    public Tetromino(int type, int row, int col) {
        this.type = type;
        this.shape = SHAPES[type - 1];  // Adjust for 0-based indexing
        this.row = row;
        this.col = col;
        this.rotation = 0;
    }
    
    /**
     * Gets the current shape of the tetromino (accounting for rotation)
     */
    public int[][] getShape() {
        // Return rotated shape based on rotation state
        return ROTATED_SHAPES[type - 1][rotation % 4];
    }
    
    /**
     * Gets the type of this tetromino
     */
    public int getType() {
        return type;
    }
    
    /**
     * Gets the row position
     */
    public int getRow() {
        return row;
    }
    
    /**
     * Sets the row position
     */
    public void setRow(int row) {
        this.row = row;
    }
    
    /**
     * Gets the column position
     */
    public int getCol() {
        return col;
    }
    
    /**
     * Sets the column position
     */
    public void setCol(int col) {
        this.col = col;
    }
    
    /**
     * Gets the color of this tetromino
     */
    public Color getColor() {
        return COLORS[type];
    }
    
    /**
     * Rotates the tetromino 90 degrees clockwise
     */
    public void rotate() {
        this.rotation = (this.rotation + 1) % 4;
    }
    
    /**
     * Gets the rotation state of this tetromino
     */
    public int getRotation() {
        return rotation;
    }
    
    /**
     * Sets the rotation state of this tetromino
     */
    public void setRotation(int rotation) {
        this.rotation = rotation;
    }
    
    /**
     * Moves the tetromino by the given offset
     */
    public void move(int rowOffset, int colOffset) {
        this.row += rowOffset;
        this.col += colOffset;
    }
}