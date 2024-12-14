package com.game;

import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class ToolBarUI extends HBox {

    private Button menuButton;

    public ToolBarUI() {
        // Create Main Menu Button
        menuButton = new Button("Main Menu");
        menuButton.setStyle("-fx-background-color: #6C63FF; -fx-text-fill: white; -fx-font-size: 14px;");
        
        this.getChildren().addAll(menuButton);
        this.setSpacing(10);
        this.setPadding(new Insets(5));
        this.setAlignment(Pos.TOP_LEFT);
    }

    public Button getMenuButton() {
        return menuButton;
    }
}

