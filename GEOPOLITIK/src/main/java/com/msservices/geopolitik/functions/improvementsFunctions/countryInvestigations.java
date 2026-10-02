package com.msservices.geopolitik.functions.improvementsFunctions;


import com.msservices.geopolitik.connection.queries.improvement.Improvement;

import java.util.HashMap;
import java.util.Map;

/*
Save all investigations on list, separate them by country
 */
public final class countryInvestigations extends investigations {

    private static final countryInvestigations INSTANCE = new countryInvestigations();

    //Integer value represents id country
    private final Map<Integer, investigations> list = new HashMap<>();

    private countryInvestigations() {
    }

    public static countryInvestigations getInstance() {
        return INSTANCE;
    }

    //Keeps the investigations already saved for that country
    public investigations addInvestigations(int index) {
        return list.computeIfAbsent(index, id -> new investigations());
    }

    public investigations getInvestigations(int idPais) {
        return list.get(idPais);
    }

    public void removeInvestigations(int index) {
        list.remove(index);
    }

    public void addImprovement(int idPais, Improvement improvement) {
        addInvestigations(idPais).addImprovement(improvement);
    }
}