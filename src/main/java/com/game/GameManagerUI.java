package com.game;

/*
 * Manages the main functionality of the game application, including user login,
 * high score tracking, and launching games. Acts as the main hub, allowing users
 * to select and start the Snake or Blackjack games, and provides access to account
 * and high score management.
 */
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;



public class GameManagerUI extends Application {

    @Override
    public void start(Stage primaryStage) {
        showLoginScreen(primaryStage);
    }

    private void showLoginScreen(Stage stage) {
        // Login form layout
        VBox loginBox = new VBox(15);
        loginBox.setPadding(new Insets(20));
        loginBox.setAlignment(Pos.CENTER);
        loginBox.setStyle("-fx-background-color: #1E1E2F;");

        // Title
        Text titleText = new Text("Sunny Games");
        titleText.setFont(Font.font("Arial", 30));
        titleText.setFill(Color.WHITE);

        // Username and Password Fields
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setStyle("-fx-background-color: #2C2C3E; -fx-text-fill: white; -fx-prompt-text-fill: gray;");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setStyle("-fx-background-color: #2C2C3E; -fx-text-fill: white; -fx-prompt-text-fill: gray;");

        // Buttons
        Button signInButton = new Button("Sign In");
        signInButton.setStyle("-fx-background-color: #6C63FF; -fx-text-fill: white;");
        signInButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            if (UserAccountManager.checkLoginInfo(username, password)) {
                showMainApp(stage); // Navigate to main UI after login
            } else {
                showAlert("Error", "Username or password is incorrect.");
            }
        });

        Button registerButton = new Button("Register");
        registerButton.setStyle("-fx-background-color: #3E3E5E; -fx-text-fill: white;");
        registerButton.setOnAction(e -> showAccountCreationForm(stage));

        // Add event listeners for Enter key
        usernameField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                passwordField.requestFocus(); // Move focus to passwordField
            }
        });

        passwordField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                signInButton.fire(); // Trigger the Sign In button click
            }
        });

        // Add components to the loginBox
        loginBox.getChildren().addAll(titleText, usernameField, passwordField, signInButton, registerButton);

        // Create and set the Scene
        Scene scene = new Scene(loginBox, 400, 300);
        stage.setTitle("Sunny Games Login");
        stage.setScene(scene);
        stage.show();
    }

    private void showAccountCreationForm(Stage stage) {
        VBox vboxAccount = new VBox(10);
        vboxAccount.setPadding(new Insets(20));
        vboxAccount.setAlignment(Pos.CENTER);
        vboxAccount.setStyle("-fx-background-color: #1E1E2F;");

        Text titleLabel = new Text("Create an Account");
        titleLabel.setFont(Font.font("Arial", 20));
        titleLabel.setFill(Color.WHITE);

        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter Username");
        usernameField.setStyle("-fx-background-color: #2C2C3E; -fx-text-fill: white; -fx-prompt-text-fill: gray;");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter Password");
        passwordField.setStyle("-fx-background-color: #2C2C3E; -fx-text-fill: white; -fx-prompt-text-fill: gray;");

        Button createButton = new Button("Create Account");
        createButton.setStyle("-fx-background-color: #6C63FF; -fx-text-fill: white;");
        createButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            if (UserAccountManager.doesUsernameExist(username)) {
                showAlert("Error", "Username already exists.");
            } else if (username.isEmpty() || password.isEmpty()) {
                showAlert("Error", "Please fill in both fields.");
            } else if (username.contains(" ") || password.contains(" ")){
                showAlert("Error", "Username and password cannot contain spaces.");
            } else {
                UserAccountManager.writeAccountFile(username, password);
                showAlert("Account Created", "Your account has been created.");
                showLoginScreen(stage);
            }
        });

        // Add event listeners for Enter key
        usernameField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                passwordField.requestFocus(); // Move focus to passwordField
            }
        });

        passwordField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                createButton.fire(); // Trigger the Sign In button click
            }
        });

        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: #3E3E5E; -fx-text-fill: white;");
        backButton.setOnAction(e -> showLoginScreen(stage));

        vboxAccount.getChildren().addAll(titleLabel, usernameField, passwordField, createButton, backButton);

        Scene accountScene = new Scene(vboxAccount, 400, 300);
        stage.setScene(accountScene);
    }

    private void showMainApp(Stage stage) {
    // Main layout
    BorderPane mainLayout = new BorderPane();
    mainLayout.setStyle("-fx-background-color: #1E1E2F;");

    // Toolbar (always visible)
    ToolBarUI toolBarUI = new ToolBarUI();
    toolBarUI.setStyle("-fx-background-color: #2C2C3E; -fx-padding: 10px;");
    toolBarUI.getMenuButton().setOnAction(e -> showMainApp(stage)); // "Main Menu" button action

    // Left side - Game options section (60% of the width)
    VBox gameOptionsBox = new VBox(15);
    gameOptionsBox.setPadding(new Insets(20));
    gameOptionsBox.setStyle("-fx-background-color: #2C2C3E; -fx-text-fill: white;");
    gameOptionsBox.setAlignment(Pos.CENTER);

    Text gameOptionsTitle = new Text("Select a Game");
    gameOptionsTitle.setFont(Font.font("Arial", 18));
    gameOptionsTitle.setFill(Color.WHITE);

    // Game buttons and images
    HBox gameButtonsBox = new HBox(20);
    gameButtonsBox.setAlignment(Pos.CENTER);

    Button blackjackButton = new Button("Play BlackJack");
    blackjackButton.setStyle("-fx-background-color: #6C63FF; -fx-text-fill: white;");
    blackjackButton.setOnAction(e -> {
        // Start Black Jack Game
    }
    );

    Button snakeButton = new Button("Play Snake");
    snakeButton.setStyle("-fx-background-color: #6C63FF; -fx-text-fill: white;");
    snakeButton.setOnAction(e -> {
        // Start Snake Game
    });

    // Images for games (replace with actual images)
    ImageView blackjackImage = new ImageView(new Image("file:resources/blackjack_icon.png"));
    blackjackImage.setFitHeight(100);
    blackjackImage.setFitWidth(100);

    ImageView snakeImage = new ImageView(new Image("file:resources/snake_icon.png"));
    snakeImage.setFitHeight(100);
    snakeImage.setFitWidth(100);

    VBox blackjackBox = new VBox(10, blackjackButton, blackjackImage);
    blackjackBox.setAlignment(Pos.CENTER);

    VBox snakeBox = new VBox(10, snakeButton, snakeImage);
    snakeBox.setAlignment(Pos.CENTER);

    gameButtonsBox.getChildren().addAll(blackjackBox, snakeBox);

    gameOptionsBox.getChildren().addAll(gameOptionsTitle, gameButtonsBox);

    // Right side - High Scores section (40% of the width)
    VBox highScoresBox = new VBox(15);
    highScoresBox.setPadding(new Insets(20));
    highScoresBox.setStyle("-fx-background-color: #2C2C3E; -fx-text-fill: white;");
    highScoresBox.setAlignment(Pos.TOP_CENTER); // Align to the top to avoid empty space below

    // Title for the high scores section
    Text highScoresTitle = new Text("Top 5 High Scores");
    highScoresTitle.setFont(Font.font("Arial", 24)); // Adjusted font size
    highScoresTitle.setFill(Color.WHITE);

    // Example high scores (dynamically loaded)
    ListView<String> highScoresList = new ListView<>();
    highScoresList.setStyle("-fx-background-color: #2C2C3E; -fx-text-fill: white;");

    // Set the cell factory to apply background and text color to each ListCell
    highScoresList.setCellFactory(lv -> {
        ListCell<String> cell = new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("-fx-background-color: #2C2C3E; -fx-text-fill: white;");
                } else {
                    setText(item);
                    setFont(Font.font("Arial", 16));
                    setStyle("-fx-background-color: #2C2C3E; -fx-text-fill: white;");
                }
            }
        };
        return cell;
    });

    // Add sample high scores (this could be dynamically loaded in your app)
    highScoresList.getItems().addAll(
        "BlackJack - Player1: 1000", 
        "Snake - Player2: 950", 
        "BlackJack - Player3: 900",
        "Snake - Player4: 850", 
        "BlackJack - Player5: 800"
    );

    // Add the title and leaderboard list to the VBox
    highScoresBox.getChildren().addAll(highScoresTitle, highScoresList);

    // Create a horizontal split layout with 60% for left side and 40% for right side
    HBox contentLayout = new HBox(20);
    contentLayout.setPadding(new Insets(20));
    contentLayout.getChildren().addAll(gameOptionsBox, highScoresBox);
    contentLayout.setStyle("-fx-background-color: #1E1E2F;");

    // Set the toolbar and content layout in the main layout
    mainLayout.setTop(toolBarUI);
    mainLayout.setCenter(contentLayout);

    // Set the widths of the sides: 60% for left side, 40% for right side
    HBox.setHgrow(gameOptionsBox, Priority.ALWAYS);
    gameOptionsBox.setMaxWidth(Double.MAX_VALUE);
    highScoresBox.setMinWidth(200); // Set minimum width for the right section

    // Create and set the scene
    Scene mainScene = new Scene(mainLayout, 800, 600);
    stage.setTitle("Sunny Games");
    stage.setScene(mainScene);
}

    // Show alert with a given title and message
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
