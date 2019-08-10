package Lesson_7.ClientGeekBrainsLogic;

import Lesson_7.Client.UI.Controller.ChatWindow;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
//        primaryStage.initStyle(StageStyle.TRANSPARENT);
//        Parent root = FXMLLoader.load(getClass().getResource("UI/Fxml/Login.fxml"));
//        primaryStage.setTitle("zChat pre alpha");
//        primaryStage.setScene(new Scene(root, 600, 400));
//        primaryStage.setResizable(false);
//        primaryStage.show();
        ChatWindow chatWindow = new ChatWindow();
        Parent root = FXMLLoader.load(getClass().getResource("UI/Fxml/ChatWindow.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> {
            System.exit(0);
        });
    }


    public static void main(String[] args) {
        launch(args);
    }
}
