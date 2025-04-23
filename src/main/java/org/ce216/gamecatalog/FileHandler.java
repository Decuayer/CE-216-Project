package org.ce216.gamecatalog;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import org.json.*;

public class FileHandler {

    public static List<Game> loadGamesFromFile(String filePath) throws IOException {
        String content = new String(Files.readAllBytes(new File(filePath).toPath()));
        JSONArray jsonArray = new JSONArray(content);
        List<Game> games = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            games.add(Game.fromJSON(jsonArray.getJSONObject(i).toString()));
        }
        return games;
    }
    public static void saveGamesToFile(List<Game> games, String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("File does not exist.");
        } else {
            System.out.println("File exists.");
        }

        // Create a JSON array to store game data
        JSONArray jsonArray = new JSONArray();
        for (Game game : games) {
            jsonArray.put(new JSONObject(game.toJSON()));
        }

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(jsonArray.toString(4));  // Write with indentation for readability
            System.out.println("Game data saved to " + filePath);
        } catch (IOException e) {
            System.err.println("Failed to save file: " + e.getMessage());
        }
    }
}
