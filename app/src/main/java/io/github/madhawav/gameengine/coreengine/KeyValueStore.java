package io.github.madhawav.gameengine.coreengine;

import android.content.Context;
import android.content.SharedPreferences;

public class KeyValueStore extends AbstractEngineModule {
    private final SharedPreferences privatePreferences;

    public KeyValueStore(Context context, String applicationId) {
        String preferenceFileKey = applicationId + ".game_settings";
        this.privatePreferences = context.getSharedPreferences(
                preferenceFileKey, Context.MODE_PRIVATE);
    }

    /**
     * Retrieve a stored value for a given key
     * @param key Key to retrieve value of
     * @param defaultValue Default value returned setting doesn't exist
     * @return Value stored for the key
     */
    public int getInteger(String key, int defaultValue){
        return privatePreferences.getInt(key, defaultValue);
    }

    /**
     * Stores a key value pair of integer value type
     * @param key Key
     * @param value Value to store
     */
    public void putInteger(String key, int value){
        SharedPreferences.Editor editor = privatePreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }
}
