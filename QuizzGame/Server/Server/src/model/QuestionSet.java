package model;

import java.util.ArrayList;

public class QuestionSet {

    private final ArrayList<Question> questions;
    private final int questionSetNumber;

    public QuestionSet(int questionSetNumber) {
        this.questions = new ArrayList<>();
        this.questionSetNumber = questionSetNumber;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public int getQuestionSetNumber() {
        return questionSetNumber;
    }

}
