package com.morg.cacheandroidsample;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;

/**
 * A generic class for caching objects in the Android cache directory.
 *
 * @param <T> the type of object to be cached
 */
public class ObjectCache<T> {
    private static final String TAG = ObjectCache.class.getSimpleName();
    private final Context context;
    private final Gson gson;

    /**
     * Constructs an ObjectCache with the specified context.
     *
     * @param context the context to be used for accessing the cache directory
     */
    public ObjectCache(Context context) {
        this.context = context;
        this.gson = new GsonBuilder().setLenient().create();
    }

    /**
     * Retrieves the cached value for the specified key.
     *
     * @param key     the key associated with the cached value
     * @param typeOfT the type of the cached value
     * @return the cached value, or null if the value does not exist or an error occurs
     */
    public T getValue(String key, Type typeOfT) {
        File file = new File(context.getCacheDir(), key);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                return gson.fromJson(sb.toString(), typeOfT);
            } catch (IOException e) {
                Log.e(TAG, "getValue: ", e);
            }
        } else {
            Log.d(TAG, "getValue: Empty");
        }
        return null;
    }

    /**
     * Caches the specified value with the specified key.
     *
     * @param key   the key to associate with the cached value
     * @param value the value to be cached
     */
    public void setValue(String key, T value) {
        deleteValue(key);
        File file = new File(context.getCacheDir(), key);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(gson.toJson(value));
            writer.flush();
        } catch (IOException e) {
            Log.e(TAG, "setValue: ", e);
        }
    }

    /**
     * Deletes the cached value associated with the specified key.
     *
     * @param key the key associated with the cached value to be deleted
     */
    public void deleteValue(String key) {
        File file = new File(context.getCacheDir(), key);
        if (file.exists() && !file.delete()) {
            Log.e(TAG, "deleteValue: Failed to delete " + key);
        }
    }

    public void deleteAllValues() {
        File cacheDir = context.getCacheDir();
        File[] files = cacheDir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (!file.delete()) {
                    Log.e(TAG, "deleteAllValues: Failed to delete " + file.getName());
                }
            }
        }
    }
}