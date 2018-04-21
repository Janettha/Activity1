package janettha.activity1.ActA;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import janettha.activity1.Models.Emocion;
import janettha.activity1.Models.Emociones;
import janettha.activity1.Models.Respuesta;
import janettha.activity1.PDF.TemplatePDF;
import janettha.activity1.R;
import janettha.activity1.Util.LockableViewPager;
import janettha.activity1.Util.MediaPlayerSounds;


public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;
    LockableViewPager vp;

    static int A1;
    private int currentVP;

    List<ActividadA> listActA = new ArrayList<>();
    Emociones emociones;

    //private Actividad0 act1, act2, act3;
    private boolean answer;
    private MediaPlayerSounds mediaPlayerSounds;

    private String idSexo;
    private String user, tutor, email;
    private SharedPreferences sharedPreferences;

    private DatabaseReference mDatabaseUser;

    /*DIALOG*/
    private Dialog dialog;
    private LinearLayout llPreactivityDialog;
    private TextView nameEmocionDialog;
    private ImageView imgEmocionDialog;
    private Button btnBack;

    /*PDF Respuesta*/
    TemplatePDF templatePDF;
    ArrayList<Respuesta>respuestas = new ArrayList<>();
    Respuesta respuestaPDF;
    String fInicio, fFin;

    public SliderAdapter(Context context, String userU, int numA1, ActividadA a0, ActividadA a1, ActividadA a2, String sexo, LockableViewPager v) {

        if(context != null) Log.e("Context", context.toString());
        this.context = context;
        this.vp = v;
        A1 = numA1;

        emociones = new Emociones();
        mDatabaseUser = FirebaseDatabase.getInstance().getReference("users");

        listActA.add(a0);
        listActA.add(a1);
        listActA.add(a2);

        mediaPlayerSounds = new MediaPlayerSounds(context);

        idSexo = sexo;
        emociones.Emociones(context, idSexo);
        respuestaPDF = new Respuesta();
        user = userU;
        tutor = tutor;
        Toast.makeText(context, "User: "+user, Toast.LENGTH_SHORT).show();
/*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            soundManager = new SoundManager(context);
        }
*/
        templatePDF = new TemplatePDF(context);
        pdfConfig();

    }


    @Override
    public int getCount() {
        return listActA.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (LinearLayout) object;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View rootView = layoutInflater.inflate(R.layout.fragment_preactivity, container, false);
        //View guardarView = layoutInflater.inflate(R.layout.guarda_respuestas,container, false );

    /*VIEW ejercicios*/
        TextView indicaciones;
        ImageView imgFeel;
        Button btnA1, btnA2, btnA3;

        currentVP = position;
        fInicio = Calendar.getInstance().getTime().toString();

        int rMain = emociones.getEmocion(listActA.get(position).emocionMain().getId()).getId();
        int rB = emociones.getEmocion(listActA.get(position).emocionB().getId()).getId();
        int rC = emociones.getEmocion(listActA.get(position).emocionC().getId()).getId();
        int r;

        indicaciones = (TextView) rootView.findViewById(R.id.section_label);
        imgFeel = (ImageView) rootView.findViewById(R.id.imgFeel);
        btnA1 = (Button) rootView.findViewById(R.id.ans1);
        btnA2 = (Button) rootView.findViewById(R.id.ans2);
        btnA3 = (Button) rootView.findViewById(R.id.ans3);

        if(idSexo.equals("f")){
            indicaciones.setText("Lee o escucha la pequeña situación y elije ¿cómo se siente Lili?");
        }else{
            indicaciones.setText("Lee o escucha la pequeña situación y elije ¿cómo se siente Juan Carlos?");
        }

        r = (int) (Math.random() * 3);
        if (r == 0) {
            rootView.setBackgroundColor(Color.parseColor(emociones.getEmocion(rMain).getColor()));
            interfaceFrame(rootView, imgFeel, btnA1, btnA2, btnA3, idSexo, rMain, rMain, rB, rC);
        } else if (r == 1) {
            rootView.setBackgroundColor(Color.parseColor(emociones.getEmocion(rMain).getColor()));
            interfaceFrame(rootView, imgFeel, btnA1, btnA2, btnA3, idSexo, rMain, rC, rMain, rB);
        } else if (r == 2) {
            rootView.setBackgroundColor(Color.parseColor(emociones.getEmocion(rMain).getColor()));
            interfaceFrame(rootView, imgFeel, btnA1, btnA2, btnA3, idSexo, rMain, rB, rC, rMain);
        }

    /*VIEW guardar
        EditText correo;
        Button YES, NO;

        correo = (EditText)guardarView.findViewById(R.id.confirmCorreo);
        YES = (Button)guardarView.findViewById(R.id.GuardarSI);
        NO = (Button)guardarView.findViewById(R.id.GuardarNO);
*/


        container.addView(rootView);
        return rootView;
    }

    private void interfaceFrame(View v, ImageView txFeel, Button txtFeel1, Button txtFeel2, Button txtFeel3, String s, int r, int r1, int r2, int r3) {

        Uri ruta;
        //answer = false;
        final int ex_1, ex_2, ex_3;
        //expl = getArguments().getString(Activity1.ARG_r);
        ex_1 = r1;
        ex_2 = r2;
        ex_3 = r3;

        final int ran, ran1, ran2, ran3;
        ran = r;
        ran1 = r1;
        ran2 = r2;
        ran3 = r3;

        /*Pictures de botones*/
        ruta = Uri.parse(emociones.getEmocion(r).getUrl());
        Picasso.with(v.getContext())
                .load(ruta).fit()
                .into(txFeel); //fit para la imagen en la vista

        /*Nombre y color de botones*/
        v.setBackgroundColor(Color.parseColor(emociones.getEmocion(r).getColor()));
        txtFeel1.setBackgroundColor(Color.parseColor(emociones.getEmocion(r).getColorB()));
        txtFeel2.setBackgroundColor(Color.parseColor(emociones.getEmocion(r).getColorB()));
        txtFeel3.setBackgroundColor(Color.parseColor(emociones.getEmocion(r).getColorB()));

        txtFeel1.setText(emociones.getEmocion(ex_1).getName());
        txtFeel2.setText(emociones.getEmocion(ex_2).getName());
        txtFeel3.setText(emociones.getEmocion(ex_3).getName());


        txtFeel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean respTF;
                if (ran == ran1) respTF=true;
                else             respTF=false;
                try {
                    mediaPlayerSounds.playSound(mediaPlayerSounds.loadSoundTF(respTF));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                MyCustomAlertDialog(v, emociones.getEmocion(ran1), emociones.getEmocion(ran1).getName(), respTF);
            }
        });
        txtFeel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean respTF;
                if (ran == ran2) respTF=true;
                else             respTF=false;
                try {
                    mediaPlayerSounds.playSound(mediaPlayerSounds.loadSoundTF(respTF));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                MyCustomAlertDialog(v, emociones.getEmocion(ran2), emociones.getEmocion(ran2).getName(), respTF);
            }
        });
        txtFeel3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean respTF;
                if (ran == ran3) respTF=true;
                else             respTF=false;
                try {
                    mediaPlayerSounds.playSound(mediaPlayerSounds.loadSoundTF(respTF));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                MyCustomAlertDialog(v, emociones.getEmocion(ran3), emociones.getEmocion(ran3).getName(), respTF);
            }
        });

    }

    public void MyCustomAlertDialog(View v, Emocion em, String respuesta, final boolean resp) {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_act0);
        dialog.setTitle("Ya casi lo logras");

        llPreactivityDialog = (LinearLayout) dialog.findViewById(R.id.lLaCT0);
        nameEmocionDialog = (TextView) dialog.findViewById(R.id.nameRespuesta);
        imgEmocionDialog = (ImageView) dialog.findViewById(R.id.imgRespuesta);
        btnBack = (Button) dialog.findViewById(R.id.btnBack);

        currentVP = vp.getPosition();
        fFin = Calendar.getInstance().getTime().toString();
        respuestaPDF = new Respuesta(emociones.getEmocion(listActA.get(currentVP).emocionMain().getId()).getId(), fInicio, fFin, em.getId(), resp);
        respuestas.add(respuestaPDF);

        Uri ruta = Uri.parse(em.getUrl());
        Picasso.with(context)
                .load(ruta).fit()
                .into(imgEmocionDialog);
        nameEmocionDialog.setText(respuesta);
        //nameEmocionDialog.setTextColor(Color.parseColor(em.getColorB()));
        llPreactivityDialog.setBackgroundColor(Color.parseColor(em.getColor()));

        if(!resp) {
            //dialog.setTitle("Inténtalo de nuevo");
            btnBack.setBackgroundResource(R.color.Incorrecto);
            btnBack.setText(" Inténtalo de nuevo ");
        }else if(resp) {
            //dialog.setTitle("Correcto");
            btnBack.setBackgroundResource(R.color.Correcto);
            btnBack.setText(" Correcto ");
        }
            //answer = true;
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(resp){
                    btnBack.setBackgroundResource(R.color.Correcto);
                    btnBack.setText(" Correcto ");
                    dialog.cancel();
                    vp.setPagingEnabled(true);
                    if(currentVP == 2) {
                        pdfConfig();
                        pdfView();
                        try {
                            mediaPlayerSounds.playSound(mediaPlayerSounds.loadFinEx());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        //mDatabaseUser.child(user).child("indiceA1").toString();
                        //Log.e("DB/A1", "UserDB: "+mDatabaseUser.child(user).child("indiceA1").toString());
                        if(A1<12) {
                            int indice=listActA.get(0).emocionMain().getId()+3;
                            mDatabaseUser.child(user).child("indiceA1").setValue(indice);
                            Log.e("DB/A1", "User: " + user + " indiceA1: " + String.valueOf(indice));
                        }else{
                            try {
                                mediaPlayerSounds.playSound(mediaPlayerSounds.loadInicio());
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    vp.setCurrentItem(currentVP+1);
                }else{
                    btnBack.setBackgroundResource(R.color.Incorrecto);
                    btnBack.setText(" Inténtalo de nuevo ");
                    dialog.cancel();
                }
            }
        });

        dialog.show();
    }

    private void pdfConfig(){
        templatePDF.openPDF();
        templatePDF.addMetaData(user);
        templatePDF.addHeader(user, fInicio, fFin, Calendar.getInstance().getTime().toString());
        templatePDF.addParrafo(1);
        templatePDF.createTable(respuestas);
        templatePDF.closeDocument();
    }

    public void pdfView(){
        templatePDF.viewPDF();
    }

    public boolean getAnswer(){
        return answer;
    }

    public void setAnswer(boolean b){
        answer = b;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)object);
    }

}
