package janettha.activity1.ActC;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.AudioManager;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.ui.phone.CompletableProgressDialog;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import github.hellocsl.cursorwheel.CursorWheelLayout;
import janettha.activity1.ActC.Adapters.WheelmageAdapter;
import janettha.activity1.Models.Emocion;
import janettha.activity1.Models.Emociones;
import janettha.activity1.R;
import janettha.activity1.Util.Date;
import janettha.activity1.Util.MediaPlayerSounds;

/*
* https://github.com/lassana/continuous-audiorecorder
* */
public class ActC extends AppCompatActivity implements CursorWheelLayout.OnMenuSelectedListener {

    CursorWheelLayout wheel_img;
    TextView textWheel;
    Emociones listImg;
    View v;
    LinearLayout lwheel;
    ImageView EmocionDialog;
    TextView NameEmocionDialog;
    Button buttonRecord, buttonStop, buttonPlay;
    Dialog dialog;
    LinearLayout llActivity2;
    boolean DialogFlag = false;

    private static final String LOG_TAG = "AudioRecordTest";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static String mFile;
    private static String mFileS;

    private final String extension = ".mp3";
    private static String mFileName = null;

    private RecordButton mRecordButton;

    MediaPlayerSounds mediaPlayerSounds;

    private MediaRecorder mRecorder;
    private StorageReference mStorage;

    private CompletableProgressDialog mProgress;

    public final String keySP = "UserSex";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editorSP;
    private String sexo, userName;

    private FirebaseAuth mAuth;

