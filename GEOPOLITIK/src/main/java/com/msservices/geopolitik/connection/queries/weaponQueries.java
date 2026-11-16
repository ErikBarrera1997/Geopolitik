package com.msservices.geopolitik.connection.queries;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class weaponQueries {

    public record Weapon(int idWeapon, String name, double cost, String type) {
    }


    private final Connection connection;

    public weaponQueries(Connection connection) {
        this.connection = connection;
    }

    public List<Weapon> getWeapons() {
        List<Weapon> weapons = new ArrayList<>();
        String sql = "SELECT id_weapon, name, cost, type FROM Weapons WHERE type NOT IN ('Infantería', 'Fuerzas especiales', 'Insurgencia', 'Mercenarios')";

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

    public List<defenseQueries.ArmyUnit> getArmy(int idCountry) {
        List<defenseQueries.ArmyUnit> army = new ArrayList<>();
        String sql = """
            SELECT w.name, SUM(pd.quantity) AS totalQuantity, AVG(p.morale) AS avgMorale, w.type
            FROM ProvinceDefenses pd
            JOIN Weapons w ON pd.id_weapon = w.id_weapon
            JOIN Provinces p ON pd.id_province = p.id_province
            WHERE p.id_country = ?
            GROUP BY w.name, w.type
            """;

        try (var stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idCountry);
            var rs = stmt.executeQuery();
            while (rs.next()) {
                army.add(new defenseQueries.ArmyUnit(
                    rs.getString("name"),
                    rs.getInt("totalQuantity"),
                    rs.getDouble("avgMorale"),
                    rs.getString("type")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return army;
    }
}
