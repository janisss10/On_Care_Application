package com.example.oca;

public class Answers {
    private String Question, answer;
    private boolean expandable;

    public boolean isExpandable() {
        return expandable;
    }

    public void setExpandable(boolean expandable) {
        this.expandable = expandable;
    }

    public Answers(String question, String answer) {
        this.Question = question;
        this.answer = answer;
        this.expandable = false;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String Question) {
        this.Question = Question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        return "Answers{" +
                "Question='" + Question + '\'' +
                ", answer='" + answer + '\'' +
                '}';
    }
}