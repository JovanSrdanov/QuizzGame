package main;

import Utils.TokenUtils;
import java.io.PrintWriter;

import model.FriendHelp;
import model.HelpUsedByContestantInSet;
import model.Question;
import model.User;
import service.ActiveSetService;
import service.FriendHelpService;
import service.HelpUsedByContestantInSetService;
import service.QuestionSetService;
import service.UserService;

public class Controller {

    static final String HELP_HALF_HALF = "help_half_half";
    static final String HELP_ASK_FRIEND = "help_ask_friend";
    static final String HELP_CHANGE_QUESTION = "help_change_question";

    public static void handleRequest(String request, PrintWriter pw) {
        try {
            String[] allData = request.split("\n", 2);
            String authtoken = allData[0];
            String[] split = allData[1].split("\n", 2);
            String method = split[0];
            String body = (split.length > 1) ? split[1].trim() : " ";
            User user = null;
            User.TypeOfUser typeOfUser = null;

            if (TokenUtils.isValidDecryption(authtoken)) {
                user = UserService.FindUser(TokenUtils.getUsernameFromEncryptedData(authtoken));
                if (user == null) {
                    pw.println("UNAUTHORIZED");
                    return;
                }
                typeOfUser = user.getTypeOfUser();
            } else {
                if (!"Login".equals(method)) {
                    pw.println("UNAUTHORIZED");
                    return;
                }
            }

            if ("Login".equals(method)) {
                Login(user, body, pw);
                return;
            }
            if ("AddUser".equals(method)) {
                if (typeOfUser != User.TypeOfUser.ADMIN) {
                    pw.println("UNAUTHORIZED FOR ADDUSER");
                    return;
                }
                AddUser(user, body, pw);
                return;
            }
            if ("RemoveUser".equals(method)) {
                if (typeOfUser != User.TypeOfUser.ADMIN) {
                    pw.println("UNAUTHORIZED FOR REMOVEUSER");
                    return;
                }
                RemoveUser(user, body, pw);
                return;
            }
            if ("SetActiveSet".equals(method)) {
                if (typeOfUser != User.TypeOfUser.ADMIN) {
                    pw.println("UNAUTHORIZED FOR SETACTIVESET");
                    return;
                }
                SetActiveSet(user, body, pw);
                return;
            }
            if ("GetActiveSet".equals(method)) {
                if (typeOfUser != User.TypeOfUser.ADMIN) {
                    pw.println("UNAUTHORIZED FOR GETACTIVESET");
                    return;
                }
                GetActiveSet(user, body, pw);
                return;
            }
            if ("GetAllUsers".equals(method)) {
                if (typeOfUser != User.TypeOfUser.ADMIN) {
                    pw.println("UNAUTHORIZED FOR GETALLUSERS");
                    return;
                }
                GetAllUsers(user, body, pw);
                return;
            }
            if ("GetTableResults".equals(method)) {
                if (typeOfUser != User.TypeOfUser.CONTESTANT) {
                    pw.println("UNAUTHORIZED FOR GETTABLERESULTS");
                    return;
                }
                GetTableResults(user, body, pw);
                return;
            }
            if ("GetCurrentQuestion".equals(method)) {
                if (typeOfUser != User.TypeOfUser.CONTESTANT) {
                    pw.println("UNAUTHORIZED FOR GETCURRENTQUESTION");
                    return;
                }
                GetCurrentQuestion(user, body, pw);
                return;
            }
            if ("AnswerCurretnQuestion".equals(method)) {
                if (typeOfUser != User.TypeOfUser.CONTESTANT) {
                    pw.println("UNAUTHORIZED FOR ANSWERCURRENTQUESTION");
                    return;
                }
                AnswerCurretnQuestion(user, body, pw);
                return;
            }
            if ("HelpFriend".equals(method)) {
                if (typeOfUser != User.TypeOfUser.CONTESTANT) {
                    pw.println("UNAUTHORIZED FOR HELPFRIEND");
                    return;
                }
                HelpFriend(user, body, pw);
                return;
            }
            if ("FriendsInNeedOfHelp".equals(method)) {
                if (typeOfUser != User.TypeOfUser.CONTESTANT) {
                    pw.println("UNAUTHORIZED FOR FRIENDSINNEEDOFHELP");
                    return;
                }
                FriendsInNeedOfHelp(user, body, pw);
                return;
            }
            if ("AskForHelpFromFriend".equals(method)) {
                if (typeOfUser != User.TypeOfUser.CONTESTANT) {
                    pw.println("UNAUTHORIZED FOR ASKFORHELPFROMFRIEND");
                    return;
                }
                AskForHelpFromFriend(user, body, pw);
                return;
            }
            if ("GetHelpFromFriends".equals(method)) {
                if (typeOfUser != User.TypeOfUser.CONTESTANT) {
                    pw.println("UNAUTHORIZED FOR GETHELPFROMFRIENDS");
                    return;
                }
                GetHelpFromFriends(user, body, pw);
                return;
            }
            if ("SwitchQuestion".equals(method)) {
                if (typeOfUser != User.TypeOfUser.CONTESTANT) {
                    pw.println("UNAUTHORIZED FOR SWITCHQUESTION");
                    return;
                }
                SwitchQuestion(user, body, pw);
                return;
            }
            if ("HalfHalfQuestion".equals(method)) {
                if (typeOfUser != User.TypeOfUser.CONTESTANT) {
                    pw.println("UNAUTHORIZED FOR HALFHALFQUESTION");
                    return;
                }
                HalfHalfQuestion(user, body, pw);
                return;
            }
            pw.println("Unknown method or anouthorized");
        } catch (Exception e) {
            pw.println("ERROR");
        }
    }

