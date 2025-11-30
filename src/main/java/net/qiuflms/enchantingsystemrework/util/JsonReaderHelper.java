package net.qiuflms.enchantingsystemrework.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class JsonReaderHelper {
    private static final Gson GSON = new Gson();

    public static JsonObject readLocalJson(String path) {
        InputStream stream = JsonReaderHelper.class.getClassLoader().getResourceAsStream(path);

        if (stream == null) {
            throw new RuntimeException("Could not find JSON file at: " + path);
        }

        // 2. Parse it using GSON
        return GSON.fromJson(new InputStreamReader(stream, StandardCharsets.UTF_8), JsonObject.class);
    }
}