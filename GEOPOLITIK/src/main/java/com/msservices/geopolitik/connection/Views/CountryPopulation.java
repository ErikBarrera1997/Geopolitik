package com.msservices.geopolitik.connection.Views;

public class CountryPopulation {

    private String countryName;
    private long totalPopulation;

    public CountryPopulation(String countryName, long totalPopulation) {
        this.countryName = countryName;
        this.totalPopulation = totalPopulation;
    }

    public String getCountryName() { return countryName; }
    public long getTotalPopulation() { return totalPopulation; }
}
