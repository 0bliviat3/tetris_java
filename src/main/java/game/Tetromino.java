package game;

import java.awt.*;

/**
 * SRS-ready Tetromino
 * - uses base shape only
 * - rotation handled mathematically
 */
public class Tetromino {

    // Types
    public static final int TYPE_I = 1;
    public static final int TYPE_O = 2;
    public static final int TYPE_T = 3;
    public static final int TYPE_S = 4;
    public static final int TYPE_Z = 5;
    public static final int TYPE_J = 6;
    public static final int TYPE_L = 7;

    /**
     * Base shapes (0° only)
     */
    private static final int[][][] SHAPES = {
            // I
            {{0,0,0,0},
                    {1,1,1,1},
                    {0,0,0,0},
                    {0,0,0,0}},

            // O
            {{1,1},
                    {1,1}},

            // T
            {{0,1,0},
                    {1,1,1},
                    {0,0,0}},

            // S
            {{0,1,1},
                    {1,1,0},
                    {0,0,0}},

            // Z
            {{1,1,0},
                    {0,1,1},
                    {0,0,0}},

            // J
            {{1,0,0},
                    {1,1,1},
                    {0,0,0}},

            // L
            {{0,0,1},
                    {1,1,1},
                    {0,0,0}}
    };

    /**
     * Colors
     */
    private static final Color[] COLORS = {
            Color.BLACK,
            Color.CYAN,
            Color.YELLOW,
            Color.MAGENTA,
            Color.GREEN,
            Color.RED,
            Color.BLUE,
            Color.ORANGE
    };

    private int type;
    private int row;
    private int col;
    private int rotation;

    public Tetromino() {
        this(TYPE_I, 0, 3);
    }

    public Tetromino(int type, int row, int col) {
        this.type = type;
        this.row = row;
        this.col = col;
        this.rotation = 0;
    }

    /**
     * SRS-style rotation (computed at runtime)
     */
    public int[][] getShape() {
        int[][] shape = SHAPES[type - 1];

        int[][] result = shape;

        for (int i = 0; i < rotation; i++) {
            result = rotateCW(result);
        }

        return result;
    }

    /**
     * 90-degree clockwise rotation
     */
    private int[][] rotateCW(int[][] shape) {

        int h = shape.length;
        int w = shape[0].length;

        int[][] result = new int[w][h];

        for (int r = 0; r < h; r++) {
            for (int c = 0; c < w; c++) {
                result[c][h - 1 - r] = shape[r][c];
            }
        }

        return result;
    }

    public void rotate() {
        rotation = (rotation + 1) % 4;
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = ((rotation % 4) + 4) % 4;
    }

    public int getType() {
        return type;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public Color getColor() {
        return COLORS[type];
    }

    public void move(int dr, int dc) {
        this.row += dr;
        this.col += dc;
    }
}