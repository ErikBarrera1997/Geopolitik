package com.msservices.geopolitik.connection.Views;

public class CountryResources {

    private long population;
    private double area;
    private int ingresos;
    private int gasNatural;
    private int petroleo;
    private int madera;
    private int pesca;
    private int uranio;
    private int oro;
    private int agricultura;
    private int acero;
    private int industria;
    private int electricidad;

    public CountryResources(long population, double area, int ingresos, int gasNatural, int petroleo, int madera,
                            int pesca, int uranio, int oro, int agricultura, int acero, int industria,
                            int electricidad) {
        this.population = population;
        this.area = area;
        this.ingresos = ingresos;
        this.gasNatural = gasNatural;
        this.petroleo = petroleo;
        this.madera = madera;
        this.pesca = pesca;
        this.uranio = uranio;
        this.oro = oro;
        this.agricultura = agricultura;
        this.acero = acero;
        this.industria = industria;
        this.electricidad = electricidad;
    }

    public long getPopulation() { return population; }
    public double getArea() { return area; }
    public int getIngresos() { return ingresos; }
    public int getGasNatural() { return gasNatural; }
    public int getPetroleo() { return petroleo; }
    public int getMadera() { return madera; }
    public int getPesca() { return pesca; }
    public int getUranio() { return uranio; }
    public int getOro() { return oro; }
    public int getAgricultura() { return agricultura; }
    public int getAcero() { return acero; }
    public int getIndustria() { return industria; }
    public int getElectricidad() { return electricidad; }

    @Override
    public String toString() {
        return "CountryResources{" +
                "population=" + population +
                ", area=" + area +
                ", ingresos=" + ingresos +
                ", gasNatural=" + gasNatural +
                ", madera=" + madera +
                ", pesca=" + pesca +
                ", uranio=" + uranio +
                ", oro=" + oro +
                ", agricultura=" + agricultura +
                ", acero=" + acero +
                ", industria=" + industria +
                ", electricidad=" + electricidad +
                '}';
    }
}
