package com.example.eravatee.recorder.Utils;

import android.support.annotation.Nullable;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class ProgressRequestBody extends RequestBody{

    @Nullable
    @Override

    public MediaType ContentType(){
        return null;
    }

}
