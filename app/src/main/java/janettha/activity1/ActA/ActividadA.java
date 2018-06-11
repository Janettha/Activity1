package janettha.activity1.ActA;

import java.util.List;

import janettha.activity1.Models.ActividadMaster;
import janettha.activity1.Models.Emocion;

/**
 * Created by janeth on 2018-02-11.
 */

public class ActividadA extends ActividadMaster{

    public ActividadA(){
        super();
    }

    public ActividadA(int id, List<Emocion> listEmociones){
        super(id,listEmociones);
    }

}
