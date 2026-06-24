package com.msservices.geopolitik.init;

import com.google.gson.Gson;
import com.msservices.geopolitik.Match;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class loadMatch {

    public static Match match;
    private static Map<String, String> colorList = new HashMap<>();

    public static Map<String, String> getColorList() {
        return colorList;
    }

    public static List<String> getJsonData(){
        Path path1 = Paths.get("matchs", "match.json");
        Path path2 = Paths.get("..", "matchs", "match.json");
        Path path = Files.exists(path1) ? path1 : path2;
        try {
            return Files.readAllLines(path);
        } catch (IOException e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public static void loadMatch(List<String> jsonData){
        Gson gson = new Gson();
        String json = String.join("\n", jsonData);
        match = gson.fromJson(json, Match.class);
        getJsonColorList();
    }

    private static void getJsonColorList(){
        Path path1 = Paths.get("data", "middle_east_list.json");
        Path path2 = Paths.get("..", "data", "middle_east_list.json");
        Path path = Files.exists(path1) ? path1 : path2;
        try {
            Gson gson = new Gson();
            String json = Files.readString(path);
            Map<String, Object> root = gson.fromJson(json, Map.class);
            List<Map<String, Object>> list = (List<Map<String, Object>>) root.get("list");
            for (Map<String, Object> entry : list) {
                String key = String.format(Locale.US, "%.2f", entry.get("value"));
                colorList.put(key, (String) entry.get("name"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Match getMatch(){
        return match;
    }

}
