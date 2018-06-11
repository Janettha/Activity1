package janettha.activity1.Models;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import janettha.activity1.R;

/**
 * Created by janeth on 08/11/2017.
 */

public class Emociones {

    List<Emocion> emociones = new ArrayList<Emocion>();

    public Emociones(){};

    //public List<Emocion> Emociones(Context c, String s)  {
    public void Emociones(Context c, String s)  {
        try {
            //InputStream fileE = view.getResources().openRawResource(R.raw.emociones);
            InputStream fileE = c.getResources().openRawResource(R.raw.emociones);
            BufferedReader brE = new BufferedReader(new InputStreamReader(fileE));
            //Lectura de emocion
            int i = 0;
            String line1, ruta="";
            if (fileE != null) {
                while ((line1 = brE.readLine()) != null) {
                    String[] array = line1.split(","); // Split according to the hyphen and put them in an array
                    if(s.equals("f")){
                        ruta = "android.resource://janettha.activity1/drawable/f";
                    }else if(s.equals("m")){
                        ruta = "android.resource://janettha.activity1/drawable/m";
                    }
                    emociones.add(i, new Emocion(Integer.parseInt(array[0]), array[1], array[2], ruta+String.valueOf(array[0]), array[3], array[4]));
                    i++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //return emociones;
    }

    public List<Emocion> getEmociones() {
        return emociones;
    }

    public Emocion getEmocion(int i) {
        return emociones.get(i);
    }

}
