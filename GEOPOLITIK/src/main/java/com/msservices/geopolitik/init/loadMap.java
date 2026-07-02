package com.msservices.geopolitik.init;

import com.msservices.geopolitik.Match;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class loadMap {

    private static double[] mapColors;
    private static int mapWidth;
    private static int mapHeight;

    public static int getMapWidth() { return mapWidth; }
    public static int getMapHeight() { return mapHeight; }

    public static void loadMapFromMatch(ImageView imageView){
        Match match = loadMatch.getMatch();
        if (match != null) {
            createMap(imageView, match.getMapStringPath());
        }
    }

    public static void createMap(ImageView imageView, String pathMap){
        if (imageView == null || pathMap == null) return;
        Image image = new Image(new File(pathMap).toURI().toString());
        imageView.setImage(image);
        loadMapColors(image);
    }

    private static final int COLOR_PRECISION = 32;

    //Creates a map colors representation of the map.
    private static void loadMapColors(Image image){
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        mapColors = new double[width * height];
        mapWidth = width;
        mapHeight = height;
        PixelReader reader = image.getPixelReader();

        Map<Integer, Integer> colorToId = new HashMap<>();
        AtomicInteger nextId = new AtomicInteger(1);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int argb = reader.getArgb(x, y);
                int r = ((argb >> 16) & 0xFF) / COLOR_PRECISION * COLOR_PRECISION;
                int g = ((argb >> 8) & 0xFF) / COLOR_PRECISION * COLOR_PRECISION;
                int b = (argb & 0xFF) / COLOR_PRECISION * COLOR_PRECISION;
                int quantizedRgb = (r << 16) | (g << 8) | b;
                int id = colorToId.computeIfAbsent(quantizedRgb, k -> nextId.getAndIncrement());
                mapColors[y * width + x] = id;
            }
        }
    }

    public static double[] getMapColors() {
        return mapColors;
    }
}
