package cn.edu.newccnu;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import weibo4j.Timeline;
import weibo4j.Weibo;
import weibo4j.model.Paging;
import weibo4j.model.Status;
import weibo4j.model.StatusWapper;
import weibo4j.model.WeiboException;

/**
 * 下载用户的所有的微博信息，只需要微博的tid和转发次数即可
 * @author Liuxingyun
 *
 */
public class DownloadUserTweete { 
	
	static int number = 5;
	/**
	 * 获取某个用户发表的最新微博,根据id
	 * 只需要微博的id即可
	 * @param uid 需要获取微博的用户
	 * @param nickName  用户昵称
	 * @param tweetCount   用户的微博总数
	 * @param bw  写入文件的信息流
	 * @throws WeiboException
	 * @throws IOException 
	 */
	public void getTweet(String uid,String nickName,int tweetCount,BufferedWriter bw) throws WeiboException, IOException
	{
		
		int numPerPage = 100;        //每页获取的微博数
		int pageCount = tweetCount/numPerPage + 1;  //总页数
		Timeline timeline = new Timeline();
		StatusWapper sw;
		String isRetweet;
		List<Status> status = new ArrayList<Status>();
		for(int j = 1; j <= pageCount; j++)
		{
			System.out.println(uid+"用户共"+pageCount+"页正在进行"+"第"+j+"页下载.......");
			Paging page = new Paging(j);
			page.setCount(numPerPage);
			try {
					sw = timeline.getUserTimelineByUid(uid, page, 0, 0);
					status = sw.getStatuses();
					for(Status s : status)
					{
						//System.out.println(i+"--"+uid+"--"+s.getId()+"--"+s.getRepostsCount());
						if(s.getText().equals("此微博已被删除。"))  //微博删除则标记为deleted
						{
							bw.write(uid+",0,deleted");
						}
						if(s.getSource() == null)//微博不存在异常
						{
							bw.write(uid+",0,not exit");
						}
						else
						{
								if(s.getRetweetedStatus() == null)   //如果是null，那么就是原创的
									isRetweet = "false";
								else
									isRetweet = "true";
							bw.write(uid+",0,"+nickName+",0,"+s.getId()+",0,"+s.getText()+",0,"+s.getSource()+",0,"+s.getCreatedAt()+",0,"+s.isFavorited()+",0,"+s.getRepostsCount()+",0,"+s.getCommentsCount()+",0,"+isRetweet);//获取对应的微博id和转发次数
							//System.out.println(uid+"--"+s.getId()+"--"+s.getRepostsCount());
						}
						bw.newLine();
				}
			} catch (WeiboException e) {
				e.printStackTrace();
				//bw.close();
			}	
		}
	}
	

	/**
	 * 根据微博id，获取微博信息
	 * 只需要微博的id即可
	 * @param tid 需要获取微博的id
	 * @param bw  写入文件的信息流
	 * @throws WeiboException
	 * @throws IOException 
	 */
	public void getTweet(String uid,String nickName,String tid,BufferedWriter bw) throws WeiboException, IOException
	{
		
		Timeline timeline = new Timeline();
		Status status;
		String isRetweet;
		try {
				status = timeline.showStatus(tid);
						//System.out.println(i+"--"+uid+"--"+s.getId()+"--"+s.getRepostsCount());
				if(status.getText().equals("此微博已被删除。")){  //微博删除则标记为deleted	
						bw.write(uid+",0,deleted");
					}
					if(status.getSource() == null){          //微博不存在异常
						bw.write(uid+",0,not exit");
					}
					else{
						if(status.getRetweetedStatus() == null)   //如果是null，那么就是原创的
							isRetweet = "false";
						else
							isRetweet = "true";
						bw.write(uid+",0,"+nickName+",0,"+status.getId()+",0,"+status.getText()+",0,"+status.getSource()+",0,"+status.getCreatedAt()+",0,"+status.isFavorited()+",0,"+status.getRepostsCount()+",0,"+status.getCommentsCount()+",0,"+isRetweet);//获取对应的微博id和转发次数
							//System.out.println(uid+"--"+s.getId()+"--"+s.getRepostsCount());
					}
					bw.newLine();
			} catch (WeiboException e) {
				e.printStackTrace();
				//bw.close();
			}	
	}
	
