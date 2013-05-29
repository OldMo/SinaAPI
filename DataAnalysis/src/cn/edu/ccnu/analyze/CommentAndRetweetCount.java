package cn.edu.ccnu.analyze;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cn.edu.ccnu.dataanalysis.FileUtil;

public class CommentAndRetweetCount {
	
	public void getTimes(String fromPath,String toPath) throws IOException{
		FileUtil fu = new FileUtil();
		BufferedReader br = fu.readFile(fromPath);
		BufferedWriter bw = fu.writeFile(toPath);
		String line,follower;
		String[] words;
		String[] followers;
		int i = 0;
		bw.write("uid---转发数");
		bw.newLine();
		while((line = br.readLine()) != null){
			System.out.println("正在处理第"+ (i+1)+"行...");
				
				words = line.split("-");
				follower = words[1];
				followers = follower.split(",");
				bw.write(words[0]+"---"+followers.length);
				bw.newLine();
				i++;
		}
		br.close();
		bw.close();
	}
	
	
	
	public void getUserCount(String fromfile1,String fromfile2,String tofile) throws IOException{
		FileUtil fu = new FileUtil();
		BufferedReader br1 = fu.readFile(fromfile1);
		BufferedReader br2 = fu.readFile(fromfile2);
		BufferedWriter bw1 = fu.writeFile(tofile);
		Map<String,String> rank1 = new HashMap<String,String>();
		Map<String,String> rank2 = new HashMap<String,String>();
		String line,rank_1;
		String[] words;
		br1.readLine();
		while((line = br1.readLine()) != null){         //将文件一中的所有id和值保存到map中
			words = line.split("---");
			rank1.put(words[0], words[1]);
		}
		br1.close();
		br2.readLine();
		bw1.write("---uid---转发次数---评论次数");
		bw1.newLine();
		while((line = br2.readLine()) != null){
			bw1.write(line+"---"+rank1.get(line.split("---")[0]));
			bw1.newLine();
		}
		br2.close();
		bw1.close();
	}
	
	
	public void getFollowers(String fromfile1,String fromfile2,String tofile) throws IOException{
		FileUtil fu = new FileUtil();
		BufferedReader br1 = fu.readFile(fromfile1);
		BufferedReader br2 = fu.readFile(fromfile2);
		BufferedWriter bw1 = fu.writeFile(tofile);
		Map<String,String> rank1 = new HashMap<String,String>();
		Map<String,String> rank2 = new HashMap<String,String>();
		String line,rank_1;
		String[] words = null,words1;
		while((line = br1.readLine()) != null){         //将文件一中的所有id和值保存到map中
			words = line.split(",");
			rank1.put(words[1], words[2]+"---"+words[3]+"---"+words[4]);
		}
		br1.close();
		int i = 0;
		while((line = br2.readLine()) != null){
			System.out.println("第"+i+"行....");
			words1 = line.split("---");
			rank_1 = words1[0];
//			if(rank_1.equals(words[1])){
				bw1.write(rank_1+"---"+"0---"+rank1.get(rank_1)+"---"+words1[1]+"---"+words1[2]);
				bw1.newLine();
//			}
			i++;
		}
		br2.close();
		bw1.close();
	}
	
	public void generateSql(String fromfile,String tofile) throws IOException{
		FileUtil fu = new FileUtil();
		BufferedReader br1 = fu.readFile(fromfile);
		BufferedWriter bw1 = fu.writeFile(tofile);
		String line;
		String[] words;
		int i = 0;
		while((line = br1.readLine()) != null){         //将文件一中的所有id和值保存到map中
			System.out.println(i+"...");
			words = line.split(";");
			bw1.write("insert into user(uid, nickName, passWord, followerNumber, weiboNumber, retweetNumber, commentNumber) value('"+words[0]+"','"+words[2]+"','"+words[1]+"',"+words[3]+","+words[4]+","+words[5]+","+words[6]+");");
			bw1.newLine();
			i++;
		}
		br1.close();
		bw1.close();
		
	}
	public static void main(String[] args) throws IOException{
		CommentAndRetweetCount cr = new CommentAndRetweetCount();
		String fromPath = "DATA/评论和转发次数统计/commentmatrix.txt";
		String toPath1 = "DATA/评论和转发次数统计/retweet.txt";
		String toPath2 = "DATA/评论和转发次数统计/comment.txt";
		String fromfile2 = "DATA/评论和转发次数统计/retweetcomment.txt";
		String fromfile1="DATA/评论和转发次数统计/uid-fid-fnickname-ffcount-ftcount.txt";
		String fromfile = "DATA/评论和转发次数统计/totle.txt";
		String tofile = "DATA/评论和转发次数统计/totlesql.txt";
		//cr.getTimes(fromPath, toPath);
		//cr.getUserCount(toPath1, toPath2, tofile);
		//cr.getFollowers(fromfile1, fromfile2, tofile);
		cr.generateSql(fromfile,tofile);
	}
	
}
