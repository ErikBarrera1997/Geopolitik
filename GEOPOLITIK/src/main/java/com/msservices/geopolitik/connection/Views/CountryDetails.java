package com.msservices.geopolitik.connection.Views;

public class CountryDetails {

    private String government;
    private long totalPopulation;
    private String capitalName;
    private int totalIngresos;

    public CountryDetails(String government, long totalPopulation, String capitalName, int totalIngresos) {
        this.government = government;
        this.totalPopulation = totalPopulation;
        this.capitalName = capitalName;
        this.totalIngresos = totalIngresos;
    }

    public String getGovernment() { return government; }
    public long getTotalPopulation() { return totalPopulation; }
    public String getCapitalName() { return capitalName; }
    public int getTotalIngresos() { return totalIngresos; }

    @Override
    public String toString() {
        return "CountryDetails{" +
                "government='" + government + '\'' +
                ", totalPopulation=" + totalPopulation +
                ", capitalName='" + capitalName + '\'' +
                ", totalIngresos=" + totalIngresos +
                '}';
    }
}
