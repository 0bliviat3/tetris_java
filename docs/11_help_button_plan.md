# Help Button Implementation Plan

## Overview
Implement a help button in the side panel that displays game controls instructions when clicked, and properly manages focus for keyboard event handling.

## Requirements
- Add a "Help" button to the side panel
- Display a popup with game controls when button is clicked
- Popup should contain clear instructions on game controls
- Close popup and restore game focus when closed
- Ensure keyboard events continue to work properly after popup close

## Implementation Details

### 1. Help Button Component
- Position button at bottom of side panel
- Use standard button styling with contrasting color
- Add tooltip text "Show Controls Help"

### 2. Help Popup Window
- Create modal dialog window with game instructions
- Include all major controls:
  - Movement: Arrow keys (left/right/down)
  - Rotation: Up arrow key
  - Drop: Spacebar
  - Pause: P key
- Display in a readable font and size
- Ensure popup is centered on screen
- Add close button or allow closing with ESC key

### 3. Focus Management
- Properly handle focus restoration when popup closes
- Ensure GamePanel can receive keyboard events after popup close
- Maintain game state continuity

### 4. Integration Points
- SidePanel.java - Add help button and popup logic
- GamePanel.java - Handle focus management
- InputHandler.java - Process popup close events

## Technical Approach
1. Create HelpDialog class extending JDialog
2. Add JButton to SidePanel with click listener
3. Implement popup display with game instructions
4. Manage focus properly when popup opens/closes
5. Test keyboard event handling after popup close

## File Modifications
- `src/main/java/game/SidePanel.java` - Add help button and popup logic
- `src/main/java/game/HelpDialog.java` - New helper class for popup (if needed)

## Testing Considerations
- Verify popup displays correctly with all instructions
- Confirm popup closes properly when X button pressed
- Test ESC key closes popup
- Verify game continues to respond to keyboard controls after popup close
- Ensure focus returns to GamePanel