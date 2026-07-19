package com.msservices.geopolitik.connection.queries.province;

import java.util.Map;

public class ProvinceData {

    private int idProvince;
    private String name;
    private int idCountry;
    private long totalCountryPopulation;
    private int totalCountryIngresos;
    private Map<String, Integer> resources;

    public ProvinceData(int idProvince, String name, int idCountry,
                        long totalCountryPopulation, int totalCountryIngresos,
                        Map<String, Integer> resources) {
        this.idProvince = idProvince;
        this.name = name;
        this.idCountry = idCountry;
        this.totalCountryPopulation = totalCountryPopulation;
        this.totalCountryIngresos = totalCountryIngresos;
        this.resources = resources;
    }

    public int getIdProvince() { return idProvince; }
    public String getName() { return name; }
    public int getIdCountry() { return idCountry; }
    public long getTotalCountryPopulation() { return totalCountryPopulation; }
    public int getTotalCountryIngresos() { return totalCountryIngresos; }
    public Map<String, Integer> getResources() { return resources; }
}
