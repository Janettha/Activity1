package janettha.activity1.Act0;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import janettha.activity1.Activities_Login.MainActivity;
import janettha.activity1.Activities_Login.SignUpActivity;
import janettha.activity1.Activities_Login.loginUser;
import janettha.activity1.Menu.MainmenuActivity;
import janettha.activity1.Models.Emocion;
import janettha.activity1.Models.Emociones;
import janettha.activity1.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAct0 extends Fragment {

    private static final String ARG_PAGE = "section_number";
    private int mPageNumber;

    List<Emocion> emociones = new ArrayList<Emocion>();
    List<Actividad0> btnList = new ArrayList<Actividad0>();
    private final int LIM_emociones = 11;

    private int idEmocionMain, idEmocionB, idEmocionC;
    private String idSexo;

    /*DIALOG*/
    Dialog dialog;
    LinearLayout llPreactivityDialog;
    TextView nameEmocionDialog;
    ImageView imgEmocionDialog;
    Button btnBack;

    public static FragmentAct0 create(int pageNumber, Context context, Actividad0 actividad0, String sexo) {

        FragmentAct0 fragment = new FragmentAct0();
        Bundle args = new Bundle();

        args.putInt(ARG_PAGE, actividad0.getIDMain());
        //args.putString(Activity1.ARG_tx,emociones.get(id).getName());
        args.putInt(Preactivity.ARG_Main,actividad0.emocionMain().getId());
        args.putInt(Preactivity.ARG_B,actividad0.emocionB().getId());
        args.putInt(Preactivity.ARG_C,actividad0.emocionC().getId());
        args.putString(loginUser.keySP, sexo);

        fragment.setArguments(args);
        return fragment;
    }

    public FragmentAct0() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Emociones em = new Emociones();

        if(getArguments() != null) {
            mPageNumber = getArguments().getInt(ARG_PAGE);
            idEmocionMain = getArguments().getInt(Preactivity.ARG_Main);
            idEmocionB = getArguments().getInt(Preactivity.ARG_B);
            idEmocionC = getArguments().getInt(Preactivity.ARG_C);

            idSexo = getArguments().getString(loginUser.keySP);
            emociones = em.Emociones(getContext(), idSexo);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /*VIEW*/
        ImageView imgFeel;
        Button btnA1, btnA2, btnA3, btNext;

        Uri ruta;
        int rMain, rB, rC;
        int r = 0;


        // Inflate the layout containing a title and body text.
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_preactivity, container, false);

        imgFeel = (ImageView) rootView.findViewById(R.id.imgFeel);
        btnA1 = (Button) rootView.findViewById(R.id.ans1);
        btnA2 = (Button) rootView.findViewById(R.id.ans2);
        btnA3 = (Button) rootView.findViewById(R.id.ans3);
        btNext = (Button) rootView.findViewById(R.id.SaveResultados);

        rMain = emociones.get(idEmocionMain).getId();
        rB = emociones.get(idEmocionB).getId();
        rC = emociones.get(idEmocionC).getId();

        r = (int) (Math.random() * LIM_emociones ) ;
        if(r < 4){
            rootView.setBackgroundColor(Color.parseColor(emociones.get(rMain).getColor()));
            interfaceFrame(rootView,imgFeel, btnA1, btnA2, btnA3, btNext, idSexo,rMain, rMain, rB, rC);
        }else if(r>3 && r<8) {
            rootView.setBackgroundColor(Color.parseColor(emociones.get(rMain).getColor()));
            interfaceFrame(rootView,imgFeel, btnA1, btnA2, btnA3, btNext, idSexo,rMain, rC, rMain, rB);
        }else if(r>7 && r<12) {
            rootView.setBackgroundColor(Color.parseColor(emociones.get(rMain).getColor()));
            interfaceFrame(rootView,imgFeel, btnA1, btnA2, btnA3, btNext, idSexo,rMain, rB, rC, rMain);
        }

        return rootView;
    }

    public int getPageNumber() {
        return mPageNumber;
    }

    private void interfaceFrame(View v, ImageView txFeel, Button txtFeel1, Button txtFeel2, Button txtFeel3, Button next, String s, int r, int r1, int r2, int r3){

        Uri ruta;

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
        ruta = Uri.parse(emociones.get(r).getUrl());
        Picasso.with(v.getContext())
                .load(ruta).fit()
                .into(txFeel); //fit para la imagen en la vista

        /*Nombre y color de botones*/
        v.setBackgroundColor(Color.parseColor(emociones.get(r).getColor()));
        txtFeel1.setBackgroundColor(Color.parseColor(emociones.get(r).getColorB()));
        txtFeel2.setBackgroundColor(Color.parseColor(emociones.get(r).getColorB()));
        txtFeel3.setBackgroundColor(Color.parseColor(emociones.get(r).getColorB()));

        txtFeel1.setText(emociones.get(ex_1).getName());
        txtFeel2.setText(emociones.get(ex_2).getName());
        txtFeel3.setText(emociones.get(ex_3).getName());


        txtFeel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ran == ran1) {
                    MyCustomAlertDialog(v, emociones.get(ran1), emociones.get(ran1).getName(), true);

                }else {
                    MyCustomAlertDialog(v,emociones.get(ran1),emociones.get(ran1).getName(),false);
                }

            }
        });
        txtFeel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ran == ran2){
                    MyCustomAlertDialog(v,emociones.get(ran2),emociones.get(ran2).getName(),true);
                }else {
                    MyCustomAlertDialog(v,emociones.get(ran2),emociones.get(ran2).getName(),false);
                }
            }
        });
        txtFeel3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ran == ran3) {
                    MyCustomAlertDialog(v, emociones.get(ran3), emociones.get(ran3).getName(), true);
                }else {
                    MyCustomAlertDialog(v,emociones.get(ran3),emociones.get(ran3).getName(),false);
                }
            }
        });


    }

    public void MyCustomAlertDialog(View v, Emocion em, String respuesta, boolean resp) {
        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_act0);
        dialog.setTitle("Ya casi lo logras");

        llPreactivityDialog = (LinearLayout) dialog.findViewById(R.id.lLaCT0);
        nameEmocionDialog = (TextView) dialog.findViewById(R.id.nameRespuesta);
        imgEmocionDialog = (ImageView) dialog.findViewById(R.id.imgRespuesta);
        btnBack = (Button) dialog.findViewById(R.id.btnBack);

        if(resp == false) {
            //dialog.setTitle("Inténtalo de nuevo");
            btnBack.setBackgroundResource(R.color.Incorrecto);
            btnBack.setText(" Inténtalo de nuevo ");
        }else if(resp == true) {
            //dialog.setTitle("Correcto");
            btnBack.setBackgroundResource(R.color.Correcto);
            btnBack.setText(" Correcto ");
        }

        Uri ruta = Uri.parse(em.getUrl());
        Picasso.with(getContext())
                .load(ruta).fit()
                .into(imgEmocionDialog);
        nameEmocionDialog.setText(respuesta);
        //nameEmocionDialog.setTextColor(Color.parseColor(em.getColorB()));
        llPreactivityDialog.setBackgroundColor(Color.parseColor(em.getColor()));

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        dialog.show();

    }
}
