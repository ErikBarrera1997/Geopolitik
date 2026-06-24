package com.msservices.geopolitik.init;

import com.msservices.geopolitik.init.entity.flag;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class loadFlags {

    private static Map<Integer, flag> flagCache;

    private static final Map<String, Integer> FILE_TO_ID = new HashMap<>();
    static {
        FILE_TO_ID.put("tukiye", 1);
        FILE_TO_ID.put("georgia", 2);
        FILE_TO_ID.put("azerbaijan", 3);
        FILE_TO_ID.put("armenia", 4);
        FILE_TO_ID.put("iraq", 5);
        FILE_TO_ID.put("iran", 6);
        FILE_TO_ID.put("turkmenistan", 7);
        FILE_TO_ID.put("uzbekistan", 8);
        FILE_TO_ID.put("afganistan", 9);
        FILE_TO_ID.put("lebanon", 10);
        FILE_TO_ID.put("israel", 11);
        FILE_TO_ID.put("jordania", 12);
        FILE_TO_ID.put("kwait", 13);
        FILE_TO_ID.put("egypt", 14);
        FILE_TO_ID.put("saudi arabia", 15);
        FILE_TO_ID.put("bahrain", 16);
        FILE_TO_ID.put("qatar", 17);
        FILE_TO_ID.put("uae", 18);
        FILE_TO_ID.put("oman", 19);
        FILE_TO_ID.put("yemen", 20);
        FILE_TO_ID.put("cyprus", 21);
        FILE_TO_ID.put("syria", 22);
    }

    public static Map<Integer, flag> getFlags() {
        if (flagCache != null) return flagCache;
        flagCache = new HashMap<>();

        File basePath1 = new File("img", "flags");
        File basePath2 = new File("..", "img/flags");
        File baseDir = basePath1.isDirectory() ? basePath1 : basePath2;

        if (!baseDir.isDirectory()) return flagCache;

        File[] files = baseDir.listFiles((dir, name) -> name.endsWith(".png"));
        if (files == null) return flagCache;

        for (File file : files) {
            String fileName = file.getName().replaceFirst("\\.\\w+$", "").toLowerCase(Locale.US);
            Integer id = FILE_TO_ID.get(fileName);
            if (id != null) {
                flagCache.put(id, new flag(id, file.toURI().toString()));
            }
        }

        return flagCache;
    }

    public static flag getFlag(int countryId) {
        return getFlags().get(countryId);
    }
}
