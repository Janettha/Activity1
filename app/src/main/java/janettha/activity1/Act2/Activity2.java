package janettha.activity1.Act2;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity2);
        //View rootView = R.layout.activity_activity2.inflate(R.layout.fragment_preactivity, container, false);
        v = initViews();
        loadData("f",v);
        wheel_img.setOnMenuSelectedListener(this);
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
        }
    }
}
