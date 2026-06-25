package com.msservices.geopolitik.init;


//Load all resources before main page execution
public class loadGlobalVariables {

    public static void load() {
        loadDataBases.load();

        loadMatch.loadMatch(loadMatch.getJsonData());

        loadImages.getIcons();
        loadFlags.getFlags();
        loadWeapons.getWeapons();
        loadCombatActions.getCombatList();
        loadCombatActions.getOperationsList();
    }
}
