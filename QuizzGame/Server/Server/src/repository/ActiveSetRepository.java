package repository;

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

public class ActiveSetRepository {

    private static List<Integer> ActiveSets = new ArrayList<>();
    private static final String FILE_NAME = "src/resources/activeSet.txt";

    public static List<Integer> getActiveSets() {
        return ActiveSets;
    }

    public static Integer getActiveSet() {
        if (ActiveSets.isEmpty()) {
            return 0;
        } else {
            return ActiveSets.get(ActiveSets.size() - 1);
        }
    }

    public static void LoadActiveSets() {

        List<Integer> activeSets = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                activeSets.add(Integer.valueOf(line.trim()));
            }
        } catch (FileNotFoundException e) {
            activeSets.add(1);
            SaveActiveSet();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ActiveSets = activeSets;

    }

    private static void SaveActiveSet() {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (int activeSet : ActiveSets) {
                writer.write(Integer.toString(activeSet));
                writer.newLine();
            }
        } catch (IOException ex) {
            Logger.getLogger(UserRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void SetActiveSet(int set) {
        ActiveSets.add(set);
        SaveActiveSet();
        LoadActiveSets();
    }
}
