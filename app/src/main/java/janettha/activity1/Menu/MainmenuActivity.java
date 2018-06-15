package janettha.activity1.Menu;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

import janettha.activity1.ActC.ActC;
import janettha.activity1.ActB.ActB;
import janettha.activity1.ActA.ActA;
import janettha.activity1.Activities_Login.loginUser;
import janettha.activity1.Models.Emociones;
import janettha.activity1.Models.Tutores;
import janettha.activity1.Models.Usuarios;
import janettha.activity1.R;
import janettha.activity1.Util.MediaPlayerSounds;

import static android.provider.Settings.System.AIRPLANE_MODE_ON;
import static janettha.activity1.Activities_Login.loginUser.keySP;

public class MainmenuActivity extends AppCompatActivity {

    private Button btnA1, btnA2, btnA3;
    private int a1, a2, a3;
    //private SoundManager soundManager;

    private MediaPlayerSounds mediaPlayerSounds;

    private DatabaseReference mDatabaseUser;

    private SharedPreferences sharedPreferences;
    String userU, sexo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mainmenu);
        btnA1 = findViewById(R.id.menu_act1);
        btnA2 = findViewById(R.id.menu_act2);
        btnA3 = findViewById(R.id.menu_act3);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        sharedPreferences = getSharedPreferences(keySP, MODE_PRIVATE);
        sexo = sharedPreferences.getString("sexo", "m");
        userU = sharedPreferences.getString("usuario", "");
        mDatabaseUser = FirebaseDatabase.getInstance().getReference("users");
        inidiceActividad();

        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        mediaPlayerSounds = new MediaPlayerSounds(this);

        btnA1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mediaPlayerSounds.playSound(mediaPlayerSounds.loadNewAct());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

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
                Intent intent = new Intent(MainmenuActivity.this, ActB.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );

                if(a1 > 16) {
                    Log.e("ActB","YA ENTRAMOS - a1="+a1);
                    try {
                        mediaPlayerSounds.playSound(mediaPlayerSounds.loadNewAct());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    intent.putExtra("a2", a2);
                    startActivity(intent);
                }
            }
        });

        btnA3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainmenuActivity.this, ActC.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );

                if(a2 > 16) {
                    try {
                        mediaPlayerSounds.playSound(mediaPlayerSounds.loadNewAct());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    intent.putExtra("a3", a3);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
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
        ValueEventListener mUserListener = mDatabaseUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<HashMap<String, Usuarios>> objectsGTypeInd = new GenericTypeIndicator<HashMap<String, Usuarios>>() {
                };
                Map<String, Usuarios> objectHashMap = dataSnapshot.getValue(objectsGTypeInd);
                ArrayList<Usuarios> objectArrayList = new ArrayList<Usuarios>(objectHashMap.values());
                for (int i = 0; i < objectArrayList.size(); i++) {
                    Usuarios userT = objectArrayList.get(i);
                    if (userT.getUser().equals(userU)) { // add newUser != null
                        a1 = userT.getIndiceA1();
                        a2 = userT.getIndiceA2();
                        a3 = userT.getIndiceA3();
                        Log.e("Indices", a1 + "-" + a2 + "-" + a3);
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

    static boolean isAirplaneModeOn(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        return Settings.System.getInt(contentResolver, AIRPLANE_MODE_ON, 0) != 0;
    }
}
