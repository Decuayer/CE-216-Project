package org.ce216.gamecatalog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileReader;
import java.io.IOException;

public class GameLoader {
        public static JSONArray loadGamesFromJson(String filePath) throws IOException {
            FileReader reader = new FileReader(filePath);
            StringBuilder jsonContent = new StringBuilder();
            int c;
            while ((c = reader.read()) != -1) {
                jsonContent.append((char) c);
            }
            reader.close();

            return new JSONArray(jsonContent.toString());
        }
}
