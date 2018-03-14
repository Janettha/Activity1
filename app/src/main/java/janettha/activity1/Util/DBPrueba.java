package janettha.activity1.Util;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import janettha.activity1.Activities_Login.User_1;
import janettha.activity1.Activities_Login.loginUser;
import janettha.activity1.Menu.MainmenuActivity;
import janettha.activity1.R;


public class DBPrueba extends AppCompatActivity {


    private static final String TAG = "MainActivity";

    public static final int RC_SIGN_IN = 1;
    public static final String ANONYMOUS = "anonymous";

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessagesDatabaseReference;
    private ChildEventListener mChildEventListener;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private String mUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbprueba);

        mUsername = ANONYMOUS;

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mMessagesDatabaseReference = mFirebaseDatabase.getReference("User_1");



        Button btn = (Button) findViewById(R.id.buttonDB);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User_1 friendlyMessage = new User_1("Janeth", "Salas",null, null,10);
                //The push method is exactly what you want to be using in this case
                // because you need a new id generated for each message.
                mFirebaseDatabase.getReference("User_1").child("Janethyh").push().setValue(friendlyMessage);
                Toast.makeText(DBPrueba.this, mMessagesDatabaseReference.getKey(), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(DBPrueba.this, MainmenuActivity.class));
            }
        });

        mAuthStateListener = new FirebaseAuth.AuthStateListener(){
            @Override   //capta modificaciones del servidor - siempre debe ir
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth){
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    // user is signed in
                    //Toast.makeText(MainActivity.this, "You're now signed in. Welcome to FriendlyChat!", Toast.LENGTH_SHORT).show();
                    onSignedInInitialize(user.getDisplayName());
                }else{
                    //user is signed out
                    onSignedOutCleanup();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN){
            if (resultCode == RESULT_OK){
                Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show();
            }else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Signed in canceled.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void attachDatabaseReadListener(){
        if  (mChildEventListener == null){
            mChildEventListener = new ChildEventListener() {
                @Override   //siempre contiene el mensaje que va a ser enviado
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    User_1 u = dataSnapshot.getValue(User_1.class);
                    //se agrega el mensaje al adaptador para que se muestre en la lista
                    Toast.makeText(DBPrueba.this, u.getToString(), Toast.LENGTH_SHORT).show();
                }
                public void onChildChanged(DataSnapshot dataSnapshot, String s) { }
                public void onChildRemoved(DataSnapshot dataSnapshot) { }
                // el mensaje cambio de posición
                public void onChildMoved(DataSnapshot dataSnapshot, String s) { }
                // algo salió mal
                public void onCancelled(DatabaseError databaseError) { }
            };
            mMessagesDatabaseReference.addChildEventListener(mChildEventListener);
        }
    }

    private void detachDatabaseReadListener(){
        if(mChildEventListener != null) {
            mMessagesDatabaseReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        if (mAuthStateListener != null){
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
        //Las dos lineas hacen que se destruya la actividad
        detachDatabaseReadListener();
    }

    @Override
    protected void onResume(){
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    private void onSignedInInitialize(String username) {
        mUsername = username;
        attachDatabaseReadListener();
    }

    private void onSignedOutCleanup() {
        mUsername = ANONYMOUS;
    }
}
