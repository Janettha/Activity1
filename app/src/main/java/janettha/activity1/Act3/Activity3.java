package janettha.activity1.Act3;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import janettha.activity1.Menu.MainmenuActivity;
import janettha.activity1.Models.Emocion;
import janettha.activity1.Models.Emociones;
import janettha.activity1.R;

public class Activity3 extends FragmentActivity {
    /* GOOGLE - Slide Between Fragments with ViewPager */
    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 3;
    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;
    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;

    public final int LIM_emociones = 11;

    public static final String ARG_r = "Redaccion";
    public static final String ARG_ex = "Explicacion";
    public static final String ARG_e1 = "Emocion1";
    public static final String ARG_IDe1 = "Emocion1ID";
    public static final String ARG_e2 = "Emocion2";
    public static final String ARG_IDe2 = "Emocion2ID";
    public static final String ARG_e3 = "Emocion3";
    public static final String ARG_IDe3 = "Emocion3ID";
    private TextToSpeech tts;

    List<Emocion> emociones = new ArrayList<Emocion>();
    List<Actividad3> listAct3 = new ArrayList<Actividad3>();

    int r1, r2, r3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3);
        Emociones em = new Emociones();
        emociones = em.Emociones(getBaseContext(),"f");
        fillData(this, "f");

        try {
            /* Se pasan parámetros a Bundle */
            Bundle b = new Bundle();
            mPager = (ViewPager) findViewById(R.id.pager);

            //b.putString(ARG_tx, emociones.get(0).getName());

            // Instantiate a ViewPager and a PagerAdapter.
            //FragmentManager fragmentManager = getSupportFragmentManager();
            mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
            mPager.setAdapter(mPagerAdapter);


        } catch (ClassCastException e) {
            Log.e("Fragment Manager", "Can't get fragment manager");
        }

        r1 = (int) (Math.random() * LIM_emociones ) ;
        r2 = (int) (Math.random() * LIM_emociones ) ;
        r3 = (int) (Math.random() * LIM_emociones ) ;


        while(r1 == r2){
            r2 = (int) (Math.random() * LIM_emociones ) ;
        }
        while(r3 == r1 || r3 == r2){
            r3 = (int) (Math.random() * LIM_emociones ) ;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_activity3, menu);

        menu.findItem(R.id.action_previous).setEnabled(mPager.getCurrentItem() > 0);

        // Add either a "next" or "finish" button to the action bar, depending on which page
        // is currently selected.
        MenuItem item = menu.add(Menu.NONE, R.id.action_next, Menu.NONE,
                (mPager.getCurrentItem() == mPagerAdapter.getCount() - 1)
                        ? R.string.action_finish
                        : R.string.action_next);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                // Navigate "up" the demo structure to the launchpad activity.
                // See http://developer.android.com/design/patterns/navigation.html for more.
                NavUtils.navigateUpTo(this, new Intent(this, MainmenuActivity.class));
                return true;

            case R.id.action_previous:
                // Go to the previous step in the wizard. If there is no previous step,
                // setCurrentItem will do nothing.
                if(mPager.getCurrentItem() != 0)
                    mPager.setCurrentItem(mPager.getCurrentItem() - 1);
                return true;

            case R.id.action_next:
                // Advance to the next step in the wizard. If there is no next step, setCurrentItem
                // will do nothing.
                if (mPager.getCurrentItem() != NUM_PAGES)
                    mPager.setCurrentItem(mPager.getCurrentItem() + 1);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @SuppressLint("WrongViewCast")
        @Override
        public Fragment getItem(int position) {
            if(position == 0) {
                return FragmentAct3.create(position, getBaseContext(), listAct3.get(r1));
            }else if(position == 1) {
                return FragmentAct3.create(position, getBaseContext(), listAct3.get(r2));
            }else if(position == 2) {
                return FragmentAct3.create(position, getBaseContext(), listAct3.get(r3));
            }else return null;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

    private List<Actividad3> fillData (Context c, String s){

        try {
            //InputStream fileE = view.getResources().openRawResource(R.raw.emociones);
            InputStream fileE = c.getResources().openRawResource(R.raw.redacciones);
            BufferedReader brE = new BufferedReader(new InputStreamReader(fileE));
            //Lectura de emocion en actividad de redacciones
            int i = 0;
            String line1, ruta="";
            if (fileE != null) {
                while ((line1 = brE.readLine()) != null) {
                    String[] array = line1.split("-"); // Split according to the hyphen and put them in an array
                    /* Revisar f o m
                    if(s.equals("f")){
                        ruta = "android.resource://janettha.activity1/drawable/f";
                    }else if(s.equals("m")){
                        ruta = "android.resource://janettha.activity1/drawable/m";
                    }*/
                    Actividad3 a3 = new Actividad3(Integer.parseInt(array[0]),array[1],emociones.get(Integer.parseInt(array[2])), array[3], emociones.get(Integer.parseInt(array[4])), array[5],emociones.get(Integer.parseInt(array[6])), array[7]);
                    //Actividad3 a3 = new Actividad3(0,array[1],emociones.get(0), array[3], emociones.get(0), array[5],emociones.get(0), array[7]);
                    //Actividad3 a3 = new Actividad3(0,array[0], emociones.get(0),array[0], emociones.get(0), array[0], emociones.get(0),array[0]);
                    listAct3.add(i, a3);
                    i++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return listAct3;
    }

    public Actividad3 getAct3(int i) {
        return listAct3.get(i);
    }
}
