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
        new menuIcons("world", "img/icons/world.jpg")
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
}
