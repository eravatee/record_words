package com.example.eravatee.recorder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sabari on 4/10/18.
 */

public class SanskritWord {

    @SerializedName("id")
    @Expose
    private int wordId;

    @SerializedName("word")
    @Expose
    private String word;

    public SanskritWord(int wordId, String word) {
        this.wordId = wordId;
        this.word = word;
    }

    public int getWordId() {
        return wordId;
    }

    public void setWordId(int wordId) {
        this.wordId = wordId;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}
