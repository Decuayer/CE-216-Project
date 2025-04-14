package org.ce216.gamecatalog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private static final String FILE_PATH = "data/users.json";
    private static final Gson gson = new Gson();

    public static void saveUsers(List<User> users) throws IOException {
      //  System.out.println("deneme");
        File file = new File(FILE_PATH);
      //  System.out.println("path bu " + file.getAbsolutePath());
        file.getParentFile().mkdirs();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try(Writer writer = new FileWriter(file)) {
            gson.toJson(users, writer);
        }
    }

    public static List<User> getUsers() throws IOException {
        File file = new File(FILE_PATH);
      //  System.out.println("path deneme " + file.getAbsolutePath());
        if(!file.exists()) { return new ArrayList<>(); }
        try(Reader reader = new FileReader(file)) {
            return gson.fromJson(reader, new TypeToken<List<User>>(){}.getType());
        }
    }

}
