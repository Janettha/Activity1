package janettha.activity1.Act0;

import android.app.Dialog;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import janettha.activity1.Models.Emocion;
import janettha.activity1.Models.Emociones;
import janettha.activity1.R;

public class Preactivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preactivity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Nueva emoción.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_preactivity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        List<Emocion> emociones = new ArrayList<Emocion>();
        List<Actividad0> btnList = new ArrayList<Actividad0>();
        private final int LIM_emociones = 11;


        /*DIALOG*/
        Dialog dialog;
        TextView nameEmocionDialog;
        ImageView imgEmocionDialog;
        Button btnBack;
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_preactivity, container, false);
            Emociones e = new Emociones();
            int r1, r2, r3;

            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            ImageView imgFeel = (ImageView) rootView.findViewById(R.id.imgFeel);
            Button btnA1 = (Button) rootView.findViewById(R.id.ans1);
            Button btnA2 = (Button) rootView.findViewById(R.id.ans2);
            Button btnA3 = (Button) rootView.findViewById(R.id.ans3);

            emociones = e.Emociones(getContext(),"f");
            listEmociones(rootView);

            //            Toast.makeText(getContext(), emociones.get(r1).getName()+", "+emociones.get(r2).getName()+", "+emociones.get(r3).getName(), Toast.LENGTH_SHORT).show();
            textView.setText("¿Cómo creés que se siente Laura?");

            r1 = (int) (Math.random() * LIM_emociones ) ;
            r2 = (int) (Math.random() * LIM_emociones ) ;
            r3 = (int) (Math.random() * LIM_emociones ) ;
            while(r1 == r2){
                r2 = (int) (Math.random() * LIM_emociones ) ;
            }
            while(r3 == r1 || r3 == r2){
                r3 = (int) (Math.random() * LIM_emociones ) ;
            }

            if(getArguments().getInt(ARG_SECTION_NUMBER) == 1){
                interfaceFrame(rootView, imgFeel,btnA1,btnA2,btnA3, emociones.get(r1).getSexo(),r1, btnList.get(r1).emocionMain().getId(), btnList.get(r1).emocionB().getId(),btnList.get(r1).emocionC().getId());
                Toast.makeText(getContext(), "->"+btnList.get(r1).emocionMain().getId()+","+btnList.get(r1).emocionB().getId()+","+btnList.get(r1).emocionC().getId(), Toast.LENGTH_SHORT).show();
            }else if(getArguments().getInt(ARG_SECTION_NUMBER) == 2) {
                interfaceFrame(rootView, imgFeel,btnA1,btnA2,btnA3, emociones.get(r2).getSexo(),r2, btnList.get(r2).emocionB().getId(), btnList.get(r2).emocionMain().getId(),btnList.get(r2).emocionC().getId());
                Toast.makeText(getContext(), btnList.get(r2).emocionB().getId()+"->"+btnList.get(r2).emocionMain().getId()+","+btnList.get(r2).emocionC().getId(), Toast.LENGTH_SHORT).show();
            }else if(getArguments().getInt(ARG_SECTION_NUMBER) == 3) {
                interfaceFrame(rootView, imgFeel,btnA1,btnA2,btnA3, emociones.get(r3).getSexo(),r3, btnList.get(r3).emocionC().getId(), btnList.get(r3).emocionB().getId(),btnList.get(r3).emocionMain().getId());
                Toast.makeText(getContext(), btnList.get(r3).emocionC().getId()+","+btnList.get(r3).emocionC().getId()+"->"+btnList.get(r3).emocionMain().getId(), Toast.LENGTH_SHORT).show();
            }

            return rootView;
        }
        public void MyCustomAlertDialog(View v, Emocion em, String respuesta, boolean resp) {
            dialog = new Dialog(getContext());
            dialog.setContentView(R.layout.dialog_act0);
            dialog.setTitle("Ya casi lo logras");

            nameEmocionDialog = (TextView) dialog.findViewById(R.id.nameRespuesta);
            imgEmocionDialog = (ImageView) dialog.findViewById(R.id.imgRespuesta);
            btnBack = (Button) dialog.findViewById(R.id.btnBack);

            if(resp == false) {
                //dialog.setTitle("Inténtalo de nuevo");
                btnBack.setBackgroundColor(Color.RED);
                btnBack.setText("Inténtalo de nuevo");
            }else if(resp == true) {
                //dialog.setTitle("Correcto");
                btnBack.setText("Correcto");
                btnBack.setBackgroundColor(Color.GREEN);
            }

            Uri ruta = Uri.parse(em.getUrl());
            Picasso.with(getContext())
                    .load(ruta).fit()
                    .into(imgEmocionDialog);
            nameEmocionDialog.setText(respuesta);

            dialog.show();

        }
        private void listEmociones(View view)  {

            Emocion eMain, e2, e3 = new Emocion();
            Actividad0 a0;

            try {
                InputStream fileE = view.getResources().openRawResource(R.raw.preactividad);
                BufferedReader brE = new BufferedReader(new InputStreamReader(fileE));
                //Lectura de emocion
                int i = 0;
                String line1;
                if (fileE != null) {
                    while ((line1 = brE.readLine()) != null) {
                        String[] array = line1.split(","); // Split according to the hyphen and put them in an array
                        //btnList.add(i, new Emocion(Integer.parseInt(array[0]), array[1], array[2], array[0]+".png", array[3]));
                        int id = Integer.parseInt(array[0]);
                        int id2 = Integer.parseInt(array[1]);
                        int id3 = Integer.parseInt(array[2]);
                        //System.out.print("----------------------ID-----------------"+id+","+id2+","+id3);
                        eMain = new Emocion(id, emociones.get(id).getName(), emociones.get(id).getSexo(), emociones.get(id).getUrl(), emociones.get(id).getColor(), emociones.get(id).getColorB());
                        e2 = new Emocion(id2, emociones.get(id2).getName(), emociones.get(id2).getSexo(), emociones.get(id2).getUrl(), emociones.get(id2).getColor(), emociones.get(id2).getColorB());
                        e3 = new Emocion(id3, emociones.get(id3).getName(), emociones.get(id3).getSexo(), emociones.get(id3).getUrl(), emociones.get(id3).getColor(), emociones.get(id3).getColorB());
                        a0 = new Actividad0(i, eMain, e2, e3);
                        btnList.add(i, a0);
                        i++;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void interfaceFrame(View v, ImageView imgFeel, Button b1, Button b2, Button b3, String s, int r, int r1, int r2, int r3){

            String foto="";//="android.resource://janettha.activity1/drawable/f"+String.valueOf(r);
            Uri ruta;

            final int ran, ran1, ran2, ran3;
            ran = r;
            ran1 = r1;
            ran2 = r2;
            ran3 = r3;

            v.setBackgroundColor(Color.parseColor(btnList.get(r).emocionMain().getColor()));
            b1.setBackgroundColor(Color.parseColor(emociones.get(r).getColorB()));
            b2.setBackgroundColor(Color.parseColor(emociones.get(r).getColorB()));
            b3.setBackgroundColor(Color.parseColor(emociones.get(r).getColorB()));


            b1.setText(emociones.get(r1).getName());
            b2.setText(emociones.get(r2).getName());
            b3.setText(emociones.get(r3).getName());

            /*
            if(s.equals("f")){
                foto = "android.resource://janettha.activity1/drawable/f"+String.valueOf(r);
            }else if(s.equals("m")){
                foto = "android.resource://janettha.activity1/drawable/m"+String.valueOf(r);
            }
            */
            ruta = Uri.parse(emociones.get(r).getUrl());
            Picasso.with(v.getContext())
                    .load(ruta).fit()
                    .into(imgFeel); //fit para la imagen en la vista

            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(ran == ran1)
                        MyCustomAlertDialog(v,emociones.get(ran1),emociones.get(ran1).getName(),true);
                    else {
                        MyCustomAlertDialog(v,emociones.get(ran1),emociones.get(ran1).getName(),false);
                    }
                }
            });
            b2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(ran == ran2)
                        MyCustomAlertDialog(v,emociones.get(ran2),emociones.get(ran2).getName(),true);
                    else {
                        MyCustomAlertDialog(v,emociones.get(ran2),emociones.get(ran2).getName(),false);
                    }
                }
            });
            b3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(ran == ran3)
                        MyCustomAlertDialog(v,emociones.get(ran3),emociones.get(ran3).getName(),true);
                    else {
                        MyCustomAlertDialog(v,emociones.get(ran3),emociones.get(ran3).getName(),false);
                    }
                }
            });
        }
    }


            /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }


}
