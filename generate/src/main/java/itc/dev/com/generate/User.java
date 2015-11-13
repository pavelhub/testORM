package itc.dev.com.generate;

import java.io.Serializable;

/**
 * Created by NataliLapshina on 02.06.2015.
 */
public class User implements Serializable {


    int id;
    String email;
    String password;
    String first_name;
    String last_name;
    String photo_url;
    String phone_code;
    String phone_value;
    boolean isAdmin;
    int age;

    float height;

    public User() {
    }


}
