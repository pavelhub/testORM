package itc.dev.com.realmprovider;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import itc.dev.com.baseprovider.ORMProvider;
import itc.dev.com.baseprovider.ProviderPostBack;
import itc.dev.com.baseprovider.UserModel;

/**
 * Created by pavel on 12/7/15.
 */
public class RealmProvider implements ORMProvider {
    String Tag = "RealmProvider";

    private Realm realm;
    ProviderPostBack providerPostBack;
Context context;
    @Override
    public void init(Context context, ProviderPostBack providerPostBack) {
        this.context=context;
        this.providerPostBack = providerPostBack;

    }

    private List<User> mapUser(List<UserModel> userModels) {
        List<User> users = new ArrayList<>();
        for (UserModel userModel : userModels) {
            User user = new User();
            user.setId(userModel.id);
            user.setAge(userModel.age);
            user.setEmail(userModel.email);
            user.setFirst_name(userModel.first_name);
            user.setLast_name(userModel.last_name);
            user.setPassword(userModel.password);
            user.setPhone_code(userModel.phone_code);
            user.setPhoto_url(userModel.photo_url);
            user.setPhone_value(userModel.phone_value);
            user.setAdmin(userModel.isAdmin);
            users.add(user);

        }
        return users;
    }


    @Override
    public void insertAll(List<UserModel> userModels) {
        List<User> entities = mapUser(userModels);
        realm = Realm.getInstance(context);
        long startTime = System.currentTimeMillis();

        insert(entities);

        long timeSpent = System.currentTimeMillis() - startTime;
        providerPostBack.onOperationComplete(Tag, "INSERT", timeSpent, "");
    }

    @Override
    public void selectAll() {
        long startTime = System.currentTimeMillis();
        int size = select();

        long timeSpent = System.currentTimeMillis() - startTime;
        providerPostBack.onOperationComplete(Tag, "SELECT", timeSpent, "size:" + size);
    }

    @Override
    public void deleteAll() {

        long startTime = System.currentTimeMillis();

        delete();


        long timeSpent = System.currentTimeMillis() - startTime;

        providerPostBack.onOperationComplete(Tag, "DELETE", timeSpent, "");
    }

    @Override
    public void update(List<UserModel> userModels) {

        long startTime = System.currentTimeMillis();
        update();

//        userDao.updateInTx(entities);

        long timeSpent = System.currentTimeMillis() - startTime;

        providerPostBack.onOperationComplete(Tag, "UPDATE", timeSpent, "");
    }

    private void insert(List<User> users) {


        realm.beginTransaction();
        for (int i = 0; i < users.size(); i++) {
            User user = realm.createObject(User.class);
            user.setId(users.get(i).getId());
            user.setEmail(users.get(i).getEmail());
            user.setPassword(users.get(i).getPassword());
            user.setFirst_name(users.get(i).getFirst_name());
            user.setLast_name(users.get(i).getLast_name());
            user.setPhoto_url(users.get(i).getPhoto_url());
            user.setPhone_code(users.get(i).getPhone_code());
            user.setPhone_value(users.get(i).getPhone_value());
            user.setAdmin(users.get(i).isAdmin());
            user.setAge(users.get(i).getAge());
        }
        realm.commitTransaction();


    }

    private int select() {

        RealmQuery<User> userRealmQuery = realm.where(User.class);
        RealmResults<User> users = userRealmQuery.findAll();
        for (User user : users) {
            user.getFirst_name();
        }
        return users.size();
    }


    private void update() {

        RealmResults<User> users = realm.where(User.class).findAll();
        ArrayList<User> list = new ArrayList<>(users.size());
        for (User user : users) {
            User user1 = new User();
            user1.setAge(user.getAge() + 1);
            user1.setAdmin(true);
            user1.setEmail(user.getEmail());
            user1.setFirst_name(user.getFirst_name());
            user1.setHeight(user.getHeight());
            user1.setId(user.getId());
            user1.setLast_name(user.getLast_name());
            user1.setPassword(user.getPassword());
            user1.setPhone_code(user.getPhone_code());
            user1.setPhone_value(user.getPhone_value());
            user1.setPhoto_url(user.getPhoto_url());
            list.add(user1);
        }
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(list);
        realm.commitTransaction();

    }

//
//    private void update() {
//
//        RealmResults<User> users = realm.where(User.class).findAll();
//
//        realm.beginTransaction();
//
//        int size = users.size();
//        Log.e("LIST ALL", "COUNT" + size);
//        for (int i = 0; i < size; i++) {
//            User user = users.get(i);
//            User newUser = realm.createObject(User.class);
//            newUser.setId(1);
//            newUser.setEmail(user.getEmail());
//            newUser.setPassword(user.getPassword());
//            newUser.setFirst_name(user.getFirst_name());
//            newUser.setLast_name(user.getLast_name());
//            newUser.setPhoto_url(user.getPhoto_url());
//            newUser.setPhone_code(user.getPhone_code());
//            newUser.setPhone_value(user.getPhone_value());
//            newUser.setAdmin(false);
//            newUser.setAge(user.getAge());
//            newUser.setHeight(user.getHeight());
//
//        }
//        users.clear();
//        realm.commitTransaction();
//
//    }

    private void delete() {
        RealmResults<User> users = realm.where(User.class).findAll();
        realm.beginTransaction();
        users.clear();
        realm.commitTransaction();

    }
}
