package com.msservices.geopolitik.connection;

import com.msservices.geopolitik.connection.Data.Country;
import com.msservices.geopolitik.connection.Data.Province;
import com.msservices.geopolitik.connection.Views.CountryDetails;
import com.msservices.geopolitik.connection.Views.CountryResources;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Queries {

    private final Connection connection;

    public Queries(Connection connection) {
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
                SUM(pr."Ingresos") AS ingresos,
                SUM(pr."Gas natural") AS gasNatural,
                SUM(pr."Petróleo") AS petroleo,
                SUM(pr."Madera") AS madera,
                SUM(pr."Pesca") AS pesca,
                SUM(pr."Uranio") AS uranio,
                SUM(pr."Oro") AS oro,
                SUM(pr."Agricultura") AS agricultura,
                SUM(pr."Acero") AS acero,
                SUM(pr."Industria") AS industria,
                SUM(pr."electricidad") AS electricidad
            FROM Provinces p
            JOIN ProvinceResources pr ON p.id_province = pr.id_provincia
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
                SUM(pr."Ingresos") AS totalIngresos
            FROM Countries c
            JOIN Provinces p ON p.id_country = c.id_country
            JOIN ProvinceResources pr ON pr.id_provincia = p.id_province
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
