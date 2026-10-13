package com.msservices.geopolitik.connection.queries.country;

import com.msservices.geopolitik.connection.queries.province.Province;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class countryQueries {

    private final Connection connection;

    public countryQueries(Connection connection) {
        this.connection = connection;
    }

    public List<Country> getCountries() {
        List<Country> countries = new ArrayList<>();
        String sql = "SELECT id_country, name, government, religion, ethnic_group, description FROM Countries";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Country country = new Country(
                        rs.getInt("id_country"),
                        rs.getString("name"),
                        rs.getString("government"),
                        rs.getString("religion"),
                        rs.getString("ethnic_group"),
                        rs.getString("description")
                );
                countries.add(country);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return countries;
    }

    public CountryResources getCountryResources(int idCountry) {
        String sql = """
            SELECT
                SUM(p.population) AS population,
                SUM(p.area) AS area,
                COALESCE(SUM(CASE WHEN r.name = 'Dinero' THEN pr.production ELSE 0 END), 0) AS ingresos,
                COALESCE(SUM(CASE WHEN r.name = 'Gas natural' THEN pr.production ELSE 0 END), 0) AS gasNatural,
                COALESCE(SUM(CASE WHEN r.name = 'Petróleo' THEN pr.production ELSE 0 END), 0) AS petroleo,
                COALESCE(SUM(CASE WHEN r.name = 'Madera' THEN pr.production ELSE 0 END), 0) AS madera,
                COALESCE(SUM(CASE WHEN r.name = 'Pesca' THEN pr.production ELSE 0 END), 0) AS pesca,
                COALESCE(SUM(CASE WHEN r.name = 'Uranio' THEN pr.production ELSE 0 END), 0) AS uranio,
                COALESCE(SUM(CASE WHEN r.name = 'Oro' THEN pr.production ELSE 0 END), 0) AS oro,
                COALESCE(SUM(CASE WHEN r.name = 'Agricultura' OR r.name = 'Agrícultura' THEN pr.production ELSE 0 END), 0) AS agricultura,
                COALESCE(SUM(CASE WHEN r.name = 'Acero' THEN pr.production ELSE 0 END), 0) AS acero,
                COALESCE(SUM(CASE WHEN r.name = 'Industria' THEN pr.production ELSE 0 END), 0) AS industria,
                COALESCE(SUM(CASE WHEN r.name = 'Electricidad' THEN pr.production ELSE 0 END), 0) AS electricidad
            FROM Provinces p
            JOIN ProvinceResources pr ON p.id_province = pr.id_province
            LEFT JOIN Resources r ON r.id_resource = pr.id_resource
            WHERE p.id_country = ?
            """;

        try (var stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idCountry);
            var rs = stmt.executeQuery();
            if (rs.next()) {
                return new CountryResources(
                        rs.getLong("population"),
                        rs.getDouble("area"),
                        rs.getInt("ingresos"),
                        rs.getInt("gasNatural"),
                        rs.getInt("petroleo"),
                        rs.getInt("madera"),
                        rs.getInt("pesca"),
                        rs.getInt("uranio"),
                        rs.getInt("oro"),
                        rs.getInt("agricultura"),
                        rs.getInt("acero"),
                        rs.getInt("industria"),
                        rs.getInt("electricidad")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Country getCountryById(int id) {
        String sql = "SELECT id_country, name, government, religion, ethnic_group, description FROM Countries WHERE id_country = ?";
        try (var stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            var rs = stmt.executeQuery();
            if (rs.next()) {
                return new Country(
                        rs.getInt("id_country"),
                        rs.getString("name"),
                        rs.getString("government"),
                        rs.getString("religion"),
                        rs.getString("ethnic_group"),
                        rs.getString("description")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Country getCountryByName(String name) {
        String sql = "SELECT id_country, name, government, religion, ethnic_group, description FROM Countries WHERE name_en = ?";
        try (var stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            var rs = stmt.executeQuery();
            if (rs.next()) {
                return new Country(
                        rs.getInt("id_country"),
                        rs.getString("name"),
                        rs.getString("government"),
                        rs.getString("religion"),
                        rs.getString("ethnic_group"),
                        rs.getString("description")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public CountryDetails getCountryDetails(int countryId) {
        String sql = """
            SELECT
                c.government,
                SUM(p.population) AS totalPopulation,
                (SELECT pr2.name FROM Provinces pr2 WHERE pr2.id_country = c.id_country AND pr2.is_capital = 1 LIMIT 1) AS capitalName,
                COALESCE(SUM(CASE WHEN r.name = 'Dinero' THEN pr.production ELSE 0 END), 0) AS totalIngresos
            FROM Countries c
            JOIN Provinces p ON p.id_country = c.id_country
            JOIN ProvinceResources pr ON pr.id_province = p.id_province
            LEFT JOIN Resources r ON r.id_resource = pr.id_resource
            WHERE c.id_country = ?
            """;

        try (var stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, countryId);
            var rs = stmt.executeQuery();
            if (rs.next()) {
                return new CountryDetails(
                        rs.getString("government"),
                        rs.getLong("totalPopulation"),
                        rs.getString("capitalName"),
                        rs.getInt("totalIngresos")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public double getCountryMoney(int countryId) {
        String sql = "SELECT COALESCE(SUM(CASE WHEN r.name = 'Dinero' THEN tr.total_amount ELSE 0 END), 0) AS money FROM TotalResources tr LEFT JOIN Resources r ON r.id_resource = tr.id_resource WHERE tr.id_country = ?";
        try (var stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, countryId);
            var rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("money");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public CountryPopulation getTotalCountryPopulation(int idCountry) {
        String sql = "SELECT v.country_name, v.total_population FROM totalCountryPopulation v JOIN Countries c ON c.name = v.country_name WHERE c.id_country = ?";
        try (var stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idCountry);
            var rs = stmt.executeQuery();
            if (rs.next()) {
                return new CountryPopulation(
                        rs.getString("country_name"),
                        rs.getLong("total_population")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Province> getProvincesByCountry(int countryId) {
        List<Province> provinces = new ArrayList<>();
        String sql = "SELECT id_province, name, environment, population, area, is_capital, is_coastal, id_country, morale FROM Provinces WHERE id_country = ?";

        try (var stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, countryId);
            var rs = stmt.executeQuery();
            while (rs.next()) {
                Province province = new Province(
                        rs.getInt("id_province"),
                        rs.getString("name"),
                        rs.getString("environment"),
                        rs.getInt("population"),
                        rs.getDouble("area"),
                        rs.getInt("is_capital") == 1,
                        rs.getInt("is_coastal") == 1,
                        rs.getInt("id_country"),
                        rs.getInt("morale")
                );
                provinces.add(province);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return provinces;
    }
}
