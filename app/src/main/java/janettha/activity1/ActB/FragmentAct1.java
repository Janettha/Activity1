package janettha.activity1.ActB;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
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
import java.util.Locale;

import janettha.activity1.ActA.ActA;
import janettha.activity1.Models.Emocion;
import janettha.activity1.Models.Emociones;
import janettha.activity1.Models.Respuesta;
import janettha.activity1.PDF.TemplatePDF;
import janettha.activity1.R;
import janettha.activity1.Util.LockableViewPager;
import janettha.activity1.Util.MediaPlayerSounds;


public class FragmentAct1 extends Fragment {

    static Context context;
    LayoutInflater layoutInflater;

    static LockableViewPager vp;
    //boolean vp;

    Emociones emociones;
    public final int LIM_emociones = 11;

    public static final String ARG_PAGE = "page";
    private static int mPageNumber, idDBA2, A2;
    private String textoRedaccion;
    private String exEmocion1, exEmocion2, exEmocion3, sexo, user;
    private int idEmocion1;
    private int idEmocion2;
    private int idEmocion3;
    TextToSpeech t1;
    private static MediaPlayerSounds mediaPlayerSounds;

    /*DIALOG*/
    Dialog dialog;
    TextView indicaciones, explicacionDialogo, nameEmocionDialog;
    ImageView imgEmocionDialog;
    Button btnBack;

    private DatabaseReference mDatabaseUser;

    /*PDF Respuesta*/
    static TemplatePDF templatePDF;
    ArrayList<Respuesta>respuestas = new ArrayList<>();
    Respuesta respuestaPDF;
    static String fInicio, fFin;

    public static FragmentAct1 create(int pageNumber, Context c, String user, int numA2DB, ActividadB actividadB, String sexo, LockableViewPager lockVP, TemplatePDF tPDF) {

        FragmentAct1 fragment = new FragmentAct1();
        Bundle args = new Bundle();
        vp = lockVP;
        templatePDF = tPDF;
        context = c;

        fInicio = Calendar.getInstance().getTime().toString();
        mPageNumber = pageNumber;
        A2 = actividadB.getIDMain();
        idDBA2 = numA2DB;

        Log.e("Fragment A2a","numPager: "+ mPageNumber);
        Log.e("Fragment A2a","numActividad: "+ A2);
        Log.e("Fragment A2a","nActividad_DB: "+ idDBA2);
        Log.e("Fragment A2a","nameActividad: "+ actividadB.emocionMain().getName());

        args.putString(ActB.ARG_r,actividadB.getRedaccion());
        args.putString(ActB.ARG_e1,actividadB.getExpl1());
        args.putInt(ActB.ARG_IDe1,actividadB.emocionMain().getId());
        args.putString(ActB.ARG_e2,actividadB.getExpl2());
        args.putInt(ActB.ARG_IDe2,actividadB.emocionB().getId());
        args.putString(ActB.ARG_e3,actividadB.getExpl3());
        args.putInt(ActB.ARG_IDe3,actividadB.emocionC().getId());
        args.putString(ActB.ARG_sexo, sexo);
        args.putString(ActB.ARG_u,user);

        fragment.setArguments(args);
        return fragment;
    }

    public FragmentAct1() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        emociones = new Emociones();

        if(getArguments() != null) {
            textoRedaccion = getArguments().getString(ActB.ARG_r);
            idEmocion1 = getArguments().getInt(ActB.ARG_IDe1);
            exEmocion1 = getArguments().getString(ActB.ARG_e1);
            idEmocion2 = getArguments().getInt(ActB.ARG_IDe2);
            exEmocion2 = getArguments().getString(ActB.ARG_e2);
            idEmocion3 = getArguments().getInt(ActB.ARG_IDe3);
            exEmocion3 = getArguments().getString(ActB.ARG_e3);
            user = getArguments().getString(ActB.ARG_u);
            sexo = getArguments().getString(ActB.ARG_sexo);
            emociones.Emociones(getContext(), sexo);
        }

