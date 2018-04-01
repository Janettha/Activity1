package janettha.activity1.Act1;

import janettha.activity1.Models.Emocion;

/**
 * Created by janettha on 28/02/18.
 */

public class Actividad1 {
    private int ID;
    private String Redaccion;
    private Emocion emocion1;
    private String expl1;
    private Emocion emocion2;
    private String expl2;
    private Emocion emocion3;
    private String expl3;

    public Actividad1(int ID, String redaccion, Emocion emocion1, String expl1, Emocion emocion2, String expl2, Emocion emocion3, String expl3) {
        this.ID = ID;
        Redaccion = redaccion;
        this.emocion1 = emocion1;
        this.expl1 = expl1;
        this.emocion2 = emocion2;
        this.expl2 = expl2;
        this.emocion3 = emocion3;
        this.expl3 = expl3;
    }

    public int getID() {        return ID;    }

    public void setID(int ID) {        this.ID = ID;    }

    public String getRedaccion() {        return Redaccion;    }

    public void setRedaccion(String redaccion) {        Redaccion = redaccion;    }

    public Emocion getEmocion1() {        return emocion1;    }

    public void setEmocion1(Emocion emocion1) {        this.emocion1 = emocion1;    }

    public String getExpl1() {        return expl1;    }

    public void setExpl1(String expl1) {        this.expl1 = expl1;    }

    public Emocion getEmocion2() {        return emocion2;    }

    public void setEmocion2(Emocion emocion2) {        this.emocion2 = emocion2;    }

    public String getExpl2() {        return expl2;    }

    public void setExpl2(String expl2) {        this.expl2 = expl2;    }

    public Emocion getEmocion3() {        return emocion3;    }

    public void setEmocion3(Emocion emocion3) {        this.emocion3 = emocion3;    }

    public String getExpl3() {        return expl3;    }

    public void setExpl3(String expl3) {        this.expl3 = expl3;    }
}
