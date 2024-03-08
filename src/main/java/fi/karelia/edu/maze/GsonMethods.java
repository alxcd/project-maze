package fi.karelia.edu.maze;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class GsonMethods {
    public static void serializeToJson(String filePath, ArrayList<Player> arrayList) {
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(filePath), StandardCharsets.UTF_8)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(arrayList, writer);
            writer.flush();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    public static ArrayList<Player> deserializeFromJson(String filePath) {
        Type listType = new TypeToken<ArrayList<Player>>(){}.getType();
        ArrayList<Player> players;
        try (Reader reader = new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            players = gson.fromJson(reader, listType);
            return players;
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

}
