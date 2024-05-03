package main;

import database.ActiveSetDataBase;
import database.FriendHelpDataBase;
import database.HelpUsedByContestantInSetDataBase;
import database.QuestionSetDataBase;
import database.UserDataBase;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerMain {

    private ServerSocket socket;
    private int port;
    private ArrayList<ConnectedClients> clients;

    public ServerSocket getSsocket() {
        return socket;
    }

    public void setSsocket(ServerSocket ssocket) {
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
                Logger.getLogger(ServerMain.class.getName()).log(Level.SEVERE, null, ex);
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

    public ServerMain(int port) {
        this.clients = new ArrayList<>();
        try {
            this.port = port;
            this.socket = new ServerSocket(port);
        } catch (IOException ex) {
            Logger.getLogger(ServerMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        System.out.println("SERVER STARTED");
        LoadData();
        ServerMain server = new ServerMain(6001);
        server.acceptClients();
    }

    private static void LoadData() {
        QuestionSetDataBase.LoadQuestionSets();
        UserDataBase.LoadUsers();
        ActiveSetDataBase.LoadActiveSets();
        HelpUsedByContestantInSetDataBase.LoadHelpsUsedByContestantsInSets();
        FriendHelpDataBase.LoadFriendHelps();
    }

}
