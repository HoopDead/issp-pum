package com.example.quizzapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import com.example.quizzapp.Question;
import java.util.ArrayList;

import android.net.Uri;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.view.View;
import android.widget.Button;
import android.widget.TableRow;

public class MainActivity extends AppCompatActivity {

    public static final String ANSWER = "";

    private ArrayList<Question> questionArrayList = new ArrayList<Question>();
    private ArrayList<Integer> questionsAnswered = new ArrayList<Integer>();

    private TextView questionDisplay;
    private int currentQuestion = 0;
    Button buttonTrue, buttonFalse, buttonShowAnswer;
    TableRow submitButtons, changeButtons, resetButtonRow, searchButtonRow, showAnswerButtonRow, cheatButtonRow;

    private double points = 0;
    private int questionsCheated = 0;
    private int questionsAnsweredCorrectly = 0;
    private int questionsAnsweredIncorrectly = 0;

    private TextView summaryDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        questionDisplay = findViewById(R.id.questionDisplay);
        submitButtons = findViewById(R.id.submitButtons);
        changeButtons = findViewById(R.id.changeButtons);
        buttonTrue = (Button) findViewById(R.id.buttonTrue);
        buttonFalse = (Button) findViewById(R.id.buttonFalse);
        searchButtonRow = findViewById(R.id.searchButtonRow);
        showAnswerButtonRow = findViewById(R.id.showAnswerButtonRow);
        buttonShowAnswer = findViewById(R.id.buttonShowAnswer);
        cheatButtonRow = findViewById(R.id.cheatButtonRow);

        initializeQuestions();

        summaryDisplay = findViewById(R.id.summaryDisplay);
        resetButtonRow = findViewById(R.id.resetButtonRow);
        questionDisplay.setText(questionArrayList.get(currentQuestion).getQuestionId());

        if (savedInstanceState != null)
        {
            questionsAnswered = savedInstanceState.getIntegerArrayList("questionsAnsweredState");
            points = savedInstanceState.getDouble("pointsState");
            questionsAnsweredCorrectly = savedInstanceState.getInt("questionsAnsweredCorrectlyState");
            questionsAnsweredIncorrectly = savedInstanceState.getInt("questionsAnsweredIncorrectlyState");
            questionsCheated = savedInstanceState.getInt("questionsCheatedState");

            for (int i = 0; i < questionArrayList.size(); i++)
            {
                if (questionsAnswered.contains(questionArrayList.get(i).getQuestionId()))
                {
                    questionArrayList.get(i).blockQuestion();
                }
            }

            refreshButtons();
        }
    }

    private void initializeQuestions()
    {
        questionArrayList.add(new Question(R.string.test_1, false));
        questionArrayList.add(new Question(R.string.test_2, false));
        questionArrayList.add(new Question(R.string.test_3, false));
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

        refreshButtons();
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

        refreshButtons();
    }

    public void submitAnswer(View view)
    {
        questionArrayList.get(currentQuestion).blockQuestion();

        switch (view.getId())
        {
            case R.id.buttonTrue:
                questionArrayList.get(currentQuestion).setCurrentAnswer(true);

                if (questionArrayList.get(currentQuestion).getCorrectAnswer() == true)
                {
                    points++;
                    questionsAnsweredCorrectly++;
                }
                else
                {
                    questionsAnsweredIncorrectly++;
                }

                break;
            case R.id.buttonFalse:
                questionArrayList.get(currentQuestion).setCurrentAnswer(false);

                if (questionArrayList.get(currentQuestion).getCorrectAnswer() == false)
                {
                    points++;
                    questionsAnsweredCorrectly++;
                }
                else
                {
                    questionsAnsweredIncorrectly++;
                }

                break;
        }

        buttonTrue.setEnabled(false);
        buttonFalse.setEnabled(false);
        showAnswerButtonRow.setVisibility(View.VISIBLE);

        questionsAnswered.add(questionArrayList.get(currentQuestion).getQuestionId());
        checkToDisplaySummary();
    }

    private void refreshButtons()
    {
        if (!questionArrayList.get(currentQuestion).isQuestionBlocked())
        {
            showAnswerButtonRow.setVisibility(View.GONE);
            buttonShowAnswer.setText("SHOW ANSWER");

            buttonTrue.setEnabled(true);
            buttonFalse.setEnabled(true);
        }
        else
        {
            buttonTrue.setEnabled(false);
            buttonFalse.setEnabled(false);

            showAnswerButtonRow.setVisibility(View.VISIBLE);
            buttonShowAnswer.setText("SHOW ANSWER");
        }
    }

    private void checkToDisplaySummary()
    {
        if (questionsAnswered.size() == questionArrayList.size())
        {
            questionDisplay.setVisibility(View.GONE);
            submitButtons.setVisibility(View.GONE);
            changeButtons.setVisibility(View.GONE);
            searchButtonRow.setVisibility(View.GONE);
            showAnswerButtonRow.setVisibility(View.GONE);
            cheatButtonRow.setVisibility(View.GONE);

            double erasedPoints = 0.15 * questionsCheated;
            points -= points * erasedPoints;

            summaryDisplay.setVisibility(View.VISIBLE);
            summaryDisplay.setText("Points: " + points +
                                    "\nCorrect Answers: " + questionsAnsweredCorrectly +
                                    "\nIncorrect Answers: " + questionsAnsweredIncorrectly +
                                    "\nQuestions cheated: " + questionsCheated);
            resetButtonRow.setVisibility(View.VISIBLE);

        }
    }

    public void resetQuiz(View view)
    {
        summaryDisplay.setVisibility(View.GONE);
        resetButtonRow.setVisibility(View.GONE);

        questionDisplay.setVisibility(View.VISIBLE);
        submitButtons.setVisibility(View.VISIBLE);
        changeButtons.setVisibility(View.VISIBLE);
        searchButtonRow.setVisibility(View.VISIBLE);
        cheatButtonRow.setVisibility(View.VISIBLE);

        for (Question q : questionArrayList)
        {
            q.unblockQuestion();
        }

        points = 0;
        questionsAnsweredCorrectly = 0;
        questionsAnsweredIncorrectly = 0;
        questionsCheated = 0;

        questionsAnswered.clear();
        refreshButtons();
        currentQuestion = 0;
        questionDisplay.setText(questionArrayList.get(currentQuestion).getQuestionId());
    }

    public void showAnswer(View view)
    {
        buttonShowAnswer.setText(Boolean.toString(questionArrayList.get(currentQuestion).getCorrectAnswer()));
    }

    public void startCheatActivity(View view)
    {
        questionsCheated++;
        Intent intent = new Intent(this, CheatActivity.class);
        intent.putExtra(ANSWER, Boolean.toString(questionArrayList.get(currentQuestion).getCorrectAnswer()));
        startActivity(intent);
    }

    public void searchAnswer(View view)
    {
        String searchText = questionDisplay.getText().toString();
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q=" + searchText)));
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntegerArrayList("questionsAnsweredState", questionsAnswered);
        outState.putDouble("pointsState", points);
        outState.putInt("questionsAnsweredCorrectlyState", questionsAnsweredCorrectly);
        outState.putInt("questionsAnsweredIncorrectlyState", questionsAnsweredIncorrectly);
        outState.putInt("questionsCheatedState", questionsCheated);
    }
}