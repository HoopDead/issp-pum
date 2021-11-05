package com.example.quizzapp;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quizzapp.Question;
import java.util.ArrayList;
import android.os.Bundle;
import android.widget.TextView;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Question> questionArrayList = new ArrayList<Question>();
    private TextView questionDisplay;
    private int currentQuestion = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeQuestions();

        questionDisplay = findViewById(R.id.questionDisplay);
        questionDisplay.setText(questionArrayList.get(currentQuestion).getQuestionId());
    }

    private void initializeQuestions()
    {
        questionArrayList.add(new Question(R.string.test_1));
        questionArrayList.add(new Question(R.string.test_2));
        questionArrayList.add(new Question(R.string.test_3));
    }

    public void nextQuestion(View view)
    {
        currentQuestion++;

        if (currentQuestion == questionArrayList.size())
        {
            currentQuestion = 0;
        }

        if (questionDisplay != null)
        {
            questionDisplay.setText(questionArrayList.get(currentQuestion).getQuestionId());
        }
    }

    public void previousQuestion(View view)
    {
        currentQuestion--;

        if (currentQuestion < 0)
        {
            currentQuestion = questionArrayList.size() - 1;
        }

        if (questionDisplay != null)
        {
            questionDisplay.setText(questionArrayList.get(currentQuestion).getQuestionId());
        }
    }
}