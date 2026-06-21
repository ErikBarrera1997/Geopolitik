package com.msservices.geopolitik.init;

import javafx.scene.image.Image;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class loadImages {

    private static final int ICON_SIZE = 24;
    private static Map<String, Image> iconCache;

    public static Map<String, Image> getIcons() {
        if (iconCache != null) return iconCache;

        iconCache = new HashMap<>();

        File basePath1 = new File("img", "icons");
        File basePath2 = new File("..", "img/icons");
        File baseDir = basePath1.isDirectory() ? basePath1 : basePath2;

        if (!baseDir.isDirectory()) return iconCache;

        File[] files = baseDir.listFiles((dir, name) -> name.endsWith(".jpg") || name.endsWith(".png"));
        if (files == null) return iconCache;

        for (File file : files) {
            String name = file.getName().replaceFirst("\\.\\w+$", "");
            Image image = new Image(file.toURI().toString(), ICON_SIZE, ICON_SIZE, true, true);
            iconCache.put(name, image);
        }

        return iconCache;
    }

    public static Image getIcon(String name) {
        return getIcons().get(name);
    }
}
