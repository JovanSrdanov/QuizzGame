package model;

import java.util.Random;

public class Question {

    private final int questionNumber;
    private final String[] answers;
    private final String[] wrongAnswers;
    private final String correctAnswer;
    private final String Question;

    public int getQuestionNumber() {
        return questionNumber;
    }

    public String[] getWrongAnswers() {
        return wrongAnswers;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public String getQuestion() {
        return Question;
    }

    public Question(String[] answers, String correctAnswer, String Question, int questionNumber) {
        this.answers = answers;
        this.correctAnswer = correctAnswer;
        this.Question = Question;
        this.questionNumber = questionNumber;
        this.wrongAnswers = new String[2];
        Random random = new Random();
        int index1 = random.nextInt(3);
        int index2;
        do {
            index2 = random.nextInt(3);
        } while (index2 == index1);
        this.wrongAnswers[0] = answers[index1];
        this.wrongAnswers[1] = answers[index2];
    }

    public String[] getAnswers() {
        return answers;
    }

    public boolean IsAnswerCorrect(String insertedAnswer) {
        return insertedAnswer.equals(correctAnswer);
    }

}
