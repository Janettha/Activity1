package janettha.activity1.Models;

/**
 * Created by janettha on 13/03/18.
 */
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Usuarios {
    private String user;
    private String nombre;
    private String apellidos;
    private String sexo;
    private String tutor;
    private int edad;
    private String inicioS;
    private String finS;

    public Usuarios() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Usuarios(String user, String nombre, String apellidos, String sexo, int edad, String tutor){
        this.user = user;
        this.nombre = nombre;
        this.apellidos= apellidos;
        this.sexo = sexo;
        this.edad = edad;
        this.tutor = tutor;
        this.inicioS = Calendar.getInstance().getTime().toString();
        this.finS = "";
    }

    public Usuarios(String user, String nombre, String apellidos, String sexo, int edad, String tutor, String finS){
        this.user = user;
        this.nombre = nombre;
        this.apellidos= apellidos;
        this.sexo = sexo;
        this.edad = edad;
        this.tutor = tutor;
        this.inicioS = Calendar.getInstance().getTime().toString();
        this.finS = finS;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return this.apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
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

    public String getInicioS() {        return inicioS;    }

    public void setInicioS(String inicioS) {        this.inicioS = inicioS;    }

    public String getFinS() {        return finS;    }

    public void setFinS(String finS) {        this.finS = finS;    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("nombre", nombre);
        result.put("apellidos", apellidos);
        result.put("tutor", tutor);
        result.put("sexo", sexo);
        result.put("user", user);
        result.put("edad", edad);
        result.put("inicioS", inicioS);
        result.put("finS", finS);
        return result;
    }

}
