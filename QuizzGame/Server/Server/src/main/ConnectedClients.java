package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectedClients implements Runnable {

    private Socket socket;
    private BufferedReader br;
    private PrintWriter pw;
    private ArrayList<ConnectedClients> allClients;

    public ConnectedClients(Socket socket, ArrayList<ConnectedClients> allClients) {
        this.socket = socket;
        this.allClients = allClients;
        try {
            this.br = new BufferedReader(new InputStreamReader(this.socket.getInputStream(), "UTF-8"));
            this.pw = new PrintWriter(new OutputStreamWriter(this.socket.getOutputStream()), true);
        } catch (IOException ex) {
            Logger.getLogger(ConnectedClients.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        try {
            StringBuilder messageBuilder = new StringBuilder();
            while (true) {
                int character = this.br.read();
                if (character == -1) {
                    break;
                }
                messageBuilder.append((char) character);
                if (messageBuilder.toString().endsWith("\r\n")) {
                    String message = messageBuilder.substring(0, messageBuilder.length() - 2);
                    Controller.handleRequest(message, this.pw);
                    messageBuilder.setLength(0);
                }
            }
        } catch (IOException ex) {
            System.out.println("CLIENT DISCONNECTED");
        } finally {
            try {

                if (br != null) {
                    br.close();
                }
                if (pw != null) {
                    pw.close();
                }
                if (socket != null) {
                    socket.close();
                }

                allClients.remove(this);
            } catch (IOException ex) {
                Logger.getLogger(ConnectedClients.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
