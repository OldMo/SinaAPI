package cn.edu.ccnu.retweet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import weibo4j.Users;
import weibo4j.Weibo;
import weibo4j.model.Status;
import weibo4j.model.User;
import weibo4j.model.WeiboException;

/**
 * 根据用户id获取其新浪中的昵称
 * @author Jelen_123
 *
 */
public class GetUserNickName {
	
	/**
	 * 获取语料中被关注者的id
	 * @param filePath 语料文件的路径
	 * @throws IOException 
	 */
	public List<String> getAllIds(String fromPath) throws IOException
	{
		List<String> allIds = new ArrayList<String>();	    //单纯放被关注人id
		int lineNo = 1;
		BufferedReader br = new FileUtil().readFile(fromPath);
		while(br.ready())
		{
			String line = br.readLine();
			String id = new FileUtil().pickUp(line);         //获得过滤后的id
			
			if(!id.equals(""))
			{
				if(lineNo%4 == 2)             //从语料分析，2,6,10...行为被关注者id
				{
					if(!allIds.contains(id))
						allIds.add(id);
				}
			}
			lineNo++;
		}
		return allIds;
	}
	
	/**
	 * 获取昵称
	 * @param fromPath  id语料路径
	 * @param toPath   获取的昵称保存的路径
	 * @param token 
	 * @param uid
	 * @throws IOException
	 * @throws WeiboException
	 */
	public void getNickName(String fromPath,String toPath,String token) throws IOException, WeiboException
	{
		List<String> allIds = getAllIds(fromPath);
		DownLoadRetweet download = new DownLoadRetweet(token);
		BufferedWriter bw = new FileUtil().writeFile(toPath);  //写入文件中
		for(String id : allIds)
		{
			User user = download.getUserNickName(id);
			bw.write(id+"-"+user.getScreenName()+"-"+user.getStatusesCount());
			bw.newLine();
		}
		bw.close();
	}

	/**
	 * 根据用户id获取其nickname和tweetcount
	 * 循环调用token获取
	 * 读取该文件的新数据信息
	 * @throws IOException
	 */
	public void getNickName() throws IOException, WeiboException
	{
		
		String retweetIdsFile = "DATA/uid-nickname.txt";  //已获取微博id的文件路径
		FileUtil fu = new FileUtil();
		String[] tokens = {                            //七个应用的七个token循环调用
				"2.00DdJ8lBQ_jEAE240a5ecc697A9Q5E",
				"2.00DdJ8lBTBYz6Eb05532c4admbVi5C",
				"2.00DdJ8lB0JbR_S340870e11fTqDWaC",
				"2.00DdJ8lBSiwMBEb6b0a439e3iJoP7C",
				"2.00DdJ8lB0xzLPBcac093f55c0JLx_C",
				"2.00DdJ8lBpRRC5E6753abb550orz6BD",
				"2.00DdJ8lB0FVbra2ecfba2fb6MzXWWE"
				}; 
		String filePath = "DATA/id-nickname-tweetcount.txt";  //保存获取到的数据的文件前缀
		BufferedWriter bw;
		bw = fu.writeFile(filePath);
		for(int i = 0; i < tokens.length; i++)  //循环调用token
		{
			System.out.println(tokens[i]+":");
			Weibo weibo = new Weibo();
			weibo.setToken(tokens[i]);          //重设token
			List<String> lines = fu.getPartfromFile(retweetIdsFile, 50); //获取前N行的数据
			try{			
				for(String l:lines)
				{
					String uid = l.split(",")[0];
					User user =new Users().showUserById(uid);
					bw.write(uid+","+user.getScreenName()+","+user.getStatusesCount());
					bw.newLine();
					//System.out.println(uid);
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
	
	public static void main(String[] args) throws IOException, WeiboException
	{
		GetUserNickName nickNames = new GetUserNickName();
		nickNames.getNickName();
	}
}
