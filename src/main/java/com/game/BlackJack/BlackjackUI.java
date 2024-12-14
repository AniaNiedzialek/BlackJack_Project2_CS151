package com.game.BlackJack;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;

public class BlackjackUI extends Application {

    private GameController gameController;

    private HBox playerBox;
    private HBox dealerBox;
    private HBox aiPlayer1Box;
    private HBox aiPlayer2Box;
    private Label playerBetLabel;
    private Label dealerBetLabel;
    private Label aiPlayer1BetLabel;
    private Label aiPlayer2BetLabel;
    private Scene welcomeScene;
    private Scene gameScene;
    private Stage mainStage;
    private Label turnLabel;
    private BorderPane tableLayout;

    @Override
    public void start(Stage primaryStage) {
        mainStage = primaryStage;

        // Create the welcome scene
        createWelcomeScene();

        // Create the game scene
        createGameScene();

        // Set the stage to the welcome scene
        mainStage.setScene(welcomeScene);
        mainStage.setTitle("BlackJack Game!");
        mainStage.show();

        turnLabel = new Label("Turn: Waiting...");
        turnLabel.setStyle("-fx-text-fill: green; -fx-font-size: 18px; -fx-font-weight: bold;");
        turnLabel.setAlignment(Pos.CENTER);

        // Add the turn label to the top of the game table
        VBox centerBox = new VBox(10, turnLabel);
        centerBox.setAlignment(Pos.TOP_CENTER);
        centerBox.setPadding(new Insets(20));
        tableLayout.setCenter(centerBox);
    }

    private void createWelcomeScene() {
        VBox welcomeLayout = new VBox(20);
        welcomeLayout.setAlignment(Pos.CENTER);
        welcomeLayout.setPadding(new Insets(20));

        // Load the background image
        try {
            String backgroundPath = "resources/background/welcome_background.png";
            Image backgroundImage = new Image(new FileInputStream(backgroundPath));
            BackgroundImage bgImage = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(
                    BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true
                )
            );
            welcomeLayout.setBackground(new Background(bgImage));
        } catch (FileNotFoundException e) {
            System.out.println("Error loading background image: " + e.getMessage());
        }

        Label welcomeLabel = new Label("Welcome to Blackjack!");
        welcomeLabel.setStyle("-fx-text-fill: #00FF00; -fx-font-size: 43px; -fx-font-weight: bold;");

        Button newGameButton = new Button("New Game");
        Button exitButton = new Button("Exit");

        newGameButton.setOnAction(e -> mainStage.setScene(gameScene));
        exitButton.setOnAction(e -> mainStage.close());

