package database;

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

public class UserDataBase {

    public static ArrayList<User> Users = new ArrayList<>();
    private static final String FILE_NAME = "src/resources/users.txt";

    public static User FindUser(String username) {

        for (User user : UserDataBase.Users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }

        return null;
    }

    public static void LoadUsers() {
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
            Logger.getLogger(UserDataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void SaveUsers() {
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
            Logger.getLogger(UserDataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static boolean AddUser(User user) {
        String password = user.getPassword();
        String username = user.getUsername();
        if (username.matches("^\\d.*") || !username.matches("^[a-zA-Z0-9]*$")) {
            return false;
        }

        if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{6,}$")) {
            return false;
        }

        for (User existingUser : Users) {
            if (existingUser.getUsername().equals(username)) {
                return false;
            }
        }

        Users.add(user);
        SaveUsers();
        LoadUsers();

        return true;
    }

    public static void DeleteByUsername(String username) {
        for (User user : Users) {
            if (user.getUsername().equals(username)) {
                Users.remove(user);
                break;
            }
        }
        SaveUsers();
        LoadUsers();

    }

}
