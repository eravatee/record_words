package com.example.eravatee.recorder;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.eravatee.recorder.Remote.RetrofitClient;
import com.example.eravatee.recorder.Remote.UploadAPI;

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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RecordFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RecordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecordFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "ListItemId";
    private int listItemId;
    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;
    private File audioFile;
    private Button record, stop, play, pause, upload_to_server;
    private static final String BASE_URL = "http://10.0.2.2/";
    private OnFragmentInteractionListener mListener;

    public RecordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RecordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecordFragment newInstance(int listItemId) {
        RecordFragment fragment = new RecordFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, listItemId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            listItemId = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_home, container, false);

        Log.d("RECORD_FRAGMENT", "Current Item No: " + listItemId);
        Toast.makeText(getActivity(), "Current Item No: " + listItemId, Toast.LENGTH_SHORT).show();

        play = view.findViewById(R.id.play);
        stop = view.findViewById(R.id.stop);
        record = view.findViewById(R.id.record);
        pause = view.findViewById(R.id.pause);
        upload_to_server = view.findViewById(R.id.upload);

        record.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    //if (checkPermissionFromDevice()) {
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
                        upload_to_server.setEnabled(false);

                        Toast.makeText(getActivity(), "Recording", Toast.LENGTH_SHORT).show();
                    //}
                    //else
                    //{
                    //    requestPermission();
                   // }
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
                    upload_to_server.setEnabled(true);
                }
            });
            play.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    stop.setEnabled(true);
                    record.setEnabled(false);
                    pause.setEnabled(true);
                    upload_to_server.setEnabled(false);

                    mediaPlayer = new MediaPlayer();
                    try{

                        mediaPlayer.setDataSource(audioFile.getAbsolutePath());
                        mediaPlayer.prepare();

                    }catch(IOException e){
                        e.printStackTrace();
                    }

                    mediaPlayer.start();
                    Toast.makeText(getActivity(), "Playing",Toast.LENGTH_SHORT ).show();

                }
            });

            pause.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    stop.setEnabled(false);
                    record.setEnabled(true);
                    pause.setEnabled(false);
                    play.setEnabled(true);
                    upload_to_server.setEnabled(true);

                    if(mediaPlayer != null)
                    {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        setUpMediaRecorder();
                    }
                }
            });

            upload_to_server.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    stop.setEnabled(false);
                    record.setEnabled(false);
                    pause.setEnabled(false);
                    play.setEnabled(false);

                    uploadFile();
                }
            });

        return view;
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

    private void uploadFile(){
        File file = new File(getContext().getFilesDir(), "audio_test.3gp");

        RequestBody filePart = RequestBody.create(MediaType.parse("multipart/form-data"),audioFile);

        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), filePart);

        UploadAPI client = RetrofitClient.getClient().create(UploadAPI.class);


        Call<ResponseBody> call = client.uploadFile(body);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(getActivity(), "upload successful", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(), "upload failure", Toast.LENGTH_SHORT).show();
                Log.d("i am failing", t.toString());
            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
