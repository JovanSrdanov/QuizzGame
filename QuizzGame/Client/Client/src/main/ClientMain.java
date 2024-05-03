package main;

import com.formdev.flatlaf.FlatDarkLaf;
import window.LoginWindow;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class ClientMain {

    private static Socket socket;
    private static BufferedReader br;
    public static PrintWriter pw;
    public static String authorizationToken = "EMPTY";

    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }

        System.out.println("CLIENT STARTED");
        try {
            socket = new Socket("127.0.0.1", 6001);
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Unable to connect to server.", "Connection Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        new LoginWindow().setVisible(true);
    }

    public static String HandleDataFromRequestAfterResponse(String request) {
        StringBuilder messageBuilder = new StringBuilder();
        try {
            RequestInterceptor(request);
            while (true) {
                int character = br.read();
                if (character == -1) {
                    break;
                }
                messageBuilder.append((char) character);
                if (messageBuilder.toString().endsWith("\r\n")) {
                    String message = messageBuilder.substring(0, messageBuilder.length() - 2); // Remove the "\r\n"
                    if (message.equals("USER NOT FOUND")) {
                        JOptionPane.showMessageDialog(null, "The user that logged in with this credentials no longer exists", "USER NOT FOUND", JOptionPane.ERROR_MESSAGE);
                        System.exit(1);
                    }
                    System.out.println("Message recieved:");
                    System.out.println(messageBuilder.toString().trim());
                    return messageBuilder.toString().trim();
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Unable to connect to server.", "Connection Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        return messageBuilder.toString();
    }

    private static void RequestInterceptor(String request) {
        System.out.println("Message sent:");
        System.out.println(request);
        pw.println(authorizationToken + "\n" + request);
    }

}
