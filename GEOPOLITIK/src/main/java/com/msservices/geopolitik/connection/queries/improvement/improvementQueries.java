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
        String sql = "SELECT id_improvement, name, price FROM Improvements";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id_improvement");
                String name = rs.getString("name");
                Object priceObj = rs.getObject("price");
                Double price = priceObj == null ? null : rs.getDouble("price");
                improvements.add(new Improvement(id, name, price));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return improvements;
    }
}
