package model;

public class FriendHelp {

    private final String WhoAskedForHelp;
    private final String AnswerGiver;
    private final String Answer;
    private final String Question;

    public String getWhoAskedForHelp() {
        return WhoAskedForHelp;
    }

    public String getQuestion() {
        return Question;
    }

    public String getAnswerGiver() {
        return AnswerGiver;
    }

    public String getAnswer() {
        return Answer;
    }

    public FriendHelp(String WhoAskedForHelp, String AnswerGiver, String Answer, String Question) {
        this.WhoAskedForHelp = WhoAskedForHelp;
        this.AnswerGiver = AnswerGiver;
        this.Answer = Answer;
        this.Question = Question;
    }

}
