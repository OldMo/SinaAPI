package cn.edu.newccnu;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import weibo4j.Friendships;
import weibo4j.Timeline;
import weibo4j.Weibo;
import weibo4j.model.Paging;
import weibo4j.model.User;
import weibo4j.model.UserWapper;
import weibo4j.model.WeiboException;



/**
 * 下载用户的所有的微博信息，只需要微博的tid和转发次数即可
 * @author Liuxingyun
 *
 */
public class DownloadUserFollowers {
	
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
	public void getFollower(String uid,int followerCount,BufferedWriter bw) throws WeiboException, IOException
	{
		
		int numPerPage = 200;        //每页获取的微博数
		int pageCount = followerCount/numPerPage + 1;  //总页数
		long nextCursor = 0;		//记录获取的开始
		Friendships friends = new Friendships();
		UserWapper uw;
		for(int j = 1; j <= pageCount; j++){
			System.out.println(uid+"用户共"+pageCount+"页正在进行"+"第"+j+"页下载.......");
			Paging page = new Paging(j);
			page.setCount(numPerPage);
			try {
				uw = friends.getFollowersById(uid, numPerPage, (int) nextCursor);
				System.out.println(uw.getTotalNumber()+"-"+uw.getNextCursor()+"--"+uw.getPreviousCursor());
				List<User> followers = uw.getUsers();  //根据id获取其关注的人
				for(User u : followers){
					bw.write(uid+","+u.getId()+","+u.getScreenName()+","+u.getFollowersCount()+","+u.getStatusesCount());
					bw.newLine();
				}
				nextCursor = uw.getNextCursor();		//记录获取下移200个
						
			}catch (WeiboException e) {
				e.printStackTrace();
				//bw.close();
			}
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
	public void getFollowers() throws IOException, WeiboException
	{
		
		String retweetIdsFile = "DATA/follower/3third/uid-fid-fnickname-ffcount-ftcount5.txt";  //已获取微博id和tweetcount的文件路径
		FileUtil fu = new FileUtil();
		String[] tokens = {                            //七个应用的七个token循环调用
				"2.00DdJ8lB0FVbra77c8f850b90fanMj",
				"2.00DdJ8lBpRRC5E791223f0472YHs6C",
				"2.00DdJ8lB0xzLPB11db85bf2bTQmhgB",
				"2.00DdJ8lBSiwMBEac9d86836dIzcnaB",
				"2.00DdJ8lB0JbR_S7daa5fad18lLYWdB",
				"2.00DdJ8lBTBYz6E8a8b1332053x2eFC",
				"2.00DdJ8lBQ_jEAE2dfd93c2a80mtt9T"
				}; 
		String path = "DATA/follower/4forth/5/uid-fid-fnickname-ffcount-ftcount5";  //保存获取到的数据的文件前缀
		String format = ".txt";
		BufferedWriter bw = null;
		String[] words;
		String filePath; //保存获取到的数据的文件路径
		filePath = path + number + format;
		bw = fu.writeFile(filePath);
		for(int i = 0; i < tokens.length; i++)  //循环调用token
		{  
			System.out.println(tokens[i]+":");
			Weibo weibo = new Weibo();
			weibo.setToken(tokens[i]);          //重设token
			List<String> lines = fu.getPartfromFile(retweetIdsFile, 100); //获取前N行的数据
			try{			
				for(String l:lines)
				{
					words = l.split(",");
					//System.out.println(words[1]+"--"+Integer.parseInt(words[3]));
					
					getFollower(words[1],Integer.parseInt(words[3]),bw);
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
		            if(count>23)
		            	 System.exit(0);
		            try 
		            {
		            	System.out.println("开始第"+number+"个文件下载------");
		            	getFollowers();
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
	       timer.schedule(task, 2*3600 * 1000, 3650  * 1000);
	} 
	public static void main(String[] args) throws WeiboException, IOException
	{
		DownloadUserFollowers getFollower = new DownloadUserFollowers();
		getFollower.operateOnTime();
//		getFollower.getFollowers();
//		Weibo weibo = new Weibo();
//		weibo.setToken("2.00bYpXtBQ_jEAE012286da49EgWl6E");
//		BufferedWriter bw = new FileUtil().writeFile("DATA/follower/second/2560102564-1.txt");
//		getFollower.getFollower("2146231802",1364,bw);
//		bw.close();

	}

	
}
