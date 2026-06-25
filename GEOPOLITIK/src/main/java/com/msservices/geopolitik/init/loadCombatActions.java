package com.msservices.geopolitik.init;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.msservices.geopolitik.init.entity.combatOption;

import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class loadCombatActions {

    private static List<combatOption> combatList;
    private static List<combatOption> operationsList;

    private static final String JSON_PATH = "data/combat_options_list.json";

    public static List<combatOption> getCombatList() {
        if (combatList == null) {
            combatList = loadList("combat_list");
        }
        return combatList;
    }

    public static List<combatOption> getOperationsList() {
        if (operationsList == null) {
            operationsList = loadList("operations_list");
        }
        return operationsList;
    }

    private static List<combatOption> loadList(String key) {
        try {
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(new FileReader(JSON_PATH), JsonObject.class);
            Type listType = new TypeToken<List<combatOption>>(){}.getType();
            return gson.fromJson(jsonObject.get(key), listType);
        } catch (Exception e) {
            System.err.println("Error loading " + key + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
