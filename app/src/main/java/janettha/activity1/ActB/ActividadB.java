package janettha.activity1.ActB;

import java.util.List;

import janettha.activity1.Models.ActividadMaster;
import janettha.activity1.Models.Emocion;

/**
 * Created by janettha on 28/02/18.
 */

public class ActividadB extends ActividadMaster{

    private List<String> explicacion;

    public ActividadB(){
        super();
    }

    public ActividadB(int id, List<Emocion> listEmociones, List<String> explicacion) {
        super(id, listEmociones);
        this.explicacion = explicacion;
    }

    public String getRedaccion() {
        return this.explicacion.get(0);
    }

    public void setRedaccion(String redaccion) {
        this.explicacion.set(0,redaccion);
    }

    public String getExpl1() {
        return this.explicacion.get(0);
    }

    public void setExpl1(String expl1) {
        this.explicacion.set(0, expl1);
    }

    public String getExpl2() {
        return this.explicacion.get(1);
    }

    public void setExpl2(String expl2) {
        this.explicacion.set(1, expl2);
    }

    public String getExpl3() {
        return this.explicacion.get(2);
    }

    public void setExpl3(String expl3) {
        this.explicacion.set(2, expl3);
    }
}
