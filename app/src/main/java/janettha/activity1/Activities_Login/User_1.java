package janettha.activity1.Activities_Login;

/**
 * Created by janettha on 13/03/18.
 */
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class User_1 {
    private String user;
    private String Nombre;
    private String Apellidos;
    private String sexo;
    private String tutor;
    private int edad;
    private boolean act1[];

    public User_1() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User_1(String user, String Nombre, String Apellidos, String sexo, int edad, String tutor){
        this.user = user;
        this.Nombre = Nombre;
        this.Apellidos= Apellidos;
        this.sexo = sexo;
        this.edad = edad;
        this.tutor = tutor;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getNombre() {
        return this.Nombre;
    }

    public void setNombre(String nombre) {
        this.Nombre = nombre;
    }

    public String getApellidos() {
        return this.Apellidos;
    }

    public void setApellidos(String apellidos) {
        this.Apellidos = apellidos;
    }

    public String getSexo() {
        return this.sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public int getEdad() {
        return this.edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getTutor(){ return this.tutor; }

    public void setTutor(String tutor){ this.tutor = tutor; }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("Nombre", Nombre);
        result.put("Apellidos", Apellidos);
        result.put("tutor", tutor);
        result.put("sexo", sexo);
        result.put("user", user);
        result.put("edad", edad);
        return result;
    }

}
