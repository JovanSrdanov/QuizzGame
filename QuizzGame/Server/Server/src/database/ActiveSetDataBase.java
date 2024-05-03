package database;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ActiveSetDataBase {

    public static int ActiveSet;
    private static final String FILE_NAME = "src/resources/activeSet.txt";

    public static List<Integer> LoadActiveSets() {

        List<Integer> activeSets = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                activeSets.add(Integer.valueOf(line.trim()));
            }
        } catch (FileNotFoundException e) {

            activeSets.add(1);
            SaveActiveSet(activeSets);

        } catch (IOException e) {
            e.printStackTrace();
        }
        ActiveSet = activeSets.get(activeSets.size() - 1);

        return activeSets;

    }

    private static void SaveActiveSet(List<Integer> activeSets) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (int activeSet : activeSets) {
                writer.write(Integer.toString(activeSet));
                writer.newLine();
            }
        } catch (IOException ex) {
            Logger.getLogger(UserDataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static boolean SetActiveSet(int set) {
        List<Integer> activeSets = LoadActiveSets();
        if (activeSets.contains(set)) {
            return false;
        }
        ActiveSet = set;
        activeSets.add(set);
        SaveActiveSet(activeSets);
        LoadActiveSets();
        return true;

    }
}
