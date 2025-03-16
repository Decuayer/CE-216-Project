import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class User extends FileHandler {
    private String username;
    private String passwordHash;
    private int age;
    private String email;
    private GameCatalog gameCatalog;
    private GameCatalog favoriteGames;

    public User() {}

    public User(String username, String password, int age, String email, GameCatalog gameCatalog, GameCatalog favoriteGames) {
        this.username = username;
        this.passwordHash = hashPassword(password);
        this.age = age;
        this.email = email;
        this.gameCatalog = gameCatalog;
        this.favoriteGames = favoriteGames;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.passwordHash = hashPassword(password);
    }

    public boolean checkPassword(String password) {
        return this.passwordHash.equals(hashPassword(password));
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
}