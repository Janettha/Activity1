package janettha.activity1.Act2;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DialogTitle;
import android.view.View;
import android.view.ViewDebug;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import github.hellocsl.cursorwheel.CursorWheelLayout;
import janettha.activity1.Act2.Adapters.WheelmageAdapter;
import janettha.activity1.Models.Emocion;
import janettha.activity1.Models.Emociones;
import janettha.activity1.R;

public class Activity2 extends AppCompatActivity implements CursorWheelLayout.OnMenuSelectedListener {

    CursorWheelLayout wheel_img;
    List<Emocion> listImg ;
    View v;
    ImageView EmocionDialog;
    TextView NameEmocionDialog;
    Button buttonRecord, buttonPlay;
    Dialog dialog;
    boolean DialogFlag=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity2);
        //View rootView = R.layout.activity_activity2.inflate(R.layout.fragment_preactivity, container, false);
        v = initViews();
        loadData("f",v);
        wheel_img.setOnMenuSelectedListener(this);

        Button buttonDialog = (Button) findViewById(R.id.EmocionDialog);
        buttonDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int posicion = wheel_img.getSelectedPosition();
                MyCustomAlertDialog(posicion);
                /*
                Toast.makeText(getApplicationContext(), "ID wheel: "+wheel_img.getSelectedPosition(), Toast.LENGTH_SHORT).show();
                if(DialogFlag == true) {
                    MyCustomAlertDialog(11);
                }
                */
            }
        });


    }

    private void loadData(String s, View view) {
        Emociones e = new Emociones();
        listImg = e.Emociones(getApplicationContext(),s);
        WheelmageAdapter adapter = new WheelmageAdapter(getBaseContext(),view, "f");
        wheel_img.setAdapter(adapter);
    }

    @SuppressLint("WrongViewCast")
    private View initViews() {
        wheel_img = (CursorWheelLayout) findViewById(R.id.wheel);
        return wheel_img;
    }

    @Override
    public void onItemSelected(CursorWheelLayout parent, View view, int pos) {
        //TextView t = (TextView) findViewById(R.id.id_wheel_menu_center_item);
        if(parent.getId() == R.id.wheel){
            Toast.makeText(getBaseContext(), listImg.get(pos).getName(), Toast.LENGTH_SHORT).show();
            //t.setText(listImg.get(pos).getName());
            //                if(pos != 0)
            /*
            MyCustomAlertDialog(pos);
            */
            wheel_img.setOnMenuSelectedListener(new CursorWheelLayout.OnMenuSelectedListener() {
                public void onItemSelected(CursorWheelLayout parent, View view, int pos) {
                    DialogFlag = true;
                    Toast.makeText(Activity2.this, "Top Menu click position:" + pos + " Dialog: "+DialogFlag, Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    public void MyCustomAlertDialog(int pos){
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_act3);
        dialog.setTitle(listImg.get(pos).getName());

        Toast.makeText(this,"Dialog", Toast.LENGTH_SHORT).show();

        buttonRecord = (Button) dialog.findViewById(R.id.btnRecord);
        buttonPlay = (Button) dialog.findViewById(R.id.btnStop);
        EmocionDialog = (ImageView) dialog.findViewById(R.id.imgEmocion);
        NameEmocionDialog = (TextView) dialog.findViewById(R.id.nameEmocion);

        Uri ruta = Uri.parse(listImg.get(pos).getUrl());
        Picasso.with(v.getContext())
                .load(ruta).fit()
                .into(EmocionDialog);
        NameEmocionDialog.setText(listImg.get(pos).getName());
        buttonRecord.setEnabled(true);
        buttonPlay.setEnabled(true);

        buttonRecord.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Recording...", Toast.LENGTH_SHORT).show();
            }
        });

        buttonPlay.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Stop...", Toast.LENGTH_SHORT).show();
                dialog.cancel();
                DialogFlag = false;
            }
        });
        dialog.show();
    }
}
