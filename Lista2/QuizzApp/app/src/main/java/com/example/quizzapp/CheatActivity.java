package com.example.quizzapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    private TextView cheatQuestionAnswer;
    private String questionAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);
        cheatQuestionAnswer = findViewById(R.id.cheatQuestionAnswer);
        questionAnswer = getIntent().getStringExtra(MainActivity.ANSWER);
        cheatQuestionAnswer.setText(questionAnswer);
    }

}
