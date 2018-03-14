package janettha.activity1.Activities_Login;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
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
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    private FirebaseAuth auth;
    //private FirebaseDatabase database = FirebaseDatabase.getInstance();
    //private FirebaseAuth.AuthStateListener authListener;

    private String userTutor, userU, passU, nameU, surnamesU, sexo, fechaU;
    int edadU;
    Usuarios userChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user);

        userChild = new Usuarios();

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

        //get current user
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        auth = FirebaseAuth.getInstance();
        FirebaseDatabase refDB = FirebaseDatabase.getInstance();
        final DatabaseReference Usuarios = refDB.getReference("Usuarios");

        final DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference("User_1");

        //setDataToView(user);
        if (user == null) {
            // user auth state is changed - user is null
            // launch login activity
            startActivity(new Intent(loginUser.this, LoginActivity.class));
            finish();
        } else {
            setDataToView(user);
        }

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                switch (group.getCheckedRadioButtonId()){
                    case 0:
                        rf.setTextColor(getResources().getColor(R.color.colorTxt));
                        sexo = "f";
                        break;
                    case 1:
                        sexo = "m";
                        rm.setTextColor(getResources().getColor(R.color.colorTxt));
                        break;
                    default: sexo = "f";
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

                //userChild = new Usuarios(edadU, fechaU, userU, passU, sexo, surnamesU, userTutor, nameU);
                //userChild = new Usuarios("janethsg_23", "dianamag2", "abc123","Diana Maritza","Álvarez Gómez", "f","22/Julio/1999", 8);
                User_1 user1 = new User_1("jsalasgo", "Janeth", "Salas", "f", 1);
                User_1 user2 = new User_1("dianamag", "Mariana", "Salas", "f", 4);
                //addUserDB(userChild);
                mDatabase.child(user1.getUser()).child("Apellido").setValue(user1.getToString(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError error, DatabaseReference reference) {
                        if (error != null) {
                            Log.e("TAG", "Failed to write message", error.toException());
                            Toast.makeText(loginUser.this, "Error 1", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(loginUser.this, "Good 1", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                mDatabase.child(user2.getUser()).push().setValue(user2);
                mDatabase.child("msalasgo").setValue(user2, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError error, DatabaseReference reference) {
                        if (error != null) {

                            Log.e("TAG", "Failed to write message", error.toException());
                            Toast.makeText(loginUser.this, "Error 2", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(loginUser.this, "Good 2", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                //Toast.makeText(v.getContext(), userChild.getUserName(), Toast.LENGTH_SHORT).show();

                startActivity(new Intent(loginUser.this, DBPrueba.class));
            }
        });

    }

    private void addUserDB(Usuarios user){
        //mensajeRef = redDB.child("Tutores").child("janethsg_23").child("Usuarios").child("0");
        //Usuarios.child(userU).push().setValue(user);
        //Toast.makeText(this, mensajeRef.toString(), Toast.LENGTH_SHORT).show();
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
                edad.setText(edadU+" años");
            }
        });
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }


    @SuppressLint("SetTextI18n")
    private void setDataToView(FirebaseUser user) {
        if(user != null)
            //email.setText("User Email: " + user.getEmail());
            email.setText("User: " + user.getDisplayName());
        else
            email.setText("Welcome");
    }



    /*
    public String getUserName(){ return this.userName; }

    public String getSexo(){ return this.sexo; }

    public String getFechaU(){ return this.fechaU; }

    public int getEdadU(){ return this.edadU; }
    */

}
