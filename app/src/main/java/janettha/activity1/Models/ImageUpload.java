package janettha.activity1.Models;

/**
 * Created by janeth on 22/11/2017.
 */

public class ImageUpload {
    private String nombre;
    private int id;
    private String sexo;
    private String url;

    public ImageUpload(String nombre, int id, String sexo, String url) {
        this.nombre = nombre;
        this.id = id;
        this.sexo = sexo;
        this.url = url;
    }

    public String getNombre() {       return nombre;    }

    public void setNombre(String nombre) {        this.nombre = nombre;    }

    public int getId() {        return id;    }

    public void setId(int id) {        this.id = id;    }

    public String getSexo() {        return sexo;    }

    public void setSexo(String sexo) {        this.sexo = sexo;    }

    public String getUrl() {        return url;    }

    public void setUrl(String url) {        this.url = url;    }
}
