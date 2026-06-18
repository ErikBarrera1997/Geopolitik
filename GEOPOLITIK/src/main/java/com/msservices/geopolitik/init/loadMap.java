package com.msservices.geopolitik.init;

import com.msservices.geopolitik.Match;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

public class loadMap {

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
    }

}
