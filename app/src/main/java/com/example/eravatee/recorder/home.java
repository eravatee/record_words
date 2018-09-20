package com.example.eravatee.recorder;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class home extends AppCompatActivity {

    private Button record, stop, play, pause ;
    String pathSave = "";
    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;
    private File audioFile;

    final int REQUEST_PERMISSION_CODE = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if(!checkPermissionFromDevice())
            requestPermission();

        play = findViewById(R.id.play);
        stop = findViewById(R.id.stop);
        record = findViewById(R.id.record);
        pause = findViewById(R.id.pause);

            record.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    if (checkPermissionFromDevice()) {
//                        pathSave = Environment.getExternalStorageDirectory()
//                                .getAbsolutePath() + "/"
//                                + UUID.randomUUID().toString() + "_audio_record.3gp";
                        audioFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
                                "audio_test.3gp");
                        setUpMediaRecorder();

                        try {
                            mediaRecorder.prepare();
                            mediaRecorder.start();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        play.setEnabled(false);
                        pause.setEnabled(false);
                        stop.setEnabled(true);

                        Toast.makeText(home.this, "Recording", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        requestPermission();
                    }
                }


            });
            stop.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    mediaRecorder.stop();
                    stop.setEnabled(false);
                    play.setEnabled(true);
                    record.setEnabled(true);
                    pause.setEnabled(false);
                }
            });
            play.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    stop.setEnabled(true);
                    record.setEnabled(false);
                    pause.setEnabled(true);

                    mediaPlayer = new MediaPlayer();
                    try{

                        mediaPlayer.setDataSource(audioFile.getAbsolutePath());
                        mediaPlayer.prepare();

                    }catch(IOException e){
                        e.printStackTrace();
                    }

                    mediaPlayer.start();
                    Toast.makeText(home.this, "Playing",Toast.LENGTH_SHORT ).show();

                }
            });

            pause.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    stop.setEnabled(false);
                    record.setEnabled(true);
                    pause.setEnabled(false);
                    play.setEnabled(true);

                    if(mediaPlayer != null)
                    {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        setUpMediaRecorder();

                    }
                }
            });

    }

    private void setUpMediaRecorder(){
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setAudioEncodingBitRate(16);
        mediaRecorder.setAudioSamplingRate(44100);

        mediaRecorder.setOutputFile(audioFile.getAbsolutePath());


    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
        }, REQUEST_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case REQUEST_PERMISSION_CODE:
            {
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, "PERMISSION GRANTED" ,Toast.LENGTH_SHORT).show();
                else
                Toast.makeText(this, "PERMISSION DENIED" ,Toast.LENGTH_SHORT).show();

            }
                break;
        }
    }

    private boolean checkPermissionFromDevice(){
        int write_external_storage_result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int record_audio_result =  ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        return write_external_storage_result == PackageManager.PERMISSION_GRANTED && record_audio_result == PackageManager.PERMISSION_GRANTED;
    }
}