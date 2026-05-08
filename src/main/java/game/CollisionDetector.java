package game;

/**
 * Handles collision detection logic
 */
public class CollisionDetector {
    
    /**
     * Checks if a tetromino collides with the board boundaries or other blocks
     * 
     * @param tetromino The tetromino to check
     * @param board The game board
     * @param offsetX Offset to apply to X position
     * @param offsetY Offset to apply to Y position
     * @return true if collision occurs, false otherwise
     */
    public static boolean checkCollision(Tetromino tetromino, Board board, int offsetX, int offsetY) {
        // Get the tetromino shape
        int[][] shape = tetromino.getShape();
        int tetrominoRow = tetromino.getRow();
        int tetrominoCol = tetromino.getCol();
        
        // Check all cells in the tetromino shape
        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[row].length; col++) {
                // Skip empty cells
                if (shape[row][col] == 0) {
                    continue;
                }
                
                // Calculate global position on board
                int boardRow = tetrominoRow + row + offsetY;
                int boardCol = tetrominoCol + col + offsetX;
                
                // Check boundary conditions
                if (boardRow >= 20 || boardCol < 0 || boardCol >= 10) {
                    return true; // Collision with walls or floor
                }
                
                // Check collision with existing blocks
                if (boardRow >= 0 && board.getGrid()[boardRow][boardCol] != 0) {
                    return true; // Collision with existing block
                }
            }
        }
        
        return false; // No collisions detected
    }
    
    /**
     * Checks if a tetromino can be placed at its current position
     * 
     * @param tetromino The tetromino to check
     * @param board The game board
     * @return true if placement is valid, false otherwise
     */
    public static boolean canPlace(Tetromino tetromino, Board board) {
        // Check collision with current board state
        return !checkCollision(tetromino, board, 0, 0);
    }
}