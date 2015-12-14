package itc.dev.com.baseprovider;

import android.content.Context;

import java.util.List;

/**
 * Created by pavel on 11/10/15.
 */
public interface ORMProvider {


    void init(Context context, ProviderPostBack providerPostBack);

    void insertAll(List<UserModel> userModels);

    void selectAll();

    void deleteAll();

    void update(List<UserModel> userModels);

}
