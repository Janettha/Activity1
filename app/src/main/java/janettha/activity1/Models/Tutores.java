package janettha.activity1.Models;

/**
 * Created by janettha on 11/03/18.
 */


import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Tutores {
    private String name;
    private String user;
    private String email;

    public Tutores() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Tutores(String username, String name, String email) {
        this.user = username;
        this.name = name;
        this.email = email;
    }

    public String getName(){
        return this.name;
    }

    public String getEmail(){
        return this.email;
    }

    public String getUser(){
        return this.user;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("user", user);
        result.put("email", email);

        return result;
    }
}
