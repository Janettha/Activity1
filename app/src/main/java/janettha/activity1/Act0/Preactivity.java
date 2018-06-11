package janettha.activity1.Act0;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import janettha.activity1.ActA.ActividadA;
import janettha.activity1.Activities_Login.loginUser;
import janettha.activity1.Menu.MainmenuActivity;
import janettha.activity1.Models.Emocion;
import janettha.activity1.Models.Emociones;
import janettha.activity1.R;

public class Preactivity extends AppCompatActivity {

    private static final int NUM_PAGES = 3;

    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private LinearLayout mDotLayout;


    public final int LIM_emociones = 11;

    private TextView []mDots;

    //private static final String keySP = "UserSex";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editorSP;
    public String sexo;

    public static final String ARG_Main = "Answer 1";
    public static final String ARG_B = "Answer 2";
    public static final String ARG_C = "Answer 3";

    Emociones emociones;
    List<ActividadA> listAct0 = new ArrayList<ActividadA>();

    int r1, r2, r3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preactivity);

        mDotLayout = (android.widget.LinearLayout) findViewById(R.id.dotsLayout);
        mPager = (ViewPager) findViewById(R.id.pager);
        Bundle b = new Bundle();
        randomID();

        sharedPreferences = getSharedPreferences(loginUser.keySP, MODE_PRIVATE);
        //editorSP = sharedPreferences.edit();
        sexo = sharedPreferences.getString("sexo", "m");

        emociones = new Emociones();
        emociones.Emociones(getBaseContext(),sexo);
        fillData(this, sexo);


        try {
            mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
            mPager.setAdapter(mPagerAdapter);
            mPager.addOnPageChangeListener(viewListener);
        } catch (ClassCastException e) {
            Log.e("Fragment Manager", "Can't get fragment manager");
        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener(){

        @Override
        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels) {
            if (position == 0 && positionOffset > 0.5) {
                mPager.setCurrentItem(0, true);
            }
        }

        @Override
        public void onPageSelected(int position) {
            //backgroundDots(position);
            addDotsIndicator(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public void randomID(){
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
                mDotLayout.setBackgroundColor(android.graphics.Color.parseColor(emociones.getEmocion(r1).getColor()));
                mDots[position].setTextColor(android.graphics.Color.parseColor(emociones.getEmocion(r1).getColorB()));
            }else if(position == 1){
                mDotLayout.setBackgroundColor(android.graphics.Color.parseColor(emociones.getEmocion(r2).getColor()));
                mDots[position].setTextColor(android.graphics.Color.parseColor(emociones.getEmocion(r2).getColorB()));
            }else if(position == 2) {
                mDotLayout.setBackgroundColor(android.graphics.Color.parseColor(emociones.getEmocion(r3).getColor()));
                mDots[position].setTextColor(android.graphics.Color.parseColor(emociones.getEmocion(r3).getColorB()));
            }
        }
    }

    public void backgroundDots(int position){
        switch (position){
            case 0:
                mDotLayout.setBackgroundColor(android.graphics.Color.parseColor(emociones.getEmocion(r1).getColor()));
                break;
            case 1:
                mDotLayout.setBackgroundColor(android.graphics.Color.parseColor(emociones.getEmocion(r2).getColor()));
                break;
            case 2:
                mDotLayout.setBackgroundColor(android.graphics.Color.parseColor(emociones.getEmocion(r3).getColor()));
                break;
            default:
                mDotLayout.setBackgroundColor(android.graphics.Color.parseColor("#abd6df"));
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_activity1, menu);

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
            //Fragment f = new Fragment();
            if(position == 0) {
                return FragmentAct0.create(0,  getBaseContext(), listAct0.get(r1), sexo);
                //mDotLayout.setBackgroundColor(android.graphics.Color.parseColor(emociones.get(r1).getColor()));
                //return f;
            }else if(position == 1) {
                return FragmentAct0.create(1, getBaseContext(), listAct0.get(r2), sexo);
                //mDotLayout.setBackgroundColor(android.graphics.Color.parseColor(emociones.get(r2).getColor()));
                //return f;
            }else if(position == 2) {
                return FragmentAct0.create(2, getBaseContext(), listAct0.get(r3), sexo);
                //mDotLayout.setBackgroundColor(android.graphics.Color.parseColor(emociones.get(r3).getColor()));
                //return f;
            }else return null;
            //return f;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

    }

    private List<ActividadA> fillData (Context c, String s){

        List<Emocion> emociones = new ArrayList<Emocion>();
        ActividadA a0;

        try {
            //InputStream fileE = view.getResources().openRawResource(R.raw.emociones);
            InputStream fileE = c.getResources().openRawResource(R.raw.preactividad);
            BufferedReader brE = new BufferedReader(new InputStreamReader(fileE));
            //Lectura de emocion en actividad de redacciones
            int i = 0;
            String line1, ruta="";
            if (fileE != null) {
                while ((line1 = brE.readLine()) != null) {
                    String[] array = line1.split(","); // Split according to the hyphen and put them in an array
                    //btnList.add(i, new Emocion(Integer.parseInt(array[0]), array[1], array[2], array[0]+".png", array[3]));
                    int id = Integer.parseInt(array[0]);
                    int id2 = Integer.parseInt(array[1]);
                    int id3 = Integer.parseInt(array[2]);
                    //System.out.print("----------------------ID-----------------"+id+","+id2+","+id3);
                    // emociones.get(id).getSexo()
                    emociones.add(new Emocion(id, emociones.get(id).getName(), s, emociones.get(id).getUrl(), emociones.get(id).getColor(), emociones.get(id).getColorB()));
                    emociones.add(new Emocion(id2, emociones.get(id2).getName(), s, emociones.get(id2).getUrl(), emociones.get(id2).getColor(), emociones.get(id2).getColorB()));
                    emociones.add(new Emocion(id3, emociones.get(id3).getName(), s, emociones.get(id3).getUrl(), emociones.get(id3).getColor(), emociones.get(id3).getColorB()));
                    a0 = new ActividadA(i, emociones);
                    listAct0.add(i, a0);
                    i++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return listAct0;
    }

}