        welcomeLayout.getChildren().addAll(welcomeLabel, newGameButton, exitButton);
        welcomeScene = new Scene(welcomeLayout, 900, 700);
    }

    private void createGameScene() {
        gameController = new GameController();

        tableLayout = new BorderPane();

        // Load the background image
        try {
            String backgroundPath = "resources/background/game_background.png";
            Image backgroundImage = new Image(new FileInputStream(backgroundPath));
            BackgroundImage bgImage = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(
                    BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true
                )
            );
            tableLayout.setBackground(new Background(bgImage));
        } catch (FileNotFoundException e) {
            System.out.println("Error loading background image: " + e.getMessage());
        }

        dealerBox = createPlayerArea("Dealer", Pos.CENTER, tableLayout, "top");
        dealerBetLabel = new Label("Bet: ");
        dealerBetLabel.setStyle("-fx-text-fill: white;");
        ((VBox) tableLayout.getTop()).getChildren().add(dealerBetLabel);

        aiPlayer1Box = createPlayerArea("AI Player 1", Pos.CENTER, tableLayout, "left");
        aiPlayer1BetLabel = new Label("Bet: ");
        aiPlayer1BetLabel.setStyle("-fx-text-fill: white;");
        ((VBox) tableLayout.getLeft()).getChildren().add(aiPlayer1BetLabel);

        aiPlayer2Box = createPlayerArea("AI Player 2", Pos.CENTER, tableLayout, "right");
        aiPlayer2BetLabel = new Label("Bet: ");
        aiPlayer2BetLabel.setStyle("-fx-text-fill: white;");
        ((VBox) tableLayout.getRight()).getChildren().add(aiPlayer2BetLabel);

        playerBox = createPlayerArea("You", Pos.CENTER, tableLayout, "bottom");
        playerBetLabel = new Label("Bet: ");
        playerBetLabel.setStyle("-fx-text-fill: white;");
        ((VBox) tableLayout.getBottom()).getChildren().add(playerBetLabel);

        Button newGameButton = new Button("Start!");
        Button hitButton = new Button("Hit");
        Button standButton = new Button("Stand");
        Button returnToMenuButton = new Button("Return to Menu");

        hitButton.setDisable(true);
        standButton.setDisable(true);

        newGameButton.setOnAction(e -> startNewGame(hitButton, standButton));
        hitButton.setOnAction(e -> hit());
        standButton.setOnAction(e -> stand());
        returnToMenuButton.setOnAction(e -> mainStage.setScene(welcomeScene));

        HBox buttonBox = new HBox(10, newGameButton, hitButton, standButton, returnToMenuButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10));

        VBox playerArea = new VBox(10, playerBox, buttonBox);
        playerArea.setAlignment(Pos.CENTER);
        tableLayout.setBottom(playerArea);

        gameScene = new Scene(tableLayout, 900, 700);
    }

    private HBox createPlayerArea(String name, Pos alignment, BorderPane tableLayout, String position) {
        HBox playerBox = new HBox(10);
        playerBox.setAlignment(alignment);

        Label playerLabel = new Label(name);
        playerLabel.setStyle("-fx-text-fill: #00FF00; -fx-font-size: 16px; -fx-font-weight: bold;");
        VBox playerArea = new VBox(10, playerLabel, playerBox);
        playerArea.setAlignment(Pos.CENTER);

        switch (position) {
            case "top":
                playerArea.setPadding(new Insets(20, 0, 10, 0));
                tableLayout.setTop(playerArea);
                break;
            case "left":
                playerArea.setPadding(new Insets(0, 10, 0, 20));
                tableLayout.setLeft(playerArea);
                break;
            case "right":
                playerArea.setPadding(new Insets(0, 20, 0, 10));
                tableLayout.setRight(playerArea);
                break;
            case "bottom":
                playerArea.setPadding(new Insets(10, 0, 20, 0));
                tableLayout.setBottom(playerArea);
                break;
        }

        return playerBox;
    }

    private void startNewGame(Button hitButton, Button standButton) {
        Random random = new Random();
        for (Player player : gameController.getPlayers()) {
            if (player instanceof HumanPlayer) {
                TextInputDialog betDialog = new TextInputDialog("100");
                betDialog.setTitle("Place Your Bet");
                betDialog.setHeaderText("Your Balance: " + player.getBalance());
                betDialog.setContentText("Enter your bet amount:");

                betDialog.showAndWait().ifPresent(bet -> {
                    int betAmount = Integer.parseInt(bet);
                    player.setBet(Math.min(betAmount, player.getBalance()));
                });
            } else {
                int aiBet = 50 + random.nextInt(51); // AI bets between 50-100
                player.setBet(Math.min(aiBet, player.getBalance()));
            }
        }

        gameController.startRound();
        hitButton.setDisable(false);
        standButton.setDisable(false);
        updateTable();
    }

    private void hit() {
        Player currentPlayer = gameController.getCurrentPlayer();
        if (currentPlayer.getHand().size() < 5 && currentPlayer instanceof HumanPlayer) {
            currentPlayer.addCard(gameController.getDeck().dealCard());
            if (currentPlayer.isBusted()) {
                gameController.nextTurn();
            }
            updateTable();
        }
    }

    private void stand() {
        gameController.nextTurn();
        processAITurns();
        updateTable();
    }

    private void processAITurns() {
        while (!(gameController.getCurrentPlayer() instanceof HumanPlayer)) {
            Player currentPlayer = gameController.getCurrentPlayer();
            if (currentPlayer.getHand().size() < 5 && currentPlayer.calculateHandValue() < 16) {
                currentPlayer.addCard(gameController.getDeck().dealCard());
            } else {
                gameController.nextTurn();
            }
        }
    }

    private void updateTable() {
        playerBox.getChildren().clear();
        dealerBox.getChildren().clear();
        aiPlayer1Box.getChildren().clear();
        aiPlayer2Box.getChildren().clear();

        updateCardBox(playerBox, gameController.getHumanPlayer().getHand());
        updateCardBox(dealerBox, gameController.getDealer().getHand());
        updateCardBox(aiPlayer1Box, gameController.getAIPlayer1().getHand());
        updateCardBox(aiPlayer2Box, gameController.getAIPlayer2().getHand());

        playerBetLabel.setText("Bet: " + gameController.getHumanPlayer().getBet());
        dealerBetLabel.setText("Bet: " + gameController.getDealer().getBet());
        aiPlayer1BetLabel.setText("Bet: " + gameController.getAIPlayer1().getBet());
        aiPlayer2BetLabel.setText("Bet: " + gameController.getAIPlayer2().getBet());

        Player currentPlayer = gameController.getCurrentPlayer();
        turnLabel.setText("Turn: " + currentPlayer.getName());
    }

    private void updateCardBox(HBox box, ArrayList<Card> hand) {
        for (Card card : hand) {
            try {
                String imagePath = "resources/cards/" + card.getRank() + "_of_" + card.getSuit() + ".png";
                ImageView cardImage = new ImageView(new Image(new FileInputStream(imagePath)));
                cardImage.setFitWidth(75);
                cardImage.setPreserveRatio(true);
                box.getChildren().add(cardImage);
            } catch (FileNotFoundException e) {
                System.out.println("Error loading card image: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
