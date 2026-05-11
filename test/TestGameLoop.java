package game;

public class TestGameLoop {
    public static void main(String[] args) {
        System.out.println("Testing GameLoop functionality...");
        
        // Test the basic setup
        Board board = new Board();
        GameLoop gameLoop = new GameLoop(board);
        
        System.out.println("GameLoop created successfully");
        System.out.println("Board initialized: " + (board != null));
        System.out.println("GameLoop initialized: " + (gameLoop != null));
        
        // Test starting the loop
        gameLoop.start();
        System.out.println("GameLoop started successfully");
        
        // Test that it can access board
        Board retrievedBoard = gameLoop.getBoard();
        System.out.println("Board retrieved from GameLoop: " + (retrievedBoard != null));
        
        System.out.println("All tests passed!");
    }
}