package janettha.activity1.PDF;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

import janettha.activity1.Act1.Activity1;
import janettha.activity1.Menu.MainmenuActivity;
import janettha.activity1.R;

public class ViewPDFActivity extends AppCompatActivity {

    PDFView pdfView;
    File file;

    public static final int REQUEST_STORAGE_PERMISSION = 150;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pdf);

        pdfView = (PDFView)findViewById(R.id.pdfView);
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
}