    private static void Login(User user, String body, PrintWriter pw) {
        try {
            String[] loginInfo = body.split(":");
            String username = loginInfo[0];
            String password = loginInfo[1];
            User.TypeOfUser typeOfUser = loginInfo[2].equals("admin") ? User.TypeOfUser.ADMIN : User.TypeOfUser.CONTESTANT;
            User u = UserService.FindUserByUsernamePasswordAndRole(username, password, typeOfUser);
            if (u != null) {
                pw.println(loginInfo[2] + ":" + TokenUtils.encryptUsernameAndDate(username) + ":" + username);
            } else {
                pw.println("FAILED");
            }
        } catch (Exception e) {
            pw.println("ERROR");
        }
    }

    private static void RemoveUser(User user, String body, PrintWriter pw) {
        try {
            UserService.DeleteByUsername(body);
            pw.println("Done");
        } catch (Exception e) {
            pw.println("ERROR");
        }
    }

    private static void SetActiveSet(User user, String body, PrintWriter pw) {
        try {
            ActiveSetService.SetActiveSet(Integer.parseInt(body));
            pw.println("Done");
        } catch (Exception e) {
            pw.println("ERROR");
        }
    }

    private static void AddUser(User user, String body, PrintWriter pw) {
        try {
            String[] userInfo = body.split(":");
            String username = userInfo[0];
            String password = userInfo[1];
            String userType = userInfo[2];
            if (UserService.AddUser(new User(username, password, userType, 0, 0, 1, 0))) {
                pw.println("SUCCESS");
            } else {
                pw.println("FAILED");
            }
        } catch (Exception e) {
            pw.println("ERROR");
        }
    }

    private static void GetTableResults(User user, String body, PrintWriter pw) {
        try {

            StringBuilder sb = new StringBuilder("");
            for (User u : UserService.GetUsersForTableResults()) {
                if (u.getTypeOfUser().equals(User.TypeOfUser.CONTESTANT)) {
                    sb.append(u.getUsername()).append(":").append(u.getTypeOfUserToString()).append(":").append(u.getCorrectAnsweredQuestions()).append(":").append(u.getTotalAnsweredQuestions()).append(":").append(u.getCurrentQuestionInSet()).append(":").append(u.getCurrentSet()).append("\n");
                }
            }
            pw.println(sb.toString());
        } catch (Exception e) {
            pw.println("ERROR");
        }
    }

    private static void GetCurrentQuestion(User user, String body, PrintWriter pw) {
        try {

            int ActiveSet = ActiveSetService.getActiveSet();
            if (ActiveSet != user.getCurrentSet()) {
                user.setCurrentSet(ActiveSet);
                user.setCurrentQuestionInSet(1);
            }
            UserService.updateUser(user);

            Question question = QuestionSetService.getQuestion(user.getCurrentSet(), user.getCurrentQuestionInSet());
            if (question == null || question.getQuestionNumber() == 11) {
                pw.println("NO QUESTION");
                return;
            }

            StringBuilder sb = new StringBuilder("");
            sb.append(question.getQuestion()).append("\n");
            for (String s : question.getAnswers()) {
                sb.append(s).append("\n");
            }
            pw.println(sb);
        } catch (Exception e) {
            pw.println("ERROR");
        }

    }

    private static void AnswerCurretnQuestion(User user, String body, PrintWriter pw) {
        try {

            Question question = QuestionSetService.getQuestionByQuestionText(body.split("\n")[0]);
            if (question == null) {
                pw.println("QUESTION NOT FOUND");
                return;
            }

            if (QuestionSetService.IsQuestionAnsweredCorrectly(question, body.split("\n")[1])) {
                user.setCorrectAnsweredQuestions(user.getCorrectAnsweredQuestions() + 1);
                pw.println("CORRECT");

            } else {
                pw.println("The correct answer was: " + question.getCorrectAnswer());
            }
            user.setCurrentQuestionInSet(user.getCurrentQuestionInSet() + 1);
            user.setTotalAnsweredQuestions(user.getTotalAnsweredQuestions() + 1);

            UserService.updateUser(user);

        } catch (Exception e) {
            pw.println("ERROR");
        }
    }

