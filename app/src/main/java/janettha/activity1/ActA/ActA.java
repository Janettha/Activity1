package janettha.activity1.ActA;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import janettha.activity1.Activities_Login.loginUser;
import janettha.activity1.Menu.MainmenuActivity;
import janettha.activity1.Models.Emocion;
import janettha.activity1.Models.Emociones;
import janettha.activity1.R;
import janettha.activity1.Util.LockableViewPager;

public class ActA extends Activity {

    public static final String ARG_Main = "Answer 1";
    public static final String ARG_B = "Answer 2";
    public static final String ARG_C = "Answer 3";

    private LockableViewPager mSlideViewPager;
    private LinearLayout mDotLayout;

    private SliderAdapter sliderAdapter;

    public final int LIM_emociones = 11;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editorSP;
    public String sexo, userU;

    int r[] = new int[3];
    int A1;
    private TextView[] mDots;

    Emociones emociones;
    List<ActividadA> listAct0 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acta);

        mSlideViewPager = (LockableViewPager) findViewById(R.id.slideViewPager);
        mDotLayout = (LinearLayout) findViewById(R.id.dotsLayout);

        //se determina número de actividad
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            A1 = bundle.getInt("a1", 0);
        }
        if (A1 > LIM_emociones) randomID();
        else secuencialID();

        sharedPreferences = getSharedPreferences(loginUser.keySP, MODE_PRIVATE);
        sexo = sharedPreferences.getString("sexo", "m");
        userU = sharedPreferences.getString("usuario", "");


        emociones = new Emociones();
        emociones.Emociones(this, sexo);
        emociones.getEmociones();
        fillData(this, sexo);
        addDotsIndicator(0);

        Context c = this;

        Log.e("emocionesA1List",listAct0.get(r[0]).emocionMain().getId()+"-"+listAct0.get(r[1]).emocionMain().getId()+"-"+listAct0.get(r[2]).emocionMain().getId());
        sliderAdapter = new SliderAdapter(c, userU, A1, listAct0.get(r[0]), listAct0.get(r[1]), listAct0.get(r[2]), sexo, mSlideViewPager);
        mSlideViewPager.setAdapter(sliderAdapter);
        mSlideViewPager.addOnPageChangeListener(viewListener);

        mSlideViewPager.setPagingEnabled(false);

        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener(){

        @Override
        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
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
                mDotLayout.setBackgroundColor(android.graphics.Color.parseColor(emociones.getEmocion(r[0]).getColor()));
                mDots[position].setTextColor(android.graphics.Color.parseColor(emociones.getEmocion(r[0]).getColorB()));
            }else if(position == 1){
                mDotLayout.setBackgroundColor(android.graphics.Color.parseColor(emociones.getEmocion(r[1]).getColor()));
                mDots[position].setTextColor(android.graphics.Color.parseColor(emociones.getEmocion(r[1]).getColorB()));
            }else if(position == 2) {
                mDotLayout.setBackgroundColor(android.graphics.Color.parseColor(emociones.getEmocion(r[2]).getColor()));
                mDots[position].setTextColor(android.graphics.Color.parseColor(emociones.getEmocion(r[2]).getColorB()));
            }
        }
    }

    private void secuencialID(){
        r[0] = A1;
        r[1] = A1+1;
        r[2] = A1+2;
    }

    private void randomID(){
        r[0] = (int) (Math.random() * LIM_emociones ) ;
        r[1] = (int) (Math.random() * LIM_emociones ) ;
        r[2] = (int) (Math.random() * LIM_emociones ) ;

        while(r[0] == r[1]){
            r[1] = (int) (Math.random() * LIM_emociones ) ;
        }
        while(r[0] == r[2] || r[1] == r[2]){
            r[2] = (int) (Math.random() * LIM_emociones ) ;
        }
    }

    private void fillData (Context c, String s){


        try {
            InputStream fileE = c.getResources().openRawResource(R.raw.preactividad);
            BufferedReader brE = new BufferedReader(new InputStreamReader(fileE));

            //Lectura de emocion en actividad de redacciones
            int i = 0;
            String line1, ruta="";
            if (fileE != null) {
                while ((line1 = brE.readLine()) != null) {
                    String[] array = line1.split(","); // Split according to the hyphen and put them in an array

                    List<Emocion> lEmociones = new ArrayList<Emocion>();
                    ActividadA a0;

                    int id = Integer.parseInt(array[0]);
                    int id2 = Integer.parseInt(array[1]);
                    int id3 = Integer.parseInt(array[2]);

                    lEmociones.add(new Emocion(id, emociones.getEmocion(id).getName(), s, emociones.getEmocion(id).getUrl(), emociones.getEmocion(id).getColor(), emociones.getEmocion(id).getColorB()));
                    lEmociones.add(new Emocion(id2, emociones.getEmocion(id2).getName(), s, emociones.getEmocion(id2).getUrl(), emociones.getEmocion(id2).getColor(), emociones.getEmocion(id2).getColorB()));
                    lEmociones.add(new Emocion(id3, emociones.getEmocion(id3).getName(), s, emociones.getEmocion(id3).getUrl(), emociones.getEmocion(id3).getColor(), emociones.getEmocion(id3).getColorB()));
                    a0 = new ActividadA(i, lEmociones);
                    listAct0.add(i, a0);
                    i++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ActA.this, MainmenuActivity.class));
    }

}
