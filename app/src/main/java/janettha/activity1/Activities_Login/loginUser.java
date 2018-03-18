package janettha.activity1.Activities_Login;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
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
import java.util.List;
import java.util.Map;

import janettha.activity1.Models.Tutores;
import janettha.activity1.Models.Usuarios;
import janettha.activity1.Util.DBPrueba;
import janettha.activity1.Util.DatePickerFragment;
import janettha.activity1.Menu.MainmenuActivity;
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

    private static final String TAG = "loginUserActivity";

    private String userTutor, userU, passU, nameU, surnamesU, sexo, fechaU;
    int edadU;
    Usuarios userChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user);

        //get current user
        mAuth = FirebaseAuth.getInstance();

        mReferenceTutor = FirebaseDatabase.getInstance().getReference("tutores");
        mDatabaseUser = FirebaseDatabase.getInstance().getReference("usuarios");
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

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                View radioButton = rg.findViewById(checkedId);
                int index = rg.indexOfChild(radioButton);
                switch (index){
                    case 0:
                        rf.setTextColor(getResources().getColor(R.color.colorTxt));
                        sexo = "f";
                        break;
                    case 1:
                        sexo = "m";
                        rm.setTextColor(getResources().getColor(R.color.colorTxt));
                        break;
                }
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

                //writeNewTutor();
                //writeNewUser();

                // copy for removing at onStop()
                mTutorListener = mReferenceTutor.addValueEventListener( new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {}
                        GenericTypeIndicator<HashMap<String, Tutores>> objectsGTypeInd = new GenericTypeIndicator<HashMap<String, Tutores>>() {};
                        Map<String, Tutores> objectHashMap = dataSnapshot.getValue(objectsGTypeInd);
                        ArrayList<Tutores> objectArrayList = new ArrayList<Tutores>(objectHashMap.values());
                        for(int i = 0; i < objectArrayList.size(); i++) {
                            Tutores tutor = objectArrayList.get(i);
                            //Tutores tutor = dataSnapshot.getValue(Tutores.class);
                            if (tutor.getEmail().equals(mAuth.getCurrentUser().getEmail())) {
                                Log.e(TAG, "onDataChange: Message data is updated: " + tutor.getName());

                                Toast.makeText(loginUser.this, tutor.toString(), Toast.LENGTH_SHORT).show();

                                //FirebaseUser tutor = mAuth.getCurrentUser();
                                User_1 user = new User_1(userU, nameU, surnamesU, sexo, edadU, tutor.getUser());
                                FirebaseDatabase.getInstance().getReference().child("users").child(user.getUser()).setValue(user);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Failed to read value
                        Log.e(TAG, "onCancelled: Failed to read message");

                    }
                });

                startActivity(new Intent(loginUser.this, DBPrueba.class));
            }
        });
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
        User_1 user = new User_1(userU, nameU, surnamesU, sexo, edadU, tutor.getProviderId());
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
            email.setText("User: " + user.getUid());
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