    private static void HelpFriend(User user, String body, PrintWriter pw) {
        try {
            FriendHelp friendHelp = FriendHelpService.FindQuestionToAnswer(user.getUsername(), body.split("\n")[0], body.split("\n")[1]);
            if (friendHelp == null) {
                pw.println("FriendHelp NOT FOUND");
                return;
            }
            friendHelp.setAnswer(body.split("\n")[2]);
            FriendHelpService.updateFriendHelpWithAnswer(friendHelp);
            pw.println("SUCCESS");

        } catch (Exception e) {
            pw.println("ERROR");
        }
    }

    private static void FriendsInNeedOfHelp(User user, String body, PrintWriter pw) {
        try {

            StringBuilder sb = new StringBuilder("");
            for (FriendHelp fh : FriendHelpService.getUnAnsweredQuestions(user.getUsername())) {
                sb.append(fh.getWhoAskedForHelp()).append("__").append(fh.getAnswerGiver()).append("__").append(fh.getAnswer()).append("__").append(fh.getQuestion()).append("\n");
            }
            pw.println(sb);

        } catch (Exception e) {
            pw.println("ERROR");
        }
    }

    private static void AskForHelpFromFriend(User user, String body, PrintWriter pw) {
        try {

            User friend = UserService.FindUser(body.split(":")[0]);
            if (friend == null) {
                pw.println("FRIEND NOT FOUND");
                return;
            }
            HelpUsedByContestantInSet help = new HelpUsedByContestantInSet(user.getCurrentSet(), user.getCurrentQuestionInSet(), user.getUsername(), HELP_ASK_FRIEND);
            if (HelpUsedByContestantInSetService.useHelp(help)) {

                Question question = QuestionSetService.getQuestion(user.getCurrentSet(), user.getCurrentQuestionInSet());
                if (question == null) {
                    pw.println("NO QUESTION");
                    return;
                }
                FriendHelpService.addFriendHelp(new FriendHelp(user.getUsername(), body.split(":")[0], "EMPTY", question.getQuestion()));
                pw.println("SUCCESS");

            } else {
                pw.println("CAN NOT USE THIS HELP AT THIS MOMENT!");
            }

        } catch (Exception e) {
            pw.println("ERROR");
        }
    }

    private static void GetHelpFromFriends(User user, String body, PrintWriter pw) {
        try {
            StringBuilder sb = new StringBuilder("");
            for (FriendHelp fh : FriendHelpService.getHelpAskedByContestant(user.getUsername())) {
                sb.append(fh.getWhoAskedForHelp()).append("__").append(fh.getAnswerGiver()).append("__").append(fh.getAnswer()).append("__").append(fh.getQuestion()).append("\n");
            }
            pw.println(sb);

        } catch (Exception e) {
            pw.println("ERROR");
        }
    }

    private static void SwitchQuestion(User user, String body, PrintWriter pw) {
        try {

            HelpUsedByContestantInSet help = new HelpUsedByContestantInSet(user.getCurrentSet(), user.getCurrentQuestionInSet(), user.getUsername(), HELP_CHANGE_QUESTION);
            if (HelpUsedByContestantInSetService.useHelp(help)) {
                Question question = QuestionSetService.getQuestion(user.getCurrentSet(), 11);
                if (question == null) {
                    pw.println("NO QUESTION");
                    return;
                }

                StringBuilder sb = new StringBuilder("");
                sb.append(question.getQuestion()).append("\n");
                for (String s : question.getAnswers()) {
                    sb.append(s).append("\n");
                }
                pw.println(sb);

            } else {
                pw.println("CAN NOT USE THIS HELP AT THIS MOMENT!");
            }

        } catch (Exception e) {
            pw.println("ERROR");
        }

    }

    private static void HalfHalfQuestion(User user, String body, PrintWriter pw) {

        try {

            HelpUsedByContestantInSet help = new HelpUsedByContestantInSet(user.getCurrentSet(), user.getCurrentQuestionInSet(), user.getUsername(), HELP_HALF_HALF);
            if (HelpUsedByContestantInSetService.useHelp(help)) {
                StringBuilder sb = new StringBuilder("");
                Question question = QuestionSetService.getQuestion(user.getCurrentSet(), user.getCurrentQuestionInSet());
                if (question == null) {
                    pw.println("NO QUESTION");
                    return;
                }
                for (String s : question.getWrongAnswers()) {
                    sb.append(s).append("\n");
                }
                pw.println(sb);

            } else {
                pw.println("CAN NOT USE THIS HELP AT THIS MOMENT!");
            }

        } catch (Exception e) {
            pw.println("ERROR");
        }
    }

    private static void GetAllUsers(User user, String body, PrintWriter pw) {
        StringBuilder sb = new StringBuilder("");
        for (User u : UserService.getUsers()) {
            sb.append(u.getUsername()).append(":").append(u.getTypeOfUserToString()).append("\n");

        }
        pw.println(sb.toString());
    }

    private static void GetActiveSet(User user, String body, PrintWriter pw) {
        StringBuilder sb = new StringBuilder("");
        for (int u : ActiveSetService.getActiveSets()) {
            sb.append(u).append("\n");
        }
        pw.println(sb.toString());

    }

}
