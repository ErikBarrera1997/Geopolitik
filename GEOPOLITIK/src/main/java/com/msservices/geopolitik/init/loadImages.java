package com.msservices.geopolitik.init;

import com.msservices.geopolitik.init.entity.menuIcons;
import javafx.scene.image.Image;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class loadImages {

    private static final int ICON_SIZE = 24;
    private static Map<String, Image> iconCache;

    private static final List<menuIcons> ICONS = List.of(
        new menuIcons("weapons", "img/icons/weapons.jpg"),
        new menuIcons("military_actions", "img/icons/military_actions.jpg"),
        new menuIcons("news", "img/icons/news.jpg"),
        new menuIcons("provinces", "img/icons/provinces.jpg"),
        new menuIcons("research", "img/icons/research.jpg"),
        new menuIcons("diplomacy", "img/icons/diplomacy.jpg"),
        new menuIcons("world", "img/icons/world.jpg"),
        new menuIcons("population", "img/icons/population.png"),
        new menuIcons("money", "img/icons/money.png"),
        new menuIcons("gas", "img/icons/gas.png"),
        new menuIcons("oil", "img/icons/oil.png"),
        new menuIcons("wood", "img/icons/wood.png"),
        new menuIcons("fishing", "img/icons/fishing.png"),
        new menuIcons("uranium", "img/icons/uranium.png"),
        new menuIcons("gold", "img/icons/gold.png"),
        new menuIcons("farming", "img/icons/farming.png"),
        new menuIcons("steel", "img/icons/steel.png"),
        new menuIcons("industry", "img/icons/industry.png"),
        new menuIcons("electricity", "img/icons/electricity.png"),
        new menuIcons("agency", "img/icons/agency.jpg"),
        new menuIcons("airdome", "img/icons/airdome.jpg"),
        new menuIcons("barracks", "img/icons/barracks.jpg"),
        new menuIcons("bunker", "img/icons/bunker.jpg"),
        new menuIcons("civillian_inf_objectives", "img/icons/civillian inf objetives.jpg"),
        new menuIcons("civillian_objectives", "img/icons/civillian objetives.jpg"),
        new menuIcons("dictatorship", "img/icons/dictatorship.jpg"),
        new menuIcons("dragons", "img/icons/dragons.jpg"),
        new menuIcons("factory", "img/icons/factory.jpg"),
        new menuIcons("iron", "img/icons/iron.png"),
        new menuIcons("laboratory", "img/icons/laboratory.jpg"),
        new menuIcons("landmines", "img/icons/landmines.jpg"),
        new menuIcons("launch_site", "img/icons/launch site.jpg"),
        new menuIcons("military_inf_objectives", "img/icons/military inf objetives.jpg"),
        new menuIcons("military_objectives", "img/icons/military objetives.jpg"),
        new menuIcons("missiles_site", "img/icons/missiles site.jpg"),
        new menuIcons("monarchy", "img/icons/monarchy.jpg"),
        new menuIcons("nuclear_plant", "img/icons/nuclear plant.jpg"),
        new menuIcons("oil_extraction", "img/icons/oil extraction.jpg"),
        new menuIcons("oil_platform", "img/icons/oil platform.jpg"),
        new menuIcons("port", "img/icons/port.jpg"),
        new menuIcons("power_plant", "img/icons/power plant.jpg"),
        new menuIcons("refinery", "img/icons/refinery.jpg"),
        new menuIcons("republic", "img/icons/republic.jpg"),
        new menuIcons("research_center", "img/icons/research center.jpg"),
        new menuIcons("sandbags", "img/icons/sandbags.jpg"),
        new menuIcons("sawmill", "img/icons/sawmill.jpg"),
        new menuIcons("siderurgic", "img/icons/siderurgic.jpg"),
        new menuIcons("capital", "img/icons/capital.jpg"),
        new menuIcons("teocracy", "img/icons/teocracy.jpg"),
        new menuIcons("trenches", "img/icons/trenches.jpg"),
        new menuIcons("weapons_factory", "img/icons/weapons factory.jpg")
    );

    public static Map<String, Image> getIcons() {
        if (iconCache != null) return iconCache;

        iconCache = new HashMap<>();

        for (menuIcons icon : ICONS) {
            File file = new File(icon.path());
            if (file.isFile()) {
                Image image = new Image(file.toURI().toString(), ICON_SIZE, ICON_SIZE, true, true);
                iconCache.put(icon.name(), image);
            }
        }

        return iconCache;
    }

    public static Image getIcon(String name) {
        return getIcons().get(name);
    }

    private static Map<String, Image> attackIconCache;

    private static final List<menuIcons> ATTACK_ICONS = List.of(
        new menuIcons("1", "img/attack icons/artillery attack.jpg"),
        new menuIcons("2", "img/attack icons/multiple rocket attack.jpg"),
        new menuIcons("3", "img/attack icons/air attack.jpg"),
        new menuIcons("4", "img/attack icons/bombardier.jpg"),
        new menuIcons("5", "img/attack icons/sea attack.jpg"),
        new menuIcons("6", "img/attack icons/submarine missile attack.jpg"),
        new menuIcons("7", "img/attack icons/balistic missile attack.jpg"),
        new menuIcons("8", "img/attack icons/cruise missile attack.jpg"),
        new menuIcons("9", "img/attack icons/air assault.jpg"),
        new menuIcons("10", "img/attack icons/drone attack.jpg"),
        new menuIcons("11", "img/attack icons/combined attack.jpg"),
        new menuIcons("12", "img/attack icons/ground incursion.jpg"),
        new menuIcons("13", "img/attack icons/sabotage.jpg"),
        new menuIcons("14", "img/attack icons/special operations.jpg")
    );

    public static Map<String, Image> getAttackIcons() {
        if (attackIconCache != null) return attackIconCache;

        attackIconCache = new HashMap<>();

        for (menuIcons icon : ATTACK_ICONS) {
            File file = new File(icon.path());
            if (file.isFile()) {
                Image image = new Image(file.toURI().toString(), ICON_SIZE, ICON_SIZE, true, true);
                attackIconCache.put(icon.name(), image);
            }
        }

        return attackIconCache;
    }

    public static Image getAttackIcon(String key) {
        return getAttackIcons().get(key);
    }

    private static Map<String, Image> weaponImageCache;
    private static final int WEAPON_ICON_SIZE = 40;

    private static final List<menuIcons> WEAPON_ICONS = List.of(
        new menuIcons("portaaviones", "img/weapons/aircraft carrier.jpg"),
        new menuIcons("sistema antiaéreo", "img/weapons/antiaircraft system.jpg"),
        new menuIcons("sistema antimisil", "img/weapons/antimissile system.jpg"),
        new menuIcons("artillería", "img/weapons/artillery.jpg"),
        new menuIcons("vehículo de asalto", "img/weapons/assault vehicle.jpg"),
        new menuIcons("dron de ataque", "img/weapons/attac drone.jpg"),
        new menuIcons("avión de ataque", "img/weapons/attacker.jpg"),
        new menuIcons("misil balístico", "img/weapons/ballistic missile.jpg"),
        new menuIcons("bombardero", "img/weapons/bomber.jpg"),
        new menuIcons("misil de crucero", "img/weapons/cruice missile.jpg"),
        new menuIcons("destructor", "img/weapons/destroyer.jpg"),
        new menuIcons("caza", "img/weapons/fighter.jpg"),
        new menuIcons("helicóptero", "img/weapons/helicopter.jpg"),
        new menuIcons("artillería propulsada", "img/weapons/propeled artillery.jpg"),
        new menuIcons("dron de reconocimiento", "img/weapons/reccon drone.jpg"),
        new menuIcons("submarino", "img/weapons/submarine.jpg"),
        new menuIcons("tanque", "img/weapons/tank.jpg")
    );

    public static Map<String, Image> getWeaponImages() {
        if (weaponImageCache != null) return weaponImageCache;

        weaponImageCache = new HashMap<>();

        for (menuIcons icon : WEAPON_ICONS) {
            File file = new File(icon.path());
            if (file.isFile()) {
                Image image = new Image(file.toURI().toString(), WEAPON_ICON_SIZE, WEAPON_ICON_SIZE, true, true);
                weaponImageCache.put(icon.name(), image);
            }
        }

        return weaponImageCache;
    }

    public static Image getWeaponImage(String name) {
        return getWeaponImages().get(name.toLowerCase());
    }
}
