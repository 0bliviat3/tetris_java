# Fixing Color Consistency Between Side Panel Preview and Main Game Panel

## Issue Description

There is an inconsistency in tetromino colors between the side panel preview and the main game panel. The preview panel uses the tetromino's own `getColor()` method which correctly fetches colors from the `Tetromino.COLORS` array, but the main game panel uses a separate `getColorForBlock` method that has hardcoded color mappings that don't align with the official tetromino color definitions.

## Root Cause Analysis

1. **Side Panel Preview (`SidePanel.java`)**: 
   - Correctly uses `nextPiece.getColor()` which returns the color from the `Tetromino.COLORS` static array
   - This ensures preview matches actual tetromino colors

2. **Main Game Panel (`GamePanel.java`)**:
   - Has a `getColorForBlock` method that maps block IDs to colors using hardcoded values
   - The mapping order differs from `Tetromino.COLORS`
   - Example: Block ID 1 (I piece) is mapped to `Color.BLUE` instead of `Color.CYAN`

## Impact

Players see different colors for the same tetromino type in different parts of the UI, creating confusion and reducing visual consistency.

## Solution Plan

### Phase 1: Identify Color Mappings
First, clearly define and document the correct color mapping for each tetromino type:

| Tetromino Type | Type Number | Expected Color |
|----------------|-------------|----------------|
| I Piece        | 1           | CYAN           |
| O Piece        | 2           | YELLOW         |
| T Piece        | 3           | MAGENTA        |
| S Piece        | 4           | GREEN          |
| Z Piece        | 5           | RED            |
| J Piece        | 6           | BLUE           |
| L Piece        | 7           | ORANGE         |

### Phase 2: Refactor GamePanel.java
Replace the hardcoded `getColorForBlock` method with a consistent color lookup mechanism that uses the same color definitions as the Tetromino class.

### Phase 3: Update Color Mapping in GamePanel.java
Update the `getColorForBlock` method in `GamePanel.java` to reference `Tetromino.COLORS` directly instead of using hardcoded values.

### Phase 4: Testing and Validation
1. Verify that both preview and main game panel now use the same color definitions
2. Test that all seven tetromino types display correctly in both contexts
3. Run existing tests to ensure no regressions

## Implementation Steps

1. **Modify `GamePanel.java`**: Update `getColorForBlock` method to properly map block IDs to colors using the same color definitions as `Tetromino.java` 
2. **Test the Changes**: Verify that both preview and gameplay now show consistent colors
3. **Document the Fix**: Add any necessary documentation to explain the change

## Expected Outcomes

After implementing this fix:
- The preview of next tetromino in the side panel will have identical colors to the actual tetromino in the main game board
- Color consistency will be maintained across all parts of the UI
- Players will experience a more visually coherent game

## Files to Modify

1. `src/main/java/game/GamePanel.java` - Update `getColorForBlock` method
2. Potentially `src/main/java/game/Tetromino.java` - If we need to make color property accessible

## Risk Assessment

Low risk - the change should only affect visual presentation without altering game logic or mechanics.