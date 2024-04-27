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
    private SharedPreferences preferences;

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


        preferences = getApplication().getSharedPreferences("score", Context.MODE_PRIVATE);
        initViews();
        setTimer();
        playNext();
    }

    private void setTimer() {
        CountDownTimer timer = new CountDownTimer(5000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000);
                seconds++;
                textViewTimer.setText(Integer.toString(seconds));
            }

            @Override
            public void onFinish() {
                textViewTimer.setText("0");
                gameOver = true;
                int max = preferences.getInt("score", 0);
                if (max <= countOfRightAnswers) {
                    SharedPreferences.Editor myEdit = preferences.edit();
                    myEdit.putInt("score", countOfRightAnswers);
                    myEdit.commit();
                }
                Intent intent = ScoreActivity.newIntent(MainActivity.this, Integer.toString(countOfRightAnswers));
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

        playNext();
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

    private String getTime(long millis) {
        int seconds = (int) (millis / 1000);
        int minutes = seconds / 60;
        seconds = minutes % 60;
        return String.format("%02d:%02d", minutes, seconds);
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