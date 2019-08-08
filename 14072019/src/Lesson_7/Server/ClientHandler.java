package Lesson_7.Server;

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
                            nickname = AuthService.getNickNameByLoginAndPass(tokens[1], tokens[2]);
                            if (nickname != null)  {
                                if (!server.isNickUsed(nickname)) {
                                    sendMessage("/authok");
                                    server.subscribe(ClientHandler.this);
                                    System.out.println(nickname + " connected");
                                    server.broadcastMessage(nickname + " joined the conversation");
                                    break;
                                } else {
                                    sendMessage("/inUse");
                                }
                            } else {
                                sendMessage("Incorrect login/password");
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
