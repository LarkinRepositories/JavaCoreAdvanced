package Lesson_7.ClientGeekBrainsLogic.UI.Controller;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Login {
    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginBtn;
    @FXML
    private Button closeBtn;
    @FXML
    Text authFailedText;


    private final String SERVER_ADDRESS = "localhost";
    private final int SERVER_PORT = 8189;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private boolean setAuthorized;

    public void connect() {
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            new Thread(new Runnable() {
                @Override
                public void run() {
                        try {
                            while(true) {
                                String str = in.readUTF();
                                if (str.startsWith("/authok")) {
                                    setAuthorized = true;
                                    break;
                                } else {
                                    authFailedText.setVisible(true);
                                }
                            }
                            while (true) {
                                String str = in.readUTF();
                                if (str.equals("/serverClosed")) {
                                    setAuthorized = false;

                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void login() {
        if (socket == null || socket.isClosed()) {
            connect();
        }
        String username = loginField.getText();
        String password = passwordField.getText();
        try {
            out.writeUTF("/auth "+username+" "+password);
            loginField.clear();
            passwordField.clear();
            //createMainChatWindow();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void createMainChatWindow() throws IOException {
        Stage stage = new Stage();
        stage.initStyle(StageStyle.TRANSPARENT);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../Fxml/ChatWindow.fxml"));
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.getScene().setFill(Color.TRANSPARENT);
        //ChatWindow chatWindow = fxmlLoader.getController();
        ((Stage) passwordField.getScene().getWindow()).close();
        ((Stage) loginBtn.getScene().getWindow()).close();
        stage.show();
    }
//    private void createMainChatWindow() throws IOException {
//        Stage stage = new Stage();
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ChatWindow.fxml"));
//        stage.setScene(new Scene(loader.load()));
//        stage.show();
//
//    }

    @FXML
    public void closeWindow() {
/*        try {
           socket.close();
            ((Stage)closeBtn.getScene().getWindow()).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
        ((Stage)closeBtn.getScene().getWindow()).close();
    }
}
