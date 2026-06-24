package com.msservices.geopolitik.connection;

import com.msservices.geopolitik.connection.Data.Weapon;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class weaponQueries {

    private final Connection connection;

    public weaponQueries(Connection connection) {
        this.connection = connection;
    }

    public List<Weapon> getWeapons() {
        List<Weapon> weapons = new ArrayList<>();
        String sql = "SELECT id_weapon, name, cost, type FROM Weapons";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                weapons.add(new Weapon(
                        rs.getInt("id_weapon"),
                        rs.getString("name"),
                        rs.getDouble("cost"),
                        rs.getString("type")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return weapons;
    }
}
