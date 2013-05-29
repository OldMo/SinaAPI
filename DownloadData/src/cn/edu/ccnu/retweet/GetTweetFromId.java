package cn.edu.ccnu.retweet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import weibo4j.Timeline;
import weibo4j.Weibo;
import weibo4j.model.Status;
import weibo4j.model.User;
import weibo4j.model.WeiboException;

public class GetTweetFromId {
	static int number = 1;
	/**
	 * 根据微博的id获取该微博用户的id
	 * @param filePath  微博id文件路径
	 * @param toPath     获取的用户id保存路径
	 * @throws IOException
	 * @throws WeiboException
	 */
	public void getUserId(String fromPath,String toPath) throws IOException
	{
		String[] words;
		FileUtil fu = new FileUtil();
		BufferedReader br = fu.readFile(fromPath);
		BufferedWriter bw = fu.writeFile(toPath);
		Timeline timeline = new Timeline();
		try
		{
			while(br.ready())
			{
				words = br.readLine().split("--");
				Status statu = timeline.showStatus(words[2]);
				bw.write(words[0]+"--"+words[1]+"--"+statu.getUser().getId());
				bw.newLine();
			}
			br.close();
		}catch(Exception e)
		{
			e.getStackTrace();
			bw.close();
		}
		bw.close();
	}
	
	/**
	 * 通过微博的id信息（已获取，同时保存到了retweetids.txt文件中），
	 * 获取发该微博的用户的id
	 * 
	 * 因为每个token一个小时只有150次，而服务器一个小时有1000次，为了充分利用服务器资源
	 * 申请了七个应用，每个应用有一个token，在一个小时内循环调用不同的token，则一个小时
	 * 内可以获取980个请求的数据。同时将已经读取的文件的行删除更新到新的行，等待下一次重新
	 * 读取该文件的新数据信息
	 * @throws IOException
	 */
	public void loopToken() throws IOException, WeiboException
	{
		
		String retweetIdsFile = "DATA/retweetids.txt";  //已获取微博id的文件路径
		FileUtil fu = new FileUtil();
		String[] tokens = {                            //七个应用的七个token循环调用
				"2.00DdJ8lB0FVbrac9087bba30ceAcVC",
				"2.00DdJ8lBpRRC5Ea8d7c3e6aecYzQ8B",
				"2.00DdJ8lB0xzLPB9215dc37c6FttplC",
				"2.00DdJ8lBSiwMBE1b78973e0ezKUVdB",
				"2.00DdJ8lB0JbR_S230e3882c6VCRvoB",
				"2.00DdJ8lBTBYz6Ebbb71faca700CH2V",
				"2.00DdJ8lBQ_jEAEb23ec9cb9b7w9S3C"
				}; 
		String path = "DATA/1/retweetuserid";  //保存获取到的数据的文件前缀
		String format = ".txt";
		BufferedWriter bw;
		String filePath = path + number + format; //保存获取到的数据的文件路径
		bw = fu.writeFile(filePath);
		for(int i = 0; i < tokens.length; i++)  //循环调用token
		{
			System.out.println(tokens[i]+":");
			Weibo weibo = new Weibo();
			weibo.setToken(tokens[i]);          //重设token
			Timeline timeline = new Timeline();
			List<String> lines = fu.getPartfromFile(retweetIdsFile, 140); //获取前N行的数据
			try{			
				for(String l:lines)
				{
					String[] words = l.split("--");
					Status statu = timeline.showStatus(words[2]);
					if(statu.getText().equals("此微博已被删除。"))  //微博删除则标记为deleted
						bw.write(words[0]+"--"+words[1]+"--deleted");
					if(statu.getSource() == null)              //微博不存在异常
						bw.write(words[0]+"--"+words[1]+"--not exit");
					else
						bw.write(words[0]+"--"+words[1]+"--"+statu.getUser().getId());//获取对应的用户id
					bw.newLine();
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
						loopToken();
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
	       timer.schedule(task, 3600 * 1000, 3650  * 1000);
	} 
	public static void main(String[] args) throws WeiboException, IOException
	{
		String token = "2.00DdJ8lBpRRC5E98129214e7xeXqYE";
		String fromPath = "DATA/retweetids.txt";
		String toPath = "DATA/retweetuserids.txt";
		GetTweetFromId getTweet = new GetTweetFromId();
		getTweet.operateOnTime();

	}

}
