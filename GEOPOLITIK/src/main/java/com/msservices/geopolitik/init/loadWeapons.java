package com.msservices.geopolitik.init;

import com.msservices.geopolitik.connection.DatabaseConnection;
import com.msservices.geopolitik.connection.weaponQueries;
import com.msservices.geopolitik.connection.Data.Weapon;

import java.util.List;

public class loadWeapons {

    private static List<Weapon> weapons;

    public static List<Weapon> getWeapons() {
        if (weapons == null) {
            weaponQueries queries = new weaponQueries(DatabaseConnection.getInstance().getConnection());
            weapons = queries.getWeapons();
        }
        return weapons;
    }


}
