package com.game;

// Singleton Class that handles the current user

public class SessionManager {
    // Create static instance of SessionManager
    private static SessionManager instance;
    private String currentUser;
    private String currentScore;
    
    // Private constructor to force one instance
    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    public String getCurrentScore() {
        return currentScore;
    }

    public void setCurrentScore(String currentScore) {
        this.currentScore = currentScore;
    }

}
