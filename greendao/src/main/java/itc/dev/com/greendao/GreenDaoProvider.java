package itc.dev.com.greendao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import itc.dev.com.baseprovider.FileUril;
import itc.dev.com.baseprovider.ORMProvider;
import itc.dev.com.baseprovider.ProviderPostBack;
import itc.dev.com.baseprovider.UserModel;

/**
 * Created by pavel on 12/6/15.
 */
public class GreenDaoProvider implements ORMProvider {
    String Tag = "GreenDaoProvider";
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private UserDao userDao;
    ProviderPostBack providerPostBack;
    List<UserModel> list;

    @Override
    public void init(Context context, ProviderPostBack providerPostBack) {
        this.providerPostBack = providerPostBack;
        File greenDaoDB = FileUril.getDataBaseFile("user_greenDao");
        greenDaoDB.delete();
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, greenDaoDB.getAbsolutePath(), null);
        SQLiteDatabase db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        userDao = daoSession.getUserDao();
    }

    @Override
    public void insertAll(List<UserModel> userModels) {
        this.list = userModels;
        List<User> entities = mapUser(userModels);
        long startTime = System.currentTimeMillis();


        userDao.insertInTx(entities);

        long timeSpent = System.currentTimeMillis() - startTime;

        providerPostBack.onOperationComplete(Tag, "INSERT", timeSpent, "");
    }

    private List<User> mapUser(List<UserModel> userModels) {
        List<User> users = new ArrayList<>();
        for (UserModel userModel : userModels) {
            User user = new User();
            user.set_id((long) userModel.id);
            user.setAge(userModel.age);
            user.setEmail(userModel.email);
            user.setFirst_name(userModel.first_name);
            user.setLast_name(userModel.last_name);
            user.setPassword(userModel.password);
            user.setPhone_code(userModel.phone_code);
            user.setPhoto_url(userModel.photo_url);
            user.setPhone_value(userModel.phone_value);
            user.setIsAdmin(userModel.isAdmin);
            users.add(user);

        }
        return users;
    }

    @Override
    public void selectAll() {
        long startTime = System.currentTimeMillis();
        List<User> list = userDao.loadAll();
        for (User user : list) {
            user.getFirst_name();
        }
        long timeSpent = System.currentTimeMillis() - startTime;
        providerPostBack.onOperationComplete(Tag, "SELECT", timeSpent, "size:" + list.size());
    }

    @Override
    public void deleteAll() {
        List<User> listUser = mapUser(list);
        long startTime = System.currentTimeMillis();


        userDao.deleteInTx(listUser);


        long timeSpent = System.currentTimeMillis() - startTime;

        providerPostBack.onOperationComplete(Tag, "DELETE", timeSpent, "");

    }

    @Override
    public void update(List<UserModel> userModels) {
        List<User> entities = mapUser(userModels);
        long startTime = System.currentTimeMillis();


        userDao.updateInTx(entities);

        long timeSpent = System.currentTimeMillis() - startTime;

        providerPostBack.onOperationComplete(Tag, "UPDATE", timeSpent, "");

    }
}
