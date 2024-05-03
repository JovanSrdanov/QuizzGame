package database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import model.FriendHelp;

public class FriendHelpDataBase {

    public static ArrayList<FriendHelp> FriendHelps = new ArrayList<>();
    private static final String FILE_NAME = "src/resources/friendHelps.txt";

    public static void LoadFriendHelps() {
        FriendHelps.clear();
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.err.println("Error creating file: " + e.getMessage());
            }
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String whoAskedForHelp = line;
                String answerGiver = reader.readLine();
                String answer = reader.readLine();
                String question = reader.readLine();
                reader.readLine(); // Consume empty line
                FriendHelps.add(new FriendHelp(whoAskedForHelp, answerGiver, answer, question));
            }
        } catch (IOException e) {
            System.err.println("Error loading FriendHelps: " + e.getMessage());
        }
    }

    public static void SaveFriendHelps() {

        try (FileWriter writer = new FileWriter(FILE_NAME)) {
            for (FriendHelp help : FriendHelps) {
                writer.write(help.getWhoAskedForHelp() + "\n");
                writer.write(help.getAnswerGiver() + "\n");
                writer.write(help.getAnswer() + "\n");
                writer.write(help.getQuestion() + "\n");
                writer.write("\n");
            }
        } catch (IOException e) {
            System.err.println("Error saving FriendHelps: " + e.getMessage());
        }

    }

    public static ArrayList<FriendHelp> getHelpAskedByContestant(String username) {
        ArrayList<FriendHelp> friendHelps = new ArrayList<>();
        for (FriendHelp fh : FriendHelps) {
            if (fh.getWhoAskedForHelp().equals(username) && !fh.getAnswer().equals("EMPTY")) {
                friendHelps.add(fh);
            }
        }
        return friendHelps;

    }

    public static ArrayList<FriendHelp> getUnAnsweredQuestions(String username) {
        ArrayList<FriendHelp> friendHelps = new ArrayList<>();
        for (FriendHelp fh : FriendHelps) {
            if (fh.getAnswerGiver().equals(username) && fh.getAnswer().equals("EMPTY")) {
                friendHelps.add(fh);
            }
        }
        return friendHelps;

    }

    public static void AddFriendHelp(FriendHelp friendHelp) {
        boolean duplicateExists = false;
        for (FriendHelp existingHelp : FriendHelps) {
            if (existingHelp.getWhoAskedForHelp().equals(friendHelp.getWhoAskedForHelp())
                    && existingHelp.getAnswerGiver().equals(friendHelp.getAnswerGiver())
                    && existingHelp.getQuestion().equals(friendHelp.getQuestion())) {
                duplicateExists = true;
                break;
            }
        }

        if (!duplicateExists) {
            FriendHelps.add(friendHelp);
            SaveFriendHelps();
            LoadFriendHelps();
        }
    }

    public static FriendHelp FindQuestionToAnswer(String username, String whoAsked, String question) {
        for (FriendHelp existingHelp : FriendHelps) {
            if (existingHelp.getWhoAskedForHelp().equals(whoAsked)
                    && existingHelp.getAnswerGiver().equals(username)
                    && existingHelp.getQuestion().equals(question)) {
                return existingHelp;
            }
        }
        return null;
    }

}
