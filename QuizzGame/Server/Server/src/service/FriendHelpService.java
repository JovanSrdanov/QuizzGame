package service;

import java.util.ArrayList;
import model.FriendHelp;
import repository.FriendHelpRepository;

public class FriendHelpService {

    public static ArrayList<FriendHelp> getHelpAskedByContestant(String username) {
        return FriendHelpRepository.getHelpAskedByContestant(username);
    }

    public static FriendHelp FindQuestionToAnswer(String username, String whoAsked, String question) {
        return FriendHelpRepository.FindQuestionToAnswer(username, whoAsked, question);
    }

    public static void updateFriendHelpWithAnswer(FriendHelp friendHelp) {
        FriendHelpRepository.updateFriendHelpWithAnswer(friendHelp);
    }

    public static Iterable<FriendHelp> getUnAnsweredQuestions(String username) {
        return FriendHelpRepository.getUnAnsweredQuestions(username);
    }

    public static void addFriendHelp(FriendHelp friendHelp) {
        boolean duplicateExists = false;
        for (FriendHelp existingHelp : FriendHelpRepository.getFriendHelps()) {
            if (existingHelp.getWhoAskedForHelp().equals(friendHelp.getWhoAskedForHelp())
                    && existingHelp.getAnswerGiver().equals(friendHelp.getAnswerGiver())
                    && existingHelp.getQuestion().equals(friendHelp.getQuestion())) {
                duplicateExists = true;
                break;
            }
        }

        if (!duplicateExists) {
            FriendHelpRepository.addFriendHelp(friendHelp);
        }

    }

}
