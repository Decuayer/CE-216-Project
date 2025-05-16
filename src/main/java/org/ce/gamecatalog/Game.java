package org.ce.gamecatalog;

import java.util.*;
import org.json.*;



public class Game {
    private String title;
    private List<String> genre;
    private String developer;
    private String publisher;
    private List<String> platforms;
    private List<String> translators;
    private String steamID;
    private int releaseYear;
    private double playtime;
    private String format;
    private String language;
    private double rating;
    private List<String> tags;
    private String coverImagePath;
    private String description;

    public String getTitle() {return title;}
    public List<String> getGenre() {return genre;}
    public String getDeveloper() {return developer;}
    public String getPublisher() {return publisher;}
    public List<String> getPlatforms() {return platforms;}
    public String getSteamID() {return steamID;}
    public List<String> getTranslators() {return translators;}
    public int getReleaseYear() {return releaseYear;}
    public double getPlaytime() {return playtime;}
    public String getFormat() {return format;}
    public String getLanguage() {return language;}
    public double getRating() {return rating;}
    public List<String> getTags() {return tags;}
    public String getCoverImagePath() {return coverImagePath;}
    public String getDescription() {return description;}
    public double getPlayTime() {return playtime;}


    public Game(String title, List<String> genre, String developer, String publisher, List<String> platforms, List<String> translators, String steamID, int releaseYear, double playtime, String format, String language, double rating, List<String> tags, String coverImagePath, String description) {
        this.title = title;
        this.genre = genre;
        this.developer = developer;
        this.publisher = publisher;
        this.platforms = platforms;
        this.translators = translators;
        this.steamID = steamID;
        this.releaseYear = releaseYear;
        this.playtime = playtime;
        this.format = format;
        this.language = language;
        this.rating = rating;
        this.tags = tags;
        this.coverImagePath = coverImagePath;
        this.description = description;
    }


    public String toJSON() {
        JSONObject json = new JSONObject();
        json.put("title", title);
        json.put("genre", genre);
        json.put("developer", developer);
        json.put("publisher", publisher);
        json.put("platforms", platforms);
        json.put("translators", translators);
        json.put("steamID", steamID);
        json.put("releaseYear", releaseYear);
        json.put("playtime", playtime);
        json.put("format", format);
        json.put("language", language);
        json.put("rating", rating);
        json.put("tags", tags);
        json.put("coverImagePath", coverImagePath);
        json.put("description", description);
        return json.toString(4);
    }

    public static Game fromJSON(String jsonStr) {
        JSONObject json = new JSONObject(jsonStr);
        return new Game(
                json.getString("title"),
                json.has("genre") ? jsonArrayToList(json.getJSONArray("genre")) : new ArrayList<>(),
                json.getString("developer"),
                json.getString("publisher"),
                json.has("platforms") ? jsonArrayToList(json.getJSONArray("platforms")) : new ArrayList<>(),
                json.has("translators") ? jsonArrayToList(json.getJSONArray("translators")) : new ArrayList<>(),
                json.getString("steamID"),
                json.getInt("releaseYear"),
                json.has("playtime") ? json.getDouble("playtime") : 0.0,
                json.getString("format"),
                json.getString("language"),
                json.has("rating") ? json.getDouble("rating") : 0.0,
                json.has("tags") ? jsonArrayToList(json.getJSONArray("tags")) : new ArrayList<>(),
                json.getString("coverImagePath"),
                json.getString("description")
        );
    }

    private static List<String> jsonArrayToList(JSONArray jsonArray) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            list.add(jsonArray.getString(i));
        }
        return list;
    }

    @Override
    public String toString() {
        return this.title;
    }


}

