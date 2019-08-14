package Lesson_7.Server;

import Lesson_7.ClientGeekBrainsLogic.UI.Controller.ChatWindow;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;

public class ClientHandler {
    private Server server;
    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;
    private String nickname;
    private int userID;

    public ClientHandler(Server server, Socket socket) {
        try {
            this.socket = socket;
            this.server = server;
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
            new Thread(() -> {
                try {
                    while (true) {
                        String str = in.readUTF();
                        if (str.startsWith("/auth")) {
                            String[] tokens = str.split(" ");
                            userID = AuthService.getUserIDByLoginAndPass(tokens[1], tokens[2]);
                            AuthService.passToHashCode(userID, tokens[2]);
                            nickname = AuthService.getNickNameByLoginAndPass(tokens[1], tokens[2]);
                            if (nickname != null)  {
                                if (!server.isNicknameBusy(nickname)) {
                                    sendMessage("/authok " +nickname);
                                    server.subscribe(ClientHandler.this);
                                    System.out.println(nickname + " connected");
                                    server.broadcastMessage(nickname + " joined the conversation");
                                    break;
                                }  else {
                                    sendMessage(String.format("%s уже авторизован", nickname));
                                }
                            } else {
                                sendMessage("Неверный логин/пароль!");
                            }
                        }
                    }

                    while (true) {
                        String message = in.readUTF();
                        System.out.println(nickname+": " + message);
                        if (message.equals("/end")) {
                            out.writeUTF("/serverClosed");
                            break;
                        }
                        if (message.startsWith("/w ")){
                            String[] tokens = message.split(" ");
                            String nickname = tokens[1];
                            String msg = message.substring(4 + nickname.length());
                            server.whisper(this, nickname, msg);
                            continue;
                        }
                        if (message.startsWith("/ignore ")) {
                            String[] tokens = message.split(" ");
                            String nickname = tokens[1];
                            AuthService.blacklist(this.userID, nickname);
                            //sendMessage(nickname + " blacklisted");
                            continue;
                        }
                       server.broadcastMessage(nickname+": "+message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        in.close();
                        out.close();
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (nickname != null) {
                        server.unsubscribe(ClientHandler.this);
                        System.out.println(nickname + " disconnected");
                    }
                }
            }).start();
            } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public synchronized void sendMessage(String message) {
        try {
            out.writeUTF(message);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String getNickname() {
        return nickname;
    }
}
