package com.msservices.geopolitik.connection.queries.province;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

public class provinceQueries {

    private final Connection connection;

    private static final String[] RESOURCE_COLUMNS = {
        "Gas natural", "Petróleo", "Madera", "Pesca", "Uranio",
        "Oro", "Agricultura", "Acero", "Industria", "electricidad"
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
                (SELECT COALESCE(SUM(pr2."Ingresos"), 0) FROM ProvinceResources pr2
                 JOIN Provinces p2 ON pr2.id_provincia = p2.id_province
                 WHERE p2.id_country = p.id_country) AS totalIngresos
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
        String cols = String.join(", ", java.util.Arrays.stream(RESOURCE_COLUMNS)
                .map(c -> "\"" + c + "\"")
                .toArray(String[]::new));
        String sql = "SELECT " + cols + " FROM ProvinceResources WHERE id_provincia = ?";
        try (var stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idProvince);
            var rs = stmt.executeQuery();
            if (rs.next()) {
                for (String col : RESOURCE_COLUMNS) {
                    resources.put(col, rs.getInt(col));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resources;
    }
}
