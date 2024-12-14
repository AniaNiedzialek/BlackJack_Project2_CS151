package com.game;

/*
 * Manages user accounts for the game application, handling account creation, login,
 * and persistent storage of user data in a file. This class ensures that user credentials
 * and high scores are saved across sessions, enabling users to log in and retrieve their data.
 */
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class UserAccountManager {
    // Create user account file
    private static final String FILE_NAME = "user_accounts.txt";

    // Write to user_accounts.txt
    public static void writeAccountFile(String username, String password) {
        // Create writer
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(username + " " + password + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Read user_accounts.txt file
    public static HashMap<String, String> readAccountFile() {
        HashMap<String, String> fileAccounts = new HashMap<>();
        // Create reader
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            // Create string for username and password
            String line;
            while ((line = reader.readLine()) != null) {
                // Create string array to split username and password
                String[] loginInfo = line.split(" ");
                // Check for fomat errors
                if (loginInfo.length == 2) {
                    // Put username and password into hashmap
                    String username = loginInfo[0].trim();
                    String password = loginInfo[1].trim();
                    fileAccounts.put(username, password);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileAccounts;
    }

    // Checks if login info is in hashmap
    public static boolean checkLoginInfo(String username, String password) {
        // Create hashmap
        HashMap<String, String> userAccounts = readAccountFile();
        // Check if username is in hashmap
        if(userAccounts.containsKey(username)){
            // Check if password matches with password in hashmap
            return password.equals(userAccounts.get(username));
        }
        // Returns false if username is not in hashmap
        return false;
    }

    //Checks if username exists
    public static boolean doesUsernameExist(String username) {
        HashMap<String, String> userAccounts = readAccountFile();
        if(userAccounts.containsKey(username)) {
            return true;
        } else {
            return false;
        }
    }
}