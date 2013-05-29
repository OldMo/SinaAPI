package cn.edu.ccnu.retweet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import weibo4j.Timeline;
import weibo4j.Users;
import weibo4j.Weibo;
import weibo4j.model.Paging;
import weibo4j.model.StatusWapper;
import weibo4j.model.User;
import weibo4j.model.Status;
import weibo4j.model.WeiboException;


/**
 * 下载新浪上的微博的转发信息
 * @author Jelen_123
 *
 */
public class DownLoadRetweet {
	static int number = 1;
	/**
	 *构造函数 
	 * @param token
	 * 
	 */
	public DownLoadRetweet(String token)
	{
		Weibo weibo = new Weibo();
		weibo.setToken(token);
	}
	
	public DownLoadRetweet()
	{
		
	}
	/**
	 * 根据用户id获取其用户名
	 * @param uid
	 * @throws WeiboException
	 * @throws IOException 
	 */
	public User getUserNickName(String uid) throws WeiboException
	{
		Users users = new Users();                 //新浪接口
		User user = users.showUserById(uid);
		System.out.println(user.getScreenName());
		return user;
	}
	

	/**
	 * 获取微博的转发微博
	 * @throws WeiboException
	 * @throws IOException 
	 */
	public void getRetweet(String uid,String tweetId,int retweetCount,BufferedWriter bw) throws WeiboException, IOException
	//public void getRetweet() throws WeiboException
	{
		int numPerPage = 50;  //每页获取的微博数
		Timeline timeline = new Timeline();
		List<Status> status = new ArrayList<Status>();
		int pageCount = retweetCount/numPerPage + 1;
		for(int i = 1; i <= pageCount; i++)
		{
			Paging page = new Paging(i);
			page.setCount(numPerPage);
			StatusWapper sw;
			try {
				sw = timeline.getRepostTimeline(tweetId,page);
				status = sw.getStatuses();
				//System.out.println(status.size());
				for(Status statu:status)
				{
					System.out.println(uid+"--"+statu.getId());
					if(statu.getText().equals("此微博已被删除。"))  //微博删除则标记为deleted
						bw.write(uid+"--"+"--deleted");
					if(statu.getSource() == null)              //微博不存在异常
						bw.write(uid+"--"+"--not exit");
					else
						bw.write(uid+"--"+tweetId+"--"+statu.getId()+"--"+statu.getUser().getId());
					bw.newLine();
				}
			} catch (WeiboException e) {
				e.printStackTrace();
				bw.close();
				System.exit(0);
			}
		}
	}
	
	/**
	 * 获取用户的微博的转发微博id和转发人id，自动执行
	 * 
	 * 因为每个token一个小时只有150次，而服务器一个小时有1000次，为了充分利用服务器资源
	 * 申请了七个应用，每个应用有一个token，在一个小时内循环调用不同的token，则一个小时
	 * 内可以获取980个请求的数据。同时将已经读取的文件的行删除更新到新的行，等待下一次重新
	 * 读取该文件的新数据信息
	 * @throws IOException
	 */
	public void getReTweets() throws IOException, WeiboException
	{
		
		String retweetIdsFile = "DATA/tweet/uid-tid-retweetcount-deletenone.txt";  //已获取微博id和tweetcount的文件路径
		FileUtil fu = new FileUtil();
		String[] tokens = {                            //七个应用的七个token循环调用
				"2.00DdJ8lBQ_jEAE3c12183378V7yEKE",
				"2.00DdJ8lBTBYz6Efa63e391651bkk5B",
				"2.00DdJ8lB0JbR_S5cf9a27bc13BWIaB",
				"2.00DdJ8lBSiwMBE332ef26f34x3SkYD",
				"2.00DdJ8lB0xzLPB3d7d016c440bUE4I",
				"2.00DdJ8lBpRRC5Eb1fea800757PU6aD",
				"2.00DdJ8lB0FVbra89799efaabznXj1E"
				}; 
		String path = "DATA/retweet/uid-tid-retweettid-retweetuid";  //保存获取到的数据的文件前缀
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
			List<String> lines = fu.getPartfromFile(retweetIdsFile, 130); //获取前N行的数据
			try{			
				for(String l:lines)
				{
					words = l.split("--");
					getRetweet(words[0],words[1],Integer.parseInt(words[2]),bw);
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
		            if(count>12)
		            	 System.exit(0);
		            try 
		            {
		            	System.out.println("开始第"+number+"个文件下载------");
		            	getReTweets();
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
	       timer.schedule(task, 5 * 1000, 3650  * 1000);
	} 
	
	
	/**
	 * 获取所有微博的转发的微博id,非自动
	 * @param tweetIdsFile   用户微博id文件路径
	 * @param retweetIdsFile   获取的转发微博的id文件路径
	 * @throws IOException
	 * @throws WeiboException
	 */
	public void getRetweets(String tweetIdsFile,String retweetIdsFile) throws IOException, WeiboException
	{
		BufferedReader br  = new FileUtil().readFile(tweetIdsFile);
		BufferedWriter bw = new FileUtil().writeFile(retweetIdsFile);
		while(br.ready())
		{
			String[] line = br.readLine().split("--");
			//System.out.println(line[0]+"-"+line[1]);
			getRetweet(line[0],line[1],Integer.parseInt(line[2]),bw);
		}
		bw.close();
	}

	
	public static void main(String[] args) throws WeiboException, IOException
	{
		DownLoadRetweet download = new DownLoadRetweet();
		download.operateOnTime();
	}

}
