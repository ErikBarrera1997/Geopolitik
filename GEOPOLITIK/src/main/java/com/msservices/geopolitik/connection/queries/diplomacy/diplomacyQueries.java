package com.msservices.geopolitik.connection.queries.diplomacy;

import java.sql.Connection;
import java.sql.SQLException;

public class diplomacyQueries {

    private final Connection connection;

    public diplomacyQueries(Connection connection) {
        this.connection = connection;
    }

    public CountryDiplomacyStatus getCountryDiplomacyStatus(int idCountry) {
        String sql = """
            SELECT cds.id_country, c.name AS countryName,
                   cds.allied, c1.name AS alliedName,
                   cds.enemy, c2.name AS enemyName,
                   cds.in_peace, c3.name AS peaceName,
                   cds.in_war, c4.name AS warName,
                   cds.neutral, c5.name AS neutralName,
                   cds.vassal, c6.name AS vassalName,
                   cds.trade_agreement, c7.name AS tradeName,
                   cds.military_alliance, c8.name AS militaryName
            FROM CountryDiplomacyStatus cds
            JOIN Countries c ON c.id_country = cds.id_country
            LEFT JOIN Countries c1 ON c1.id_country = cds.allied
            LEFT JOIN Countries c2 ON c2.id_country = cds.enemy
            LEFT JOIN Countries c3 ON c3.id_country = cds.in_peace
            LEFT JOIN Countries c4 ON c4.id_country = cds.in_war
            LEFT JOIN Countries c5 ON c5.id_country = cds.neutral
            LEFT JOIN Countries c6 ON c6.id_country = cds.vassal
            LEFT JOIN Countries c7 ON c7.id_country = cds.trade_agreement
            LEFT JOIN Countries c8 ON c8.id_country = cds.military_alliance
            WHERE cds.id_country = ?
            """;

        try (var stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idCountry);
            var rs = stmt.executeQuery();
            if (rs.next()) {
                return new CountryDiplomacyStatus(
                        rs.getInt("id_country"),
                        rs.getString("countryName"),
                        rs.getInt("allied"),
                        rs.getString("alliedName"),
                        rs.getInt("enemy"),
                        rs.getString("enemyName"),
                        rs.getInt("in_peace"),
                        rs.getString("peaceName"),
                        rs.getInt("in_war"),
                        rs.getString("warName"),
                        rs.getInt("neutral"),
                        rs.getString("neutralName"),
                        rs.getInt("vassal"),
                        rs.getString("vassalName"),
                        rs.getInt("trade_agreement"),
                        rs.getString("tradeName"),
                        rs.getInt("military_alliance"),
                        rs.getString("militaryName")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public DiplomacyRate getDiplomacyRate(int idCountry, int neighborId) {
        String sql = """
            SELECT dr.morale, dr.friendship_rate,
                   c1.name AS countryName, c2.name AS neighborName
            FROM DiplomacyRate dr
            JOIN Countries c1 ON c1.id_country = dr.id_country
            JOIN Countries c2 ON c2.id_country = dr.neighbor
            WHERE dr.id_country = ? AND dr.neighbor = ?
            """;

        try (var stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idCountry);
            stmt.setInt(2, neighborId);
            var rs = stmt.executeQuery();
            if (rs.next()) {
                return new DiplomacyRate(
                        idCountry,
                        rs.getString("countryName"),
                        neighborId,
                        rs.getString("neighborName"),
                        rs.getDouble("morale"),
                        rs.getDouble("friendship_rate")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
