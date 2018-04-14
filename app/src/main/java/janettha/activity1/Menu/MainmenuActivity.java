package janettha.activity1.Menu;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import janettha.activity1.Act0.Preactivity;
import janettha.activity1.Act2.Activity2;
import janettha.activity1.Act1.Activity1;
import janettha.activity1.ActA.ActA;
import janettha.activity1.ActB.ActB;
import janettha.activity1.Activities_Login.loginUser;
import janettha.activity1.Models.Tutores;
import janettha.activity1.Models.Usuarios;
import janettha.activity1.R;

import static janettha.activity1.Activities_Login.loginUser.keySP;

public class MainmenuActivity extends AppCompatActivity {

    private Button btnA1, btnA2, btnA3;
    private int a1, a2, a3;

    private DatabaseReference mDatabaseUser;
    private ValueEventListener mUserListener;

    private SharedPreferences sharedPreferences;
    String userU;
    private Tutores tutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mainmenu);
        btnA1 = (Button) findViewById(R.id.menu_act1);
        btnA2 = (Button) findViewById(R.id.menu_act2);
        btnA3 = (Button) findViewById(R.id.menu_act3);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        sharedPreferences = getSharedPreferences(keySP, MODE_PRIVATE);
        userU = sharedPreferences.getString("usuario", "");
        //mDatabaseUser = FirebaseDatabase.getInstance().getReference("users");
        mDatabaseUser = FirebaseDatabase.getInstance().getReference("users");
        inidiceActividad();

        btnA1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String time = Calendar.getInstance().getTime().toString();
                FirebaseDatabase.getInstance().getReference().child("users").child(userU).child("finS").setValue(time);
                Intent intent = new Intent(MainmenuActivity.this, ActA.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
                intent.putExtra("a1", a1);
                startActivity( intent );
            }
        });

        btnA2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String time = Calendar.getInstance().getTime().toString();
                FirebaseDatabase.getInstance().getReference().child("users").child(userU).child("finS").setValue(time);
                Intent intent = new Intent(MainmenuActivity.this, Activity1.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );

                if(a1 == 12) {
                    intent.putExtra("a2", a2);
                    startActivity(intent);
                }
            }
        });

        btnA3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainmenuActivity.this, Activity2.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
                if(a2 == 16) {
                    intent.putExtra("a3", a3);
                    startActivity(intent);
                }
            }
        });



        //getIntent().setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
        //getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onBackPressed() {
        //final Intent intent = new Intent(this, loginUser.class);

        new AlertDialog.Builder(this)
                .setTitle("¿Realmente deseas salir?")
                .setMessage("El usuario actual será olvidado.")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        MainmenuActivity.super.onBackPressed();
                        if (arg1 == 0) {
                            startActivity(new Intent(MainmenuActivity.this, loginUser.class));
                        }else{
                            String time = Calendar.getInstance().getTime().toString();
                            FirebaseDatabase.getInstance().getReference().child("users").child(userU).child("finS").setValue(time);
                            finish();
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                }).create().show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_activity1, menu);

        menu.findItem(R.id.action_previous).setEnabled(true);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: //hago un case por si en un futuro agrego mas opciones
                Log.i("ActionBar", "Atrás!");
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void inidiceActividad(){
        mUserListener = mDatabaseUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {}
                GenericTypeIndicator<HashMap<String, Usuarios>> objectsGTypeInd = new GenericTypeIndicator<HashMap<String, Usuarios>>() {
                };
                Map<String, Usuarios> objectHashMap = dataSnapshot.getValue(objectsGTypeInd);
                ArrayList<Usuarios> objectArrayList = new ArrayList<Usuarios>(objectHashMap.values());
                for (int i = 0; i < objectArrayList.size(); i++) {
                    Usuarios userT = objectArrayList.get(i);
                    //Tutores tutor = dataSnapshot.getValue(Tutores.class);
                    //Log.e(TAG, "onUserFound: Nuevo usuario: "+newUser);
                    //Log.e(TAG, "onUserFound: Usuario DB: " + userT.getUser() + " user app:"+uName);
                    if (userT.getUser().equals(userU)) { // add newUser != null
                        a1 = userT.getIndiceA1();
                        a2 = userT.getIndiceA2();
                        a3 = userT.getIndiceA3();
                        //FirebaseDatabase.getInstance().getReference().child("users").child(user.getUser()).setValue(user);
                        //Log.e(TAG, "onUserFound: Se ha agregado a un newUser: "+ newUser + " Edad: "+ userT.getEdad());

                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to read value
                Log.e("DB Users/A123", "onCancelled: Failed to read DB Users");

            }
        });
    }

}
