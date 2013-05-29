package cn.edu.ccnu;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import weibo4j.Users;
import weibo4j.Weibo;
import weibo4j.model.User;
import weibo4j.model.WeiboException;

public class GetUserFocus {
	public int number = 1;
	public void getFocus(String uid,BufferedWriter bw) throws WeiboException, IOException
	{
		Users users = new Users();
		User user;
		try {
				user = users.showUserById(uid);
					bw.write(uid+","+user.getScreenName()+","+user.getFriendsCount()+","+user.getFollowersCount()+","+user.getStatusesCount());
					bw.newLine();
						
			}catch (WeiboException e) {
				e.printStackTrace();
				//bw.close();
		}	
	}
	
	public void getFocuses() throws IOException, WeiboException
	{
		
		String retweetIdsFile = "DATA/focusnumber/rank.txt";  //已获取微博id和tweetcount的文件路径
		FileUtil fu = new FileUtil();
		String[] tokens = {                            //七个应用的七个token循环调用
				"2.007csZDD0FVbraf512501df22QQVwB",
				"2.007csZDDpRRC5Ea8ab792fd1kSiJOC",
				"2.007csZDD0xzLPB835ccf022d1OiiND",
				"2.007csZDDSiwMBEb11ae353cbBVegTD",
				"2.007csZDD0JbR_Sedf3fd9f02wmr9aB",
				"2.007csZDDTBYz6E44a0232d520zxR8d",
				"2.007csZDDQ_jEAE85c81f29e1YCDqiB"
				}; 
		String path = "DATA/focusnumber/rank";  //保存获取到的数据的文件前缀
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
			List<String> lines = fu.getPartfromFile(retweetIdsFile, 130); //获取前N行的数据
			try{			
				for(String l:lines)
				{
					words = l.split(":");
					//System.out.println(words[1]+"--"+Integer.parseInt(words[3]));
					
					getFocus(words[0],bw);
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

	public static void main(String[] args) throws IOException, WeiboException{
		GetUserFocus uf = new GetUserFocus();
		uf.getFocuses();
	}
}
