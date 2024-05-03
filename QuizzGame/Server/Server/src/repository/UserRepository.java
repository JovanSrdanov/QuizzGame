package repository;

import encription.Encription;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.User;

public class UserRepository {

    private static final ArrayList<User> Users = new ArrayList<>();
    private static final String FILE_NAME = "src/resources/users.txt";

    public static User FindUser(String username) {

        for (User user : UserRepository.Users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }

        return null;
    }

    public static ArrayList<User> getUsers() {
        return Users;
    }

    public static void loadUsers() {
        try {
            Encription encription = new Encription();
            Users.clear();
            try (DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(FILE_NAME)))) {
                byte[] encryptedData = new byte[dis.available()];
                dis.readFully(encryptedData);
                String decryptedData = encription.do_AESDecryption(encryptedData);
                String[] allUsersData = decryptedData.split("\n");
                for (String userData : allUsersData) {
                    String[] splitUserData = userData.split(":");
                    Users.add(new User(splitUserData[0], splitUserData[1], splitUserData[2].trim(),
                            Integer.parseInt(splitUserData[3].trim()), Integer.parseInt(splitUserData[4].trim()),
                            Integer.parseInt(splitUserData[5].trim()), Integer.parseInt(splitUserData[6].trim())));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception ex) {
            Logger.getLogger(UserRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void saveUsers() {
        try {
            Encription encription = new Encription();
            StringBuilder stringBuilder = new StringBuilder();
            for (User user : Users) {
                stringBuilder.append(user.getUsername()).append(":").append(user.getPassword()).append(":").append(user.getTypeOfUser().equals(User.TypeOfUser.ADMIN) ? "admin" : "contestant").append(":");
                stringBuilder.append(user.getCorrectAnsweredQuestions()).append(":").append(user.getTotalAnsweredQuestions()).append(":").append(user.getCurrentQuestionInSet()).append(":").append(user.getCurrentSet());
                stringBuilder.append("\n");
            }
            String dataToEncrypt = stringBuilder.toString();
            byte[] encryptedData = encription.do_AESEncryption(dataToEncrypt);
            try (DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(FILE_NAME)))) {
                dos.write(encryptedData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception ex) {
            Logger.getLogger(UserRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void addUser(User user) {

        Users.add(user);
        saveUsers();
        loadUsers();
    }

    public static void deleteByUsername(String username) {
        for (User user : Users) {
            if (user.getUsername().equals(username)) {
                Users.remove(user);
                break;
            }
        }
        saveUsers();
        loadUsers();

    }

    public static void updateUser(User user) {
        for (User u : Users) {
            if (u.getUsername().equals(user.getUsername())) {
                Users.remove(u);
                Users.add(user);
                break;
            }
        }
        saveUsers();
        loadUsers();
    }

    public static User findUserByUsernamePasswordAndRole(String username, String password, User.TypeOfUser typeOfUser) {
        for (User user : UserRepository.Users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password) && user.getTypeOfUser() == typeOfUser) {

                return user;
            }
        }
        return null;
    }

}
