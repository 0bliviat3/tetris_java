package game;

import game.constants.GameConstants;

/**
 * Stable Board implementation (fixed tetromino handling & positioning)
 */
public class Board {

    private static final int BOARD_WIDTH = GameConstants.BOARD_WIDTH;
    private static final int BOARD_HEIGHT = GameConstants.BOARD_HEIGHT;

    private int[][] grid;

    private Tetromino currentTetromino;
    private Tetromino nextTetromino;

    private boolean isGameOver;
    private boolean isPaused;

    private int score;
    private int level;
    private int linesCleared;

    public Board() {
        grid = new int[BOARD_HEIGHT][BOARD_WIDTH];

        isGameOver = false;
        isPaused = false;
        score = 0;
        level = 1;
        linesCleared = 0;

        clearGrid();
        generateNewTetromino();
    }

    private void clearGrid() {
        for (int r = 0; r < BOARD_HEIGHT; r++) {
            for (int c = 0; c < BOARD_WIDTH; c++) {
                grid[r][c] = 0;
            }
        }
    }

    /**
     * Validate position using current shape
     */
    public boolean isValidPosition(Tetromino t, int offsetX, int offsetY) {

        int[][] shape = t.getShape();
        int baseRow = t.getRow();
        int baseCol = t.getCol();

        for (int r = 0; r < shape.length; r++) {
            for (int c = 0; c < shape[r].length; c++) {

                if (shape[r][c] == 0) continue;

                int newRow = baseRow + r + offsetY;
                int newCol = baseCol + c + offsetX;

                if (newRow < 0 || newRow >= BOARD_HEIGHT ||
                        newCol < 0 || newCol >= BOARD_WIDTH) {
                    return false;
                }

                if (grid[newRow][newCol] != 0) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Move left
     */
    public boolean moveLeft() {
        if (currentTetromino == null) return false;

        if (isValidPosition(currentTetromino, -1, 0)) {
            currentTetromino.move(0, -1);
            return true;
        }
        return false;
    }

    /**
     * Move right
     */
    public boolean moveRight() {
        if (currentTetromino == null) return false;

        if (isValidPosition(currentTetromino, 1, 0)) {
            currentTetromino.move(0, 1);
            return true;
        }
        return false;
    }

    /**
     * Move down (fixed timing-safe logic)
     */
    public boolean moveDown() {
        if (currentTetromino == null) return false;

        if (isValidPosition(currentTetromino, 0, 1)) {
            currentTetromino.move(1, 0);
            return true;
        }

        placeTetromino();
        clearLines();

        generateNewTetromino();

        return true;
    }

    /**
     * Rotate with rollback safety
     */
    public boolean rotate() {

        if (currentTetromino == null) return false;

        int originalRotation = currentTetromino.getRotation();

        currentTetromino.rotate();

        // 1. 먼저 원래 자리에서 검사
        if (isValidPosition(currentTetromino, 0, 0)) {
            return true;
        }

        // 2. SRS simple wall kick
        int[][] kicks = {
                {0, 0},
                {-1, 0},
                {1, 0},
                {0, -1},
                {-2, 0},
                {2, 0}
        };

        for (int[] kick : kicks) {

            int dx = kick[1];
            int dy = kick[0];

            if (isValidPosition(currentTetromino, dx, dy)) {
                currentTetromino.move(dy, dx);
                return true;
            }
        }

        // 3. 실패 → 롤백
        currentTetromino.setRotation(originalRotation);
        return false;
    }

    /**
     * Hard drop
     */
    public void hardDrop() {
        if (currentTetromino == null) return;

        while (isValidPosition(currentTetromino, 0, 1)) {
            currentTetromino.move(1, 0);
        }

        placeTetromino();
        clearLines();
        generateNewTetromino();
    }

    /**
     * Place piece into grid
     */
    public void placeTetromino() {
        if (currentTetromino == null) return;

        int[][] shape = currentTetromino.getShape();
        int row = currentTetromino.getRow();
        int col = currentTetromino.getCol();

        for (int r = 0; r < shape.length; r++) {
            for (int c = 0; c < shape[r].length; c++) {

                if (shape[r][c] == 0) continue;

                int gr = row + r;
                int gc = col + c;

                if (gr >= 0 && gr < BOARD_HEIGHT &&
                        gc >= 0 && gc < BOARD_WIDTH) {

                    grid[gr][gc] = currentTetromino.getType();
                }
            }
        }
    }

    /**
     * Clear full lines
     */
    public int clearLines() {

        int cleared = 0;

        for (int r = BOARD_HEIGHT - 1; r >= 0; r--) {

            boolean full = true;

            for (int c = 0; c < BOARD_WIDTH; c++) {
                if (grid[r][c] == 0) {
                    full = false;
                    break;
                }
            }

            if (full) {

                for (int i = r; i > 0; i--) {
                    System.arraycopy(grid[i - 1], 0, grid[i], 0, BOARD_WIDTH);
                }

                for (int c = 0; c < BOARD_WIDTH; c++) {
                    grid[0][c] = 0;
                }

                cleared++;
                r++;
            }
        }

        if (cleared > 0) {
            updateScore(cleared);
        }

        return cleared;
    }

    private void updateScore(int n) {

        int points = switch (n) {
            case 1 -> 100;
            case 2 -> 300;
            case 3 -> 500;
            case 4 -> 800;
            default -> 0;
        };

        score += points;
        linesCleared += n;
        level = (linesCleared / GameConstants.LINES_PER_LEVEL) + 1;
    }

    /**
     * Spawn new piece (FIXED CENTER SPAWN)
     */
    public void generateNewTetromino() {

        if (nextTetromino != null) {
            currentTetromino = nextTetromino;
        } else {
            currentTetromino = generateRandomTetromino();
        }

        // FIX: proper center spawn (prevents shape mismatch illusion)
        currentTetromino.setRow(0);
        currentTetromino.setCol(BOARD_WIDTH / 2 - 2);

        nextTetromino = generateRandomTetromino();

        if (!isValidPosition(currentTetromino, 0, 0)) {
            isGameOver = true;
        }
    }

    /**
     * Random piece
     */
    public Tetromino generateRandomTetromino() {
        int type = 1 + (int)(Math.random() * 7);
        return new Tetromino(type, 0, BOARD_WIDTH / 2 - 2);
    }

    public boolean isGameOver() { return isGameOver; }
    public boolean isPaused() { return isPaused; }
    public void setPaused(boolean p) { isPaused = p; }

    public int[][] getGrid() { return grid; }
    public Tetromino getCurrentTetromino() { return currentTetromino; }
    public Tetromino getNextTetromino() { return nextTetromino; }

    public int getScore() { return score; }
    public int getLevel() { return level; }
    public int getLinesCleared() { return linesCleared; }

    public void reset() {
        clearGrid();
        isGameOver = false;
        score = 0;
        level = 1;
        linesCleared = 0;
        currentTetromino = null;
        nextTetromino = null;
        generateNewTetromino();
    }
}