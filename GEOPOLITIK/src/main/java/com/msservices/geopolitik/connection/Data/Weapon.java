package com.msservices.geopolitik.connection.Data;

public class Weapon {

    private final int idWeapon;
    private final String name;
    private final double cost;
    private final String type;

    public Weapon(int idWeapon, String name, double cost, String type) {
        this.idWeapon = idWeapon;
        this.name = name;
        this.cost = cost;
        this.type = type;
    }

    public int getIdWeapon() { return idWeapon; }
    public String getName() { return name; }
    public double getCost() { return cost; }
    public String getType() { return type; }
}
