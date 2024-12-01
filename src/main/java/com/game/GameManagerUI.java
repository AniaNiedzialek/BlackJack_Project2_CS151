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
        signInButton.setOnAction(e -> showMainApp(stage)); // Navigate to main UI after login

        Button registerButton = new Button("Register");
        registerButton.setStyle("-fx-background-color: #3E3E5E; -fx-text-fill: white;");
        registerButton.setOnAction(e -> showAccountCreationForm(stage));

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

        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: #3E3E5E; -fx-text-fill: white;");
        backButton.setOnAction(e -> showLoginScreen(stage));

        vboxAccount.getChildren().addAll(titleLabel, usernameField, passwordField, createButton, backButton);

        Scene accountScene = new Scene(vboxAccount, 400, 300);
        stage.setScene(accountScene);
    }

    private void showMainApp(Stage stage) {
        BorderPane mainLayout = new BorderPane();
        mainLayout.setStyle("-fx-background-color: #1E1E2F;");

        // Create and add ToolBarUI
        ToolBarUI toolBarUI = new ToolBarUI();
        toolBarUI.setStyle("-fx-background-color: #2C2C3E; -fx-padding: 10px;");

        // Main content area
        VBox contentArea = new VBox();
        contentArea.setAlignment(Pos.CENTER);
        contentArea.setPadding(new Insets(20));
        Text welcomeText = new Text("Welcome to Sunny Games!");
        welcomeText.setFont(Font.font("Arial", 24));
        welcomeText.setFill(Color.WHITE);
        contentArea.getChildren().add(welcomeText);

        // Set the toolbar and content in the layout
        mainLayout.setTop(toolBarUI);
        mainLayout.setCenter(contentArea);

        // Create and set the scene
        Scene mainScene = new Scene(mainLayout, 800, 600);
        stage.setTitle("Sunny Games");
        stage.setScene(mainScene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
