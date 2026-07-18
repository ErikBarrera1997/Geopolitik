package com.msservices.geopolitik.connection.Views;

import java.sql.Connection;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class defenseQueries {

    private final Connection connection;

    public defenseQueries(Connection connection) {
        this.connection = connection;
    }

    public List<Map<String, Object>> getMilitaryInfrastructureByCountry(int idCountry) {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = """
            SELECT v.country_name, v.infraestructure_name, v.province_name, v.quantity, v.level
            FROM militaryInfraestructureCountry v
            JOIN Countries c ON c.name = v.country_name
            WHERE c.id_country = ?
            """;
        try (var stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idCountry);
            var rs = stmt.executeQuery();
            ResultSetMetaData meta = rs.getMetaData();
            int cols = meta.getColumnCount();
            while (rs.next()) {
                Map<String, Object> row = new LinkedHashMap<>();
                for (int i = 1; i <= cols; i++) {
                    row.put(meta.getColumnLabel(i), rs.getObject(i));
                }
                list.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Map<String, Object>> getTotalCountryDefensesByCountry(int idCountry) {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = """
            SELECT v.country_name, v.weapon_name, v.total_quantity
            FROM TotalCountryDefenses v
            JOIN Countries c ON c.name = v.country_name
            WHERE c.id_country = ?
            """;
        try (var stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idCountry);
            var rs = stmt.executeQuery();
            ResultSetMetaData meta = rs.getMetaData();
            int cols = meta.getColumnCount();
            while (rs.next()) {
                Map<String, Object> row = new LinkedHashMap<>();
                for (int i = 1; i <= cols; i++) {
                    row.put(meta.getColumnLabel(i), rs.getObject(i));
                }
                list.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Map<String, Object> getTotalCountryPopulationByCountry(int idCountry) {
        String sql = """
            SELECT v.country_name, v.total_population
            FROM totalCountryPopulation v
            JOIN Countries c ON c.name = v.country_name
            WHERE c.id_country = ?
            """;
        try (var stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idCountry);
            var rs = stmt.executeQuery();
            ResultSetMetaData meta = rs.getMetaData();
            int cols = meta.getColumnCount();
            if (rs.next()) {
                Map<String, Object> row = new LinkedHashMap<>();
                for (int i = 1; i <= cols; i++) {
                    row.put(meta.getColumnLabel(i), rs.getObject(i));
                }
                return row;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
