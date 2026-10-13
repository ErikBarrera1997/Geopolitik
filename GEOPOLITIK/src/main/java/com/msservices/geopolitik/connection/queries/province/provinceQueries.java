package com.msservices.geopolitik.connection.queries.province;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

public class provinceQueries {

    private final Connection connection;

    private static final String[] RESOURCE_COLUMNS = {
        "Gas natural", "Petróleo", "Madera", "Pesca", "Uranio",
        "Oro", "Agricultura", "Agrícultura", "Acero", "Industria", "electricidad"
    };

    public provinceQueries(Connection connection) {
        this.connection = connection;
    }

    public ProvinceData getProvinceData(String provinceName, int countryId) {
        String sql = """
            SELECT
                p.id_province,
                p.name,
                p.id_country,
                (SELECT COALESCE(SUM(population), 0) FROM Provinces WHERE id_country = p.id_country) AS totalPopulation,
                (SELECT COALESCE(SUM(pr2.production), 0)
                 FROM ProvinceResources pr2
                 JOIN Provinces p2 ON pr2.id_province = p2.id_province
                 LEFT JOIN Resources r2 ON r2.id_resource = pr2.id_resource
                 WHERE p2.id_country = p.id_country AND r2.name = 'Dinero') AS totalIngresos
            FROM Provinces p
            WHERE p.name = ? AND p.id_country = ?
            """;

        try (var stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, provinceName);
            stmt.setInt(2, countryId);
            var rs = stmt.executeQuery();
            if (rs.next()) {
                int idProvince = rs.getInt("id_province");
                String name = rs.getString("name");
                int idCountry = rs.getInt("id_country");
                long totalPopulation = rs.getLong("totalPopulation");
                int totalIngresos = rs.getInt("totalIngresos");
                Map<String, Integer> resources = getProvinceResources(idProvince);
                return new ProvinceData(idProvince, name, idCountry, totalPopulation, totalIngresos, resources);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Map<String, Integer> getProvinceResources(int idProvince) {
        Map<String, Integer> resources = new LinkedHashMap<>();
        String sql = """
            SELECT r.name, pr.production
            FROM ProvinceResources pr
            JOIN Resources r ON r.id_resource = pr.id_resource
            WHERE pr.id_province = ?
            ORDER BY r.id_resource
            """;
        try (var stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idProvince);
            var rs = stmt.executeQuery();
            while (rs.next()) {
                String name = rs.getString("name");
                int prod = rs.getInt("production");
                if (name != null && !name.isEmpty()) {
                    resources.put(name, prod);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resources;
    }
}
