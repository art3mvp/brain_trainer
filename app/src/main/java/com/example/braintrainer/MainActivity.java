package com.example.braintrainer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TextView textViewTimer;
    private TextView textViewOption1;
    private TextView textViewOption2;
    private TextView textViewOption3;
    private TextView textViewOption4;
    private TextView textViewScore;
    private TextView textViewMath;
    private String question;
    private int rightAnswer;
    private int rightAnswerPosition;
    private boolean isPositive;
    private int min = 5;
    private int max = 30;
    private ArrayList<TextView> options = new ArrayList<>();
    private int countOfQuestions = 0;
    private int countOfRightAnswers = 0;
    private boolean gameOver = false;
    private MainViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        initViews();
        setTimer();
        setOnClickListeners();
        playNext();
    }

    private void setOnClickListeners() {
        textViewOption1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickAnswer(view);
            }
        });

        textViewOption2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickAnswer(view);
            }
        });

        textViewOption3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickAnswer(view);
            }
        });

        textViewOption4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickAnswer(view);
            }
        });
    }

    private void setTimer() {
        CountDownTimer timer = new CountDownTimer(70000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                textViewTimer.setText(setTimeAndColor(millisUntilFinished));
            }

            @Override
            public void onFinish() {
                gameOver = true;
                viewModel.saveScore(MainActivity.this, countOfRightAnswers);
                Intent intent = ScoreActivity.newIntent(
                        MainActivity.this, countOfRightAnswers
                );
                startActivity(intent);
            }
        };
        timer.start();
    }

    private void initViews() {
        textViewTimer = findViewById(R.id.textViewTimer2);
        textViewOption1 = findViewById(R.id.textViewOption1);
        textViewOption2 = findViewById(R.id.textViewOption2);
        textViewOption3 = findViewById(R.id.textViewOption3);
        textViewOption4 = findViewById(R.id.textViewOption4);
        textViewScore = findViewById(R.id.textViewScore);
        textViewMath = findViewById(R.id.textViewMath);

        options.add(textViewOption1);
        options.add(textViewOption2);
        options.add(textViewOption3);
        options.add(textViewOption4);
    }

    private void playNext() {
        if (!gameOver) {
            textViewScore.setText(String.format("%d / %d", countOfRightAnswers, countOfQuestions));
            questionGeneration();
            for (int i = 0; i < options.size(); i++) {
                if (i == rightAnswerPosition) {
                    options.get(i).setText(Integer.toString(rightAnswer));
                } else {
                    options.get(i).setText(Integer.toString(wrongAnswerGeneration()));
                }
            }
        }
    }

    private String setTimeAndColor(long millis) {
        int seconds = (int) (millis / 1000);
        seconds++;
        int minutes = seconds / 60;
        int restSeconds = seconds % 60;
        if (seconds <= 10) {
            textViewTimer.setTextColor(
                    getResources().getColor(android.R.color.holo_red_dark)
            );
        }
        return String.format("%02d:%02d", minutes, restSeconds);
    }

    private void questionGeneration() {
        int a = (int) (Math.random() * (max - min + 1) + min);
        int b = (int) (Math.random() * (max - min + 1) + min);

        int mark = (int) (Math.random() * 2);
        isPositive = mark == 1;
        if (isPositive) {
            rightAnswer = a + b;
            question = String.format("%d + %d = ?", a, b);
        } else {
            rightAnswer = a - b;
            question = String.format("%d - %d = ?", a, b);
        }
        rightAnswerPosition = (int) (Math.random() * (4));
        textViewMath.setText(question);
    }

    private int wrongAnswerGeneration() {
        int result;
        do {
            result = (int) (Math.random() * max * 2 + 1) - (max - min);
        } while (result == rightAnswer);
        return result;
    }

    public void onClickAnswer(View view) {
        TextView textView = (TextView) view;
        int answer = Integer.parseInt(textView.getText().toString());
        if (answer == rightAnswer) {
            Toast.makeText(MainActivity.this, "correct answer", Toast.LENGTH_SHORT).show();
            countOfRightAnswers++;
        } else {
            Toast.makeText(MainActivity.this, "wrong answer", Toast.LENGTH_SHORT).show();
        }
        countOfQuestions++;
        playNext();
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }
}