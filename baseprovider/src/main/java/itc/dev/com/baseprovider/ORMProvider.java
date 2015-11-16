package itc.dev.com.baseprovider;

import android.content.Context;

import java.util.List;

/**
 * Created by pavel on 11/10/15.
 */
public interface ORMProvider {

    void init(Context context,ProviderPostBack providerPostBack);

    void insertAll(List<UserModel> userModels);

    void select(String key, Object value);

    void delete(String key, Object value);

    void update(String key, Object newValue);

}
