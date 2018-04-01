package janettha.activity1.Util;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;


/**
 * Created by janettha on 7/03/18.
 */


public class Date {

    private int diaU, mesU, anioU;

    public Date(int diaU, int mesU, int anioU){
        this.diaU = diaU;
        this.mesU = mesU;
        this.anioU = anioU;
    }

    public String getToString(){
        return twoDigits(this.diaU) + "/" + getSMes(this.mesU) + "/" + twoDigits(this.anioU);
    }


    private String twoDigits(int n){
        return (n<=9) ? ("0"+n) : String.valueOf(n);
    }

    private String normalDate(){
        return this.anioU + "/" + twoDigits(this.mesU)  + "/" +twoDigits(this.diaU);
    }

    private String getSMes(int month) {
        String sMes;
        switch (month) {
            case 0:
                sMes = "Enero";
                break;
            case 1:
                sMes = "Febrero";
                break;
            case 2:
                sMes = "Marzo";
                break;
            case 3:
                sMes = "Abril";
                break;
            case 4:
                sMes = "Mayo";
                break;
            case 5:
                sMes = "Junio";
                break;
            case 6:
                sMes = "Julio";
                break;
            case 7:
                sMes = "Agosto";
                break;
            case 8:
                sMes = "Septiembre";
                break;
            case 9:
                sMes = "Octubre";
                break;
            case 10:
                sMes = "Noviembre";
                break;
            case 11:
                sMes = "Diciembre";
                break;
            default:
                sMes = String.valueOf(twoDigits(month));
                break;
        }
        return sMes;
    }

    public int calculaEdad() {
        Calendar today = Calendar.getInstance();

        int diff_year = today.get(Calendar.YEAR) -  anioU;
        int diff_month = today.get(Calendar.MONTH) - mesU;
        int diff_day = today.get(Calendar.DAY_OF_MONTH) - diaU;

        //Si está en ese año pero todavía no los ha cumplido
        if (diff_month < 0 || (diff_month == 0 && diff_day < 0)) {
            diff_year = diff_year - 1; //no aparecían los dos guiones del postincremento :|
        }
        return diff_year;
    }

}
