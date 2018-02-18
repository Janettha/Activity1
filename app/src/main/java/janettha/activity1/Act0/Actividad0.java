package janettha.activity1.Act0;

import java.util.ArrayList;
import java.util.List;

import janettha.activity1.Models.Emocion;

/**
 * Created by janeth on 2018-02-11.
 */

public class Actividad0 {
    int id;
    List<Emocion> listEmociones = new ArrayList<Emocion>();

    public Actividad0(){}

    public Actividad0(int id, Emocion eMain, Emocion e2, Emocion e3){
        this.id = id;
        this.listEmociones.add(0,eMain);
        this.listEmociones.add(1,e2);
        this.listEmociones.add(2,e3);
    }

    public int getIDMain(){ return this.id; }

    public Emocion emocionMain(){
        return this.listEmociones.get(0);
    }
    public Emocion emocionB(){
        return this.listEmociones.get(1);
    }
    public Emocion emocionC(){ return this.listEmociones.get(2); }

}
