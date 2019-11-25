package com.schoolproject.recordnotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.io.IOException;
import java.util.UUID;

public class CreatingRecord extends AppCompatActivity {

    FloatingActionButton startrecording,stoprecording,playrecord,stoprecord,recordconfirm;
    String pathSave = "";
    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;
    final int REQUEST_PERMISSION_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creating_record);

        startrecording = (FloatingActionButton)findViewById(R.id.startrecording);
        stoprecording = (FloatingActionButton)findViewById(R.id.stoprecording);
        playrecord = (FloatingActionButton)findViewById(R.id.playrecord);
        stoprecord = (FloatingActionButton)findViewById(R.id.stoprecord);



        if(!checkPermissionFromDevice()){
            requestPermission();
        }


        if(checkPermissionFromDevice()){

            startrecording.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    pathSave = Environment.getExternalStorageDirectory()
                            .getAbsolutePath()
                            +"/"
                            + UUID.randomUUID().toString()+"_audio_record.3gp";
                    setupMediaRecorder();
                    try {
                        mediaRecorder.prepare();
                        mediaRecorder.start();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                    playrecord.setEnabled(false);
                    stoprecord.setEnabled(false);

                    Toast.makeText(CreatingRecord.this,"Recording...",Toast.LENGTH_SHORT).show();
                }
            });

//            recordconfirm.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent moveToMain = new Intent(CreatingRecord.this,MainActivity.class);
//                    startActivity(moveToMain);
//                }
//            });


            stoprecording.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mediaRecorder.stop();
                    stoprecording.setEnabled(false);
                    startrecording.setEnabled(true);
                    playrecord.setEnabled(true);
                    stoprecord.setEnabled(false);
                }
            });

            playrecord.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    stoprecord.setEnabled(true);
                    stoprecording.setEnabled(false);
                    startrecording.setEnabled(false);

                    mediaPlayer = new MediaPlayer();
                    try {
                        mediaPlayer.setDataSource(pathSave);
                        mediaPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    mediaPlayer.start();
                    Toast.makeText(CreatingRecord.this, "Playing...", Toast.LENGTH_SHORT).show();
                }
            });

            stoprecord.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    stoprecording.setEnabled(false);
                    startrecording.setEnabled(true);
                    stoprecord.setEnabled(false);
                    playrecord.setEnabled(true);

                    if(mediaPlayer != null){
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        setupMediaRecorder();
                    }

                }
            });


        }else{
            requestPermission();
        }

    }

    private void setupMediaRecorder() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(pathSave);
    }

    private boolean checkPermissionFromDevice() {
         int write_external_storage_result = ContextCompat
                 .checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE);
         int record_audio_result = ContextCompat
                 .checkSelfPermission(this,Manifest.permission.RECORD_AUDIO);
         return write_external_storage_result == PackageManager.PERMISSION_GRANTED &&
                 record_audio_result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(this,new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
        },REQUEST_PERMISSION_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_PERMISSION_CODE:
            {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
            }
                break;
        }
    }
}
