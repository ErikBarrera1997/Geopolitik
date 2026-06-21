package com.msservices.geopolitik.connection.Data;

public class Province {

    private int idProvince;
    private String name;
    private String environment;
    private int population;
    private double area;
    private boolean isCapital;
    private boolean isCoastal;
    private int idCountry;
    private int morale;

    public Province(int idProvince, String name, String environment, int population, double area,
                    boolean isCapital, boolean isCoastal, int idCountry, int morale) {
        this.idProvince = idProvince;
        this.name = name;
        this.environment = environment;
        this.population = population;
        this.area = area;
        this.isCapital = isCapital;
        this.isCoastal = isCoastal;
        this.idCountry = idCountry;
        this.morale = morale;
    }

    public int getIdProvince() { return idProvince; }
    public String getName() { return name; }
    public String getEnvironment() { return environment; }
    public int getPopulation() { return population; }
    public double getArea() { return area; }
    public boolean isCapital() { return isCapital; }
    public boolean isCoastal() { return isCoastal; }
    public int getIdCountry() { return idCountry; }
    public int getMorale() { return morale; }

    @Override
    public String toString() {
        return "Province{" +
                "idProvince=" + idProvince +
                ", name='" + name + '\'' +
                ", environment='" + environment + '\'' +
                ", population=" + population +
                ", area=" + area +
                ", isCapital=" + isCapital +
                ", isCoastal=" + isCoastal +
                ", idCountry=" + idCountry +
                ", morale=" + morale +
                '}';
    }
}
