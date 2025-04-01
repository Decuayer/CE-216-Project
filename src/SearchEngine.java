import java.util.ArrayList;
import java.util.List;

public class SearchEngine extends User {
    public static List<Game> searchByTitle(List<Game> games, String title) {
        List<Game> results = new ArrayList<>();
        for (Game game : games) {
            if (game.getTitle().toLowerCase().contains(title.toLowerCase())) {
                results.add(game);
            }
        }
        return results;
    }

    public static List<Game> searchByDeveloper(List<Game> games, String developer) {
        List<Game> results = new ArrayList<>();
        for (Game game : games) {
            if (game.getDeveloper().equalsIgnoreCase(developer)) {
                results.add(game);
            }
        }
        return results;
    }

    public static List<Game> searchByGenre(List<Game> games, String genre) {
        List<Game> results = new ArrayList<>();
        for (Game game : games) {
            if (game.getGenre().contains(genre)) {
                results.add(game);
            }
        }
        return results;
    }

    public static List<Game> searchByYear(List<Game> games, int year) {
        List<Game> results = new ArrayList<>();
        for (Game game : games) {
            if (game.getReleaseYear() == year) {
                results.add(game);
            }
        }
        return results;
    }

    public static List<Game> searchByTag(List<Game> games, String tag) {
        List<Game> results = new ArrayList<>();
        for (Game game : games) {
            if (game.getTags().contains(tag)) {
                results.add(game);
            }
        }
        return results;
    }
    // tryout push pull for github, tryout 1
}
