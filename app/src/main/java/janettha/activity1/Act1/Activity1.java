package janettha.activity1.Act1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.itextpdf.text.pdf.codec.TiffWriter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import janettha.activity1.ActA.ActA;
import janettha.activity1.Activities_Login.loginUser;
import janettha.activity1.Menu.MainmenuActivity;
import janettha.activity1.Models.Emocion;
import janettha.activity1.Models.Emociones;
import janettha.activity1.Models.Respuesta;
import janettha.activity1.PDF.TemplatePDF;
import janettha.activity1.R;
import janettha.activity1.Util.LockableViewPager;

public class Activity1 extends FragmentActivity {
    /* GOOGLE - Slide Between Fragments with ViewPager */
    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 3;
    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private LockableViewPager mPager;
    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;

    public final int LIM_emociones = 16;

    public static final String ARG_r = "Redaccion";
    public static final String ARG_u = "User";
    public static final String ARG_sexo = "SexoUser";
    public static final String ARG_e1 = "Emocion1";
    public static final String ARG_IDe1 = "Emocion1ID";
    public static final String ARG_e2 = "Emocion2";
    public static final String ARG_IDe2 = "Emocion2ID";
    public static final String ARG_e3 = "Emocion3";
    public static final String ARG_IDe3 = "Emocion3ID";
    private TextToSpeech tts;


    //public final String keySP = "UserSex";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editorSP;
    private String sexo, userU;

    List<Emocion> emociones = new ArrayList<Emocion>();
    List<Actividad1> listAct1 = new ArrayList<Actividad1>();

    int r1, r2, r3, A2;
    private TextView[]mDots;
    private LinearLayout mDotLayout;

    Context context;
    TemplatePDF templatePDF;
    ArrayList<Respuesta>respuestas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_1);
        mPager = (LockableViewPager) findViewById(R.id.pager);
        mDotLayout = (LinearLayout) findViewById(R.id.dotsLayout);

        sharedPreferences = getSharedPreferences(loginUser.keySP, MODE_PRIVATE);
        //editorSP = sharedPreferences.edit();
        sexo = sharedPreferences.getString("sexo", "m");
        userU = sharedPreferences.getString("usuario", "");

        Emociones em = new Emociones();
        emociones = em.Emociones(getBaseContext(),sexo);
        fillData(this, sexo);
        context = getBaseContext();
        templatePDF = new TemplatePDF(context);

        Bundle b = getIntent().getExtras();
        if(b != null){
            A2 = b.getInt("a2",0);
        }
        if(A2>LIM_emociones)   randomID();
        else         secuencialID(A2);

        addDotsIndicator(r1);

        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.addOnPageChangeListener(viewListener);

    }

    private void secuencialID(int indice){
        r1 = indice;
        if(A2 != 16) {
            r2 = 0;
            r3 = 1;
        }else{
            r2 = indice + 1;
            r3 = indice + 2;
        }
    }

    private void randomID(){
        r1 = (int) (Math.random() * LIM_emociones ) ;
        r2 = (int) (Math.random() * LIM_emociones ) ;
        r3 = (int) (Math.random() * LIM_emociones ) ;

        while(r1 == r2){
            r2 = (int) (Math.random() * LIM_emociones ) ;
        }
        while(r1 == r3 || r2 == r3){
            r3 = (int) (Math.random() * LIM_emociones ) ;
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @SuppressLint("WrongViewCast")
        @Override
        public Fragment getItem(int position) {
            FragmentAct1 f;
            if(position == 0) {
                //mPager.setPagingEnabled(false);
                f = FragmentAct1.create(position, context, userU, A2, listAct1.get(r1), sexo, mPager, templatePDF);
                return f;
            }else if(position == 1) {
                //mPager.setPagingEnabled(false);
                f = FragmentAct1.create(position, context, userU, A2, listAct1.get(r2), sexo, mPager, templatePDF);
                return f;
            }else if(position == 2) {
                //mPager.setPagingEnabled(false);
                f = FragmentAct1.create(position, context, userU, A2, listAct1.get(r3), sexo, mPager, templatePDF);
                return f;
            }else return null;


        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener(){

        @Override
        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            //backgroundDots(position);
            //sliderAdapter.setAnswer(false);
            mPager.setPagingEnabled(false);
            mPager.setPosition(position);
            addDotsIndicator(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    private List<Actividad1> fillData (Context c, String s){
        InputStream fileE;
        try {
            //InputStream fileE = view.getResources().openRawResource(R.raw.emociones);
            if(s.equals("f"))
                fileE = c.getResources().openRawResource(R.raw.redaccionesf);
            else
                fileE = c.getResources().openRawResource(R.raw.redaccionesm);

            BufferedReader brE = new BufferedReader(new InputStreamReader(fileE));
            //Lectura de emocion en actividad de redacciones
            int i = 0;
            String line1="";
            if (fileE != null) {
                while ((line1 = brE.readLine()) != null) {
                    String[] array = line1.split("-"); // Split according to the hyphen and put them in an array
                    /* Revisar f o m
                    if(s.equals("f")){
                        ruta = "android.resource://janettha.activity1/drawable/f";
                    }else if(s.equals("m")){
                        ruta = "android.resource://janettha.activity1/drawable/m";
                    }*/
                    Actividad1 a1 = new Actividad1(Integer.parseInt(array[0]),array[1],emociones.get(Integer.parseInt(array[2])), array[3], emociones.get(Integer.parseInt(array[4])), array[5],emociones.get(Integer.parseInt(array[6])), array[7]);
                    //Actividad1 a3 = new Actividad1(0,array[1],emociones.get(0), array[3], emociones.get(0), array[5],emociones.get(0), array[7]);
                    //Actividad1 a3 = new Actividad1(0,array[0], emociones.get(0),array[0], emociones.get(0), array[0], emociones.get(0),array[0]);
                    listAct1.add(i, a1);
                    i++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return listAct1;
    }

    public void addDotsIndicator(int position){
        mDots = new TextView[3];
        mDotLayout.removeAllViews();
        for(int i=0; i< mDots.length; i++){
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.white));
            mDotLayout.addView(mDots[i]);
        }
        if (mDots.length > 0){
            if(position == 0) {
                mDotLayout.setBackgroundColor(android.graphics.Color.parseColor(emociones.get(listAct1.get(r1).getEmocion1().getId()).getColor()));
                mDots[position].setTextColor(android.graphics.Color.parseColor(emociones.get(listAct1.get(r1).getEmocion1().getId()).getColorB()));
            }else if(position == 1){
                mDotLayout.setBackgroundColor(android.graphics.Color.parseColor(emociones.get(listAct1.get(r2).getEmocion1().getId()).getColor()));
                mDots[position].setTextColor(android.graphics.Color.parseColor(emociones.get(listAct1.get(r2).getEmocion1().getId()).getColorB()));
            }else if(position == 2) {
                mDotLayout.setBackgroundColor(android.graphics.Color.parseColor(emociones.get(listAct1.get(r3).getEmocion1().getId()).getColor()));
                mDots[position].setTextColor(android.graphics.Color.parseColor(emociones.get(listAct1.get(r3).getEmocion1().getId()).getColorB()));
            }
        }
    }

    public Actividad1 getAct1(int i) {
        return listAct1.get(i);
    }

/*    @Override
    public void onBackPressed() {
        //final Intent intent = new Intent(this, loginUser.class);
        startActivity(new Intent(Activity1.this, MainmenuActivity.class));
    }*/

}
