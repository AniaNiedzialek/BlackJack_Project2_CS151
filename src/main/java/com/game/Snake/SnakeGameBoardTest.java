package com.game.Snake;

import com.game.DirectionType;
import com.game.GameManagerUI;
import com.game.SessionManager;
import com.game.ToolBarUI;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/*
 * Entry point for the Snake game. Handles menus, game start, pause, and exit functionality.
 */
public class SnakeGameBoardTest extends Application {
    private SnakeGameBoard gameBoard; // Main game board
    private VBox pauseMenu;           // Pause menu layout
    private VBox mainMenu;            // Main menu layout

    private static final double WIDTH = 800;  // Game width
    private static final double HEIGHT = 600; // Game height

            // Button styling with hover effects
            String buttonStyle = """
                -fx-background-color: #1a472a; 
                -fx-text-fill: #98ff98;
                -fx-font-size: 16px;
                -fx-font-weight: bold;
                -fx-min-width: 200px;
                -fx-min-height: 45px;
                -fx-background-radius: 5;
                -fx-border-radius: 5;
                -fx-border-color: #2ecc71;
                -fx-border-width: 2;
                -fx-cursor: hand;
            """;
    
            String buttonHoverStyle = """
                -fx-background-color: #2ecc71;
                -fx-text-fill: #ffffff;
                -fx-font-size: 16px;
                -fx-font-weight: bold;
                -fx-min-width: 200px;
                -fx-min-height: 45px;
                -fx-background-radius: 5;
                -fx-border-radius: 5;
                -fx-border-color: #1a472a;
                -fx-border-width: 2;
                -fx-cursor: hand;
            """;
    

    @Override
    public void start(Stage primaryStage) {
        // Initialize the game board first
        gameBoard = new SnakeGameBoard(WIDTH, HEIGHT);

        // Create main container with toolbar
        VBox mainContainer = new VBox();
        ToolBarUI toolBarUI = new ToolBarUI();
        toolBarUI.setStyle("-fx-background-color: #2C2C3E;");

        // Set up main menu button action
        toolBarUI.getMenuButton().setOnAction(e -> {
            gameBoard.getSnakeController().stopGame();
            primaryStage.close();
            GameManagerUI gameManager = new GameManagerUI();
            gameManager.showMainApp(primaryStage);
        });

        // Add toolbar and game board to container
        mainContainer.getChildren().addAll(toolBarUI, gameBoard);

        // Create scene with the main container
        Scene scene = new Scene(mainContainer);
        
        // Configure main menu callback
        gameBoard.getSnakeController().setOnMainMenuRequest(this::returnToMainMenu);

        // Initialize menus
        mainMenu = createMainMenu();
        pauseMenu = createPauseMenu();

        // Display main menu initially
        mainMenu.setVisible(true);
        pauseMenu.setVisible(false);

        // Apply styles to menus
        String menuStyle = "-fx-background-color: linear-gradient(to bottom, #1a1a2e, #16213e);";
        mainMenu.setStyle(menuStyle);
        pauseMenu.setStyle(menuStyle);

        // Add menus to the game board
        gameBoard.getChildren().addAll(mainMenu, pauseMenu);

        // Handle key events for game controls
        scene.setOnKeyPressed(event -> {
            if (mainMenu.isVisible()) return; // Ignore keys if main menu is visible
            if (pauseMenu.isVisible() && event.getCode() != javafx.scene.input.KeyCode.ESCAPE) return;

            switch (event.getCode()) {
                case ESCAPE:
                    if (gameBoard.getSnakeController().isRunning()) {
                        pauseGame();
                    } else if (pauseMenu.isVisible()) {
                        resumeGame();
                    }
                    break;
                case UP:
                    gameBoard.getSnakeController().updateSnakeDirection(DirectionType.UP);
                    event.consume(); // Prevent event bubbling
                    break;
                case DOWN:
                    gameBoard.getSnakeController().updateSnakeDirection(DirectionType.DOWN);
                    event.consume();
                    break;
                case LEFT:
                    gameBoard.getSnakeController().updateSnakeDirection(DirectionType.LEFT);
                    event.consume(); 
                    break;
                case RIGHT:
                    gameBoard.getSnakeController().updateSnakeDirection(DirectionType.RIGHT);
                    event.consume();
                    break;
                default:
                    break;
            }
        });

        // Set up the stage
        primaryStage.setTitle("Snake Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Group createAnimatedSnakeTitle() {
        String text = "SNAKE GAME";
        Group titleGroup = new Group();
        double x = 0;
        
        for (int i = 0; i < text.length(); i++) {
            Text letter = new Text(String.valueOf(text.charAt(i)));
            letter.setFont(Font.font("Arial", FontWeight.BOLD, 48));
            letter.setFill(Color.rgb(50, 205, 50));
            
            // Initial wave effect
            double angle = Math.sin(i * 0.5) * 15;
            double yOffset = Math.sin(i * 0.8) * 20;
            
            letter.setX(x);
            letter.setY(yOffset);
            letter.setRotate(angle);
            
            // Add glow effect
            DropShadow glow = new DropShadow();
            glow.setColor(Color.rgb(50, 205, 50, 0.5));
            glow.setRadius(10);
            letter.setEffect(glow);
            
            titleGroup.getChildren().add(letter);
            
            // Create oscillating animation for each letter
            final int delay = i * 100; // Stagger the animation for each letter
            Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(letter.translateYProperty(), 0)),
                new KeyFrame(Duration.millis(1000), new KeyValue(letter.translateYProperty(), 30)),
                new KeyFrame(Duration.millis(2000), new KeyValue(letter.translateYProperty(), 0))
            );
            timeline.setDelay(Duration.millis(delay));
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
            
            x += letter.getBoundsInLocal().getWidth() + 5;
        }
        
