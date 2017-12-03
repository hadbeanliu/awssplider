package com.splider.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CrawlerController implements Initializable{

    @FXML
    private Button startCrawler;
    @FXML
    private TextField url;
    @FXML
    private RadioButton isAll;

    @FXML
    public void startCrawlerAction(ActionEvent event){
        System.out.println("coming here!!!!");
        startCrawler.setText("fucker");
        System.out.println(url.getText());
        System.out.println(isAll.isSelected());
//        Stage taskMenu=new Stage();
//        try {
//            Parent root = FXMLLoader.load(getClass().getResource("/TaskMenu.fxml"));
//            Scene scene =new Scene(root,600,450);
//            taskMenu.setScene(scene);
//            taskMenu.show();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


    }

    public void initialize(URL location, ResourceBundle resources) {

    }
}