        respuestaPDF = new Respuesta();
        mDatabaseUser = FirebaseDatabase.getInstance().getReference("users");
        mediaPlayerSounds = new MediaPlayerSounds(context);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String ex1, ex2, ex3;
        Uri ruta;
        int id1, id2, id3;
        int r = 0;

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_act1, container, false);

        /*REDACCION*/
        ((TextView) rootView.findViewById(R.id.txtText)).setText(textoRedaccion);
        indicaciones = (TextView) rootView.findViewById(R.id.Instruccion);
        Button btn = (Button) rootView.findViewById(R.id.btnSpeak);

        /*background*/
        LinearLayout bgAct1 = (LinearLayout) rootView.findViewById(R.id.LRed);
        TextView txRedaccion = (TextView) rootView.findViewById(R.id.txtText);

        /*RESPUESTAS*/
        ImageButton btnE1 = (ImageButton) rootView.findViewById(R.id.imgRedaccion);
        TextView txR1 = (TextView) rootView.findViewById(R.id.txRedaccion);
        ImageButton btnE2 = (ImageButton) rootView.findViewById(R.id.imgRedaccion2);
        TextView txR2 = (TextView) rootView.findViewById(R.id.txRedaccion2);
        ImageButton btnE3 = (ImageButton) rootView.findViewById(R.id.imgRedaccion3);
        TextView txR3 = (TextView) rootView.findViewById(R.id.txRedaccion3);

        id1 = emociones.getEmocion(idEmocion1).getId();
        id2 = emociones.getEmocion(idEmocion2).getId();
        id3 = emociones.getEmocion(idEmocion3).getId();

        if(sexo.equals("f")){
            indicaciones.setText("Lee o escucha la pequeña situación y elije ¿cómo se siente Lili?");
        }else{
            indicaciones.setText("Lee o escucha la pequeña situación y elije ¿cómo se siente Juan Carlos?");
        }

        bgAct1.setBackgroundColor(Color.parseColor(emociones.getEmocion(id1).getColor()));
        txRedaccion.setBackgroundColor(Color.parseColor(emociones.getEmocion(id1).getColor()));

        r = (int) (Math.random() * 3 ) ;
        switch (r){
            case 0:
                rootView.setBackgroundColor(Color.parseColor(emociones.getEmocion(id1).getColor()));
                interfaceFrame(rootView, btnE1, txR1, btnE2, txR2, btnE3, txR3, sexo,id1, id1, id2, id3, exEmocion1, exEmocion2, exEmocion3);
                break;
            case 1:
                rootView.setBackgroundColor(Color.parseColor(emociones.getEmocion(id1).getColor()));
                interfaceFrame(rootView, btnE1, txR1, btnE2, txR2, btnE3, txR3, sexo, id1, id3, id1, id2, exEmocion3, exEmocion1, exEmocion2);
                break;
            case 2:
                rootView.setBackgroundColor(Color.parseColor(emociones.getEmocion(id1).getColor()));
                interfaceFrame(rootView, btnE1, txR1, btnE2, txR2, btnE3, txR3, sexo, id1, id2, id3, id1, exEmocion2, exEmocion3, exEmocion1);
                break;
        }
        t1=new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.getDefault());
                }
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toSpeak = textoRedaccion;
                Toast.makeText(getContext(), toSpeak,Toast.LENGTH_SHORT).show();
                t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        return rootView;
    }

    public void onPause(){
        if(t1 !=null){
            t1.stop();
            t1.shutdown();
        }
        super.onPause();
    }

    /**
     * Returns the page number represented by this fragment object.
     */
    public int getPageNumber() {
        return mPageNumber;
    }

    private void interfaceFrame(View v, ImageButton imgFeel, TextView txFeel1, ImageButton imgFeel2, TextView txFeel2, ImageButton imgFeel3, TextView txFeel3, String s, int r, int r1, int r2, int r3, String e1, String e2, String e3){

        Uri ruta;

        final String ex_1, ex_2, ex_3;
        //expl = getArguments().getString(Activity1.ARG_r);
        ex_1 = e1;
        ex_2 = e2;
        ex_3 = e3;

        final int ran, ran1, ran2, ran3;
        ran = r;
        ran1 = r1;
        ran2 = r2;
        ran3 = r3;

        /*Nombre de botones*/
        txFeel1.setText(emociones.getEmocion(r1).getName());
        txFeel2.setText(emociones.getEmocion(r2).getName());
        txFeel3.setText(emociones.getEmocion(r3).getName());

        /*Pictures de botones*/
        ruta = Uri.parse(emociones.getEmocion(r1).getUrl());
        Picasso.with(v.getContext())
                .load(ruta).fit()
                .into(imgFeel); //fit para la imagen en la vista

        ruta = Uri.parse(emociones.getEmocion(r2).getUrl());
        Picasso.with(v.getContext())
                .load(ruta).fit()
                .into(imgFeel2); //fit para la imagen en la vista

        ruta = Uri.parse(emociones.getEmocion(r3).getUrl());
        Picasso.with(v.getContext())
                .load(ruta).fit()
                .into(imgFeel3); //fit para la imagen en la vista

        imgFeel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ran == ran1) {
                    try {
                        mediaPlayerSounds.playSound(mediaPlayerSounds.loadSoundTF(true));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    MyCustomAlertDialog(v, ran, emociones.getEmocion(ran1), "CORRECTO");
                }else {
                    try {
                        mediaPlayerSounds.playSound(mediaPlayerSounds.loadSoundTF(false));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    MyCustomAlertDialog(v, ran, emociones.getEmocion(ran1), ex_1);
                }
            }
        });
        imgFeel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ran == ran2) {
                    try {
                        mediaPlayerSounds.playSound(mediaPlayerSounds.loadSoundTF(true));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    MyCustomAlertDialog(v, ran, emociones.getEmocion(ran2), "CORRECTO");
                }else{
                    try {
                        mediaPlayerSounds.playSound(mediaPlayerSounds.loadSoundTF(false));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    MyCustomAlertDialog(v, ran, emociones.getEmocion(ran2),ex_2);
                }
            }
        });
        imgFeel3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ran == ran3) {
                    try {
                        mediaPlayerSounds.playSound(mediaPlayerSounds.loadSoundTF(true));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    MyCustomAlertDialog(v, ran, emociones.getEmocion(ran3), "CORRECTO");
                }else {
                    try {
                        mediaPlayerSounds.playSound(mediaPlayerSounds.loadSoundTF(false));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    MyCustomAlertDialog(v, ran, emociones.getEmocion(ran3), ex_3);
                }
            }
        });

    }

    @SuppressLint("ResourceAsColor")
    public void MyCustomAlertDialog(View v, int mainE, Emocion em, String explicacion) {

        final boolean respuesta = (mainE == em.getId());

        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_act1);
        dialog.setTitle(em.getName());

        LinearLayout llDialog = (LinearLayout) dialog.findViewById(R.id.LLDialogA1);
        explicacionDialogo = (TextView) dialog.findViewById(R.id.Explicacion);
        nameEmocionDialog = (TextView) dialog.findViewById(R.id.nameRespuesta);
        imgEmocionDialog = (ImageView) dialog.findViewById(R.id.imgRespuesta);
        btnBack = (Button) dialog.findViewById(R.id.btnBack);

        llDialog.setBackgroundColor(Color.parseColor(em.getColor()));

        Uri ruta = Uri.parse(em.getUrl());
        Picasso.with(getContext())
                .load(ruta).fit()
                .into(imgEmocionDialog);
        explicacionDialogo.setText(explicacion);
        nameEmocionDialog.setText(em.getName());

        if(!respuesta){
            btnBack.setBackgroundResource(R.color.Incorrecto);
            btnBack.setText(" Inténtalo de nuevo ");
        }else {
            btnBack.setBackgroundResource(R.color.Correcto);
            nameEmocionDialog.setText(" ");
            btnBack.setText(em.getName());
            explicacionDialogo.setText(" ¡LO LOGRASTE! ");
        }

        fFin = Calendar.getInstance().getTime().toString();
        respuestaPDF = new Respuesta(em.getId(), fInicio, fFin, emociones.getEmocion(mainE).getId(), respuesta);
        respuestas.add(respuestaPDF);
        templatePDF.addRespuesta(respuestaPDF);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("Fragment A2 Dialog", "INI numPager: " + mPageNumber);
                Log.e("Fragment A2 Dialog", "INI vpNum: " + vp.getCurrentItem());
                if (respuesta && vp.getCurrentItem() == 2) {
                    try {
                        mediaPlayerSounds.playSound(mediaPlayerSounds.loadFinEx());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    pdfConfig();
                    pdfView();
                    Log.e("DB/A2", "UserDB: " + mDatabaseUser.child(user).child("indiceA2").toString());
                    if (idDBA2 < 17) {
                        int indice = idDBA2 + 3;
                        mDatabaseUser.child(user).child("indiceA2").setValue(indice);
                        Log.e("DB/A2", "User: " + user + " indiceA2: " + String.valueOf(indice));
                    }else{
                        try {
                            mediaPlayerSounds.playSound(mediaPlayerSounds.loadInicio());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (respuesta && vp.getCurrentItem() < 2) {
                    vp.setCurrentItem(vp.getCurrentItem() + 1);
                    vp.setPagingEnabled(true);
                }
                Log.e("Fragment A2 Dialog", "FIN numPager: " + mPageNumber);
                Log.e("Fragment A2 Dialog", "FIN vpNum: " + vp.getCurrentItem());

                dialog.cancel();
            }
        });

        dialog.show();

    }
    private void pdfConfig(){
        templatePDF.openPDF();
        templatePDF.addMetaData(user);
        templatePDF.addHeader(user, fInicio, fFin, Calendar.getInstance().getTime().toString());
        templatePDF.addParrafo(2);
        templatePDF.createTable(templatePDF.getRespuestasActB());
        templatePDF.closeDocument();
    }

    public void pdfView(){
        templatePDF.viewPDF();
    }

    public ArrayList<Respuesta> getRespuestas(){
        return respuestas;
    }

}
