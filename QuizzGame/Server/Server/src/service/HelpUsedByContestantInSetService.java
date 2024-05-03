package service;

import model.HelpUsedByContestantInSet;
import repository.HelpUsedByContestantInSetRepository;

public class HelpUsedByContestantInSetService {

    public static boolean useHelp(HelpUsedByContestantInSet helpUsedByContestantInSet) {

        HelpUsedByContestantInSet help = HelpUsedByContestantInSetRepository.hasUsedHelpUsedByContestantInSet(helpUsedByContestantInSet);

        if (help == null) {
            HelpUsedByContestantInSetRepository.addHelpUsedByContestantInSet(helpUsedByContestantInSet);
            return true;
        }
        return help.getQuestion() == helpUsedByContestantInSet.getQuestion();

    }

}
