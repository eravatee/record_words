package com.example.eravatee.recorder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sabari on 4/10/18.
 */

public class SanskritWord {

    @SerializedName("word")
    @Expose
    private String word;

    @SerializedName("location")
    @Expose
    private String location;

    public SanskritWord(String word, String location) {
        this.word = word;
        this.location = location;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
