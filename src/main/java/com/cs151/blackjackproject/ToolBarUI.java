package com.cs151.blackjackproject;

import javafx.scene.layout.HBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;

public class ToolBarUI extends HBox {
    
    private Button menuButton;

    public ToolBarUI() {
        
        // Create Main Menu Button
        menuButton = new Button("Main Menu");
        this.getChildren().addAll(menuButton);
        this.setSpacing(10);
        this.setPadding(new Insets(5));
        this.setAlignment(Pos.TOP_LEFT);

    }

    public Button getMenuButton() {
        return menuButton;
    }

}
