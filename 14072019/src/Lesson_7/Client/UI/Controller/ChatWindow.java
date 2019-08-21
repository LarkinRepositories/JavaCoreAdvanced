package Lesson_7.Client.UI.Controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class ChatWindow implements Initializable {
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private final String IP_ADDRESS = "localhost";
    private final int PORT = 8189;
    private boolean isAuthorized;
    private static String nickname;

    @FXML
    private TextArea inputMessageArea;
    @FXML
    private TextFlow emojiList;
    @FXML
    private TextArea messageArea;
    @FXML
    private Button logoutButton;
    @FXML
    private VBox chatBox;


    public void connect() {
        try {
            socket = new Socket(IP_ADDRESS, PORT);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
                        while (true) {
                            String str = in.readUTF();
                            //messageArea.appendText(str +"\n");
                            //Label label = new Label(str + "\n");
                            Label label;
                            VBox vBox = new VBox();
                            String[] tokens = str.split(" ");
                            if (tokens[0].substring(0, tokens[0].length()-1).equalsIgnoreCase(nickname)) {
                                vBox.setAlignment(Pos.TOP_RIGHT);
                                label = new Label(tokens[1]+ "\n");
                            }
                            else {
                                vBox.setAlignment(Pos.TOP_LEFT);
                                label = new Label(str + "\n");
                            }
                            vBox.getChildren().add(label);
                            Platform.runLater(() -> chatBox.getChildren().add(vBox));
                            if (str.equals("/serverClosed")) {
                                //setAuthorized(false);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }



    @FXML
    void emojiAction(ActionEvent event) {
        if(emojiList.isVisible()){

            emojiList.setVisible(false);
        }else {
            emojiList.setVisible(true);
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        connect();
        for (Node text: emojiList.getChildren()) {
            text.setOnMouseClicked(event -> {
            inputMessageArea.setText(inputMessageArea.getText()+" "+((Text)text).getText());
            emojiList.setVisible(false);
            });
        }
    }
    @FXML
    void sendMsg(ActionEvent e) {
        //messageArea.appendText(inputMessageArea.getText()+"\n");
        try {
            out.writeUTF(inputMessageArea.getText());
            inputMessageArea.clear();
            inputMessageArea.requestFocus();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    @FXML
    void logout(ActionEvent e) {
        ((Stage)logoutButton.getScene().getWindow()).close();
    }
}
