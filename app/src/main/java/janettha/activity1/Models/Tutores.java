package janettha.activity1.Models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by janettha on 11/03/18.
 */

public class Tutores {

    String username, email, name, users;
    List<Usuarios> U = new ArrayList<Usuarios>();

    public Tutores(String username, String email, String name, String users) {
        this.username = username;
        this.email = email;
        this.name = name;
        this.users = users;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsers() {
        return users;
    }

    public void setUsers(String users) {
        this.users = users;
    }

    public void addUser(Usuarios u){
        U.add(u);
    }

    public Usuarios getUsuario(int id){
        return U.get(id);
    }
}
