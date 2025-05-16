package org.ce.gamecatalog;

import java.io.IOException;
import java.util.*;
import org.json.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class User extends FileHandler {
    private String username;
    private String passwordHash;
    private int age;
    private String email;
    private List<Game> gameCatalog;
    private List<Game> favoriteGames;
    private String favoriteGenre;


    public User(String username, String password, int age, String email, List<Game> gameCatalog, List<Game> favoriteGames, String favoriteGenre) {
        this.username = username;
        this.passwordHash = password;
        this.age = age;
        this.email = email;
        this.gameCatalog = gameCatalog;
        this.favoriteGames = favoriteGames;
        this.favoriteGenre = favoriteGenre;
    }


    public String getUsername() {return username;}
    public int getAge() {return age;}
    public String getEmail() {return email;}
    public List<Game> getGameCatalog() {return gameCatalog;}
    public List<Game> getFavoriteGames() {return favoriteGames;}
    public String getFavoriteGenre() {return favoriteGenre;}
    public String toJSON() {
        JSONObject json = new JSONObject();
        json.put("username", username);
        json.put("passwordHash", passwordHash);
        json.put("age", age);
        json.put("email", email);
        JSONArray catalogIDs = new JSONArray();
        for (Game game : gameCatalog) {
            catalogIDs.put(game.getSteamID());
        }
        json.put("gameCatalog", catalogIDs);
        JSONArray favoriteIDs = new JSONArray();
        for (Game game : favoriteGames) {
            favoriteIDs.put(game.getSteamID());
        }
        json.put("favoriteGames", favoriteIDs);
        json.put("favoriteGenre", favoriteGenre);
        return json.toString(4);
    }

    public static User fromJSON(String jsonStr) {
        JSONObject json = new JSONObject(jsonStr);

        FileHandler fh = new FileHandler();
        List<Game> allgames;
        try {
            allgames = fh.loadFromJSONGames();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String username = json.getString("username");
        String passwordHash = json.getString("passwordHash");
        int age = json.getInt("age");
        String email = json.getString("email");
        String favoriteGenre = json.getString("favoriteGenre");

        List<Game> gameCatalog = getGamesBySteamIDs(json.getJSONArray("gameCatalog"), allgames);
        List<Game> favoriteGames  = getGamesBySteamIDs(json.getJSONArray("favoriteGames"), allgames);

        return new User(username, passwordHash, age, email, gameCatalog, favoriteGames, favoriteGenre);
    }

    private static List<Game> getGamesBySteamIDs(JSONArray steamIds, List<Game> allgames) {
        List<Game> games = new ArrayList<>();

        for(int i = 0; i < steamIds.length();i++) {
            String steamID = steamIds.getString(i);
            for(Game game : allgames) {
                if(game.getSteamID().equals(steamID)) {
                    games.add(game);
                    break;
                }
            }
        }

        return games;
    }


    public void setPassword(String password) {
        this.passwordHash = hashPassword(password);
    }
    public String getPasswordHash() { return passwordHash; }
    public boolean checkPassword(String password) {
        return this.passwordHash.equals(hashPassword(password));
    }

    public String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }


}
