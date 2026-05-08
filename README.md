# Tetris Game - Windows Deployment Guide

This guide explains how to build and deploy the Tetris game on Windows using various IDEs and development environments.

## Prerequisites

- Java Development Kit (JDK) 17 or higher
- IDE (IntelliJ IDEA, Eclipse, or VS Code with Java extension)
- Gradle or Maven build tool (optional but recommended)

## Building and Running

### Method 1: Using IntelliJ IDEA

1. **Open Project**
   - Select "Open" in IntelliJ IDEA
   - Navigate to the project directory and select it

2. **Configure JDK**
   - Go to File → Project Structure
   - Under "SDKs", add JDK 17 if not already configured
   - Set project SDK to JDK 17

3. **Build and Run**
   ```bash
   # In IntelliJ IDEA Terminal
   ./gradlew build
   ./gradlew run
   ```

### Method 2: Using Eclipse

1. **Import Project**
   - Select "Import" → "General" → "Existing Projects into Workspace"
   - Browse to the project directory

2. **Configure Build Path**
   - Right-click project → Properties
   - Go to Java Build Path → Libraries
   - Add JDK 17 if not already added

3. **Create Run Configuration**
   - Right-click project → Run As → Java Application
   - Specify `Main.java` as the main class

### Method 3: Using VS Code

1. **Install Extensions**
   - Install "Java Extension Pack"
   - Install "Gradle for Java"

2. **Open Project**
   - Open VS Code in the project directory

3. **Build and Run**
   ```bash
   # In VS Code Terminal
   gradle build
   gradle run
   ```

## Manual Compilation (Command Line)

1. **Navigate to Project Root**
   ```bash
   cd path/to/tetris-project
   ```

2. **Compile Source Files**
   ```bash
   javac -d bin src/main/java/game/*.java src/main/java/game/constants/*.java
   ```

3. **Run the Application**
   ```bash
   java -cp bin game.Main
   ```

## Project Structure

```
tetris/
├── src/
│   └── main/
│       └── java/
│           └── game/
│               ├── Board.java          # Game board logic
│               ├── CollisionDetector.java  # Collision detection
│               ├── GameLoop.java       # Game timing and loop
│               ├── GamePanel.java      # GUI rendering and event handling
│               ├── InputHandler.java   # Keyboard input handling
│               ├── Main.java           # Entry point
│               ├── Tetromino.java      # Tetromino/Block definitions
│               └── constants/
│                   └── GameConstants.java  # Game configuration
└── build.gradle            # Build configuration
```

## Dependencies

The project uses only Java Standard Library and Swing/AWT components:
- `java.awt`
- `javax.swing`
- `java.util`

No external libraries are required.

## Running the Game

After compiling, run the application with:
```bash
java -cp bin game.Main
```

## Controls

- **Arrow Keys**: Move tetrominoes left/right/down
- **Up Arrow**: Rotate tetromino
- **Space**: Hard drop
- **P**: Pause game
- **R**: Restart game

## Build Configuration

### Using Gradle (Recommended)

```gradle
plugins {
    id 'java'
}

group = 'com.tetris'
version = '1.0-SNAPSHOT'

repositories {
    mavenCentral()
}

dependencies {
    implementation 'org.jetbrains:annotations:23.0.0'
    testImplementation 'junit:junit:4.13.2'
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
```

## Troubleshooting

### Common Issues:
1. **"javac is not recognized"** - Ensure JDK is properly installed and PATH is set
2. **"UnsupportedClassVersionError"** - Make sure you're using JDK 17 or higher
3. **GUI not displaying properly** - Verify Swing components are correctly integrated

### Solution for Missing Dependencies:
```bash
# If using Gradle
./gradlew dependencies

# If using Maven
mvn dependency:tree
```

## Packaging for Distribution

To create a jar file for distribution:
```bash
# Using Gradle
./gradlew jar

# Using Manual Compilation
jar cvfm Tetris.jar MANIFEST.MF -C bin .
```

## Notes

- Game uses a 10×20 board with 30px blocks
- Implements proper game loop with Swing Timer
- All tetrominoes have full rotation support
- Follows standard Tetris scoring and line clearing rules
- Supports game state management (paused, game over, restart)