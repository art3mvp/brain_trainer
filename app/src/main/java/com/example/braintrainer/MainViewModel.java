package com.example.braintrainer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {
    private static final String SCORE = "score";
    private SharedPreferences.Editor editor;
    private SharedPreferences preferences;


    public void saveScore(Context context, int score) {
        preferences = context.getSharedPreferences(SCORE, Context.MODE_PRIVATE);
        int currentBestScore = preferences.getInt(SCORE, 0);
        if (currentBestScore <= score) {
            editor = preferences.edit();
            editor.putInt(SCORE, score);
            editor.apply();
        }
    }
}
