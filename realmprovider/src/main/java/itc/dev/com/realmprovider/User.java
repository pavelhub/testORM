package itc.dev.com.realmprovider;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by uvart on 22.11.2015.
 */
public class User extends RealmObject {

    @PrimaryKey
    private int id;

    private String email;

    private String password;

    private String first_name;

    private String last_name;

    private String photo_url;

    private String phone_code;

    private String phone_value;

    private boolean admin;

    private int age;

    private float height;

    public User() {
    }

    public User(int id, String email, String password, String first_name, String last_name, String photo_url, String phone_code, String phone_value, boolean admin, int age, float height) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.first_name = first_name;
        this.last_name = last_name;
        this.photo_url = photo_url;
        this.phone_code = phone_code;
        this.phone_value = phone_value;
        this.admin = admin;
        this.age = age;
        this.height = height;
    }

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

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
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
