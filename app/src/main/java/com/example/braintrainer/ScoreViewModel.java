package com.example.braintrainer;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ScoreViewModel extends ViewModel {

    private static final String SCORE = "score";
    private MutableLiveData<Integer> maxScore = new MutableLiveData();
    public LiveData<Integer> getMaxScore() {
        return maxScore;
    }

    public void loadMaxScore(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SCORE, Context.MODE_PRIVATE);
        maxScore.setValue(preferences.getInt(SCORE, 0));
    }
}
