package service;

import java.util.ArrayList;
import java.util.Collections;
import model.User;
import repository.UserRepository;

public class UserService {

    public static User FindUserByUsernamePasswordAndRole(String username, String password, User.TypeOfUser typeOfUser) {
        return UserRepository.findUserByUsernamePasswordAndRole(username, password, typeOfUser);
    }

    public static void DeleteByUsername(String username) {
        UserRepository.deleteByUsername(username);
    }

    public static boolean AddUser(User user) {

        if (user.getUsername().matches("^\\d.*") || !user.getUsername().matches("^[a-zA-Z0-9]*$")) {
            return false;
        }
        if (!user.getPassword().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{6,}$")) {
            return false;
        }
        if (UserRepository.findUserByUsernamePasswordAndRole(user.getUsername(), user.getPassword(), user.getTypeOfUser()) != null) {
            return false;
        }
        UserRepository.addUser(user);
        return true;
    }

    public static Iterable<User> GetUsersForTableResults() {
        ArrayList<User> users = new ArrayList<>(UserRepository.getUsers());
        Collections.sort(users, (User user1, User user2) -> Integer.compare(user2.getCorrectAnsweredQuestions(), user1.getCorrectAnsweredQuestions()));
        return users;
    }

    public static Iterable<User> getUsers() {
        return UserRepository.getUsers();
    }

    public static void updateUser(User user) {
        UserRepository.updateUser(user);
    }

    public static User FindUser(String username) {
        return UserRepository.FindUser(username);
    }

}
