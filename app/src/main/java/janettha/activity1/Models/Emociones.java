package janettha.activity1.Models;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileReader;
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

    public Emociones(Context context)  {

        try {
            fill(context);
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*
        Emocion e1 = new Emocion(1,"ENTUSIASMO","F", "1.png","FCF3CF");
        Emocion e2 = new Emocion(2,"FELICIDAD","F","2.png","FDFFD0");
        Emocion e3 = new Emocion(3,"SORPRESA","F","3.png","D6EAF8");

        emociones.add(0, e1);
        emociones.add(1, e2);
        emociones.add(2, e3);
        */

    }

    public void fill(Context context) throws IOException{
        InputStream fileE = null;
        FileReader frE = null;
        BufferedReader brE = null;
        String line1;

        try {
            //fileE = new File("emociones.txt");
            InputStream is = context.getResources().openRawResource(R.raw.emociones);
            //fileE = new InputStream(context.getResources().getAssets().open("emociones.txt"));
            ////frE = new FileReader (fileE);
            brE = new BufferedReader(new InputStreamReader(is));

            //Lectura de emocion
            int i = 0;
            if (is != null) {
                while ((line1 = brE.readLine()) != null || line1 != "\n") {
                    String s[] = line1.split(",");
                    String url = s[0] + ".png";
                    System.out.println(s[0]);
                    System.out.println(s[1]);
                    System.out.println(s[2]);
                    System.out.println(s[3]);
                    System.out.println(url);
                    emociones.add(i, new Emocion(Integer.parseInt(s[0]), s[1], s[2], url, s[3]));
                    System.out.println(emociones.get(i).getName());
                    i++;
                }
            }
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public Emocion getEmocion(int i) {
        return emociones.get(i);
    }

}