    public ActC() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity2);

        mAuth = FirebaseAuth.getInstance();

        sharedPreferences = getSharedPreferences(keySP, MODE_PRIVATE);
        //editorSP = sharedPreferences.edit();
        sexo = sharedPreferences.getString("sexo", "m");
        String nuevoUser = mAuth.getCurrentUser()+"_"+Calendar.getInstance().getTime();
        userName = sharedPreferences.getString("usuario", nuevoUser);

        //Crear directorio en la memoria interna.
        mFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/audiosEmociones/"+userName+"/";
        mFileS = Environment.getExternalStorageDirectory().getAbsolutePath() + "/audiosEmociones/";
        File directorio = new File(mFileS, userName);
        //Muestro un mensaje en el logcat si no se creo la carpeta por algun motivo
        if (!directorio.mkdirs()) {
            Log.e("FILE", "Error: No se creo el directorio privado");
        }

        //View rootView = R.layout.activity_activity2.inflate(R.layout.fragment_preactivity, container, false);
        v = initViews();
        listImg = new Emociones();
        loadData(sexo, v);
        wheel_img.setOnMenuSelectedListener(this);
        /* Recording audio FIREBASE */
        mStorage = FirebaseStorage.getInstance().getReference();

        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        mediaPlayerSounds = new MediaPlayerSounds(ActC.this);
        wheel_img.setOnMenuItemClickListener(new CursorWheelLayout.OnMenuItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                try {
                    mediaPlayerSounds.playSound(mediaPlayerSounds.loadRuleta());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void loadData(String s, View view) {
        //Emociones e = new Emociones();
        listImg.Emociones(getApplicationContext(), s);
        WheelmageAdapter adapter = new WheelmageAdapter(getBaseContext(), view, sexo);
        wheel_img.setAdapter(adapter);
    }

    @SuppressLint("WrongViewCast")
    private View initViews() {
        wheel_img = (CursorWheelLayout) findViewById(R.id.wheel);
        return wheel_img;
    }



    @Override
    public void onItemSelected(CursorWheelLayout parent, View view, int pos) {

        final LinearLayout lwheel = (LinearLayout) findViewById(R.id.wheelLayout);
        final Button buttonDialog = (Button) findViewById(R.id.EmocionDialog);
        final TextView textWheel = (TextView) findViewById(R.id.textWheel);


        if(parent.getId() == R.id.wheel){
            //Toast.makeText(getBaseContext(), listImg.get(pos).getName(), Toast.LENGTH_SHORT).show();

            wheel_img.setOnMenuSelectedListener(new CursorWheelLayout.OnMenuSelectedListener() {
                public void onItemSelected(CursorWheelLayout parent, View view, int pos) {

                    Date date = new Date();
                    DialogFlag = true;
                    //Toast.makeText(Activity2.this, "Top Menu click position:" + pos + " Dialog: "+DialogFlag, Toast.LENGTH_SHORT).show();

                    final int posicion = wheel_img.getSelectedPosition();
                    //mFileName = ""mFile;
                    mFileName = mFile;
                    //mFileName += listImg.get(wheel_img.getSelectedPosition()).getId() + "_" + listImg.get(wheel_img.getSelectedPosition()).getName() + extension;
                    mFileName += (pos+1)+"_"+listImg.getEmocion(pos).getName()+"_"+date.getTime()+extension;

                    textWheel.setText(String.valueOf(posicion+1));
                    lwheel.setBackgroundColor(Color.parseColor(listImg.getEmocion(posicion).getColor()));
                    buttonDialog.setVisibility(View.VISIBLE);
                    buttonDialog.setText(listImg.getEmocion(posicion).getName());
                    buttonDialog.setBackgroundColor(Color.parseColor(listImg.getEmocion(posicion).getColorB()));
                    buttonDialog.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                mediaPlayerSounds.playSound(mediaPlayerSounds.loadInicio());
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
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
        dialog.setTitle(listImg.getEmocion(pos).getName());

        Toast.makeText(this,"Dialog", Toast.LENGTH_SHORT).show();

        llActivity2 = (LinearLayout) dialog.findViewById(R.id.llact2);
        buttonRecord = (Button) dialog.findViewById(R.id.btnRecord);
        buttonStop = (Button) dialog.findViewById(R.id.btnStop);
        buttonPlay = (Button) dialog.findViewById(R.id.btnPlay);
        EmocionDialog = (ImageView) dialog.findViewById(R.id.imgEmocion);
        NameEmocionDialog = (TextView) dialog.findViewById(R.id.nameEmocion);

        Uri ruta = Uri.parse(listImg.getEmocion(pos).getUrl());
        Picasso.with(v.getContext())
                .load(ruta).fit()
                .into(EmocionDialog);
        llActivity2.setBackgroundColor(Color.parseColor(listImg.getEmocion(pos).getColor()));
        NameEmocionDialog.setText(listImg.getEmocion(pos).getName());
        buttonRecord.setEnabled(true);
        buttonStop.setEnabled(false);

        // Permisos
        if(checkPermissionFromDevice()) {
            buttonRecord.setEnabled(true);
            buttonRecord.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        //mRecorder.prepare();
                        //mRecorder.start();
                    buttonRecord.setEnabled(false);
                    buttonStop.setEnabled(true);
                    //buttonRecord.setEnabled(false);
                    startRecording();
                    buttonRecord.setVisibility(View.INVISIBLE);
                    buttonStop.setVisibility(View.VISIBLE);
                    buttonStop.setEnabled(true);
                    Toast.makeText(ActC.this, "Recording...", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            requestPermissions();

        }


        buttonStop.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //buttonRecord.setEnabled(true);
                buttonStop.setEnabled(true);

                stopRecording();
                buttonStop.setVisibility(View.INVISIBLE);
                buttonPlay.setVisibility(View.VISIBLE);
                buttonPlay.setEnabled(true);
                Toast.makeText(getApplicationContext(), "Record Stopped...", Toast.LENGTH_SHORT).show();
                //DialogFlag = false;

            }
        });

        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonStop.setVisibility(View.INVISIBLE);
                mostrarFile();
                dialog.cancel();
            }
        });

        dialog.show();
    }

    public void mostrarFile(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        String path = mFileName;
        Uri uri = Uri.parse(path);
        //Uri uri = Uri.parse(mFileS+userName);
        Toast.makeText(this, path+"\n"+uri.toString(), Toast.LENGTH_SHORT).show();
        intent.setDataAndType(uri,"audio/mp3");
        if (intent.resolveActivity(getPackageManager()) != null) {
            //startActivity(intent);
            startActivity(Intent.createChooser(intent,"Abrir archivo"));
        }
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
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
        String path = listImg.getEmocion(wheel_img.getSelectedPosition()).getId() + "_" + listImg.getEmocion(wheel_img.getSelectedPosition()).getName() + extension;
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
