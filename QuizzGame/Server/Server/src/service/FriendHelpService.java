package service;

import java.util.ArrayList;
import model.FriendHelp;
import repository.FriendHelpRepository;

public class FriendHelpService {

    public static ArrayList<FriendHelp> getHelpAskedByContestant(String username) {
        return FriendHelpRepository.getHelpAskedByContestant(username);
    }

}
