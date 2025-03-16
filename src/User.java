public class User {
    private String username;
    private String password;
    private int age;
    private String email;
    private GameCatalog gameCatalog;
    private GameCatalog favoriteGames;

    public User() {}

    public User(String username, String password, int age, String email, GameCatalog gameCatalog, GameCatalog favoriteGames) {
        this.username = username;
        this.password = password;
        this.age = age;
        this.email = email;
        this.gameCatalog = gameCatalog;
        this.favoriteGames = favoriteGames;
    }
}
