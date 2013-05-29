package cn.edu.ccnu;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import weibo4j.Account;
import weibo4j.Timeline;
import weibo4j.Users;
import weibo4j.Weibo;
import weibo4j.model.Paging;
import weibo4j.model.Status;
import weibo4j.model.StatusWapper;
import weibo4j.model.User;
import weibo4j.model.WeiboException;

/*
 * 从.xml语料中获取被关注者的id，在无法评论的情况下，
 * 获取其三天时间内的微博转发数量，便于与能转发的情况作对比
 * 
 */
public class DownLoadRetweetCount {
	static int number = 1;
	List<String> allIds = new ArrayList<String>();
//	public DownLoadRetweetCount(String token)
//	{
//		Weibo weibo = new Weibo();
//		weibo.setToken(token);
//	}
	public void getUserId(String fromFile,String toFile) throws IOException
	{
		FileUtil fu = new FileUtil();
		List<Node> ids = fu.getAllIds(fromFile);
		BufferedWriter bw = fu.writeFile(toFile);
		for(Node node:ids)
		{
			if(!allIds.contains(node.getId())) //过滤重复ID
			{
				allIds.add(node.getId());
				bw.write(node.getId());
				bw.newLine();
				System.out.println(node.getId());
			}
		}
		bw.close();
	}
	
	/**
	 * 获取昵称
	 * @param toPath   获取的昵称保存的路径
	 * @param token 
	 * @throws IOException
	 * @throws WeiboException
	 */
	public void getNickName(String fromPath,String toPath,String token) throws IOException, WeiboException
	{
		String line;
		int i = 1;
		BufferedReader br = new FileUtil().readFile(fromPath);
		BufferedWriter bw = new FileUtil().writeFile(toPath);  //写入文件中
		while((line = br.readLine()) != null)
		{
			if(i == 145)
				break;
			Users users = new Users();                 //新浪接口
			User user = users.showUserById(line);
			bw.write(line+"--"+user.getScreenName());
			System.out.println(user.getScreenName());
			bw.newLine();
			i++;
		}
		bw.close();
	}
	
	/**
	 * 获取某个用户发表的最新微博,根据id
	 * 只需要微博的id即可
	 * @param uid 需要获取微博的用户
	 * @param nickName  用户昵称
	 * @param bw  写入文件的信息流
	 * @throws WeiboException
	 * @throws IOException 
	 */
	public void getTweets(String uid,String nickName,BufferedWriter bw) throws WeiboException, IOException
	{
		
		int numPerPage = 50;        //每页获取的微博数
		Timeline timeline = new Timeline();
		List<Status> status = new ArrayList<Status>();
		StatusWapper sw;
		Paging page = new Paging(1);
		page.setCount(numPerPage);
		try {
				sw = timeline.getUserTimelineByUid(uid, page, 0, 0);
				status = sw.getStatuses();
				for(Status s : status)
				{
					bw.write(uid+"--"+s.getId()+"--"+s.getRepostsCount());
					System.out.println(uid+"--"+s.getId()+"--"+s.getRepostsCount());
					bw.newLine();
				}
			} catch (WeiboException e) {
				e.printStackTrace();
				bw.close();
			}
		System.out.println("下载完毕");
	}
	
	/**
	 * 通过微博的id信息（已获取，同时保存到了tweetids.txt文件中），
	 * 获取转发该微博的微博信息，包括id和微博的内容
	 * 
	 * 因为每个token一个小时只有150次，而服务器一个小时有1000次，为了充分利用服务器资源
	 * 申请了七个应用，每个应用有一个token，在一个小时内循环调用不同的token，则一个小时
	 * 内可以获取980个请求的数据。同时将已经读取的文件的行删除更新到新的行，等待下一次重新
	 * 读取该文件的新数据信息
	 * @throws IOException
	 */
	public void getRetweetsCount() throws IOException
	{
		
		String followeeid = "DATA/followeeidnickname.txt";  //已获取微博id的文件路径
		FileUtil fu = new FileUtil();
		//注意在更新token时对path进行修改，1,2,3表示第一，第二，第三天的下载内容
		String[] tokens = {
				"2.00bYpXtBQ_jEAE74784817a11QkfoD",
				"2.00bYpXtBTBYz6Ed5c6f8b169TVX7gB",
				"2.00bYpXtB0JbR_S3a03283566BbJTSD",
				"2.00bYpXtBSiwMBEd204a1eb0cOFwTZC"
		}; //七个应用的七个token循环调用
		String path = "DATA/retweetcount";  //保存获取到的数据的文件前缀
		String format = ".txt";
		BufferedWriter bw;
		String filePath = path + number + format; //保存获取到的数据的文件路径
		bw = fu.writeFile(filePath);
		for(int i = 0; i < tokens.length; i++)  //循环调用token
		{
			System.out.println(tokens[i]+":");
			Weibo weibo = new Weibo();
			weibo.setToken(tokens[i]);          //重设token
			List<String> lines = fu.getPartfromFile(followeeid, 90); //获取前N行的数据
			try{			
				for(String l:lines)
				{
					String[] words = l.split("--");
					getTweets(words[0],words[1],bw);
				}
			}catch(Exception e)
			{
				e.getStackTrace();
				bw.close();
			}
			fu.deletePartfromFile(followeeid, lines);//删除已经读取过的行，并更新文件
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
	        	   System.out.println("第"+count+"次");
		            if(count > 2)
		            	 System.exit(0);
		            try 
		            {
		            	getRetweetsCount();
						number++;
					} catch (IOException e) 
					{
						e.printStackTrace();
					}
		            count++;
	 	       }
	       };
	       Timer timer = new Timer();
	       int delaytime = 2500;
	       System.out.println(delaytime + "秒后执行程序....");
	       // 设值 5 秒钟后开始执行第一次，以后每隔 一个小时执行一次
	       timer.schedule(task, delaytime * 1000, 3650 * 1000);
	} 
	public static void main(String[] args) throws IOException, WeiboException
	{
		String token = "2.00bYpXtB0JbR_S3a03283566BbJTSD";
		String fromFile = "DATA/followrelation.xml";
		String toFile = "DATA/followeeid.txt";
		String toNickFile = "DATA/followeeidnickname2.txt";
		DownLoadRetweetCount download = new DownLoadRetweetCount();
		download.operateOnTime();
		//download.getUserId(fromFile, toFile);
//		download.getNickName(toFile,toNickFile, token);
//		Account a = new Account();
//		System.out.println(a.getAccountRateLimitStatus().getUserLimit());
	}

}
