import java.util.*;


public class Game extends FileHandler {
    private String title;
    private List<String> genre;
    private String developer;
    private String publisher;
    private List<String> platforms;
    private List<String> translators;
    private String steamId;
    private int steamID;
    private int relaseYear;
    private double playtime;
    private String format;
    private String language;
    private double rating;
    private List<String> tags;
    private String coverImagePath;

    public Game(String title, List<String> genre, String developer, String publisher, List<String> platforms, List<String> translators, String steamId, int steamID, int relaseYear, double playtime, String format, String language, double rating, List<String> tags, String coverImagePath) {
        this.title = title;
        this.genre = genre;
        this.developer = developer;
        this.publisher = publisher;
        this.platforms = platforms;
        this.translators = translators;
        this.steamId = steamId;
        this.steamID = steamID;
        this.relaseYear = relaseYear;
        this.playtime = playtime;
        this.format = format;
        this.language = language;
        this.rating = rating;
        this.tags = tags;
        this.coverImagePath = coverImagePath;
    }


    public String toJSON() {
        return null;
    }

    public Game (String json) {}
}
