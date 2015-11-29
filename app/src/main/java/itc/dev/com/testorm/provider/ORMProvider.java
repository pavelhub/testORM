package itc.dev.com.testorm.provider;

import android.content.Context;

import com.google.gson.JsonArray;

/**
 * Created by pavel on 11/10/15.
 */
public interface ORMProvider {
    void init(Context context);

    void insertAll(JsonArray jsonModel);

    void select(String key, Object value);

    void delete(String key, Object value);

    void update(String key, Object newValue);

}
