package com.msservices.geopolitik.connection;

import com.msservices.geopolitik.connection.Views.ProvinceDefense;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class defensesQueries {

    private final Connection connection;

    public defensesQueries(Connection connection) {
        this.connection = connection;
    }

    public List<ProvinceDefense> getProvinceDefenses(int idProvince) {
        List<ProvinceDefense> defenses = new ArrayList<>();
        String sql = """
            SELECT w.name, pd.quantity
            FROM ProvinceDefenses pd
            JOIN Weapons w ON pd.id_weapon = w.id_weapon
            WHERE pd.id_province = ?
            """;
        try (var stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idProvince);
            var rs = stmt.executeQuery();
            while (rs.next()) {
                defenses.add(new ProvinceDefense(
                    rs.getString("name"),
                    rs.getInt("quantity")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return defenses;
    }
}
