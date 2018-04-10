package janettha.activity1.Models;


public class Respuesta {
    private int id;
    private String inicioS;
    private String finS;
    private int respuesta;
    private boolean calif;

    public Respuesta(int id, String inicioS, String finS, int respuesta, boolean calif) {
        this.id = id;
        this.inicioS = inicioS;
        this.finS = finS;
        this.respuesta = respuesta;
        this.calif = calif;
    }

    public int getId() {        return id;    }

    public String getInicioS() {        return inicioS;    }

    public String getFinS() {        return finS;    }

    public int getRespuesta() {        return respuesta;    }

    public boolean isCalif() {        return calif;    }

}
