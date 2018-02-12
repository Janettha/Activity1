package janettha.activity1.Models;

import android.view.View;

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

    public List<Emocion> Emociones(View view)  {

        try {
            InputStream fileE = view.getResources().openRawResource(R.raw.emociones);
            BufferedReader brE = new BufferedReader(new InputStreamReader(fileE));
            //Lectura de emocion
            int i = 0;
            String line1;
            if (fileE != null) {
                while ((line1 = brE.readLine()) != null) {
                    String[] array = line1.split(","); // Split according to the hyphen and put them in an array
                    emociones.add(i, new Emocion(Integer.parseInt(array[0]), array[1], array[2], array[0]+".png", array[3]));
                    i++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return emociones;
    }


    public Emocion getEmocion(int i) {
        return emociones.get(i);
    }

}
