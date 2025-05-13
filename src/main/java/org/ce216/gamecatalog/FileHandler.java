package org.ce216.gamecatalog;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import javafx.print.PrintColor;
import org.json.*;

public class FileHandler {
    private static final String GAMESPATH = "data/games.json";
    private static final String USERSPATH = "data/users.json";

    public static List<Game> loadFromJSONGames() throws IOException {
        String content = new String(Files.readAllBytes(new File(GAMESPATH).toPath()));
        JSONArray jsonArray = new JSONArray(content);
        List<Game> games = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            games.add(Game.fromJSON(jsonArray.getJSONObject(i).toString()));
        }
        return games;
    }

    public static List<User> loadFromJSONUsers() throws IOException {
        String content = new String(Files.readAllBytes(new File(USERSPATH).toPath()));
        JSONArray jsonArray = new JSONArray(content);
        List<User> users = new ArrayList<>();
        for(int i = 0; i < jsonArray.length(); i++) {
            users.add(User.fromJSON(jsonArray.getJSONObject(i).toString()));
        }
        return users;
    }

    public static void addGameToUserCatalog(String username, String steamID) throws IOException {
        File userFile = new File(USERSPATH);
        String content = new String(Files.readAllBytes(userFile.toPath()));
        JSONArray usersArray = new JSONArray(content);

        for (int i = 0; i < usersArray.length(); i++) {
            JSONObject userObj = usersArray.getJSONObject(i);
            if (userObj.getString("username").equals(username)) {
                JSONArray gameCatalog = userObj.getJSONArray("gameCatalog");

                boolean alreadyExists = false;
                for (int j = 0; j < gameCatalog.length(); j++) {
                    if (gameCatalog.getString(j).equals(steamID)) {
                        alreadyExists = true;
                        break;
                    }
                }

                if (!alreadyExists) {
                    gameCatalog.put(steamID);
                    System.out.println("SteamID " + steamID + " added to user " + username);
                } else {
                    System.out.println("SteamID " + steamID + " already exists for user " + username);
                }

                Files.write(userFile.toPath(), usersArray.toString(4).getBytes());
                return;
            }
        }

        System.out.println("User not found: " + username);
    }

    public static void removeGameFromUserCatalog(String username, String steamID) throws IOException {
        File userFile = new File(USERSPATH);
        String content = new String(Files.readAllBytes(userFile.toPath()));
        JSONArray usersArray = new JSONArray(content);

        for (int i = 0; i < usersArray.length(); i++) {
            JSONObject userObj = usersArray.getJSONObject(i);
            if (userObj.getString("username").equals(username)) {
                JSONArray gameCatalog = userObj.getJSONArray("gameCatalog");

                for (int j = 0; j < gameCatalog.length(); j++) {
                    if (gameCatalog.getString(j).equals(steamID)) {
                        gameCatalog.remove(j);
                        System.out.println("SteamID " + steamID + " removed from user " + username);
                        break;
                    }
                }

                Files.write(userFile.toPath(), usersArray.toString(4).getBytes());
                return;
            }
        }

        System.out.println("User not found: " + username);
    }

    public static void addGameToJson(Game game) throws IOException {
        File file = new File(GAMESPATH);
        String content = new String(Files.readAllBytes(file.toPath()));
        JSONArray jsonArray = new JSONArray(content);

        JSONObject newGameJson = new JSONObject(game.toJSON());
        jsonArray.put(newGameJson);

        Files.write(file.toPath(), jsonArray.toString(4).getBytes());
    }

    public static void importGameFromJsonFile(String filePath) throws IOException {
        File importFile = new File(filePath);

        if (!importFile.exists()) {
            throw new IOException("Belirtilen dosya bulunamadı: " + filePath);
        }

        try {
            String content = new String(Files.readAllBytes(importFile.toPath()));
            JSONArray gameJson = new JSONArray(content);

            List<Game> games = new ArrayList<>();
            for (int i = 0; i < gameJson.length(); i++) {
                games.add(Game.fromJSON(gameJson.getJSONObject(i).toString()));
            }
            for (Game game : games) {
                addGameToJson(game);
                System.out.println("Yeni oyun başarıyla eklendi: " + game.getTitle());
            }

        } catch (JSONException je) {
            System.err.println("JSON formatı hatalı: " + je.getMessage());
            throw new IOException("Geçersiz JSON formatı.");
        } catch (Exception e) {
            System.err.println("Bir hata oluştu: " + e.getMessage());
            throw new IOException("Game nesnesi oluşturulamadı.");
        }
    }

}

