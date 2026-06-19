package com.msservices.geopolitik.functions;

import com.msservices.geopolitik.init.loadMatch;

import java.util.Locale;

import static com.msservices.geopolitik.init.loadMap.getMapColors;
import static com.msservices.geopolitik.init.loadMap.getMapWidth;

public class mapFunctions {

    public static double getColor(int x, int y){
        int color = (int) getMapColors()[y * getMapWidth() + x];
        return getRGBValue(color);
    }

    public static double getRGBValue(int color) {
        int r = (color >>> 16) & 0xFF;
        int g = (color >>> 8) & 0xFF;
        int b = color & 0xFF;
        double value = r + g + b;
        double sqrt = Math.cbrt(value);
        return Math.round((sqrt - (Math.floor(sqrt) - 1)) * 100.0) / 100.0;
    }

    //Identifies the country by color code from the list
    public static String getCountryFromList(double color){
        String key = String.format(Locale.US, "%.2f", color);
        return loadMatch.getColorList().getOrDefault(key, "Desconocido");
    }

}
