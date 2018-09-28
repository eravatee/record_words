package com.example.eravatee.recorder;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.eravatee.recorder.dummy.DummyContent;
import com.example.eravatee.recorder.Remote.UploadAPI;
import com.example.eravatee.recorder.Remote.RetrofitClient;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class home extends AppCompatActivity implements ItemFragment.OnListFragmentInteractionListener,
        RecordFragment.OnFragmentInteractionListener{


    final int REQUEST_PERMISSION_CODE = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_home);

        Fragment fragment = ItemFragment.newInstance(1);
        //Fragment fragment = RecordFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_home, fragment)
                .commit();

        if(!checkPermissionFromDevice())
            requestPermission();
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



    @Override
    public void onListFragmentInteraction(int listItemId) {
        Fragment fragment = RecordFragment.newInstance(listItemId);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_home, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}