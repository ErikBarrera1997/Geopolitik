package com.msservices.geopolitik.init;

import com.msservices.geopolitik.init.entity.flag;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class loadFlags {

    private static Map<Integer, flag> flagCache;

    private static final Map<String, Integer> FLAGS = new HashMap<>();
    static {
        FLAGS.put("tukiye", 1);
        FLAGS.put("georgia", 2);
        FLAGS.put("azerbaijan", 3);
        FLAGS.put("armenia", 4);
        FLAGS.put("iraq", 5);
        FLAGS.put("iran", 6);
        FLAGS.put("turkmenistan", 7);
        FLAGS.put("uzbekistan", 8);
        FLAGS.put("afganistan", 9);
        FLAGS.put("lebanon", 10);
        FLAGS.put("israel", 11);
        FLAGS.put("jordania", 12);
        FLAGS.put("kwait", 13);
        FLAGS.put("egypt", 14);
        FLAGS.put("saudi arabia", 15);
        FLAGS.put("bahrain", 16);
        FLAGS.put("qatar", 17);
        FLAGS.put("uae", 18);
        FLAGS.put("oman", 19);
        FLAGS.put("yemen", 20);
        FLAGS.put("cyprus", 21);
        FLAGS.put("syria", 22);
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
            Integer id = FLAGS.get(fileName);
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
