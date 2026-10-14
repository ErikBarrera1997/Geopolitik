package com.msservices.geopolitik.connection.queries.diplomacy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class countryResourceQueries {

    private final Connection connection;

    public countryResourceQueries(Connection connection) {
        this.connection = connection;
    }

    public List<countryResource> getCountryResources(int countryId) {
        List<countryResource> resources = new ArrayList<>();
        String sql = """
            SELECT r.name, COALESCE(SUM(tr.total_amount), 0) AS total_amount
            FROM TotalResources tr
            JOIN Resources r ON r.id_resource = tr.id_resource
            WHERE tr.id_country = ?
            GROUP BY r.id_resource, r.name
            ORDER BY r.name
            """;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, countryId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                resources.add(new countryResource(rs.getString("name"), rs.getInt("total_amount")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resources;
    }
}