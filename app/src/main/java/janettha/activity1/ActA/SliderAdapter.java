package janettha.activity1.ActA;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import janettha.activity1.Act0.Actividad0;
import janettha.activity1.Models.Emocion;
import janettha.activity1.Models.Emociones;
import janettha.activity1.R;


public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;
    LockableViewPager vp;

    private int currentVP;

    List<Emocion> emociones;
    List<Actividad0> listAct0 = new ArrayList<>();
    Emociones em;

    //private Actividad0 act1, act2, act3;
    private boolean answer;
    private String idSexo;

    /*DIALOG*/
    private Dialog dialog;
    private LinearLayout llPreactivityDialog;
    private TextView nameEmocionDialog;
    private ImageView imgEmocionDialog;
    private Button btnBack;

    public SliderAdapter(Context context, Actividad0 a0, Actividad0 a1, Actividad0 a2, String sexo, LockableViewPager v) {
        this.context = context;
        this.vp = v;
        em = new Emociones();
        listAct0.add(a0);
        listAct0.add(a1);
        listAct0.add(a2);
        //act1 = a0;
        //act2 = a1;
        //act3 = a2;
        idSexo = sexo;
        emociones = em.Emociones(context, idSexo);
    }


    @Override
    public int getCount() {
        return listAct0.size();
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
        View guardarView = layoutInflater.inflate(R.layout.guarda_respuestas,container, false );

    /*VIEW ejercicios*/
        ImageView imgFeel;
        Button btnA1, btnA2, btnA3;

        currentVP = position;

        int rMain = emociones.get(listAct0.get(position).emocionMain().getId()).getId();
        int rB = emociones.get(listAct0.get(position).emocionB().getId()).getId();
        int rC = emociones.get(listAct0.get(position).emocionC().getId()).getId();
        int r;

        imgFeel = (ImageView) rootView.findViewById(R.id.imgFeel);
        btnA1 = (Button) rootView.findViewById(R.id.ans1);
        btnA2 = (Button) rootView.findViewById(R.id.ans2);
        btnA3 = (Button) rootView.findViewById(R.id.ans3);

        r = (int) (Math.random() * 2);
        if (r == 0) {
            rootView.setBackgroundColor(Color.parseColor(emociones.get(rMain).getColor()));
            interfaceFrame(rootView, imgFeel, btnA1, btnA2, btnA3, idSexo, rMain, rMain, rB, rC);
        } else if (r == 1) {
            rootView.setBackgroundColor(Color.parseColor(emociones.get(rMain).getColor()));
            interfaceFrame(rootView, imgFeel, btnA1, btnA2, btnA3, idSexo, rMain, rC, rMain, rB);
        } else if (r == 2) {
            rootView.setBackgroundColor(Color.parseColor(emociones.get(rMain).getColor()));
            interfaceFrame(rootView, imgFeel, btnA1, btnA2, btnA3, idSexo, rMain, rB, rC, rMain);
        }

    /*VIEW guardar*/
        EditText correo;
        Button YES, NO;

        correo = (EditText)guardarView.findViewById(R.id.confirmCorreo);
        YES = (Button)guardarView.findViewById(R.id.GuardarSI);
        NO = (Button)guardarView.findViewById(R.id.GuardarNO);

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
                if (ran == ran1) {
                    MyCustomAlertDialog(v, emociones.get(ran1), emociones.get(ran1).getName(), true);

                } else {
                    MyCustomAlertDialog(v, emociones.get(ran1), emociones.get(ran1).getName(), false);
                }

            }
        });
        txtFeel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ran == ran2) {
                    MyCustomAlertDialog(v, emociones.get(ran2), emociones.get(ran2).getName(), true);
                } else {
                    MyCustomAlertDialog(v, emociones.get(ran2), emociones.get(ran2).getName(), false);
                }
            }
        });
        txtFeel3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ran == ran3) {
                    MyCustomAlertDialog(v, emociones.get(ran3), emociones.get(ran3).getName(), true);
                } else {
                    MyCustomAlertDialog(v, emociones.get(ran3), emociones.get(ran3).getName(), false);
                }
            }
        });

    }

    public void MyCustomAlertDialog(View v, Emocion em, String respuesta, boolean resp) {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_act0);
        dialog.setTitle("Ya casi lo logras");

        llPreactivityDialog = (LinearLayout) dialog.findViewById(R.id.lLaCT0);
        nameEmocionDialog = (TextView) dialog.findViewById(R.id.nameRespuesta);
        imgEmocionDialog = (ImageView) dialog.findViewById(R.id.imgRespuesta);
        btnBack = (Button) dialog.findViewById(R.id.btnBack);

        if(!resp) {
            //dialog.setTitle("Inténtalo de nuevo");
            btnBack.setBackgroundResource(R.color.Incorrecto);
            btnBack.setText(" Inténtalo de nuevo ");
        }else if(resp) {
            //dialog.setTitle("Correcto");
            btnBack.setBackgroundResource(R.color.Correcto);
            btnBack.setText(" Correcto ");
            vp.setPagingEnabled(true);
            currentVP = vp.getPosition();
            //answer = true;
        }

        Uri ruta = Uri.parse(em.getUrl());
        Picasso.with(context)
                .load(ruta).fit()
                .into(imgEmocionDialog);
        nameEmocionDialog.setText(respuesta);
        //nameEmocionDialog.setTextColor(Color.parseColor(em.getColorB()));
        llPreactivityDialog.setBackgroundColor(Color.parseColor(em.getColor()));

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentVP <3)
                    vp.setCurrentItem(currentVP+1);
                else


                dialog.cancel();
            }
        });

        dialog.show();

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


    /* *
         SWIPPING CONFIGURATION
    * */
}
