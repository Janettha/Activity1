package janettha.activity1.Menu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import janettha.activity1.Activities_Login.loginUser;
import janettha.activity1.Models.Tutores;
import janettha.activity1.Models.Usuarios;
import janettha.activity1.R;

public class UserList extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser tutor;
    private DatabaseReference mReferenceTutor;
    private DatabaseReference mDatabaseUser;
    private ValueEventListener mTutorListener;
    private ValueEventListener mUserListener;

    List<Usuarios> listUsers = new ArrayList<Usuarios>();
    TextView lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        lista = findViewById(R.id.listaUsuarios);

        mAuth = FirebaseAuth.getInstance();
        mReferenceTutor = FirebaseDatabase.getInstance().getReference("tutores");
        mDatabaseUser = FirebaseDatabase.getInstance().getReference("users");

        mTutorListener = mReferenceTutor.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {}
                GenericTypeIndicator<HashMap<String, Tutores>> objectsGTypeInd = new GenericTypeIndicator<HashMap<String, Tutores>>() {
                };
                Map<String, Tutores> objectHashMap = dataSnapshot.getValue(objectsGTypeInd);
                ArrayList<Tutores> objectArrayList = new ArrayList<Tutores>(objectHashMap.values());
                for (int i = 0; i < objectArrayList.size(); i++) {
                    final Tutores tutor = objectArrayList.get(i);
                    if (tutor.getEmail().equals(mAuth.getCurrentUser().getEmail())) {
                        Log.e("MENUser:", "onTutorFound: Se encontró tutor: " + tutor.getName());
                        Toast.makeText(UserList.this, tutor.toString(), Toast.LENGTH_SHORT).show();

                        mUserListener = mDatabaseUser.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                GenericTypeIndicator<HashMap<String, Usuarios>> objectsGTypeInd = new GenericTypeIndicator<HashMap<String, Usuarios>>() {
                                };
                                Map<String, Usuarios> objectHashMap = dataSnapshot.getValue(objectsGTypeInd);
                                ArrayList<Usuarios> objectArrayList = new ArrayList<Usuarios>(objectHashMap.values());
                                for (int j = 0; j < objectArrayList.size(); j++) {
                                    //Usuarios user = new Usuarios(userU, nameU, surnamesU, sexo, edadU, tutor.getUser());
                                    Usuarios user = objectArrayList.get(j);
                                    if(user.getTutor().equals(tutor.getUser())){
                                        listUsers.add(user);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.e("MENUser", "onCancelled: Failed to read message");
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to read value
                Log.e("MENUser", "onCancelled: Failed to read message");

            }
        });

        lista.setText(muestraList());
        Log.e("UsuerList:", muestraList());
    }

    private String muestraList(){
        String usuarios = "";
        for (int i = 0; i<listUsers.size(); i++){
            usuarios += listUsers.get(i).getUser();
        }
        Log.e("UsuariosList: ",usuarios);

        return usuarios;
    }
}
