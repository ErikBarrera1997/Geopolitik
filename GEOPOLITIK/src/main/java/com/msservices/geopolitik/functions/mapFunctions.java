package com.msservices.geopolitik.functions;

import com.msservices.geopolitik.init.loadMatch;

import java.util.Locale;

import static com.msservices.geopolitik.init.loadMap.getMapColors;
import static com.msservices.geopolitik.init.loadMap.getMapWidth;

public class mapFunctions {

    public static double getColor(int x, int y){
        System.out.println((int) getMapColors()[y * getMapWidth() + x]);
        return (int) getMapColors()[y * getMapWidth() + x];
    }

    //Identifies the country by color code from the list
    public static String getCountryFromList(double color){
        String key = String.format(Locale.US, "%.2f", color);
        return loadMatch.getColorList().getOrDefault(key, "Desconocido");
    }

}
