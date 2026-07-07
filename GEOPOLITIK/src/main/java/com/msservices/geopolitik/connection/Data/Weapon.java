package com.msservices.geopolitik.connection.Data;

public class Weapon {

    private final int idWeapon;
    private final String name;
    private final double cost;
    private final String type;
    private final String imagePath;

    public Weapon(int idWeapon, String name, double cost, String type, String imagePath) {
        this.idWeapon = idWeapon;
        this.name = name;
        this.cost = cost;
        this.type = type;
        this.imagePath = imagePath;
    }

    public Weapon(int idWeapon, String name, double cost, String type) {
        this(idWeapon, name, cost, type, "img/weapons/" + name.toLowerCase() + ".jpg");
    }

    public int getIdWeapon() { return idWeapon; }
    public String getName() { return name; }
    public double getCost() { return cost; }
    public String getType() { return type; }
    public String getImagePath() { return imagePath; }
}
