package cn.edu.ccnu.comment;

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
	
	static int number = 1;
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
							bw.write(uid+"--deleted");
						}
						if(s.getSource() == null)//微博不存在异常
						{
							bw.write(uid+"--not exit");
						}
						else
						{
							bw.write(uid+",,"+s.getId()+",,"+s.getText()+",,"+s.getSource()+",,"+s.getCreatedAt()+",,"+s.isFavorited()+",,"+s.getRepostsCount()+",,"+s.getCommentsCount());//获取对应的微博id和转发次数
							//System.out.println(uid+"--"+s.getId()+"--"+s.getRepostsCount());
						}
						bw.newLine();
				}
			} catch (WeiboException e) {
				e.printStackTrace();
				bw.close();
			}	
		}
	}
	
	/**
	 * 下载所有用户的所有微博id，非自动下载
	 * @param nickNameFile  用户的id，昵称文件
	 * @throws IOException
	 * @throws WeiboException
	 */
	public void getTweets(String nickNameFile,String tweetIdsFile) throws IOException, WeiboException
	{
		BufferedReader br = new FileUtil().readFile(nickNameFile);                            //读取用户的id，昵称和总微博数
		BufferedWriter bw = new FileUtil().writeFile("DATA/tweetids1.txt");        //将用户微博id保存到此文件
		while(br.ready())
		{
			String[] line = br.readLine().split("-");
			System.out.println(line[0]+"-"+line[1]);
			getTweet(line[0],line[1],Integer.parseInt(line[2]),bw);
		}
		bw.close();
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
		
		String retweetIdsFile = "DATA/new retweet/small/id-nickname-tweetcount.txt";  //已获取微博id和tweetcount的文件路径
		FileUtil fu = new FileUtil();
		String[] tokens = {                            //七个应用的七个token循环调用
				"2.00DdJ8lBQ_jEAEfefa720792WfZ2YE",
				"2.00DdJ8lBTBYz6E5b2c3955bf5a5aaD",
				"2.00DdJ8lB0JbR_S87dacba4dbubcqID",
				"2.00DdJ8lBSiwMBE4ac38a52040BGgJr",
				"2.00DdJ8lB0xzLPB8e9b42f85eQLlvLD",
				"2.00DdJ8lBpRRC5Eb5d9a3d0d4UJ9npD",
				"2.00DdJ8lB0FVbrad81777a370UFQk8E"
				}; 
		String path = "DATA/comment/small/uid-tid-commentcount";  //保存获取到的数据的文件前缀
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
			List<String> lines = fu.getPartfromFile(retweetIdsFile, 1); //获取前N行的数据
			try{			
				for(String l:lines)
				{
					words = l.split(",");
					//System.out.println(words[0]+"--"+words[1]+"--"+words[2]);
					getTweet(words[0],words[1],Integer.parseInt(words[2]),bw);
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
		            if(count>3)
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
	       timer.schedule(task, 7200 * 1000, 3850  * 1000);
	} 
	public static void main(String[] args) throws WeiboException, IOException
	{
		DownloadUserTweete getTweet = new DownloadUserTweete();
	//	getTweet.operateOnTime();
		
		Weibo weibo = new Weibo();
		weibo.setToken("2.00DdJ8lBpRRC5Eb5d9a3d0d4UJ9npD");
		BufferedWriter bw = new FileUtil().writeFile("DATA/tweet/uid-tid-retweetcount222.txt");
		getTweet.getTweet("10315", "我是汪汪汪汪汪", 499, bw);
		bw.close();

	}

	
}
