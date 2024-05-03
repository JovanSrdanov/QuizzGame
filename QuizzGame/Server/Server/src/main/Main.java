package main;

import repository.ActiveSetRepository;
import repository.FriendHelpRepository;
import repository.HelpUsedByContestantInSetRepository;
import repository.QuestionSetRepository;
import repository.UserRepository;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    private ServerSocket socket;
    private int port;
    private ArrayList<ConnectedClients> clients;

    public ServerSocket getSocket() {
        return socket;
    }

    public void setSocket(ServerSocket ssocket) {
        this.socket = ssocket;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void acceptClients() {
        Socket client = null;
        Thread thr;
        while (true) {
            try {
                client = this.socket.accept();
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (client != null) {

                ConnectedClients clnt = new ConnectedClients(client, clients);
                clients.add(clnt);
                thr = new Thread(clnt);
                thr.start();
            } else {
                break;
            }
        }
    }

    public Main(int port) {
        this.clients = new ArrayList<>();
        try {
            this.port = port;
            this.socket = new ServerSocket(port);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        System.out.println("SERVER STARTED");
        loadData();
        Main server = new Main(6001);
        server.acceptClients();
    }

    private static void loadData() {
        QuestionSetRepository.loadQuestionSets();
        UserRepository.loadUsers();
        ActiveSetRepository.loadActiveSets();
        HelpUsedByContestantInSetRepository.loadHelpsUsedByContestantsInSets();
        FriendHelpRepository.loadFriendHelps();
    }

}
