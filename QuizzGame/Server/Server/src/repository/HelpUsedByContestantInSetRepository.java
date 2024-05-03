package repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import model.HelpUsedByContestantInSet;

public class HelpUsedByContestantInSetRepository {

    public static ArrayList<HelpUsedByContestantInSet> HelpsUsedByContestantsInSets = new ArrayList<>();
    private static final String FILE_NAME = "src/resources/helpUsed.txt";

    public static void addHelpUsedByContestantInSet(HelpUsedByContestantInSet helpUsedByContestantInSet) {
        HelpsUsedByContestantsInSets.add(helpUsedByContestantInSet);
        saveHelpsUsedByContestantsInSets();
    }

    public static HelpUsedByContestantInSet hasUsedHelpUsedByContestantInSet(HelpUsedByContestantInSet helpUsedByContestantInSet) {
        for (HelpUsedByContestantInSet help : HelpsUsedByContestantsInSets) {
            if (help.getSet() == helpUsedByContestantInSet.getSet()
                    && help.getContestant().equals(helpUsedByContestantInSet.getContestant())
                    && help.getHelp().equals(helpUsedByContestantInSet.getHelp())) {
                return help;
            }
        }
        return null;
    }

    public static boolean useHelp(HelpUsedByContestantInSet helpUsedByContestantInSet) {
        HelpUsedByContestantInSet help = hasUsedHelpUsedByContestantInSet(helpUsedByContestantInSet);

        if (help == null) {
            HelpsUsedByContestantsInSets.add(helpUsedByContestantInSet);
            saveHelpsUsedByContestantsInSets();
            return true;
        }
        return help.getQuestion() == helpUsedByContestantInSet.getQuestion();
    }

    public static void loadHelpsUsedByContestantsInSets() {

        ArrayList<HelpUsedByContestantInSet> helpsUsedByContestantsInSets = new ArrayList<>();

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(FILE_NAME));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 4) {
                    int set = Integer.parseInt(parts[0]);
                    int question = Integer.parseInt(parts[1]);
                    String contestant = parts[2];
                    String help = parts[3];
                    helpsUsedByContestantsInSets.add(new HelpUsedByContestantInSet(set, question, contestant, help));
                } else {

                    System.err.println("Invalid line format: " + line);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + FILE_NAME);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        HelpsUsedByContestantsInSets = helpsUsedByContestantsInSets;
    }

    public static void saveHelpsUsedByContestantsInSets() {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(FILE_NAME));
            for (HelpUsedByContestantInSet help : HelpsUsedByContestantsInSets) {
                String line = help.getSet() + ":" + help.getQuestion() + ":" + help.getContestant() + ":" + help.getHelp();
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
