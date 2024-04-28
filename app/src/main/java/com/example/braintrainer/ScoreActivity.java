package com.example.braintrainer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

public class ScoreActivity extends AppCompatActivity {

    private Button buttonNewGame;
    private static final String SCORE = "score";
    private TextView textViewResult;
    private ScoreViewModel viewModel;
    private static final String RESULT_TEMPLATE = "Last result: %s\n\nBest result: %s";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_score);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();

        viewModel = new ViewModelProvider(this).get(ScoreViewModel.class);
        viewModel.getMaxScore().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer score) {
                setValueUI(score);
            }
        });
        viewModel.loadMaxScore(this);
        setOnClickListeners();
    }

    private void setOnClickListeners() {
        buttonNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(MainActivity.newIntent(ScoreActivity.this));
            }
        });
    }

    private void setValueUI(Integer score) {
        int lastResult = getIntent().getIntExtra(SCORE, 0);
        textViewResult.setText(String.format(RESULT_TEMPLATE, lastResult, score));
    }

    private void initViews() {
        buttonNewGame = findViewById(R.id.buttonNewGame);
        textViewResult = findViewById(R.id.textViewResult);
    }

    public static Intent newIntent(Context context, Integer score) {
        Intent intent = new Intent(context, ScoreActivity.class);
        intent.putExtra(SCORE, score);
        return intent;
    }
}