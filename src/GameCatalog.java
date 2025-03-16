import java.util.ArrayList;
import java.util.List;

public class GameCatalog {
    private List<Game> games = new ArrayList<>();


    public void addGame(Game Game) {
        games.add(Game);
    }
    public void removeGame(String steamID) {
        games.removeIf(game -> game.getSteamID().equals(steamID));
    }
    public void updateGame(Game game) {}
    public List<Game> listGames() {
        return games;
    }
}
