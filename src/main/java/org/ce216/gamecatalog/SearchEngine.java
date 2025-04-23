package org.ce216.gamecatalog;

import java.util.ArrayList;
import java.util.List;

public class SearchEngine {
    public static List<Game> searchGames(List<Game> games, String query) {
        List<Game> results = new ArrayList<>();
        String lowerQuery = query.toLowerCase();

        for (Game game : games) {
            if (
                    game.getTitle().toLowerCase().contains(lowerQuery) ||
                            game.getDeveloper().toLowerCase().contains(lowerQuery) ||
                            game.getPublisher().toLowerCase().contains(lowerQuery) ||
                            String.valueOf(game.getReleaseYear()).contains(lowerQuery) ||
                            game.getGenre().stream().anyMatch(g -> g.toLowerCase().contains(lowerQuery)) ||
                            game.getTags().stream().anyMatch(tag -> tag.toLowerCase().contains(lowerQuery)) ||
                            game.getPlatforms().stream().anyMatch(p -> p.toLowerCase().contains(lowerQuery)) ||
                            game.getTranslators().stream().anyMatch(t -> t.toLowerCase().contains(lowerQuery)) ||
                            game.getLanguage().toLowerCase().contains(lowerQuery)||
                        String.valueOf(game.getSteamID()).contains(lowerQuery)
            ) {
                results.add(game);
            }
        }

        return results;
    }
}
