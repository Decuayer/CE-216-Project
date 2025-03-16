import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import org.json.*;

public class FileHandler {
    public static List<Game> loadFromJSON(String filePath) throws IOException {
        String content = new String(Files.readAllBytes(new File(filePath).toPath()));
        JSONArray jsonArray = new JSONArray(content);
        List<Game> games = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            games.add(Game.fromJSON(jsonArray.getJSONObject(i).toString()));
        }
        return games;
    }

    public static void saveToJSON(String filePath, List<Game> games) throws IOException {
        JSONArray jsonArray = new JSONArray();
        for (Game game : games) {
            jsonArray.put(new JSONObject(game.toJSON()));
        }
        Files.write(new File(filePath).toPath(), jsonArray.toString(4).getBytes());
    }
}
