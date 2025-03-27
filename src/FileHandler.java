import org.json.JSONArray;
import org.json.JSONObject;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.io.IOException;

public class FileHandler {
    public static void main(String[] args) {
        try {
            String filePath = "game.json";

            if (!Files.exists(Paths.get(filePath))) {
                throw new IOException("Error: JSON file not found!");
            }

            String content = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);

            if (content.trim().startsWith("[")) {
                JSONArray jsonArray = new JSONArray(content);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json = jsonArray.getJSONObject(i);
                    //DEFAULT VALUES
                    String title = json.optString("title", "Unknown Title");
                    int releaseYear = json.optInt("releaseYear", 2000);
                    double rating = json.optDouble("rating", 0.0);

                    System.out.println("Title: " + title);
                    System.out.println("Release Year: " + releaseYear);
                    System.out.println("Rating: " + rating);
                    System.out.println("----------------------");
                }
            } else {
                System.out.println("Error: JSON file must be an array.");
            }

        } catch (IOException e) {
            System.err.println("File Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("JSON Parsing Error: " + e.getMessage());
        }
    }
}
