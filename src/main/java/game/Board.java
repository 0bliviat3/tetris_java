package game;

import game.constants.GameConstants;

/**
 * Enhanced Board class with game logic implementation
 */
public class Board {
    private static final int BOARD_WIDTH = GameConstants.BOARD_WIDTH;
    private static final int BOARD_HEIGHT = GameConstants.BOARD_HEIGHT;
    
    // Game board grid - 0 represents empty cell
    private int[][] grid;
    
    // Current falling tetromino
    private Tetromino currentTetromino;
    
    // Next tetromino to display
    private Tetromino nextTetromino;
    
    // Game state
    private boolean isGameOver;
    private boolean isPaused;
    
    // Scores and level
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
        
        // Initialize with empty grid
        clearGrid();
        
        // Generate first tetromino
        generateNewTetromino();
    }
    
    /**
     * Clears the game grid
     */
    private void clearGrid() {
        for (int row = 0; row < BOARD_HEIGHT; row++) {
            for (int col = 0; col < BOARD_WIDTH; col++) {
                grid[row][col] = 0;
            }
        }
    }
    
    /**
     * Checks if the given position is valid (within bounds and empty)
     */
    public boolean isValidPosition(Tetromino tetromino, int offsetX, int offsetY) {
        int[][] shape = tetromino.getShape();
        int tetrominoRow = tetromino.getRow();
        int tetrominoCol = tetromino.getCol();
        
        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[row].length; col++) {
                if (shape[row][col] != 0) {
                    int boardRow = tetrominoRow + row + offsetY;
                    int boardCol = tetrominoCol + col + offsetX;
                    
                    // Check if position is within board boundaries
                    if (boardRow >= BOARD_HEIGHT || boardCol < 0 || boardCol >= BOARD_WIDTH) {
                        return false;
                    }
                    
                    // Check if position is occupied by other block
                    if (boardRow >= 0 && grid[boardRow][boardCol] != 0) {
                        return false;
                    }
                }
            }
        }
        
        return true;
    }
    
    /**
     * Places the current tetromino onto the board
     */
    public void placeTetromino() {
        if (currentTetromino == null) return;
        
        int[][] shape = currentTetromino.getShape();
        int tetrominoRow = currentTetromino.getRow();
        int tetrominoCol = currentTetromino.getCol();
        
        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[row].length; col++) {
                if (shape[row][col] != 0) {
                    int boardRow = tetrominoRow + row;
                    int boardCol = tetrominoCol + col;
                    
                    if (boardRow >= 0 && boardRow < BOARD_HEIGHT && 
                        boardCol >= 0 && boardCol < BOARD_WIDTH) {
                        grid[boardRow][boardCol] = currentTetromino.getType();
                    }
                }
            }
        }
    }
    
    /**
     * Clears completed lines and returns number of lines cleared
     */
    public int clearLines() {
        int linesCleared = 0;
        
        // Iterate from bottom to top to avoid index shifting issues
        for (int row = BOARD_HEIGHT - 1; row >= 0; row--) {
            boolean isLineComplete = true;
            
            // Check if line is complete (all cells filled)
            for (int col = 0; col < BOARD_WIDTH; col++) {
                if (grid[row][col] == 0) {
                    isLineComplete = false;
                    break;
                }
            }
            
            // If line is complete, remove it and shift all above lines down
            if (isLineComplete) {
                // Shift all rows above down by one position
                for (int r = row; r > 0; r--) {
                    System.arraycopy(grid[r - 1], 0, grid[r], 0, BOARD_WIDTH);
                }
                
                // Clear top row
                for (int col = 0; col < BOARD_WIDTH; col++) {
                    grid[0][col] = 0;
                }
                
                linesCleared++;
                row++; // Recheck same row since it's been shifted
            }
        }
        
        // Update score and level
        if (linesCleared > 0) {
            updateScore(linesCleared);
        }
        
        return linesCleared;
    }
    
    /**
     * Updates score based on lines cleared
     */
    private void updateScore(int linesCleared) {
        // Simple scoring system based on lines cleared
        int points = 0;
        switch(linesCleared) {
            case 1: points = 100; break;
            case 2: points = 300; break;
            case 3: points = 500; break;
            case 4: points = 800; break;
        }
        
        this.score += points;
        this.linesCleared += linesCleared;
        this.level = (this.linesCleared / GameConstants.LINES_PER_LEVEL) + 1;
    }
    
    /**
     * Generates a new random tetromino
     */
    public Tetromino generateRandomTetromino() {
        // Simple implementation - in practice, would use a proper random selection
        int type = 1 + (int)(Math.random() * 7); // Generate random type 1-7
        return new Tetromino(type, 0, 3); // Start at top center
    }
    
    /**
     * Moves the current tetromino left
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
     * Moves the current tetromino right
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
     * Moves the current tetromino down
     */
    public boolean moveDown() {
        if (currentTetromino == null) return false;
        if (isValidPosition(currentTetromino, 0, 1)) {
            currentTetromino.move(1, 0);
            return true;
        } else {
            // If we can't move down, place the tetromino
            placeTetromino();
            
            // Check for completed lines
            int linesCleared = clearLines();
            
            // Check if game is over (if new tetromino can't be placed)
            if (nextTetromino != null && !isValidPosition(nextTetromino, 0, 0)) {
                isGameOver = true;
                return false;
            }
            
            // Generate a new tetromino
            generateNewTetromino();
            return true;
        }
    }
    
    /**
     * Rotates the current tetromino
     */
    public boolean rotate() {
        if (currentTetromino == null) return false;
        
        // Save the original rotation
        int originalRotation = currentTetromino.getRotation();
        
        // Try rotating
        currentTetromino.rotate();
        
        // If rotation causes collision, revert
        if (!isValidPosition(currentTetromino, 0, 0)) {
            currentTetromino.setRotation(originalRotation);
            return false;
        }
        
        return true;
    }
    
    /**
     * Hard drops the tetromino to the bottom
     */
    public void hardDrop() {
        if (currentTetromino == null) return;
        
        // Keep moving down until we hit something
        while (moveDown()) {
            // Continue moving down
        }
    }
    
    /**
     * Generates a new tetromino (for the next piece)
     */
    public void generateNewTetromino() {
        // Set current tetromino to next tetromino
        if (nextTetromino != null) {
            currentTetromino = nextTetromino;
        } else {
            currentTetromino = generateRandomTetromino();
        }
        
        // Generate next tetromino
        nextTetromino = generateRandomTetromino();
        
        // Check if game is over (if the new tetromino can't be placed)
        if (!isValidPosition(currentTetromino, 0, 0)) {
            isGameOver = true;
        }
    }
    
    /**
     * Checks if game is over (when new tetromino can't be placed)
     */
    public boolean isGameOver() {
        return isGameOver;
    }
    
    /**
     * Sets game over state
     */
    public void setGameOver(boolean gameOver) {
        isGameOver = gameOver;
    }
    
    /**
     * Gets the game grid
     */
    public int[][] getGrid() {
        return grid;
    }
    
    /**
     * Gets current tetromino
     */
    public Tetromino getCurrentTetromino() {
        return currentTetromino;
    }
    
    /**
     * Sets current tetromino
     */
    public void setCurrentTetromino(Tetromino tetromino) {
        this.currentTetromino = tetromino;
    }
    
    /**
     * Gets next tetromino
     */
    public Tetromino getNextTetromino() {
        return nextTetromino;
    }
    
    /**
     * Sets next tetromino
     */
    public void setNextTetromino(Tetromino tetromino) {
        this.nextTetromino = tetromino;
    }
    
    /**
     * Gets game paused state
     */
    public boolean isPaused() {
        return isPaused;
    }
    
    /**
     * Sets game paused state
     */
    public void setPaused(boolean paused) {
        isPaused = paused;
    }
    
    /**
     * Gets current score
     */
    public int getScore() {
        return score;
    }
    
    /**
     * Gets current level
     */
    public int getLevel() {
        return level;
    }
    
    /**
     * Gets lines cleared
     */
    public int getLinesCleared() {
        return linesCleared;
    }
    
    /**
     * Gets the rotation of the current tetromino
     */
    public int getCurrentTetrominoRotation() {
        return currentTetromino != null ? currentTetromino.getRotation() : 0;
    }
    
    /**
     * Sets the rotation of the current tetromino (for testing)
     */
    public void setCurrentTetrominoRotation(int rotation) {
        if (currentTetromino != null) {
            currentTetromino.setRotation(rotation);
        }
    }
}