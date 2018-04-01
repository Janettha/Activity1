package janettha.activity1.Activities_Login;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import janettha.activity1.Models.Tutores;
import janettha.activity1.R;

public class SignUpActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword, inputName, inputSurnames, inputUser;
    private Button btnSignIn, btnSignUp, btnResetPassword;
    private ProgressBar progressBar;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Get Firebase auth instance
        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("tutores");

        btnSignIn = (Button) findViewById(R.id.sign_in_button);
        btnSignUp = (Button) findViewById(R.id.sign_up_button);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        inputName = (EditText) findViewById(R.id.nombre);
        inputSurnames = (EditText) findViewById(R.id.apellidos);
        inputUser = (EditText) findViewById(R.id.usuario);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnResetPassword = (Button) findViewById(R.id.btn_reset_password);

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, ResetPasswordActivity.class));
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                finish();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                //create user
                mFirebaseAuth.createUserWithEmailAndPassword(email, password)//Te recomiendo usar listener .addOnSuccessListener y .addOnFailureListener para asegurar ambos eventos
                        .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(SignUpActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {   //y evitar estos  ifs
                                    Toast.makeText(SignUpActivity.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                        FirebaseUser tutor = mFirebaseAuth.getCurrentUser();
                                        //,  )
                                        Tutores user = new Tutores(inputUser.getText().toString(), inputName.getText().toString(), tutor.getEmail());

                                        Toast.makeText(getApplication().getBaseContext(), inputName.getText().toString(), Toast.LENGTH_SHORT).show();

                                        //FirebaseDatabase.getInstance().getReference().child("tutores").child(inputUser.getText().toString()).setValue(user);
                                        mDatabaseRef.child(inputUser.getText().toString()).setValue(user);

                                        startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                                        finish();
                                }
                            }
                        });

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        existeUser();
        progressBar.setVisibility(View.GONE);
    }

    public void existeUser(){
        ValueEventListener tutorListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Tutores tutor = dataSnapshot.getValue(Tutores.class);

                    Log.e("TutorOK", "onDataChange: Message data is updated: " + tutor.getName() + ", " + tutor.getEmail() + ", " + tutor.getUser());
                    Toast.makeText(SignUpActivity.this, tutor.toString(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to read value
                Log.e("TutorFAIL", "onCancelled: Failed to read message");

            }
        };
        mDatabaseRef.addValueEventListener(tutorListener);

        // copy for removing at onStop()
        //mTutorListener = tutorListener;
    }
}
