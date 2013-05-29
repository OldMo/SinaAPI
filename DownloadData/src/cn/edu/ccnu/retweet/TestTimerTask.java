package cn.edu.ccnu.retweet;

import java.util.Timer;
import java.util.TimerTask;


public class TestTimerTask extends TimerTask {
	
    /**
     * 此计时器任务要执行的操作。
     */
    public void run() 
    {
 
            System.out.println("本次任务执行的时间是");
    }

    public static void main(String[] args) 
    {
          Timer timer = new Timer();
          TimerTask task = new TestTimerTask();
          timer.schedule(task, 1000, 1000);
     }
}

