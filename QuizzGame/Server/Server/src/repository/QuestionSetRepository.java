package repository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import model.Question;
import model.QuestionSet;

public class QuestionSetRepository {

    public static ArrayList<QuestionSet> QuestionSets = new ArrayList<>();

    public static void LoadQuestionSets() {
        for (int j = 1; j <= 4; j++) {
            try (BufferedReader br = new BufferedReader(new FileReader("src/resources/set" + j + ".txt"))) {
                String line;
                int questionNumber = 0;
                String question = "";
                String[] answers = new String[4];
                QuestionSet questionSet = new QuestionSet(j);

                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty()) {
                        continue;
                    }

                    if (Character.isDigit(line.charAt(0))) {
                        if (!question.isEmpty()) {
                            questionSet.getQuestions().add(new Question(answers, answers[3], question, questionNumber));
                            question = "";
                        }
                        questionNumber++;
                        question = line.substring(line.indexOf(' ') + 1);
                        answers = new String[4];
                    } else {

                        char answerLetter = line.charAt(0);
                        String answerText = line.substring(line.indexOf(' ') + 1);
                        int index = answerLetter - 'a';
                        answers[index] = answerText;
                    }
                }

                if (!question.isEmpty()) {
                    questionSet.getQuestions().add(new Question(answers, answers[3], question, questionNumber));
                }
                QuestionSets.add(questionSet);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Question GetQuestion(int set, int questionNumber) {
        QuestionSet questionSet = null;
        for (QuestionSet qs : QuestionSets) {
            if (qs.getQuestionSetNumber() == set) {
                questionSet = qs;
                break;
            }
        }
        if (questionSet == null) {
            return null;
        }

        Question question = null;
        for (Question q : questionSet.getQuestions()) {
            if (q.getQuestionNumber() == questionNumber) {
                question = q;
                break;
            }
        }

        return question;
    }

    public static Question getQuestionByQuestionText(String questionText) {
        for (QuestionSet qs : QuestionSets) {
            for (Question q : qs.getQuestions()) {
                if (q.getQuestion().equals(questionText)) {
                    return q;
                }
            }
        }
        return null;
    }

}
