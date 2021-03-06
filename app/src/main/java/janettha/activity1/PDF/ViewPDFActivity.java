package janettha.activity1.PDF;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.util.Calendar;

import janettha.activity1.Menu.MainmenuActivity;
import janettha.activity1.R;

public class ViewPDFActivity extends AppCompatActivity {

    PDFView pdfView;
    File file;
    Button correo, menu;


    public final String keySP = "UserSex";
    private SharedPreferences sharedPreferences;
    private String user;

    private FirebaseAuth mAuth;


    public static final int REQUEST_STORAGE_PERMISSION = 150;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pdf);

        pdfView = (PDFView)findViewById(R.id.pdfView);
        correo = (Button) findViewById(R.id.correo);
        menu = (Button) findViewById(R.id.backMenu);


        mAuth = FirebaseAuth.getInstance();

        sharedPreferences = getSharedPreferences(keySP, MODE_PRIVATE);
        //editorSP = sharedPreferences.edit();
        String nuevoUser = mAuth.getCurrentUser()+"_"+ Calendar.getInstance().getTime();
        user = sharedPreferences.getString("usuario", nuevoUser);

        requestPermissions();
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            file = new File(bundle.getString("path",""));
        }
        pdfView.fromFile(file)              //se muestra el pdf desde archivo
                .enableSwipe(true)          //se pueden cambiar las paginas del pdf
                .swipeHorizontal(false)     //se pueden cambiar las hojas del pdf horizontal
                .enableDoubletap(true)      //se hace un zoom en el pdf
                .enableAntialiasing(true)   //se visualiza el pdf en pantallas de baja resolución
                .load();                    //se carga pdf

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewPDFActivity.this, MainmenuActivity.class));
            }
        });

        correo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail(user);
            }
        });
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
        }, REQUEST_STORAGE_PERMISSION);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_STORAGE_PERMISSION:
            {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, "Permission Granted.", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "Permission Denied.", Toast.LENGTH_SHORT).show();
            }
            break;
        }
    }

    @Override
    public void onBackPressed() {
        //final Intent intent = new Intent(this, loginUser.class);
        startActivity(new Intent(ViewPDFActivity.this, MainmenuActivity.class));
    }

    private void sendEmail(String user){
        Uri uri = Uri.fromFile(file);
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, mAuth.getCurrentUser().getEmail());
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Reporte de actividades: "+user);
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,"Hola, cualquier duda o aclaración no dudes en contactarnos.");
        emailIntent.setType("application/pdf");
        emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(emailIntent, "Mandar correo usando: "));
    }
}
