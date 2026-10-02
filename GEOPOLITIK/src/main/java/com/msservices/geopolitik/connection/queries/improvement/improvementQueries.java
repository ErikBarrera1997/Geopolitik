package com.msservices.geopolitik.connection.queries.improvement;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class improvementQueries {

    private final Connection connection;

    public improvementQueries(Connection connection) {
        this.connection = connection;
    }

    public List<Improvement> getImprovements() {
        List<Improvement> improvements = new ArrayList<>();
        String sql = """
            SELECT id_improvement, name, production_value, price, type, description, duration
            FROM Improvements
            ORDER BY id_improvement
            """;

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id_improvement");
                String name = rs.getString("name");
                Object productionValueObj = rs.getObject("production_value");
                Double productionValue = productionValueObj == null ? null : rs.getDouble("production_value");
                Object priceObj = rs.getObject("price");
                Double price = priceObj == null ? null : rs.getDouble("price");
                String type = rs.getString("type");
                String description = rs.getString("description");
                int duration = rs.getInt("duration");
                improvements.add(new Improvement(id, name, productionValue, price, type, description, duration));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return improvements;
    }

    public Improvement getImprovementById(int idImprovement) {
        String sql = """
            SELECT id_improvement, name, production_value, price, type, description, duration
            FROM Improvements
            WHERE id_improvement = ?
            """;

        try (var stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idImprovement);
            var rs = stmt.executeQuery();
            if (rs.next()) {
                Object productionValueObj = rs.getObject("production_value");
                Double productionValue = productionValueObj == null ? null : rs.getDouble("production_value");
                Object priceObj = rs.getObject("price");
                Double price = priceObj == null ? null : rs.getDouble("price");
                return new Improvement(
                        rs.getInt("id_improvement"),
                        rs.getString("name"),
                        productionValue,
                        price,
                        rs.getString("type"),
                        rs.getString("description"),
                        rs.getInt("duration")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
