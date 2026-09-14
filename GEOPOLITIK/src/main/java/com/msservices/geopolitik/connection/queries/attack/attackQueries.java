package com.msservices.geopolitik.connection.queries.attack;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class attackQueries {

    private final Connection connection;

    public attackQueries(Connection connection) {
        this.connection = connection;
    }

    public List<ProvinceObjective> getCivilInfrastructureByCountry(int idCountry) {
        return getInfrastructureByType(idCountry, "Civil");
    }

    public List<ProvinceObjective> getMilitaryInfrastructureByCountry(int idCountry) {
        return getInfrastructureByType(idCountry, "Militar");
    }

    private List<ProvinceObjective> getInfrastructureByType(int idCountry, String type) {
        List<ProvinceObjective> result = new ArrayList<>();
        String sql = """
            SELECT
                p.id_province,
                p.name AS province_name,
                pi.id_infraestructure,
                i.name AS infra_name,
                pi.quantity
            FROM Provinces p
            LEFT JOIN ProvinceInfraestructure pi
                   ON pi.id_province = p.id_province
                  AND pi.id_infraestructure IN (SELECT id_infraestructure FROM Infraestructure WHERE type = ?)
            LEFT JOIN Infraestructure i ON i.id_infraestructure = pi.id_infraestructure
            WHERE p.id_country = ?
            ORDER BY p.id_province, i.name
            """;
        try (var stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, type);
            stmt.setInt(2, idCountry);
            var rs = stmt.executeQuery();
            while (rs.next()) {
                result.add(new ProvinceObjective(
                        rs.getInt("id_province"),
                        rs.getString("province_name"),
                        rs.getInt("id_infraestructure"),
                        rs.getString("infra_name"),
                        rs.getInt("quantity")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<ProvinceObjective> getCiviliansByCountry(int idCountry) {
        List<ProvinceObjective> result = new ArrayList<>();
        String sql = """
            SELECT
                p.id_province,
                p.name AS province_name,
                p.population
            FROM Provinces p
            WHERE p.id_country = ?
            ORDER BY p.id_province
            """;
        try (var stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idCountry);
            var rs = stmt.executeQuery();
            while (rs.next()) {
                int idProvince = rs.getInt("id_province");
                result.add(new ProvinceObjective(
                        idProvince,
                        rs.getString("province_name"),
                        idProvince,
                        "Población civil",
                        rs.getInt("population")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<ProvinceObjective> getMilitaryObjectivesByCountry(int idCountry) {
        List<ProvinceObjective> result = new ArrayList<>();
        String sql = """
            SELECT
                p.id_province,
                p.name AS province_name,
                w.id_weapon,
                w.name AS weapon_name,
                pd.quantity
            FROM ProvinceDefenses pd
            JOIN Provinces p ON pd.id_province = p.id_province
            JOIN Weapons w ON pd.id_weapon = w.id_weapon
            WHERE p.id_country = ?
            ORDER BY p.id_province, w.name
            """;
        try (var stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idCountry);
            var rs = stmt.executeQuery();
            while (rs.next()) {
                result.add(new ProvinceObjective(
                        rs.getInt("id_province"),
                        rs.getString("province_name"),
                        rs.getInt("id_weapon"),
                        rs.getString("weapon_name"),
                        rs.getInt("quantity")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}