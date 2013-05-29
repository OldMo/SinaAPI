package cn.edu.newccnu;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import weibo4j.Comments;
import weibo4j.Timeline;
import weibo4j.Users;
import weibo4j.Weibo;
import weibo4j.model.Comment;
import weibo4j.model.CommentWapper;
import weibo4j.model.Paging;
import weibo4j.model.Status;
import weibo4j.model.User;
import weibo4j.model.WeiboException;


/**
 * 下载新浪上的微博的转发信息
 * @author Jelen_123
 *
 */
public class DownLoadComment {
	static int number = 11;
	/**
	 *构造函数 
	 * @param token
	 * 
	 */
	public DownLoadComment(String token)
	{
		Weibo weibo = new Weibo();
		weibo.setToken(token);
	}
//	
	public DownLoadComment()
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
	public void getComment(String uid,String nickName,String tweetId,int retweetCount,BufferedWriter bw) throws WeiboException, IOException
	//public void getRetweet() throws WeiboException
	{
		int numPerPage = 50;  //每页获取的微博数
		Comments comment = new Comments();
		List<Comment> comments = new ArrayList<Comment>();
		int pageCount = retweetCount/numPerPage + 1;
		for(int i = 1; i <= pageCount; i++)
		{
			Paging page = new Paging(i);
			page.setCount(numPerPage);
			CommentWapper cw;
			try {
				cw = comment.getCommentById(tweetId, page, 0);
				comments = cw.getComments();
				//System.out.println(status.size());
				for(Comment com:comments)
				{
					//System.out.println(uid+"--"+com.getId());
					if(com.getText().equals("此微博已被删除。"))  //微博删除则标记为deleted
						bw.write(uid+",00,"+",00,deleted");
					if(com.getSource() == null)              //微博不存在异常
						bw.write(uid+",00,"+",00,not exit");
					else
						bw.write(uid+",00,"+nickName+",00,"+tweetId+",00,"+com.getId()+",00,"+com.getText()+",00,"+com.getCreatedAt()+",00,"+com.getSource()+",00,"+com.getUser().getId());
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
	public void getComments() throws IOException, WeiboException
	{
		
		String retweetIdsFile = "DATA/new/comment/uid-nickname-tid-text-source-date-collected-retweetcount-commentcount-isretweet-deletenonecomment.txt";  //已获取微博id和tweetcount的文件路径
		FileUtil fu = new FileUtil();
		String[] tokens = {                            //七个应用的七个token循环调用
				"2.007csZDD0FVbra040c5f11e2sIbJ1D",
				"2.007csZDDpRRC5Ec10e3211739LhPYC",
				"2.007csZDD0xzLPBd010d6d179c3TCIC",
				"2.007csZDDSiwMBEefd6e720d6_MqXeD",
				"2.007csZDD0JbR_S811c918ef9qLr4bE",
				"2.007csZDDTBYz6E78d8b633c0KER7vC",
				"2.007csZDDQ_jEAE5f0020a6110GUoMd"
				}; 
		String path = "DATA/new/comment/uid-nickname-tid-text-source-date-collected-retweetcount-commentcount-isretweet-deletenonecomment";  //保存获取到的数据的文件前缀
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
					words = l.split(",0,");
					getComment(words[0],words[1],words[2],Integer.parseInt(words[8]),bw);
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
		            if(count>20)
		            	 System.exit(0);
		            try 
		            {
		            	System.out.println("开始第"+number+"个文件下载------");
		            	getComments();
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
	       timer.schedule(task, 2*3600* 1000, 3750  * 1000);
	} 
	
	
	/**
	 * 获取所有微博的转发的微博id,非自动
	 * @param tweetIdsFile   用户微博id文件路径
	 * @param retweetIdsFile   获取的转发微博的id文件路径
	 * @throws IOException
	 * @throws WeiboException
	 */
//	public void getComments(String tweetIdsFile,String retweetIdsFile) throws IOException, WeiboException
//	{
//		BufferedReader br  = new FileUtil().readFile(tweetIdsFile);
//		BufferedWriter bw = new FileUtil().writeFile(retweetIdsFile);
//		while(br.ready())
//		{
//			String[] line = br.readLine().split("--");
//			//System.out.println(line[0]+"-"+line[1]);
//			getComment(line[0],line[1],Integer.parseInt(line[2]),bw);
//		}
//		bw.close();
//	}

	
	public static void main(String[] args) throws WeiboException, IOException
	{
		DownLoadComment download = new DownLoadComment();
		download.operateOnTime();
	}

}
