package janettha.activity1.Models;

/**
 * Created by janettha on 9/03/18.
 */

public class Usuarios {

    String tutor, user, pass, name, surnames, sexo, fechaN;
    int edad;

    public Usuarios(){}

    //public Usuarios(int edad, String fechaN, String userName, String pass, String sexo,  String surnames, String userTutor, String user) {
    public Usuarios (String tutor, String user, String pass, String name, String surnames, String sexo, String fechaN, int edad){
        this.user = user;
        this.pass = pass;
        this.tutor = tutor;
        this.name = name;
        this.surnames = surnames;
        this.sexo = sexo;
        this.fechaN = fechaN;
        this.edad = edad;
    }


    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getSurnames() {
        return surnames;
    }

    public void setSurnames(String surnames) {
        this.surnames = surnames;
    }

    public String getUserTutor() {
        return tutor;
    }

    public void setUserTutor(String userTutor) {
        this.tutor = userTutor;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUserName() {
        return user;
    }

    public void setUserName(String userName) {
        this.user = userName;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getFechaN() {
        return fechaN;
    }

    public void setFechaN(String fechaN) {
        this.fechaN = fechaN;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }
}
