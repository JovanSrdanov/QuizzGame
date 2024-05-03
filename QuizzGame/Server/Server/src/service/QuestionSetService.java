package service;

import model.Question;
import repository.QuestionSetRepository;

public class QuestionSetService {

    public static Question getQuestion(int currentSet, int currentQuestionInSet) {
        return QuestionSetRepository.getQuestion(currentSet, currentQuestionInSet);
    }

    public static Question getQuestionByQuestionText(String questionText) {
        return QuestionSetRepository.getQuestionByQuestionText(questionText);
    }

    public static boolean IsQuestionAnsweredCorrectly(Question question, String answer) {
        return question.isAnswerCorrect(answer);
    }

}
