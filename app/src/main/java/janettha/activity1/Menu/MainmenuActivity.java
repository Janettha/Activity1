package janettha.activity1.Menu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import janettha.activity1.Act0.Preactivity;
import janettha.activity1.Act2.Activity2;
import janettha.activity1.Act1.Activity1;
import janettha.activity1.R;

public class MainmenuActivity extends AppCompatActivity {

    private Button btnA1, btnA2, btnA3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mainmenu);
        btnA1 = (Button) findViewById(R.id.menu_act1);
        btnA2 = (Button) findViewById(R.id.menu_act2);
        btnA3 = (Button) findViewById(R.id.menu_act3);

        btnA1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainmenuActivity.this, Preactivity.class));
            }
        });

        btnA2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainmenuActivity.this, Activity1.class));
            }
        });

        btnA3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainmenuActivity.this, Activity2.class));
            }
        });
    }

}
