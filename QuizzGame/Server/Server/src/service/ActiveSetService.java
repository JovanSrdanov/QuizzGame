package service;

import java.util.List;
import repository.ActiveSetRepository;

public class ActiveSetService {

    public static void SetActiveSet(int set) {
        if (ActiveSetRepository.getActiveSets().contains(set)) {
            return;
        }
        ActiveSetRepository.setActiveSet(set);
    }

    public static List<Integer> getActiveSets() {
        return ActiveSetRepository.getActiveSets();
    }

    public static Integer getActiveSet() {
        return ActiveSetRepository.getActiveSet();

    }
}
