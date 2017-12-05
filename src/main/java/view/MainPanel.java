package view;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.stage.WindowEvent;
import view.controller.CrawlerController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainPanel extends Application {
    public void start(Stage stage) throws Exception {
        stage.setTitle("crawler");

        Parent root = FXMLLoader.load(getClass().getResource("/main.fxml"));
        Scene scene =new Scene(root,600,450);
        stage.setScene(scene);
        CrawlerController controller=new CrawlerController();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Platform.exit();
                System.exit(0);
            }
        });
        stage.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}
