package com.cs151.blackjackproject;

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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class GameManagerUI extends Application {

    private Scene mainMenuScene;

    @Override
    public void start(Stage primaryStage) {

        // Create title
        Label titleLabel = new Label("Welcome");
        titleLabel.setStyle("-fx-font-size: 40px;");

        // Create login button
        Button loginButton = new Button("Login");
        loginButton.setFont(new Font("Georgia", 20));
        loginButton.setPrefSize(100, 50);
        loginButton.setOnAction(e -> showLoginForm(primaryStage)); // Show login form

        // Create "create account" button
        Button accountButton = new Button("Create Account");
        accountButton.setFont(new Font("Georgia", 20));
        accountButton.setPrefSize(200, 50);
        accountButton.setOnAction(e -> showAccountForm(primaryStage));  

        // Add toolbar from toolbarUI
        ToolBarUI toolbar = new ToolBarUI();

        // Create stack pane for toolbar
        StackPane toolbarContainer = new StackPane();
        toolbarContainer.setStyle("-fx-background-color: #ECECEC; -fx-border-color: #DEDEDE; -fx-border-width: 2px; -fx-border-radius: 5;");
        toolbarContainer.getChildren().addAll(toolbar);

        // Create vbox
        VBox vbox = new VBox(20);
        vbox.setPadding(new Insets(2)); // 15px padding      
        vbox.setAlignment(Pos.TOP_CENTER);

        // Create an HBox for the buttons
        HBox hbox = new HBox(15); // 20px spacing between the buttons
        hbox.setPadding(new Insets(2)); // 15px padding      
        hbox.setAlignment(Pos.CENTER);
        hbox.setStyle("-fx-background-color: #ECECEC; -fx-border-color: #DEDEDE; -fx-border-width: 2px; -fx-border-radius: 5;");

        // Add buttons to hbox
        hbox.getChildren().addAll(titleLabel, loginButton, accountButton);

        vbox.getChildren().setAll(toolbarContainer, titleLabel, hbox);

        // Set primary scene with hbox
        mainMenuScene = new Scene(vbox, 400, 200);

        // Set the stage
        primaryStage.setTitle("Game Manager");
        primaryStage.setScene(mainMenuScene);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    private void showLoginForm(Stage stage) {
        VBox vboxLogin = new VBox(10);
        vboxLogin.setPadding(new Insets(15));
        vboxLogin.setAlignment(Pos.CENTER);

        // Username Label
        Label usernameLabel = new Label("Enter Username:");
        usernameLabel.setFont(new Font("Georgia", 20));

        // Username Textfield
        TextField usernameTextField = new TextField();
        usernameTextField.setPromptText("Enter username here");

        // Password Label
        Label passwordLabel = new Label("Enter Password:");
        passwordLabel.setFont(new Font("Georgia", 20));

        // Password Field (not TextField for security reasons)
        PasswordField passwordTextField = new PasswordField();
        passwordTextField.setPromptText("Enter password");

        // Submit Button
        Button submitButton = new Button("Login");

        // Back Button with functionality to return to primary scene
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> stage.setScene(mainMenuScene));  // Go back to the main screen

        // Add all elements to vbox
        vboxLogin.getChildren().addAll(usernameLabel, usernameTextField, passwordLabel, passwordTextField, submitButton, backButton);

        // Create scene with vboxLogin as the root
        Scene loginScene = new Scene(vboxLogin, 400, 300);

        // Set stage with the login scene
        stage.setScene(loginScene);
    }

    private void showAccountForm(Stage stage) {
        VBox vboxAccount = new VBox(10);
        vboxAccount.setPadding(new Insets(15));
        vboxAccount.setAlignment(Pos.CENTER_LEFT);

        // Welcome Label
        Label welcomeLabel = new Label("Create an Account");
        welcomeLabel.setStyle("-fx-font-size: 40px;");

        // Username Label
        Label usernameLabel = new Label("Enter Username:");

        // Username Textfield
        TextField usernameTextField = new TextField();
        usernameTextField.setPromptText("Enter username here");

        // Password Label
        Label passwordLabel = new Label("Enter Password:");

        // Password Field (not TextField for security reasons)
        PasswordField passwordTextField = new PasswordField();
        passwordTextField.setPromptText("Enter password");

        // Submit Button
        Button submitButton = new Button("Submit");

        // Back Button with functionality to return to primary scene
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> stage.setScene(mainMenuScene));  // Go back to the main screen

        VBox titleBox = new VBox(10);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.getChildren().addAll(welcomeLabel);

        // Add all elements to vbox
        vboxAccount.getChildren().addAll(titleBox, usernameLabel, usernameTextField, passwordLabel, passwordTextField, submitButton, backButton);

        // Create scene with vboxLogin as the root
        Scene accountScene = new Scene(vboxAccount, 400, 300);

        // Set stage with the login scene
        stage.setScene(accountScene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
