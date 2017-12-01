package com.splider.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class CrawlerController implements Initializable{

    @FXML
    private Button startCrawler;
    @FXML
    private Text urlText;

    @FXML
    public void startCrawlerAction(ActionEvent event){
        System.out.println("coming here!!!!");
        startCrawler.setText("fucker");
    }

    public void initialize(URL location, ResourceBundle resources) {

    }
}
