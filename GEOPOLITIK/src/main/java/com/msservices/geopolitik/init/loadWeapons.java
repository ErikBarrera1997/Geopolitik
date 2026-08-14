package com.msservices.geopolitik.init;

import com.msservices.geopolitik.connection.DatabaseConnection;
import com.msservices.geopolitik.connection.queries.weapon.weaponQueries;
import com.msservices.geopolitik.connection.queries.weapon.Weapon;

import java.util.List;

public class loadWeapons {

    public static List<Weapon> weapons;

    public static void getWeapons() {
        if (weapons == null) {
            weaponQueries queries = new weaponQueries(DatabaseConnection.getInstance().getConnection());
            weapons = queries.getWeapons();
        }
    }

    public static int getWeaponCount(){
        return weapons.size();
    }

    public static Weapon getWeapon(int index){
        return weapons.get(index);
    }

    public static int getWeaponId(int index){
        return weapons.get(index).idWeapon();
    }

    public static String getWeaponName(int index){
        return weapons.get(index).name();
    }

    public static double getWeaponCost(int index){
        return weapons.get(index).cost();
    }

    public static String getWeaponType(int index){
        return weapons.get(index).type();
    }

    public static List<Weapon> getWeaponsList(){
        return weapons;
    }

}
