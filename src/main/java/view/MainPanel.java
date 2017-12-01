package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainPanel extends Application {
    public void start(Stage stage) throws Exception {
        stage.setTitle("crawler");

        Parent root = FXMLLoader.load(getClass().getResource("/main.fxml"));
        Scene scene =new Scene(root,300,200);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}
