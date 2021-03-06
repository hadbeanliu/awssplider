package view.controller;

import com.splider.crawler.CrawlTaskExecutor;
import com.splider.rule.CrawlType;
import com.splider.rule.UrlCrawlRule;
import com.splider.store.PageCount;
import com.splider.utils.HtmlUtils;
import com.splider.utils.PropertiesMgr;
import com.splider.utils.XLSOperater;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class CrawlerController implements Initializable{

    private PageCount count=PageCount.getCount();

    @FXML
    private Button startCrawler;
    @FXML
    private Button stopCrawler;
    @FXML
    private TextField url;
    @FXML
    private TextField prefix;
    @FXML
    private RadioButton isAll;
    @FXML
    private RadioButton download;
    @FXML
    private ProgressBar progress;
    @FXML
    private Label success;
    @FXML
    private Label all;

    @FXML
    public void startCrawlerAction(ActionEvent event){
        startCrawler.setText("抓取中...");
        startCrawler.setDisable(true);
        stopCrawler.setDisable(false);
        count.clear();
        if(download.isSelected()){
            PropertiesMgr.set("download.file","1");
        }else {
            PropertiesMgr.set("download.file","0");
        }
        if(prefix.getText()!=null){
            PropertiesMgr.set("prefix",prefix.getText());
        }
        Platform.runLater(() -> {
            UrlCrawlRule rule=UrlCrawlRule.build(url.getText(),isAll.isSelected()? CrawlType.FLIP:CrawlType.DETAIL);
            HtmlUtils.getInstance().startCrawl(rule);
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
    @FXML
    public void stopCrawlerAction(ActionEvent event){
        stopCrawler.setDisable(true);
        stopCrawler.setText(".....");
        CrawlTaskExecutor.getInstance().shutDownImmediately();
        XLSOperater.getXlsWrite().output(null,true);
        stopCrawler.setText("强制停止");
        startCrawler.setText("开始");
        startCrawler.setDisable(false);
    }
    public void refresh(ActionEvent event){

        success.setText(count.getSuccessNum() + "");
        all.setText(count.getAll()+"");
        double rate = count.getAll()==0? 0:(count.getSuccessNum() * 1.0 / count.getAll());
//        System.out.println(count.getSuccessNum()+":" + rate);
        progress.setProgress(rate);
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
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(1000),
                ae -> {
                    success.setText(count.getSuccessNum() + "");
                    all.setText(count.getAll()+"");
                    double rate = count.getAll()==0? 0:((count.getSuccessNum()+count.getFailNum()) * 1.0 / count.getAll());
                    progress.setProgress(rate);
                    if(rate >= 1){
                        startCrawler.setText("开始");
                        startCrawler.setDisable(false);
                    }
                }));
        timeline.setCycleCount(Integer.MAX_VALUE);
        timeline.play();
    }
}
