package com.msservices.geopolitik.init;

import com.msservices.geopolitik.Match;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    //Creates a map colors representation of the map.
    private static void loadMapColors(Image image){
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        mapColors = new double[width * height];
        mapWidth = width;
        mapHeight = height;
        PixelReader reader = image.getPixelReader();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                mapColors[y * width + x] = reader.getArgb(x, y);
            }
        }
    }

    public static double[] getMapColors() {
        return mapColors;
    }
}
