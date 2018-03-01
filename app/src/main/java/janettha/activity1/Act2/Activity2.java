package janettha.activity1.Act2;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.ui.phone.CompletableProgressDialog;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.List;

import github.hellocsl.cursorwheel.CursorWheelLayout;
import janettha.activity1.Act2.Adapters.WheelmageAdapter;
import janettha.activity1.Models.Emocion;
import janettha.activity1.Models.Emociones;
import janettha.activity1.R;

public class Activity2 extends AppCompatActivity implements CursorWheelLayout.OnMenuSelectedListener {

    CursorWheelLayout wheel_img;
    List<Emocion> listImg ;
    View v;
    ImageView EmocionDialog;
    TextView NameEmocionDialog;
    Button buttonRecord, buttonStop;
    Dialog dialog;
    boolean DialogFlag=false;

    private static final String LOG_TAG = "AudioRecordTest";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static String mFileName = null;

    private RecordButton mRecordButton;
    private MediaRecorder mRecorder;

    private StorageReference mStorage;
    private CompletableProgressDialog mProgress;

    private final String extension = ".3gp";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity2);

        //View rootView = R.layout.activity_activity2.inflate(R.layout.fragment_preactivity, container, false);
        v = initViews();
        loadData("f",v);
        wheel_img.setOnMenuSelectedListener(this);
                    /* Recording audio FIREBASE */
        mStorage = FirebaseStorage.getInstance().getReference();
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath()+"/";
        //mFileName += "/recorded_audio.3gp";



    }

    private void loadData(String s, View view) {
        Emociones e = new Emociones();
        listImg = e.Emociones(getApplicationContext(),s);
        WheelmageAdapter adapter = new WheelmageAdapter(getBaseContext(),view, "f");
        wheel_img.setAdapter(adapter);
    }

    @SuppressLint("WrongViewCast")
    private View initViews() {
        wheel_img = (CursorWheelLayout) findViewById(R.id.wheel);
        return wheel_img;
    }

    @Override
    public void onItemSelected(CursorWheelLayout parent, View view, int pos) {
        //TextView t = (TextView) findViewById(R.id.id_wheel_menu_center_item);
        final Button buttonDialog = (Button) findViewById(R.id.EmocionDialog);
        if(parent.getId() == R.id.wheel){
            Toast.makeText(getBaseContext(), listImg.get(pos).getName(), Toast.LENGTH_SHORT).show();
            //t.setText(listImg.get(pos).getName());
            //                if(pos != 0)
            /*
            MyCustomAlertDialog(pos);
            */
            wheel_img.setOnMenuSelectedListener(new CursorWheelLayout.OnMenuSelectedListener() {
                public void onItemSelected(CursorWheelLayout parent, View view, int pos) {
                    DialogFlag = true;
                    Toast.makeText(Activity2.this, "Top Menu click position:" + pos + " Dialog: "+DialogFlag, Toast.LENGTH_SHORT).show();

                    final int posicion = wheel_img.getSelectedPosition();

                    mFileName += listImg.get(wheel_img.getSelectedPosition()).getId() + "_" + listImg.get(wheel_img.getSelectedPosition()).getName() + extension;


                    buttonDialog.setVisibility(View.VISIBLE);
                    buttonDialog.setText(listImg.get(posicion).getName());
                    buttonDialog.setBackgroundColor(Color.parseColor(listImg.get(posicion).getColorB()));
                    buttonDialog.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //
                            MyCustomAlertDialog(posicion);
                        }
                    });
                }
            });

        }
    }

    public void MyCustomAlertDialog(int pos){
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_act2);
        dialog.setTitle(listImg.get(pos).getName());

        Toast.makeText(this,"Dialog", Toast.LENGTH_SHORT).show();

        buttonRecord = (Button) dialog.findViewById(R.id.btnRecord);
        buttonStop = (Button) dialog.findViewById(R.id.btnStop);
        EmocionDialog = (ImageView) dialog.findViewById(R.id.imgEmocion);
        NameEmocionDialog = (TextView) dialog.findViewById(R.id.nameEmocion);

        Uri ruta = Uri.parse(listImg.get(pos).getUrl());
        Picasso.with(v.getContext())
                .load(ruta).fit()
                .into(EmocionDialog);
        NameEmocionDialog.setText(listImg.get(pos).getName());
        buttonRecord.setEnabled(true);
        buttonStop.setEnabled(false);

        /*
        buttonRecord.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Recording...", Toast.LENGTH_SHORT).show();
            }
        });
        */
        // Permisos
        if(checkPermissionFromDevice()) {

            /*
            buttonRecord.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        startRecording();
                        Toast.makeText(getApplicationContext(), "Recording...", Toast.LENGTH_SHORT).show();
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        stopRecording();
                        Toast.makeText(getApplicationContext(), "Record Stopped...", Toast.LENGTH_SHORT).show();
                    }

                    return false;
                }
            });
            */

            buttonRecord.setEnabled(true);
            buttonRecord.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        //mRecorder.prepare();
                        //mRecorder.start();
                    buttonRecord.setEnabled(false);
                    buttonStop.setEnabled(true);
                    buttonRecord.setEnabled(false);
                    startRecording();
                    Toast.makeText(Activity2.this, "Recording...", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            requestPermissions();

        }


        buttonStop.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                buttonRecord.setEnabled(true);
                buttonStop.setEnabled(false);
                stopRecording();
                Toast.makeText(getApplicationContext(), "Record Stopped...", Toast.LENGTH_SHORT).show();

                //DialogFlag = false;
            }
        });
        dialog.show();
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
        }, REQUEST_RECORD_AUDIO_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
            {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, "Permission Granted.", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "Permission Denied.", Toast.LENGTH_SHORT).show();
            }
            break;
        }
    }

    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        //mRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        mRecorder.start();
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;

        uploadAudio();
    }

    private void uploadAudio() {
        String path = listImg.get(wheel_img.getSelectedPosition()).getId() + "_" + listImg.get(wheel_img.getSelectedPosition()).getName() + extension;
        StorageReference filepath = mStorage.child("Audio").child(path);
        Uri uri = Uri.fromFile(new File(mFileName));
        filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getApplicationContext(),"Saved", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    public class RecordButton extends android.support.v7.widget.AppCompatButton {

        boolean mStartRecording = true;

        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
                onRecord(mStartRecording);
                if (mStartRecording) {
                    setText("Stop recording");
                } else {
                    setText("Start recording");
                }
                mStartRecording = !mStartRecording;
            }
        };

        public RecordButton(Context ctx) {
            super(ctx);
            setText("Start recording");
            setOnClickListener(clicker);
        }
    }

    private boolean checkPermissionFromDevice(){
        int write_external_storage_result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int record_audio_result = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        return (write_external_storage_result == PackageManager.PERMISSION_GRANTED &&
                record_audio_result == PackageManager.PERMISSION_GRANTED);
    }

}
