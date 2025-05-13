package org.ce216.gamecatalog;

import java.io.File;
import java.io.FileNotFoundException;
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


        JSONArray jsonArray;
        if (!file.exists()) {
            jsonArray = new JSONArray();
        } else {
            String content = new String(Files.readAllBytes(file.toPath()));
            jsonArray = new JSONArray(content);
        }

        String newSteamID = game.getSteamID();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject existingGame = jsonArray.getJSONObject(i);
            if (existingGame.optString("steamID").equalsIgnoreCase(newSteamID)) {
                throw new IOException("Bu SteamID'ye sahip bir oyun zaten mevcut: " + newSteamID);
            }
        }
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
        } catch (IOException io) {
            throw new IOException("Eklediğiniz dosyada uygulamada mevcut olan bir steamID mevcut.");
        } catch (Exception e) {
            System.err.println("Bir hata oluştu: " + e.getMessage());
            throw new IOException("Game nesnesi oluşturulamadı.");
        }
    }

    public static void removeGameFromJson(String steamID) throws IOException {
        File file = new File(GAMESPATH);
        String content = new String(Files.readAllBytes(file.toPath()));
        JSONArray jsonArray = new JSONArray(content);

        boolean found = false;
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            if (obj.getString("steamID").equals(steamID)) {
                jsonArray.remove(i);
                found = true;
                break;
            }
        }

        if (!found) {
            throw new IOException("SteamID '" + steamID + "' ile eşleşen bir oyun bulunamadı.");
        }

        Files.write(file.toPath(), jsonArray.toString(4).getBytes());
    }

    public static int removeGamesMatchingSteamIDsFromFile(File fileWithGames) throws IOException {
        if (!fileWithGames.exists()) {
            throw new FileNotFoundException("Dosya bulunamadı: " + fileWithGames.getPath());
        }

        String content = new String(Files.readAllBytes(fileWithGames.toPath()));
        JSONArray importedGames = new JSONArray(content);

        int removedCount = 0;

        for (int i = 0; i < importedGames.length(); i++) {
            JSONObject gameJson = importedGames.getJSONObject(i);
            String steamID = gameJson.getString("steamID");

            try {
                removeGameFromJson(steamID);  // Zaten mevcut fonksiyon
                removedCount++;
            } catch (IOException e) {
                System.err.println("SteamID ile silme başarısız: " + steamID);
            }
        }

        if (removedCount > 0) {
            boolean deleted = fileWithGames.delete();
            if (!deleted) {
                System.err.println("Dosya silinemedi: " + fileWithGames.getName());
            }
        }

        return removedCount;
    }

}

