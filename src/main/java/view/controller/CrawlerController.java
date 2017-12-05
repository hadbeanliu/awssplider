package view.controller;

import com.gargoylesoftware.htmlunit.Page;
import com.splider.rule.CrawlType;
import com.splider.rule.UrlCrawlRule;
import com.splider.store.PageCount;
import com.splider.utils.HtmlUtils;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.css.SimpleStyleableObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class CrawlerController implements Initializable{

    private PageCount count=PageCount.getCount();

    @FXML
    private Button startCrawler;
    @FXML
    private TextField url;
    @FXML
    private RadioButton isAll;
    @FXML
    private ProgressBar progress;
    @FXML
    private Label success;
    @FXML
    private Label all;
    Random r=new Random();
    @FXML
    public void startCrawlerAction(ActionEvent event){
        System.out.println("coming here!!!!");
        System.out.println(url.getText());
        System.out.println(isAll.isSelected());
        progress.setProgress(0.5);
//        startCrawler.setDisable(true);
        startCrawler.setText("...");
//        this.progress.setProgress(r.nextDouble());
//        success.setText(r.nextInt(10) + "");
//        all.setText(r.nextInt(50)+"");
//        count.addAll(10);
        Platform.runLater(() -> {
            UrlCrawlRule rule=UrlCrawlRule.build(url.getText(),isAll.isSelected()? CrawlType.DETAIL:CrawlType.FLIP);
            HtmlUtils.getInstance().startCrawl(rule);
//            count.addAll(10);

        });
        //https://store.shopping.yahoo.co.jp/allhqfashion/a5dba1bca5.html
//        Platform.runLater(() -> {
//            while(true) {
//                success.setText(count.getSuccessNum() + "");
//                all.setText(count.getAll()+"");
//                double rate = count.getAll()==0? 0:(count.getSuccessNum() * 1.0 / count.getAll());
//                System.out.println(count.getSuccessNum()+":" + rate);
//                progress.setProgress(1);
//                if(rate == 1)
//                    break;
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        });

//        System.out.println("---------------????----------");
    }

    public void refresh(ActionEvent event){

        success.setText(count.getSuccessNum() + "");
        all.setText(count.getAll()+"");
        double rate = count.getAll()==0? 0:(count.getSuccessNum() * 1.0 / count.getAll());
        System.out.println(count.getSuccessNum()+":" + rate);
        progress.setProgress(rate);
    }

    public ProgressBar getShedule() {
        return progress;
    }

    public void setShedule(ProgressBar progress) {
        this.progress = progress;
    }

    public Label getSuccess() {
        return success;
    }

    public void setSuccess(Label success) {
        this.success = success;
    }

    public Label getAll() {
        return all;
    }

    public void setAll(Label all) {
        this.all = all;
    }

    public void initialize(URL location, ResourceBundle resources) {

    }
}
