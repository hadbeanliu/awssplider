package view;

import com.splider.controller.CrawlerController;
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
        stage.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}
