package janettha.activity1.ActB;

import android.content.Context;
import android.content.SharedPreferences;
import android.speech.tts.TextToSpeech;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import janettha.activity1.Act1.Actividad1;
import janettha.activity1.ActB.SliderAdapter;
import janettha.activity1.Activities_Login.loginUser;
import janettha.activity1.Models.Emocion;
import janettha.activity1.Models.Emociones;
import janettha.activity1.R;
import janettha.activity1.Util.LockableViewPager;

public class ActB extends AppCompatActivity {

    private TextToSpeech tts;
    public final int LIM_emociones = 16;

    private LockableViewPager mSlideViewPager;
    private SliderAdapter sliderAdapter;
    private LinearLayout mDotLayout;

    private SharedPreferences sharedPreferences;

    List<Emocion> emociones = new ArrayList<>();
    List<Actividad1> listAct1 = new ArrayList<>();

    int r1, r2, r3, A2;
    private TextView[]mDots;
    private String sexo, userU;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actb);

        sharedPreferences = getSharedPreferences(loginUser.keySP, MODE_PRIVATE);
        //editorSP = sharedPreferences.edit();
        sexo = sharedPreferences.getString("sexo", "m");
        userU = sharedPreferences.getString("usuario", "");

        Emociones em = new Emociones();
        emociones = em.Emociones(getBaseContext(),sexo);
        fillData(this, sexo);

        mSlideViewPager = (LockableViewPager)findViewById(R.id.slideViewPager);
        mDotLayout = (LinearLayout) findViewById(R.id.dotsLayout);

        //se determina número de actividad
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            A2 = bundle.getInt("a2",0);
        }
        if(A2==LIM_emociones)   randomID();
        else         secuencialID(A2);

        sliderAdapter = new SliderAdapter(this, userU, listAct1.get(r1), listAct1.get(r2), listAct1.get(r3), sexo, mSlideViewPager);
        mSlideViewPager.setAdapter(sliderAdapter);
        //mSlideViewPager = new ActA.ScreenSlidePagerAdapter(getSupportFragmentManager());
        //mSlideViewPager.setAdapter(mPagerAdapter);
        mSlideViewPager.addOnPageChangeListener(viewListener);

        mSlideViewPager.setPagingEnabled(false);

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
            mSlideViewPager.setPagingEnabled(false);
            mSlideViewPager.setPosition(position);
            addDotsIndicator(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

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
                mDotLayout.setBackgroundColor(android.graphics.Color.parseColor(emociones.get(r1).getColor()));
                mDots[position].setTextColor(android.graphics.Color.parseColor(emociones.get(r1).getColorB()));
            }else if(position == 1){
                mDotLayout.setBackgroundColor(android.graphics.Color.parseColor(emociones.get(r2).getColor()));
                mDots[position].setTextColor(android.graphics.Color.parseColor(emociones.get(r2).getColorB()));
            }else if(position == 2) {
                mDotLayout.setBackgroundColor(android.graphics.Color.parseColor(emociones.get(r3).getColor()));
                mDots[position].setTextColor(android.graphics.Color.parseColor(emociones.get(r3).getColorB()));
            }
        }
    }

    private void secuencialID(int indice){
        r1 = indice;
        r2 = indice+1;
        r3 = indice+2;
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
}
