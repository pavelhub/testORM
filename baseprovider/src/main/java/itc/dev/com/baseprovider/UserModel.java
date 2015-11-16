package itc.dev.com.baseprovider;

import java.io.Serializable;

/**
 * Created by NataliLapshina on 02.06.2015.
 */
public class UserModel implements Serializable {


    public int id;
    public String email;
    public String password;
    public String first_name;
    public String last_name;
    public String photo_url;
    public String phone_code;
    public String phone_value;
    public boolean isAdmin;
    public int age;

    public float height;

    public UserModel() {
    }


}
