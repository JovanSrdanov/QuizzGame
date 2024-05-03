package repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.FriendHelp;

public class FriendHelpRepository {

    public static ArrayList<FriendHelp> FriendHelps = new ArrayList<>();
    private static final String FILE_NAME = "src/resources/friendHelps.json";

    public static void LoadFriendHelps() {
        FriendHelps.clear();

        try (FileReader reader = new FileReader(FILE_NAME)) {
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;
            List<Map<String, String>> friendHelpList = new ArrayList<>();

            while ((line = bufferedReader.readLine()) != null) {
                if (line.isEmpty() || line.startsWith("//")) {
                    continue; // Skip empty lines and comments
                }
                Map<String, String> friendHelpMap = parseLine(line);
                friendHelpList.add(friendHelpMap);
            }

            for (Map<String, String> friendHelpMap : friendHelpList) {
                String whoAskedForHelp = friendHelpMap.get("WhoAskedForHelp");
                String answerGiver = friendHelpMap.get("AnswerGiver");
                String answer = friendHelpMap.get("Answer");
                String question = friendHelpMap.get("Question");

                FriendHelps.add(new FriendHelp(whoAskedForHelp, answerGiver, answer, question));
            }
        } catch (IOException e) {
            // Handle potential IOException and create an empty file if it doesn't exist
            if (e instanceof FileNotFoundException) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
                    writer.write(""); // Create an empty file
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            } else {
                e.printStackTrace();
            }
        }
    }

    public static void SaveFriendHelps() {
        List<Map<String, String>> friendHelpList = new ArrayList<>();

        for (FriendHelp friendHelp : FriendHelps) {
            Map<String, String> friendHelpMap = new HashMap<>();
            friendHelpMap.put("WhoAskedForHelp", friendHelp.getWhoAskedForHelp());
            friendHelpMap.put("AnswerGiver", friendHelp.getAnswerGiver());
            friendHelpMap.put("Answer", friendHelp.getAnswer());
            friendHelpMap.put("Question", friendHelp.getQuestion());

            friendHelpList.add(friendHelpMap);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Map<String, String> friendHelpMap : friendHelpList) {
                writer.write(toJsonString(friendHelpMap, 2) + "\n"); // Indent with 2 spaces
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Map<String, String> parseLine(String line) {
        Map<String, String> friendHelpMap = new HashMap<>();
        String[] keyValuePairs = line.split(",");
        for (String keyValuePair : keyValuePairs) {
            String[] keyValue = keyValuePair.trim().split(":");
            friendHelpMap.put(keyValue[0].trim().replace("\"", ""), keyValue[1].trim().replace("\"", ""));
        }
        return friendHelpMap;
    }

    private static String toJsonString(Map<String, String> friendHelpMap, int indentLevel) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : friendHelpMap.entrySet()) {
            sb.append(" ".repeat(indentLevel)).append("\"").append(entry.getKey()).append("\": \"").append(entry.getValue()).append("\",\n");
        }
        if (sb.length() > 1) {
            sb.deleteCharAt(sb.length() - 2); // Remove trailing comma and newline
        }
        return sb.toString();
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
