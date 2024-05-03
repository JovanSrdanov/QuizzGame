package main;

import database.ActiveSetDataBase;
import database.FriendHelpDataBase;
import database.HelpUsedByContestantInSetDataBase;
import database.QuestionSetDataBase;
import database.UserDataBase;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.FriendHelp;
import model.HelpUsedByContestantInSet;
import model.Question;
import model.User;

public class ConnectedClients implements Runnable {

    private Socket socket;
    private BufferedReader br;
    private PrintWriter pw;
    private ArrayList<ConnectedClients> allClients;

    public ConnectedClients(Socket socket, ArrayList<ConnectedClients> allClients) {
        this.socket = socket;
        this.allClients = allClients;
        try {

            this.br = new BufferedReader(new InputStreamReader(this.socket.getInputStream(), "UTF-8"));
            this.pw = new PrintWriter(new OutputStreamWriter(this.socket.getOutputStream()), true);
        } catch (IOException ex) {
            Logger.getLogger(ConnectedClients.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        try {
            StringBuilder messageBuilder = new StringBuilder();
            while (true) {
                int character = this.br.read();
                if (character == -1) {
                    break;
                }
                messageBuilder.append((char) character);
                if (messageBuilder.toString().endsWith("\r\n")) {
                    String message = messageBuilder.substring(0, messageBuilder.length() - 2);
                    HandleRequest(message, this.pw);
                    messageBuilder.setLength(0);
                }
            }
        } catch (IOException ex) {
            System.out.println("CLIENT DISCONNECTED");
        } finally {
            try {

                if (br != null) {
                    br.close();
                }
                if (pw != null) {
                    pw.close();
                }
                if (socket != null) {
                    socket.close();
                }

                allClients.remove(this);
            } catch (IOException ex) {
                Logger.getLogger(ConnectedClients.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void HandleRequest(String message, PrintWriter pw) {
        try {
            String[] split = message.split("\n", 2);
            String method = split[0];
            String body = (split.length > 1) ? split[1].trim() : " ";

            if ("Login".equals(method)) {
                Login(body, pw);
                return;
            }
            if ("AddUser".equals(method)) {
                AddUser(body, pw);
                return;
            }
            if ("RemoveUser".equals(method)) {
                RemoveUser(body, pw);
                return;
            }
            if ("SetActiveSet".equals(method)) {
                SetActiveSet(body, pw);
                return;
            }
            if ("GetActiveSet".equals(method)) {
                GetActiveSet(body, pw);
                return;
            }
            if ("GetAllUsers".equals(method)) {
                GetAllUsers(body, pw);
                return;
            }
            if ("GetTableResults".equals(method)) {
                GetTableResults(body, pw);
                return;
            }
            if ("GetCurrentQuestion".equals(method)) {
                GetCurrentQuestion(body, pw);
                return;
            }
            if ("AnswerCurretnQuestion".equals(method)) {
                AnswerCurretnQuestion(body, pw);
                return;
            }
            if ("HelpFriend".equals(method)) {
                HelpFriend(body, pw);
                return;
            }
            if ("FriendsInNeedOfHelp".equals(method)) {
                FriendsInNeedOfHelp(body, pw);
                return;
            }
            if ("AskForHelpFromFriend".equals(method)) {
                AskForHelpFromFriend(body, pw);
                return;
            }
            if ("GetHelpFromFriends".equals(method)) {
                GetHelpFromFriends(body, pw);
                return;
            }
            if ("SwitchQuestion".equals(method)) {
                SwitchQuestion(body, pw);
                return;
            }
            if ("HalfHalfQuestion".equals(method)) {
                HalfHalfQuestion(body, pw);
                return;
            }
            pw.println("Unknown method");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void Login(String body, PrintWriter pw) {

        try {
            String[] loginInfo = body.split(":");
            String username = loginInfo[0];
            String password = loginInfo[1];
            User.TypeOfUser typeOfUser = loginInfo[2].equals("admin") ? User.TypeOfUser.ADMIN : User.TypeOfUser.CONTESTANT;

            boolean found = false;
            for (User user : UserDataBase.Users) {
                if (user.getUsername().equals(username) && user.getPassword().equals(password) && user.getTypeOfUser() == typeOfUser) {
                    found = true;
                    break;
                }
            }
            if (found) {
                pw.println(loginInfo[2] + ":" + loginInfo[0]);
            } else {
                pw.println("Failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void RemoveUser(String body, PrintWriter pw) {

        UserDataBase.DeleteByUsername(body);
        pw.println("Done");
    }

    private static void SetActiveSet(String body, PrintWriter pw) {
        ActiveSetDataBase.SetActiveSet(Integer.parseInt(body));
        pw.println("Done");
    }

    private static void AddUser(String body, PrintWriter pw) {
        try {
            String[] userInfo = body.split(":");
            String username = userInfo[0];
            String password = userInfo[1];
            String userType = userInfo[2];
            if (UserDataBase.AddUser(new User(username, password, userType, 0, 0, 1, 0))) {
                pw.println("SUCCESS");
            } else {
                pw.println("FAILED");
            }

        } catch (Exception e) {
            e.printStackTrace();
            pw.println("FAILED");
        }
    }

    private static void GetTableResults(String body, PrintWriter pw) {
        User user = UserDataBase.FindUser(body.trim());
        if (user == null) {
            pw.println("USER NOT FOUND");
            return;
        }

        StringBuilder sb = new StringBuilder("");

        Collections.sort(UserDataBase.Users, (User user1, User user2) -> Integer.compare(user2.getCorrectAnsweredQuestions(), user1.getCorrectAnsweredQuestions()));

        for (User u : UserDataBase.Users) {
            if (u.getTypeOfUser().equals(User.TypeOfUser.CONTESTANT)) {
                sb.append(u.getUsername()).append(":").append(u.getTypeOfUserToString()).append(":").append(u.getCorrectAnsweredQuestions()).append(":").append(u.getTotalAnsweredQuestions()).append(":").append(u.getCurrentQuestionInSet()).append(":").append(u.getCurrentSet()).append("\n");
            }

        }
        pw.println(sb.toString());
    }

    private static void GetCurrentQuestion(String body, PrintWriter pw) {

        User user = UserDataBase.FindUser(body);
        if (user == null) {
            pw.println("USER NOT FOUND");
            return;
        }
        int ActiveSet = ActiveSetDataBase.ActiveSet;
        if (ActiveSet != user.getCurrentSet()) {
            user.setCurrentSet(ActiveSet);
            user.setCurrentQuestionInSet(1);
        }
        UserDataBase.SaveUsers();

        Question question = QuestionSetDataBase.GetQuestion(user.getCurrentSet(), user.getCurrentQuestionInSet());
        if (question == null) {
            pw.println("NO QUESTION");
            return;
        }
        if (question.getQuestionNumber() == 11) {
            pw.println("NO QUESTION");
            return;
        }
        StringBuilder sb = new StringBuilder("");
        sb.append(question.getQuestion()).append("\n");
        for (String s : question.getAnswers()) {
            sb.append(s).append("\n");
        }
        pw.println(sb);

    }

    private static void AnswerCurretnQuestion(String body, PrintWriter pw) {
        try {

            User user = UserDataBase.FindUser(body.split("\n")[0]);
            if (user == null) {
                pw.println("USER NOT FOUND");
                return;
            }
            Question question = QuestionSetDataBase.getQuestionByQuestionText(body.split("\n")[1]);
            if (question == null) {
                pw.println("QUESTION NOT FOUND");
                return;
            }

            if (question.IsAnswerCorrect(body.split("\n")[2])) {
                user.setCorrectAnsweredQuestions(user.getCorrectAnsweredQuestions() + 1);
                pw.println("CORRECT");
            } else {
                pw.println("The correct answer was: " + question.getCorrectAnswer());
            }
            user.setCurrentQuestionInSet(user.getCurrentQuestionInSet() + 1);
            user.setTotalAnsweredQuestions(user.getTotalAnsweredQuestions() + 1);

            UserDataBase.SaveUsers();

        } catch (Exception e) {
            pw.println("ERROR");
        }
    }

    private static void HelpFriend(String body, PrintWriter pw) {
        try {
            User user = UserDataBase.FindUser(body.split("\n")[0]);
            if (user == null) {
                pw.println("USER NOT FOUND");
                return;
            }
            FriendHelp friendHelp = FriendHelpDataBase.FindQuestionToAnswer(user.getUsername(), body.split("\n")[1], body.split("\n")[2]);

            if (friendHelp == null) {
                pw.println("FriendHelp NOT FOUND");
                return;
            }
            friendHelp.setAnswer(body.split("\n")[3]);
            FriendHelpDataBase.SaveFriendHelps();

            pw.println("SUCCESS");

        } catch (Exception e) {
            pw.println("ERROR");
        }
    }

    private static void FriendsInNeedOfHelp(String body, PrintWriter pw) {
        try {
            User user = UserDataBase.FindUser(body);
            if (user == null) {
                pw.println("USER NOT FOUND");
                return;
            }

            StringBuilder sb = new StringBuilder("");
            for (FriendHelp fh : FriendHelpDataBase.getUnAnsweredQuestions(user.getUsername())) {
                sb.append(fh.getWhoAskedForHelp()).append("__").append(fh.getAnswerGiver()).append("__").append(fh.getAnswer()).append("__").append(fh.getQuestion()).append("\n");
            }
            pw.println(sb);

        } catch (Exception e) {
            pw.println("ERROR");
        }
    }

    private static void AskForHelpFromFriend(String body, PrintWriter pw) {
        try {

            User user = UserDataBase.FindUser(body.split(":")[0]);
            if (user == null) {
                pw.println("USER NOT FOUND");
                return;
            }
            User friend = UserDataBase.FindUser(body.split(":")[1]);
            if (friend == null) {
                pw.println("FRIEND NOT FOUND");
                return;
            }
            HelpUsedByContestantInSet help = new HelpUsedByContestantInSet(user.getCurrentSet(), user.getCurrentQuestionInSet(), user.getUsername(), HELP_ASK_FRIEND);
            if (HelpUsedByContestantInSetDataBase.UseHelp(help)) {

                Question question = QuestionSetDataBase.GetQuestion(user.getCurrentSet(), user.getCurrentQuestionInSet());
                if (question == null) {
                    pw.println("NO QUESTION");
                    return;
                }
                FriendHelpDataBase.AddFriendHelp(new FriendHelp(user.getUsername(), body.split(":")[1], "EMPTY", question.getQuestion()));
                pw.println("SUCCESS");

            } else {
                pw.println("CAN NOT USE THIS HELP AT THIS MOMENT!");
            }

        } catch (Exception e) {
            pw.println("ERROR");
        }
    }

    private static void GetHelpFromFriends(String body, PrintWriter pw) {

        try {
            User user = UserDataBase.FindUser(body);
            if (user == null) {
                pw.println("USER NOT FOUND");
                return;
            }

            StringBuilder sb = new StringBuilder("");
            for (FriendHelp fh : FriendHelpDataBase.getHelpAskedByContestant(user.getUsername())) {
                sb.append(fh.getWhoAskedForHelp()).append("__").append(fh.getAnswerGiver()).append("__").append(fh.getAnswer()).append("__").append(fh.getQuestion()).append("\n");
            }
            pw.println(sb);

        } catch (Exception e) {
            pw.println("ERROR");
        }
    }

    private static void SwitchQuestion(String body, PrintWriter pw) {
        try {

            User user = UserDataBase.FindUser(body);
            if (user == null) {
                pw.println("USER NOT FOUND");
                return;
            }
            HelpUsedByContestantInSet help = new HelpUsedByContestantInSet(user.getCurrentSet(), user.getCurrentQuestionInSet(), user.getUsername(), HELP_CHANGE_QUESTION);
            if (HelpUsedByContestantInSetDataBase.UseHelp(help)) {
                Question question = QuestionSetDataBase.GetQuestion(user.getCurrentSet(), 11);
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

    private static void HalfHalfQuestion(String body, PrintWriter pw) {

        try {

            User user = UserDataBase.FindUser(body);
            if (user == null) {
                pw.println("USER NOT FOUND");
                return;
            }
            HelpUsedByContestantInSet help = new HelpUsedByContestantInSet(user.getCurrentSet(), user.getCurrentQuestionInSet(), user.getUsername(), HELP_HALF_HALF);
            if (HelpUsedByContestantInSetDataBase.UseHelp(help)) {
                StringBuilder sb = new StringBuilder("");
                Question question = QuestionSetDataBase.GetQuestion(user.getCurrentSet(), user.getCurrentQuestionInSet());
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

    private static void GetAllUsers(String body, PrintWriter pw) {
        StringBuilder sb = new StringBuilder("");
        for (User u : UserDataBase.Users) {
            sb.append(u.getUsername()).append(":").append(u.getTypeOfUserToString()).append("\n");

        }
        pw.println(sb.toString());
    }

    private static void GetActiveSet(String body, PrintWriter pw) {
        StringBuilder sb = new StringBuilder("");
        for (int u : ActiveSetDataBase.LoadActiveSets()) {
            sb.append(u).append("\n");
        }
        pw.println(sb.toString());

    }

    static final String HELP_HALF_HALF = "help_half_half";
    static final String HELP_ASK_FRIEND = "help_ask_friend";
    static final String HELP_CHANGE_QUESTION = "help_change_question";

}
