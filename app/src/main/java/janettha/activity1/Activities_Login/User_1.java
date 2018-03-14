package janettha.activity1.Activities_Login;

import com.firebase.ui.auth.User;

/**
 * Created by janettha on 13/03/18.
 */

public class User_1 {
    String user, Nombre, Apellidos, sexo;
    int edad;

    public User_1(){}

    public User_1(String user, String Nombre, String Apellidos, String sexo, int edad){
        this.user = user;
        this.Nombre = Nombre;
        this.Apellidos= Apellidos;
        this.sexo = sexo;
        this.edad = edad;
    }

    public String getUser(){
        return this.user;
    }

    public String getToString(){
        return "User_1:"+getUser();
    }


}
