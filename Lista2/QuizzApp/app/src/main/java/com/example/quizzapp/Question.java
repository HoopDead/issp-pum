package com.example.quizzapp;

public class Question {
    private int questionId;

    private boolean questionAnswered;
    public boolean isQuestionBlocked;

    private boolean correctAnswer;
    public boolean getCorrectAnswer;

    private boolean currentAnswer;
    public boolean setCurrentAnswer;
    public boolean getCurrentAnswer;

    public Question(int questionId, boolean correctAnswer)
    {
        this.questionId = questionId;
        this.correctAnswer = correctAnswer;
        this.questionAnswered = false;
    }

    public int getQuestionId()
    {
        return questionId;
    }

    public void blockQuestion() { this.questionAnswered = true; }
    public void unblockQuestion() { this.questionAnswered = false; }
    public boolean isQuestionBlocked() { return this.questionAnswered; }

    public void setCurrentAnswer(boolean currentAnswer) { this.currentAnswer = currentAnswer; }
    public boolean getCurrentAnswer() { return this.currentAnswer; }

    public boolean getCorrectAnswer() { return this.correctAnswer; }
}
