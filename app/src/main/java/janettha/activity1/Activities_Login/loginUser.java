package janettha.activity1.Activities_Login;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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

import janettha.activity1.Menu.MainmenuActivity;
import janettha.activity1.Models.Tutores;
import janettha.activity1.Models.Usuarios;
import janettha.activity1.Util.DBPrueba;
import janettha.activity1.Util.DatePickerFragment;
import janettha.activity1.R;
import janettha.activity1.Util.Date;

public class loginUser extends AppCompatActivity {

    private Button btnStart;

    private TextView email, edad;
    private EditText userName, pass, name, surnames, dateU;
    private RadioGroup rg;
    private RadioButton rf, rm;

    /* Firebase autentificación y DB */
    private FirebaseAuth mAuth;
    private FirebaseUser tutor;
    private DatabaseReference mReferenceTutor;
    private DatabaseReference mDatabaseUser;
    private ValueEventListener mTutorListener;

    public static final String keySP = "UserSex";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editorSP;

    private static final String TAG = "loginUserActivity";

    private String userTutor, userU, passU, nameU, surnamesU, sexo, fechaU;
    int edadU;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user);

        //get current user
        mAuth = FirebaseAuth.getInstance();

        mReferenceTutor = FirebaseDatabase.getInstance().getReference("tutores");
        mDatabaseUser = FirebaseDatabase.getInstance().getReference("users");

        sharedPreferences = getSharedPreferences(keySP, MODE_PRIVATE);
        editorSP = sharedPreferences.edit();

        //tutor = mAuth.getCurrentUser();
        /*
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase refDB = FirebaseDatabase.getInstance();
        userChild = new Usuarios();
         */

        email = (TextView) findViewById(R.id.userTutor);

        userName = (EditText) findViewById(R.id.User);
        pass = (EditText) findViewById(R.id.Password);
        name = (EditText) findViewById(R.id.NombreUser);
        surnames = (EditText) findViewById(R.id.SurnamesUser);
        rg = (RadioGroup) findViewById(R.id.rGroup);
        rf = (RadioButton) findViewById(R.id.radioFemenino);
        rm = (RadioButton) findViewById(R.id.radioMasculino);
        dateU = (EditText) findViewById(R.id.date);
        edad = (TextView) findViewById(R.id.edad);

        btnStart = (Button) findViewById(R.id.Iniciar);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                View radioButton = rg.findViewById(checkedId);
                int index = rg.indexOfChild(radioButton);
                switch (index) {
                    case 0:
                        rf.setTextColor(getResources().getColor(R.color.colorTxt));
                        sexo = "f";
                        break;
                    case 1:
                        sexo = "m";
                        rm.setTextColor(getResources().getColor(R.color.colorTxt));
                        break;
                }
                editorSP.putString("sexo", sexo);
                editorSP.apply();
            }

        });

        dateU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.date:
                        showDatePickerDialog();
                        break;
                }
            }
        });


        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userU = userName.getText().toString();
                passU = pass.getText().toString();
                nameU = name.getText().toString();
                surnamesU = surnames.getText().toString();
                fechaU = dateU.getText().toString();

                guardarDatos();
                //writeNewTutor();
                //writeNewUser();

                // copy for removing at onStop()
                mTutorListener = mReferenceTutor.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {}
                        GenericTypeIndicator<HashMap<String, Tutores>> objectsGTypeInd = new GenericTypeIndicator<HashMap<String, Tutores>>() {
                        };
                        Map<String, Tutores> objectHashMap = dataSnapshot.getValue(objectsGTypeInd);
                        ArrayList<Tutores> objectArrayList = new ArrayList<Tutores>(objectHashMap.values());
                        for (int i = 0; i < objectArrayList.size(); i++) {
                            Tutores tutor = objectArrayList.get(i);
                            //Tutores tutor = dataSnapshot.getValue(Tutores.class);
                            if (tutor.getEmail().equals(mAuth.getCurrentUser().getEmail())) {
                                Log.e(TAG, "onDataChange: Message data is updated: " + tutor.getName());

                                Toast.makeText(loginUser.this, tutor.toString(), Toast.LENGTH_SHORT).show();

                                //FirebaseUser tutor = mAuth.getCurrentUser();
                                Usuarios user = new Usuarios(userU, nameU, surnamesU, sexo, edadU, tutor.getUser());
                                FirebaseDatabase.getInstance().getReference().child("users").child(user.getUser()).setValue(user);

                                editorSP.putString("usuario", user.getUser());
                                editorSP.apply();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Failed to read value
                        Log.e(TAG, "onCancelled: Failed to read message");

                    }
                });
                if(userU != null || userU != "") {
                    startActivity(new Intent(loginUser.this, MainmenuActivity.class));
                }else{
                    Toast.makeText(loginUser.this, "Ingrese los datos solicitados", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if(! sharedPreferences.getString("usuario", "").isEmpty()){
            cargarDatos();
        }
    }

    @Override
    public void onBackPressed() {
        //final Intent intent = new Intent(this, loginUser.class);
        String time = Calendar.getInstance().getTime().toString();
        FirebaseDatabase.getInstance().getReference().child("users").child(userU).child("finS").setValue(time);

        new AlertDialog.Builder(this)
                .setTitle("¿Realmente deseas salir?")
                //.setMessage("El usuario actual será olvidado.")
                .setNegativeButton(android.R.string.no, null)

                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                        loginUser.super.onBackPressed();

                        String time = Calendar.getInstance().getTime().toString();
                        FirebaseDatabase.getInstance().getReference().child("users").child(userU).child("finS").setValue(time);

                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        finish();
                        startActivity(intent);
                    }
                }).create().show();

    }


    private void guardarDatos(){
        editorSP.putString("usuario", userU);
        editorSP.putString("password", passU);
        editorSP.putString("nombre", nameU);
        editorSP.putString("apellidos", surnamesU);
        editorSP.putString("sexo", sexo);
        editorSP.putString("fechaNacimiento", String.valueOf(dateU));
        editorSP.putInt("edad", edadU);
        editorSP.apply();
    }

    private void cargarDatos(){
        userName.setText(sharedPreferences.getString("usuario", ""));
        pass.setText(sharedPreferences.getString("password", ""));
        name.setText(sharedPreferences.getString("nombre", ""));
        surnames.setText(sharedPreferences.getString("apellidos", ""));
        //dateU.setText(sharedPreferences.getString("fechaNacimiento", ""));
        //edad.setText(sharedPreferences.getInt("edad", 0)+" años");
        /*
        switch (sharedPreferences.getString("sexo", "f")){
            case "m":
                rm.setSelected(true);
                rf.setSelected(false);
                break;
            case "f":
                rf.setSelected(true);
                rm.setSelected(false);
                break;
            default: rf.setSelected(true);
        }
        */
    }

    private void writeNewTutor() {
        FirebaseUser tutor = mAuth.getCurrentUser();
        //,  )
        Tutores user = new Tutores(getUsernameFromEmail(tutor.getEmail()), tutor.getDisplayName(), tutor.getEmail());

        Toast.makeText(this, getUsernameFromEmail(tutor.getEmail()), Toast.LENGTH_SHORT).show();

        Calendar time = Calendar.getInstance();
        String idUser = time.getTime() + mAuth.getCurrentUser().getDisplayName();

        FirebaseDatabase.getInstance().getReference().child("tutores").child(idUser).setValue(user);

    }

    private void writeNewUser() {
        FirebaseUser tutor = mAuth.getCurrentUser();
        //,  )
        //Tutores user = new Tutores(getUsernameFromEmail(u.getEmail()), u.getDisplayName(), u.getEmail());
        Usuarios user = new Usuarios(userU, nameU, surnamesU, sexo, edadU, tutor.getProviderId());
        Toast.makeText(this, getUsernameFromEmail(tutor.getEmail()), Toast.LENGTH_SHORT).show();
        //FirebaseDatabase.getInstance().getReference().child("users").child("janettha").setValue(user);
        FirebaseDatabase.getInstance().getReference().child("usuarios").child(userU).setValue(user);
    }

    private String getUsernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }

    private void showDatePickerDialog() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because january is zero
                final String selectedDate, sMes;
                Date d = new Date(day, month, year);

                selectedDate = d.getToString();
                dateU.setText(selectedDate);

                edadU = d.calculaEdad();
                if(edadU != 1)
                    edad.setText(edadU+" años");
                else
                    edad.setText(edadU+" año");
            }
        });
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }


    @SuppressLint("SetTextI18n")
    private void setDataToView(FirebaseUser user) {
        if(user != null)
            email.setText("User: " + getUsernameFromEmail(user.getEmail()));
        else
            email.setText("Welcome");
    }

    @Override
    protected void onStart() {
        super.onStart();

        tutor = mAuth.getCurrentUser();
        setDataToView(tutor);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mTutorListener != null) {
            mReferenceTutor.removeEventListener(mTutorListener);
        }
    }

}
