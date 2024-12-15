package com.game.BlackJack;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Optional;

import com.game.GameManagerUI;
import com.game.ToolBarUI;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class BlackjackUI extends Application {
    private BlackjackGameController gameController;
    private HBox playerBox, dealerBox, aiPlayer1Box, aiPlayer2Box;
    private Label turnLabel, decisionLabel, resultsLabel;
    private Scene gameScene, welcomeScene;
    private Stage mainStage;
    private BorderPane tableLayout;
    private Button hitButton, standButton, saveButton, loadGame; 
    
    private Label playerValueLabel, aiPlayer1ValueLabel, aiPlayer2ValueLabel, dealerValueLabel;

    private static final String BUTTON_STYLE = """
        -fx-background-color: #1a1a1a;
        -fx-text-fill: #00ff00;
        -fx-font-size: 16px;
        -fx-padding: 10 20;
        -fx-background-radius: 5;
        -fx-border-color: #00ff00;
        -fx-border-radius: 5;
        -fx-border-width: 2;
        -fx-cursor: hand;
        """;
    
    private static final String BUTTON_HOVER_STYLE = """
        -fx-background-color: #00ff00;
        -fx-text-fill: #1a1a1a;
        """;

        @Override
        public void start(Stage primaryStage) {
            mainStage = primaryStage;
            createWelcomeScene();
            createGameScene();
    
            mainStage.setScene(welcomeScene);
            mainStage.setTitle("♠️ BlackJack Casino ♣️");
            mainStage.show();
        }
    
        private void createWelcomeScene() {
            VBox layout = new VBox(30);
            layout.setAlignment(Pos.CENTER);
            layout.setPadding(new Insets(40));
            layout.setBackground(new Background(new BackgroundFill(
                Color.web("#1a1a1a"), CornerRadii.EMPTY, Insets.EMPTY)));
        
            Label welcomeLabel = new Label("Welcome to\nBlackjack Casino");
            welcomeLabel.setStyle("""
                -fx-font-size: 48px;
                -fx-font-family: 'Arial Black';
                -fx-text-fill: #00ff00;
                -fx-font-weight: bold;
                -fx-text-alignment: center;
                -fx-effect: dropshadow(gaussian, #00ff00, 3, 0.7, 0.0, 0.0);
                """);
            Button newGameButton = createStyledButton("New Game");
            Button exitButton = createStyledButton("Exit");
        
            newGameButton.setOnAction(e -> mainStage.setScene(gameScene));
            exitButton.setOnAction(e -> mainStage.close());
        
            layout.getChildren().addAll(welcomeLabel, newGameButton, exitButton);
        
            BorderPane welcomeLayout = new BorderPane();
            welcomeLayout.setTop(createToolbar()); // Add the toolbar
            welcomeLayout.setCenter(layout);
        
            welcomeScene = new Scene(welcomeLayout, 1200, 800);
        }
        
        private void createGameScene() {
            gameController = new BlackjackGameController();
        
            // Create the main layout for the game
            tableLayout = new BorderPane();
            tableLayout.setBackground(new Background(new BackgroundFill(
                Color.web("#0d4d0d"), CornerRadii.EMPTY, Insets.EMPTY
            )));
            tableLayout.setPadding(new Insets(20));
        
            // Add the toolbar at the top
            ToolBarUI toolbar = createToolbar(); // Reuse the toolbar creation method
            toolbar.setStyle("-fx-background-color: rgba(0, 51, 0, 0.8); -fx-border-color: #00ff00; -fx-border-width: 2px;");
        
            BorderPane gameLayout = new BorderPane();
            gameLayout.setTop(toolbar); // Add toolbar to the top of the game layout
            gameLayout.setCenter(tableLayout); // Set tableLayout as the center content
        
            turnLabel = createStyledLabel("Waiting...", 24);
            decisionLabel = createStyledLabel("Decision: Waiting...", 20);
            resultsLabel = createStyledLabel("", 18);
        
            VBox centerBox = new VBox(15);
            centerBox.setAlignment(Pos.TOP_CENTER);
            centerBox.getChildren().addAll(turnLabel, decisionLabel, resultsLabel);
            centerBox.setPadding(new Insets(20));
            tableLayout.setCenter(centerBox);
        
            setupPlayerAreas();
        
            hitButton = createStyledButton("Hit");
            hitButton.setDisable(true); // Initially disabled
        
            standButton = createStyledButton("Stand");
            standButton.setDisable(true); // Initially disabled
        
            Button newGameButton = createStyledButton("Start New Game!");
            saveButton = createStyledButton("Save Game!");
            loadGame = createStyledButton("Load Game!");
        
            hitButton.setOnAction(e -> hit());
            standButton.setOnAction(e -> stand());
            newGameButton.setOnAction(e -> startNewGame());
            saveButton.setOnAction(e -> savegame());
            loadGame.setOnAction(e -> loadGame());
        
            HBox buttonBox = new HBox(20, newGameButton, hitButton, standButton, saveButton, loadGame);
            buttonBox.setAlignment(Pos.CENTER);
            buttonBox.setPadding(new Insets(20));
        
            VBox bottomBox = new VBox(20, playerBox, buttonBox);
            bottomBox.setAlignment(Pos.CENTER);
            tableLayout.setBottom(bottomBox);
        
            gameScene = new Scene(gameLayout, 1200, 800);
        }
        
        private ToolBarUI createToolbar() {
            ToolBarUI toolbar = new ToolBarUI();
            toolbar.setStyle("-fx-background-color: #003300; -fx-border-color: #00ff00; -fx-border-width: 2px;");
            toolbar.getMenuButton().setOnAction(e -> {
                mainStage.close();
                GameManagerUI gameManager = new GameManagerUI();
                gameManager.showMainApp(mainStage);
            });
            return toolbar;
        }
        
                
    private void savegame() {
        String saveStateString = gameController.generateSaveStateString(); // Generate save string
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Saved");
        alert.setHeaderText("Copy and save this string to load the game later:");
        alert.setContentText(saveStateString);
    
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-background-color: #1a1a1a;");
        dialogPane.lookupAll(".label").forEach(label -> label.setStyle("-fx-text-fill: #00ff00;"));
        dialogPane.lookupAll(".button").forEach(button -> button.setStyle("-fx-background-color: #1a1a1a; -fx-text-fill: #00ff00;"));
    
        alert.showAndWait();
    }
    
    private void loadGame() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Load Game");
        dialog.setHeaderText("Paste your saved game string:");
        dialog.setContentText("Save State String:");
    
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(saveStateString -> {
            try {
                gameController.loadSaveStateString(saveStateString); // Load saved state
                updateTable(); // Refresh the table UI
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Game Loaded");
                successAlert.setHeaderText("Success!");
                successAlert.setContentText("Game state loaded successfully.");
                successAlert.showAndWait();
            } catch (Exception e) {
                showError("Invalid save state string. Please try again.");
            }
        });
    }
    
    

    private void setupPlayerAreas() {
        playerValueLabel = createStyledLabel("Value: 0", 16);
        aiPlayer1ValueLabel = createStyledLabel("Value: 0", 16);
        aiPlayer2ValueLabel = createStyledLabel("Value: 0", 16);
        dealerValueLabel = createStyledLabel("Value: 0", 16);

        dealerBox = createPlayerArea("Dealer");
        aiPlayer1Box = createPlayerArea("AI Player 1");
        aiPlayer2Box = createPlayerArea("AI Player 2");
        playerBox = createPlayerArea("You");

        VBox dealerArea = new VBox(5);
        dealerArea.setAlignment(Pos.CENTER);
        dealerArea.getChildren().addAll(createStyledLabel("♠️ Dealer ♣️", 20), dealerBox, dealerValueLabel);

        VBox aiPlayer1Area = new VBox(5);
        aiPlayer1Area.setAlignment(Pos.CENTER);
        aiPlayer1Area.getChildren().addAll(createStyledLabel("♥️ Player 1 ♦️", 20), aiPlayer1Box, aiPlayer1ValueLabel);

        VBox aiPlayer2Area = new VBox(5);
        aiPlayer2Area.setAlignment(Pos.CENTER);
        aiPlayer2Area.getChildren().addAll(createStyledLabel("♦️ Player 2 ♥️", 20), aiPlayer2Box, aiPlayer2ValueLabel);

        VBox playerArea = new VBox(5);
        playerArea.setAlignment(Pos.CENTER);
        playerArea.getChildren().addAll(createStyledLabel("♥️ You ♠️", 20), playerBox, playerValueLabel);

        tableLayout.setTop(dealerArea);
        tableLayout.setLeft(aiPlayer1Area);
        tableLayout.setRight(aiPlayer2Area);
        tableLayout.setBottom(playerArea);
    }
    private HBox createPlayerArea(String name) {
        HBox box = new HBox(0);  // Set spacing to 0 since we'll handle card spacing manually
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(15));
        box.setMinWidth(300);  // Slightly wider to accommodate overlapping cards
        box.setMinHeight(150);
        box.setStyle("""
            -fx-background-color: rgba(0, 0, 0, 0.3);
            -fx-background-radius: 15;
            -fx-border-color: linear-gradient(to bottom right, #00ff00, #008000);
            -fx-border-radius: 15;
            -fx-border-width: 2;
            -fx-effect: dropshadow(gaussian, rgba(0,255,0,0.3), 10, 0.5, 0.0, 0.0);
            """);
        return box;
    }

    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setStyle(BUTTON_STYLE);
        button.setOnMouseEntered(e -> button.setStyle(BUTTON_STYLE + BUTTON_HOVER_STYLE));
        button.setOnMouseExited(e -> button.setStyle(BUTTON_STYLE));
        button.setMinWidth(150);
        return button;
    }

    private Label createStyledLabel(String text, int fontSize) {
        Label label = new Label(text);
        label.setStyle(String.format("""
            -fx-font-size: %dpx;
            -fx-text-fill: #ffffff;
            -fx-font-weight: bold;
            -fx-effect: dropshadow(gaussian, rgba(0, 255, 0, 0.3), 10, 0.5, 0.0, 0.0);
            """, fontSize));
        return label;
    }

    private int promptForBet(String playerName, int balance) {
        TextInputDialog dialog = new TextInputDialog("10");
        dialog.setTitle("Place Your Bet");
        dialog.setHeaderText(playerName + ", your balance is $" + balance);
        dialog.setContentText("Enter your bet amount:");
    
        // Style the dialog pane
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.setStyle("""
            -fx-background-color: #1a1a1a;
            -fx-border-color: #00ff00;
            -fx-border-width: 2px;
            """);
    
        // Style the header text
        dialogPane.lookupAll(".header-panel").forEach(header -> 
            header.setStyle("""
                -fx-background-color: #1a1a1a;
                -fx-text-fill: #00ff00;
                -fx-font-size: 16px;
                -fx-font-weight: bold;
                """));
    
        // Style all labels
        dialogPane.lookupAll(".label").forEach(label -> 
            label.setStyle("""
                -fx-text-fill: #00ff00;
                -fx-font-size: 14px;
                """));
    
        // Style the text input field
        dialog.getEditor().setStyle("""
            -fx-background-color: #2a2a2a;
            -fx-text-fill: #00ff00;
            -fx-border-color: #00ff00;
            -fx-border-width: 1px;
            -fx-font-size: 14px;
            """);
    
        // Style the buttons
        dialogPane.lookupAll(".button").forEach(button -> {
            button.setStyle("""
                -fx-background-color: #1a1a1a;
                -fx-text-fill: #00ff00;
                -fx-border-color: #00ff00;
                -fx-border-width: 1px;
                -fx-font-size: 14px;
                """);
            
            // Add hover effect
            button.setOnMouseEntered(e -> 
                button.setStyle("""
                    -fx-background-color: #00ff00;
                    -fx-text-fill: #1a1a1a;
                    -fx-border-color: #00ff00;
                    -fx-border-width: 1px;
                    -fx-font-size: 14px;
                    """)
            );
            
            button.setOnMouseExited(e -> 
                button.setStyle("""
                    -fx-background-color: #1a1a1a;
                    -fx-text-fill: #00ff00;
                    -fx-border-color: #00ff00;
                    -fx-border-width: 1px;
                    -fx-font-size: 14px;
                    """)
            );
        });
    
        Optional<String> result = dialog.showAndWait();
        try {
            if (result.isPresent()) {
                int bet = Integer.parseInt(result.get());
                if (bet <= 0 || bet > balance) {
                    showError("Invalid bet amount. Please bet between $1 and $" + balance);
                    return promptForBet(playerName, balance);
                }
                return bet;
            }
            return 10; // Default bet if cancelled
        } catch (NumberFormatException e) {
            showError("Please enter a valid number");
            return promptForBet(playerName, balance);
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("""
            -fx-background-color: #1a1a1a;
            """);
        
        // Style all labels
        dialogPane.lookupAll(".label").forEach(label -> 
            label.setStyle("-fx-text-fill: #00ff00; -fx-font-weight: bold;"));
        
        // Style the buttons
        dialogPane.lookupAll(".button").forEach(button -> 
            button.setStyle("""
                -fx-background-color: #1a1a1a;
                -fx-text-fill: #00ff00;
                -fx-border-color: #00ff00;
                -fx-border-width: 1px;
                """));
        
        alert.showAndWait();
    }

    private void startNewGame() {
        gameController.startRound();
        
        // Get human player's bet
        HumanPlayer humanPlayer = gameController.getHumanPlayer();
        int bet = promptForBet(humanPlayer.getName(), humanPlayer.getBalance());
        humanPlayer.placeBet(bet);
        
        // Set AI bets (you can adjust these amounts)
        AIPlayer ai1 = gameController.getAIPlayer1();
        AIPlayer ai2 = gameController.getAIPlayer2();
        try {
            ai1.placeBet(Math.min(10, ai1.getBalance()));
            ai2.placeBet(Math.min(10, ai2.getBalance()));
        } catch (IllegalArgumentException e) {
            showError("Game cannot continue: AI players have insufficient funds");
            return;
        }
    
        hitButton.setDisable(false);
        standButton.setDisable(false);
        resultsLabel.setText("");
        
        VBox centerBox = (VBox) tableLayout.getCenter();
        centerBox.getChildren().clear();
        centerBox.getChildren().addAll(turnLabel, decisionLabel, resultsLabel);
        
        updateTable();
        turnLabel.setText("Your Turn");
    }   

    private void hit() {
        Player player = gameController.getCurrentPlayer();
        if (player instanceof HumanPlayer) {
            player.addCard(gameController.getDeck().dealCard());
            updateTable();

            if (player.isBusted()) {
                decisionLabel.setText("You busted!");
                gameController.nextTurn();
                processNextTurn();
            }
        }
    }

    private void stand() {
        decisionLabel.setText("You stand!");
        gameController.nextTurn();
        processNextTurn();
    }

    private void processNextTurn() {
        if (gameController.isRoundOver()) {
            processDealerTurn();
        } else {
            Player currentPlayer = gameController.getCurrentPlayer();
            if (currentPlayer instanceof AIPlayer) {
                handleAITurn((AIPlayer) currentPlayer);
            }
        }
    }

    private boolean shouldAIHit(int playerValue, int dealerUpCardValue) {
        // Basic strategy for AI
        if (playerValue < 12) return true;
        if (playerValue >= 17) return false;
        if (playerValue >= 13 && playerValue <= 16) {
            return dealerUpCardValue >= 7;
        }
        return dealerUpCardValue >= 4 && dealerUpCardValue <= 6;
    }

    private void handleAITurn(AIPlayer aiPlayer) {
        turnLabel.setText(aiPlayer.getName() + " Turn");
        PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
    
        pause.setOnFinished(e -> {
            // Add safety check for dealer's hand
            ArrayList<Card> dealerHand = gameController.getDealer().getHand();
            if (dealerHand.isEmpty()) {
                // If dealer has no cards, use a default value or handle appropriately
                decisionLabel.setText(aiPlayer.getName() + " stands!");
                gameController.nextTurn();
                processNextTurn();
                return;
            }
    
            int dealerUpCardValue = dealerHand.get(0).getValue();
            int playerValue = aiPlayer.calculateHandValue();
            
            boolean shouldHit = shouldAIHit(playerValue, dealerUpCardValue);
            
            if (shouldHit) {
                decisionLabel.setText(aiPlayer.getName() + " hits!");
                aiPlayer.addCard(gameController.getDeck().dealCard());
                updateTable();
                
                if (aiPlayer.isBusted()) {
                    decisionLabel.setText(aiPlayer.getName() + " busted!");
                    gameController.nextTurn();
                    processNextTurn();
                } else {
                    handleAITurn(aiPlayer);
                }
            } else {
                decisionLabel.setText(aiPlayer.getName() + " stands!");
                gameController.nextTurn();
                processNextTurn();
            }
        });
    
        pause.play();
    }

    private void processDealerTurn() {
        turnLabel.setText("Dealer's Turn");
        PauseTransition dealerPause = new PauseTransition(Duration.seconds(1.5));

        dealerPause.setOnFinished(e -> {
            Dealer dealer = gameController.getDealer();
            if (dealer.calculateHandValue() < 17) {
                decisionLabel.setText("Dealer hits!");
                dealer.addCard(gameController.getDeck().dealCard());
                updateTable();
                processDealerTurn();
            } else {
                decisionLabel.setText("Dealer stands!");
                displayFinalResults();
            }
        });
        dealerPause.play();
    }

    private ArrayList<ImageView> createPlayerCardImages(ArrayList<Card> hand, boolean isCurrentPlayer, boolean isRoundOver) {
        ArrayList<ImageView> images = new ArrayList<>();
        double cardSpacing = 10; // Adjust spacing between cards
        
        for (int i = 0; i < hand.size(); i++) {
            try {
                ImageView imageView;
    
                // Determine if this card should be face up or down
                if (isRoundOver || isCurrentPlayer) {
                    // Show actual card
                    Card card = hand.get(i);
                    String path = "resources/cards/" + card.getRank().toLowerCase() + "_of_" + card.getSuit().toLowerCase() + ".png";
                    imageView = new ImageView(new Image(new FileInputStream(path)));
                } else {
                    // Show card back
                    String backPath = "resources/cards/back.png";
                    imageView = new ImageView(new Image(new FileInputStream(backPath)));
                }
    
                // Standard card formatting
                imageView.setFitWidth(100);
                imageView.setPreserveRatio(true);
                imageView.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 10, 0, 0, 0);");
                
                // Set spacing between cards
                HBox.setMargin(imageView, new Insets(0, cardSpacing, 0, 0));
                images.add(imageView);
            } catch (FileNotFoundException e) {
                System.out.println("Card image not found: " + e.getMessage());
            }
        }
    
        // Remove margin for the last card to avoid unnecessary spacing
        if (!images.isEmpty()) {
            HBox.setMargin(images.get(images.size() - 1), new Insets(0));
        }
    
        return images;
    }
            
    private void displayFinalResults() {
        ArrayList<String> results = gameController.resolveRound();
    
        VBox resultsBox = new VBox(20);
        resultsBox.setAlignment(Pos.CENTER);
        resultsBox.setStyle("""
            -fx-background-color: linear-gradient(to bottom, #000000, #0d4d0d);
            -fx-padding: 30;
            -fx-background-radius: 20;
            -fx-border-color: #00ff00;
            -fx-border-width: 3;
            -fx-border-radius: 20;
            """);
    
        Label roundOverLabel = new Label("🎉 Round Over! 🎉");
        roundOverLabel.setStyle("""
            -fx-font-size: 20px;
            -fx-text-fill: #00ff00;
            -fx-font-weight: bold;
            """);
    
        VBox resultsList = new VBox(10);
        resultsList.setAlignment(Pos.CENTER);
    
        for (String result : results) {
            Label resultLabel = new Label(result);
            resultLabel.setStyle("""
                -fx-font-size: 16px;
                -fx-text-fill: white;
                """);
            resultsList.getChildren().add(resultLabel);
        }
    
        resultsBox.getChildren().addAll(roundOverLabel, resultsList);
    
        VBox centerBox = new VBox(15);
        centerBox.setAlignment(Pos.TOP_CENTER);
        centerBox.getChildren().addAll(turnLabel, resultsBox);
        tableLayout.setCenter(centerBox); // Only modify the center section
    
        turnLabel.setText("Round Over!");
        hitButton.setDisable(true);
        standButton.setDisable(true);
    }
        
    private void updateTable() {
        clearAllPlayerAreas();
        boolean isRoundOver = gameController.isRoundOver();
        
        // Player's cards and bet info
        playerBox.getChildren().addAll(createPlayerCardImages(
            gameController.getHumanPlayer().getHand(), 
            true, 
            true
        ));
        playerValueLabel.setText(String.format("Value: %d | Bet: $%d | Balance: $%d",
            gameController.getHumanPlayer().calculateHandValue(),
            gameController.getHumanPlayer().getBet(),
            gameController.getHumanPlayer().getBalance()));
    
        // AI Player 1
        aiPlayer1Box.getChildren().addAll(createPlayerCardImages(
            gameController.getAIPlayer1().getHand(),
            false,
            isRoundOver
        ));
        aiPlayer1ValueLabel.setText(String.format("%s | Bet: $%d | Balance: $%d",
            isRoundOver ? "Value: " + gameController.getAIPlayer1().calculateHandValue() : "Value: ?",
            gameController.getAIPlayer1().getBet(),
            gameController.getAIPlayer1().getBalance()));
    
        // AI Player 2
        aiPlayer2Box.getChildren().addAll(createPlayerCardImages(
            gameController.getAIPlayer2().getHand(),
            false,
            isRoundOver
        ));
        aiPlayer2ValueLabel.setText(String.format("%s | Bet: $%d | Balance: $%d",
            isRoundOver ? "Value: " + gameController.getAIPlayer2().calculateHandValue() : "Value: ?",
            gameController.getAIPlayer2().getBet(),
            gameController.getAIPlayer2().getBalance()));
    
        // Dealer's cards
        ArrayList<Card> dealerCards = gameController.getDealer().getHand();
        dealerBox.getChildren().clear(); // Clear previous cards
    
        if (!dealerCards.isEmpty()) {
            for (int i = 0; i < dealerCards.size(); i++) {
                ImageView cardImage;
    
                if (isRoundOver || i == 0) { // Reveal all cards if round over, otherwise show first card
                    ArrayList<Card> singleCard = new ArrayList<>();
                    singleCard.add(dealerCards.get(i));
                    cardImage = createPlayerCardImages(singleCard, true, true).get(0);
                } else {
                    cardImage = createHiddenCard();
                }
    
                // Apply consistent margin for spacing
                HBox.setMargin(cardImage, new Insets(0, 10, 0, 0)); // Right margin for spacing
                dealerBox.getChildren().add(cardImage);
            }
    
            // Remove margin for the last card
            if (!dealerBox.getChildren().isEmpty()) {
                HBox.setMargin(dealerBox.getChildren().get(dealerBox.getChildren().size() - 1), new Insets(0));
            }
            dealerValueLabel.setText(isRoundOver ? 
                "Value: " + gameController.getDealer().calculateHandValue() :
                "Value: ?"
            );
        }
    
        // Fix margins for last cards
        fixLastCardMargin(playerBox);
        fixLastCardMargin(aiPlayer1Box);
        fixLastCardMargin(aiPlayer2Box);
        fixLastCardMargin(dealerBox);
    }    
    
    private void fixLastCardMargin(HBox box) {
        if (!box.getChildren().isEmpty()) {
            HBox.setMargin(box.getChildren().get(box.getChildren().size() - 1), new Insets(0));
        }
    }

    private void clearAllPlayerAreas() {
        playerBox.getChildren().clear();
        dealerBox.getChildren().clear();
        aiPlayer1Box.getChildren().clear();
        aiPlayer2Box.getChildren().clear();
    }

    private ImageView createHiddenCard() {
        try {
            // Remove the leading slash
            String backPath = "resources/cards/back.png";
            ImageView imageView = new ImageView(new Image(new FileInputStream(backPath)));
            imageView.setFitWidth(100);
            imageView.setPreserveRatio(true);
            imageView.setRotate(180);
            imageView.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 10, 0, 0, 0);");
            return imageView;
        } catch (FileNotFoundException e) {
            System.out.println("Back card image not found at: " + e.getMessage());
            // Create a black rectangle as fallback
            Rectangle fallback = new Rectangle(100, 140);
            fallback.setFill(Color.BLACK);
            fallback.setStroke(Color.GREEN);
            ImageView fallbackView = new ImageView();
            fallbackView.setFitWidth(100);
            fallbackView.setFitHeight(140);
            return fallbackView;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}