package janettha.activity1.ActB;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
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

import janettha.activity1.Act0.Actividad0;
import janettha.activity1.Act1.Actividad1;
import janettha.activity1.Models.Emocion;
import janettha.activity1.Models.Emociones;
import janettha.activity1.Models.Respuesta;
import janettha.activity1.PDF.TemplatePDF;
import janettha.activity1.R;
import janettha.activity1.Util.LockableViewPager;

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;
    LockableViewPager vp;

    private int currentVP;

    List<Emocion> emociones;
    List<Actividad1> listAct1 = new ArrayList<>();
    Emociones em;


    //private Actividad0 act1, act2, act3;
    private boolean answer;
    private String idSexo, user, tutor, email;
    private SharedPreferences sharedPreferences;


    //Vista ActB
    private String textoRedaccion;
    private String exEmocion1, exEmocion2, exEmocion3, sexo;
    TextToSpeech t1;

    /*DIALOG*/
    Dialog dialog;
    TextView indicaciones, explicacionDialogo, nameEmocionDialog;
    ImageView imgEmocionDialog;
    Button btnBack;

    /*PDF Respuesta*/
    TemplatePDF templatePDF;
    ArrayList<Respuesta>respuestas = new ArrayList<>();
    Respuesta respuestaPDF;
    String fInicio, fFin;

    public SliderAdapter(Context context, String userU, Actividad1 a0, Actividad1 a1, Actividad1 a2, String sexo, LockableViewPager v){
        this.context = context;
        this.vp = v;
        em = new Emociones();
        listAct1.add(a0);
        listAct1.add(a1);
        listAct1.add(a2);
        //act1 = a0;
        //act2 = a1;
        //act3 = a2;
        idSexo = sexo;
        emociones = em.Emociones(context, idSexo);
        respuestaPDF = new Respuesta();
        user = userU;
        Toast.makeText(context, "User: "+user, Toast.LENGTH_SHORT).show();
        templatePDF = new TemplatePDF(context);
        //pdfConfig();
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (LinearLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View rootView = layoutInflater.inflate(R.layout.fragment_act1, container, false);

        /* VIEW ejercicios*/
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

        currentVP = position;
        fInicio = Calendar.getInstance().getTime().toString();

        int rMain = emociones.get(listAct1.get(position).getEmocion1().getId()).getId();
        int rB = emociones.get(listAct1.get(position).getEmocion2().getId()).getId();
        int rC = emociones.get(listAct1.get(position).getEmocion3().getId()).getId();
        int r;

        bgAct1.setBackgroundColor(Color.parseColor(emociones.get(rMain).getColor()));
        txRedaccion.setBackgroundColor(Color.parseColor(emociones.get(rMain).getColor()));

        r = (int) (Math.random() * 2);
        if (r == 0) {
            rootView.setBackgroundColor(Color.parseColor(emociones.get(rMain).getColor()));
            interfaceFrame(rootView, btnE1, txR1, btnE2, txR2, btnE3, txR3, sexo,rMain, rMain, rB, rC, exEmocion1, exEmocion2, exEmocion3);
        } else if (r == 1) {
            rootView.setBackgroundColor(Color.parseColor(emociones.get(rMain).getColor()));
            interfaceFrame(rootView, btnE1, txR1, btnE2, txR2, btnE3, txR3, sexo, rMain, rC, rMain, rB, exEmocion3, exEmocion1, exEmocion2);
        } else if (r == 2) {
            rootView.setBackgroundColor(Color.parseColor(emociones.get(rMain).getColor()));
            interfaceFrame(rootView, btnE1, txR1, btnE2, txR2, btnE3, txR3, sexo, rMain, rB, rC, rMain, exEmocion2, exEmocion3, exEmocion1);
        }


        container.addView(rootView);
        return rootView;
    }

    private void interfaceFrame(View v, ImageButton imgFeel, TextView txFeel1, ImageButton imgFeel2, TextView txFeel2, ImageButton imgFeel3, TextView txFeel3, String s, int r, int r1, int r2, int r3, String e1, String e2, String e3) {
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
        txFeel1.setText(emociones.get(r1).getName());
        txFeel2.setText(emociones.get(r2).getName());
        txFeel3.setText(emociones.get(r3).getName());

        /*Pictures de botones*/
        ruta = Uri.parse(emociones.get(r1).getUrl());
        Picasso.with(v.getContext())
                .load(ruta).fit()
                .into(imgFeel); //fit para la imagen en la vista

        ruta = Uri.parse(emociones.get(r2).getUrl());
        Picasso.with(v.getContext())
                .load(ruta).fit()
                .into(imgFeel2); //fit para la imagen en la vista

        ruta = Uri.parse(emociones.get(r3).getUrl());
        Picasso.with(v.getContext())
                .load(ruta).fit()
                .into(imgFeel3); //fit para la imagen en la vista

        imgFeel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ran == ran1)
                    MyCustomAlertDialog(v,emociones.get(ran1),"CORRECTO");
                else {
                    MyCustomAlertDialog(v, emociones.get(ran1), ex_1);
                }
            }
        });
        imgFeel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ran == ran2)
                    MyCustomAlertDialog(v,emociones.get(ran2),"CORRECTO");
                else{
                    MyCustomAlertDialog(v,emociones.get(ran2),ex_2);
                }
            }
        });
        imgFeel3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ran == ran3)
                    MyCustomAlertDialog(v, emociones.get(ran3),"CORRECTO");
                else {
                    MyCustomAlertDialog(v, emociones.get(ran3), ex_3);
                }
            }
        });
    }

    @SuppressLint("ResourceAsColor")
    public void MyCustomAlertDialog(View v, Emocion em, String explicacion) {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_act1);
        dialog.setTitle(em.getName());

        LinearLayout llDialog = (LinearLayout) dialog.findViewById(R.id.LLDialogA1);
        explicacionDialogo = (TextView) dialog.findViewById(R.id.Explicacion);
        nameEmocionDialog = (TextView) dialog.findViewById(R.id.nameRespuesta);
        imgEmocionDialog = (ImageView) dialog.findViewById(R.id.imgRespuesta);
        btnBack = (Button) dialog.findViewById(R.id.btnBack);

        llDialog.setBackgroundColor(Color.parseColor(em.getColor()));

        Uri ruta = Uri.parse(em.getUrl());
        Picasso.with(context)
                .load(ruta).fit()
                .into(imgEmocionDialog);
        explicacionDialogo.setText(explicacion);
        //explicacionDialogo.setTextColor(Color.parseColor(em.getColorB()));
        nameEmocionDialog.setText(em.getName());
        //nameEmocionDialog.setTextColor(Color.parseColor(em.getColorB()));

        if(explicacion.substring(0,1).equals("¿")) {
            btnBack.setBackgroundResource(R.color.Incorrecto);
            btnBack.setText(" Inténtalo de nuevo ");
        }else if(explicacion.equals("CORRECTO")) {
            btnBack.setBackgroundResource(R.color.Correcto);
            nameEmocionDialog.setText(" ");
            //btnBack.setText(" Correcto ");
            btnBack.setText(em.getName());
            explicacionDialogo.setText(" ¡LO LOGRASTE! ");
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment, getPageNumber());
                if(currentVP == 3){
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

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)object);
    }

    @Override
    public void startUpdate(@NonNull ViewGroup container) {

        super.startUpdate(container);
    }
}
