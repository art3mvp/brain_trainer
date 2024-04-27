package com.example.braintrainer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ScoreActivity extends AppCompatActivity {

    Button button;
    TextView textView;
    SharedPreferences preferences;

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

        button = findViewById(R.id.buttonNewGame);
        textView = findViewById(R.id.textViewResult);
        preferences = getApplication().getSharedPreferences("score", Context.MODE_PRIVATE);
        String max = String.valueOf(preferences.getInt("score", 0));
        String text = String.format("last result: %s \n best result: %s", getIntent().getStringExtra("score"), max);
        textView.setText(text);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(MainActivity.newIntent(ScoreActivity.this));
            }
        });
    }

    public static Intent newIntent(Context context, String score) {
        Intent intent = new Intent(context, ScoreActivity.class);
        intent.putExtra("score", score);
        return intent;
    }
}