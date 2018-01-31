package janettha.activity1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by janeth on 08/11/2017.
 */

public class Feelings {
    List<Feel> feel = new ArrayList<Feel>();

    public Feelings() {
        fill();
        System.out.println("-----------------------------------");
        System.out.println("-----------------------------------");
        System.out.println("-----------------------------------");
        for (int i = 0; i <= feel.size()-1; i++) {
            System.out.println("ID: " + feel.get(i)
                    + " Feeling: " + feel.get(i).getName()
                    + " Color: " + feel.get(i).getColor());
        }
    }

    public Feel getFeelOne(int i) {
        return feel.get(i);
    }

    public void fill(){
        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;
        try{
            archivo = new File ("feelings.txt");
            fr = new FileReader (archivo);
            br = new BufferedReader(fr);
            // Lectura de datos
            String linea;
            while((linea=br.readLine())!=null || linea!="\n"){
                String s[] = linea.split("-");
                feel.add(new Feel(s[0], s[1]));
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if( null != fr ){
                    fr.close();
                }
            }catch (Exception e2){
                e2.printStackTrace();
            }
        }
    }
}
