package com.msservices.geopolitik.init;

import com.google.gson.Gson;
import com.msservices.geopolitik.Match;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class loadMatch {

    public static Match match;

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
    }

    public static Match getMatch(){
        return match;
    }

}
