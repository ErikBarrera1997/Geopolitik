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
        new menuIcons("electricity", "img/icons/electricity.png")
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
}
