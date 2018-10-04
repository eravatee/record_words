package com.example.eravatee.recorder.Remote;

import com.example.eravatee.recorder.SanskritWord;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UploadAPI {
    @Multipart
    @POST("uploads")
    Call<ResponseBody>uploadFile(@Part MultipartBody.Part file);

    @Headers("Content-Type: application/json")
    @GET("words")
    Call<List<SanskritWord>> getWords();
}