        // Center the entire group
        titleGroup.setTranslateX((600 - x) / 2);
        titleGroup.setTranslateY(10);
        
        return titleGroup;
    }

    /*
     * Creates the main menu layout.
     */
    private VBox createMainMenu() {
        VBox menu = new VBox(20);
        menu.setAlignment(Pos.CENTER);
        menu.setPrefSize(600, 400);

        // Create a container for absolute positioning
        javafx.scene.layout.StackPane contentPane = new javafx.scene.layout.StackPane();
        contentPane.setPrefSize(600, 400);

        // Create animated title
        Group animatedTitle = createAnimatedSnakeTitle();
        animatedTitle.setTranslateY(-150); // Adjust tthe Y-Value

        // Create buttons in their own VBox
        VBox buttonBox = new VBox(20);
        buttonBox.setAlignment(Pos.CENTER);

        // Create Start game button
        Button startButton = new Button("Start Game");
        startButton.setStyle(buttonStyle);

       // Apply styles to buttons with hover effects
       startButton.setOnMouseEntered(e -> startButton.setStyle(buttonHoverStyle));
       startButton.setOnMouseExited(e -> startButton.setStyle(buttonStyle));

        startButton.setOnAction(e -> startNewGame());
        buttonBox.getChildren().addAll(startButton);
        startButton.setTranslateY(50); // Adjust tthe Y-Value

        // Add both title and buttons to the content pane
        contentPane.getChildren().addAll(animatedTitle, buttonBox);

        // Add content pane to the menu
        menu.getChildren().add(contentPane);
        
        return menu;
    }

    /*
     * Creates the pause menu layout.
     */
    private VBox createPauseMenu() {
        VBox menu = new VBox(20);
        menu.setAlignment(Pos.CENTER);
        menu.setPrefSize(600, 400);

        Text pauseText = new Text("PAUSED");
        pauseText.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        pauseText.setFill(Color.rgb(50, 205, 50));
        pauseText.setEffect(new Glow(0.4));

        Button resumeButton = new Button("Resume Game");
        Button mainMenuButton = new Button("Main Menu");

        resumeButton.setStyle(buttonStyle);
        mainMenuButton.setStyle(buttonStyle);
    
        resumeButton.setOnMouseEntered(e -> resumeButton.setStyle(buttonHoverStyle));
        resumeButton.setOnMouseExited(e -> resumeButton.setStyle(buttonStyle));
        mainMenuButton.setOnMouseEntered(e -> mainMenuButton.setStyle(buttonHoverStyle));
        mainMenuButton.setOnMouseExited(e -> mainMenuButton.setStyle(buttonStyle));
    
        resumeButton.setOnAction(e -> resumeGame());
        mainMenuButton.setOnAction(e -> returnToMainMenu());
    
        menu.getChildren().addAll(pauseText, resumeButton, mainMenuButton);
        return menu;
    }

    /*
     * Starts a new game by resetting the game board.
     */
    private void startNewGame() {
        mainMenu.setVisible(false);
        pauseMenu.setVisible(false);
        gameBoard.getSnakeController().resetGame();
        gameBoard.getSnakeController().startGame();
        gameBoard.requestFocus();
    }

    /*
     * Pauses the game and shows the pause menu.
     */
    private void pauseGame() {
        gameBoard.getSnakeController().stopGame();
        pauseMenu.setVisible(true);
    }

    /*
     * Resumes the game from the pause menu.
     */
    private void resumeGame() {
        pauseMenu.setVisible(false);
        gameBoard.getSnakeController().startGame();
        gameBoard.requestFocus();
    }

    /*
     * Returns to the main menu, resetting the game board.
     */
    private void returnToMainMenu() {
        pauseMenu.setVisible(false);
        mainMenu.setVisible(true);

        GraphicsContext gc = gameBoard.getCanvas().getGraphicsContext2D();
        gc.clearRect(0, 0, gameBoard.getCanvas().getWidth(), gameBoard.getCanvas().getHeight());
        gameBoard.drawBorder(); // Optional: redraw border
        gameBoard.getSnakeController().stopGame();
        gameBoard.getSnakeController().resetGame();
    }

    public static void main(String[] args) {
        launch(args);
    }
}