	/**
	 * 获取用户的所有的微博tid和转发次数，自动执行
	 * 
	 * 因为每个token一个小时只有150次，而服务器一个小时有1000次，为了充分利用服务器资源
	 * 申请了七个应用，每个应用有一个token，在一个小时内循环调用不同的token，则一个小时
	 * 内可以获取980个请求的数据。同时将已经读取的文件的行删除更新到新的行，等待下一次重新
	 * 读取该文件的新数据信息
	 * @throws IOException
	 */
	public void getTweets() throws IOException, WeiboException
	{
		
		String retweetIdsFile = "DATA/uid-nickname.txt";  //已获取微博id和tweetcount的文件路径
		FileUtil fu = new FileUtil();
		String[] tokens = {                            //七个应用的七个token循环调用
				"2.007csZDDQ_jEAEf8f41c5f22rgQrSE",
				"2.007csZDDTBYz6E8b94fd05ab_vYNsD",
				"2.007csZDD0JbR_Sf426658806ZkS4AB",
				"2.007csZDDSiwMBE7c62be5471PV6dSC",
				"2.007csZDD0xzLPBbe936bc1e7JnJjXB",
				"2.007csZDDpRRC5E2fbd8919390QGRt5",
				"2.007csZDD0FVbra8499ba3e2fXIfiEC"
				}; 
		String path = "DATA/uid-nickname";  //保存获取到的数据的文件前缀
		String format = ".txt";
		BufferedWriter bw;
		String[] words;
		String filePath = path + number + format; //保存获取到的数据的文件路径
		bw = fu.writeFile(filePath);
		for(int i = 0; i < tokens.length; i++)  //循环调用token
		{  
			System.out.println(tokens[i]+":");
			Weibo weibo = new Weibo();
			weibo.setToken(tokens[i]);          //重设token
			Timeline timeline = new Timeline();
			List<String> lines = fu.getPartfromFile(retweetIdsFile, 10); //获取前N行的数据
			try{			
				for(String l:lines)
				{
					if(l.contains(",")){
						words = l.split(",");
						System.out.println(words[2]);
						//bw.write(words[2]);
						getTweet(words[1],words[2],Integer.parseInt(words[4]),bw);
					}                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      
				}
			}catch(Exception e)
			{
				e.getStackTrace();
				bw.close();
			}
			fu.deletePartfromFile(retweetIdsFile, lines);//删除已经读取过的行，并更新文件
		}
		bw.close();
	}

	/**
	 *设置定时器，每隔一个小时执行一次程序 ，一天执行23次
	 *执行一次生成一个文件
	 */
	public  void operateOnTime() 
	{ 
	       TimerTask task = new TimerTask() 
	       {
	    	   int count = 1;
	           public void run() 
	           {
	        	   System.out.println("第"+count+"个小时执行");
		            if(count>8)
		            	 System.exit(0);
		            try 
		            {
		            	System.out.println("开始第"+number+"个文件下载------");
		            	getTweets();
						System.out.println("结束第"+number+"个文件下载!");
						number++;
						
					} catch (IOException e) 
					{
						e.printStackTrace();
					} catch (WeiboException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		            count++;
	 	       }
	       };
	       Timer timer = new Timer();
	       // 设值 5 秒钟后开始执行第一次，以后每隔 一个小时执行一次
	       timer.schedule(task, 1 * 1000, 3650  * 1000);
	} 
	public static void main(String[] args) throws WeiboException, IOException
	{
		DownloadUserTweete getTweet = new DownloadUserTweete();
		getTweet.operateOnTime();
		
//		Weibo weibo = new Weibo();
//		weibo.setToken("2.00DdJ8lB0FVbrafafc4e5bf0xkPSZC");
//		BufferedWriter bw = new FileUtil().writeFile("DATA/newdownload/uid-tid-retweetcount2222.txt");
//		getTweet.getTweet("1617092497", "都叫我老莫", 175, bw);
//		bw.close();

	}

	
}
