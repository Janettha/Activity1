package janettha.activity1.Models;

import java.util.ArrayList;
import java.util.List;

import janettha.activity1.Models.Emocion;

public class ActividadMaster{
    protected int id;
    protected List<Emocion> listEmociones = new ArrayList<Emocion>();

    public ActividadMaster (){}

    public ActividadMaster (int id, List<Emocion> lEmociones){
        this.id = id;
        this.listEmociones.add(0,lEmociones.get(0));    //emocion Main
        this.listEmociones.add(1,lEmociones.get(1));    //emocion 2
        this.listEmociones.add(2,lEmociones.get(2));    //emocion 1
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Emocion> getListEmociones() {
        return listEmociones;
    }

    public void setListEmociones(List<Emocion> listEmociones) {
        this.listEmociones = listEmociones;
    }

    public Emocion emocionMain(){        return this.listEmociones.get(0); }
    public Emocion emocionB(){
                                                                return this.listEmociones.get(1);
    }
    public Emocion emocionC(){ return this.listEmociones.get(2); }

}