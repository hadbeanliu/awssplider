package com.splider.crowler;

import com.splider.rule.UrlCrawlRule;
import com.splider.utils.PropertiesMgr;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CrawlTaskExecutor{
    private int nThreads;
    private static CrawlTaskExecutor executor;
    private ExecutorService threadPool = Executors.newFixedThreadPool(3);

    private static List<UrlCrawlRule> his=new Vector<UrlCrawlRule>();
    private static List<UrlCrawlRule> fail=new Vector<UrlCrawlRule>();
    private static Map<String,Integer> crawHis=new Hashtable<String, Integer>();
    private static HistoryManager hismgr= HistoryManager.getInstance(null);
//    private PropertiesMgr prop=new PropertiesMgr();



    public static CrawlTaskExecutor getInstance(){
        if(executor==null)
            executor=new CrawlTaskExecutor();
        return executor;
    }


    public void execute(Worker task){
        if(hismgr.hasCrawl(task.getRule().getUrl())){
            System.out.println("be slate");
            return ;
        }
        threadPool.execute(task);
    }
    public void shutDown(){
        threadPool.shutdown();
        while(true){
            System.out.println(threadPool.isTerminated());
            if(threadPool.isTerminated()){
                break;
            }
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
