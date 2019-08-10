package Lesson_7.Server;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Vector;

public class Server {

    private Vector<ClientHandler> clients;

    public Server() throws SQLException, ClassNotFoundException {
        clients = new Vector<>();
        ServerSocket server = null;
        Socket socket = null;

        try {
            AuthService.connect();
            //String test = AuthService.getNickNameByLoginAndPass("login1", "password1");
            //System.out.println(test);
            server = new ServerSocket(8189);
            System.out.println("Server is running!");
            while (true) {
                socket = server.accept();
                new ClientHandler(this, socket);
                //clients.add(new ClientHandler(this, socket));
                //System.out.println(clients +" connected!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
            }
            try {
                server.close();
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
            }
            AuthService.disconnect();
        }
    }
    public synchronized void broadcastMessage(String message) {
        for (ClientHandler client: clients) {
            client.sendMessage(message);
        }
    }

    public synchronized void whisper(ClientHandler donor, String acceptor, String message) {
        for (ClientHandler client:clients) {
            if (client.getNickname().equalsIgnoreCase(acceptor)) {
                client.sendMessage(String.format("%s whispers: %s", donor.getNickname(), message));
                donor.sendMessage(String.format("You whispered to %s: %s",acceptor, message));
                return;
            }
        }
        donor.sendMessage(String.format("Пользователя с ником %s нет в чате", acceptor));
    }


    public void subscribe(ClientHandler client) {
       clients.add(client);
    }

    public void unsubscribe(ClientHandler client) {
        clients.remove(client);
    }

    public boolean isNickUsed(String nickname) {
        for (ClientHandler client: clients) {
            if (client.getNickname().equalsIgnoreCase(nickname)) return true;
        }
        return false;
    }
}
