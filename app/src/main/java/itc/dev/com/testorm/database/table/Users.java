package itc.dev.com.testorm.database.table;

import android.provider.BaseColumns;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation.DefaultContentMimeTypeVnd;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation.DefaultContentUri;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation.DefaultSortOrder;

import itc.dev.com.generate.User;

@DatabaseTable(tableName = "users")
@DefaultContentUri(authority = "itc.dev.com.testorm", path = "users")
@DefaultContentMimeTypeVnd(name = "users", type = "itc.dev.com.testorm.provider")
public class Users{

    public Users() {
    }

    @DatabaseField(columnName = BaseColumns._ID, generatedId = true)
    @DefaultSortOrder
    private int id;

    @DatabaseField
    private String email;

    @DatabaseField
    private String password;

    @DatabaseField
    private String first_name;

    @DatabaseField
    private String last_name;

    @DatabaseField
    private String photo_url;

    @DatabaseField
    private String phone_code;

    @DatabaseField
    private String phone_value;

    @DatabaseField
    private boolean is_admin;

    @DatabaseField
    private int age;

    @DatabaseField
    private float height;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }

    public String getPhone_code() {
        return phone_code;
    }

    public void setPhone_code(String phone_code) {
        this.phone_code = phone_code;
    }

    public String getPhone_value() {
        return phone_value;
    }

    public void setPhone_value(String phone_value) {
        this.phone_value = phone_value;
    }

    public boolean is_admin() {
        return is_admin;
    }

    public void setIs_admin(boolean is_admin) {
        this.is_admin = is_admin;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }
}
