package janettha.activity1.Models;

/**
 * Created by janeth on 08/11/2017.
 */

public class Emocion {
    private String name;
    private String color;
    private int id;
    private String sexo;
    private String url;

    public Emocion(int id, String nombre, String sexo, String url, String color){
        this.id = id;
        this.name = nombre;
        this.sexo = sexo;
        this.url = url;
        this.color = color;
    }

    public String getName() {        return name;   }

    public void setName(String name) {        this.name = name;    }

    public String getColor() {        return color;    }

    public void setColor(String color) {        this.color = color;    }

    public int getId() {        return id;    }

    public void setId(int id) {        this.id = id;    }

    public String getSexo() {        return sexo;    }

    public void setSexo(String sexo) {        this.sexo = sexo;    }

    public String getUrl() {        return url;    }

    public void setUrl(String url) {        this.url = url;    }
}
