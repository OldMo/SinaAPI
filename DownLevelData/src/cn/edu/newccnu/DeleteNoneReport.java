package cn.edu.newccnu;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

/**
 * 去除没有被转发的微博
 * @author Administrator
 *
 */
public class DeleteNoneReport {
	
	/**
	 * 除去没有被转发的微博，将有被转发的重新保存到新的文件中
	 * 为下一步获取转发该微博的微博做准备
	 * @param fromFile  所有的微博文件
	 * @param toFile    去除没有被转发的微博的文件
	 */
	public void delete(String fromFile,String toFile1,String toFile2)
	{
		
		FileUtil fu = new FileUtil();
		BufferedReader br;
		BufferedWriter bw1,bw2;
		String line;
		String[] words;
		int i = 0;
		try 
		{
			br = fu.readFile(fromFile);
			bw1 = fu.writeFile(toFile1);
			bw2 = fu.writeFile(toFile2);
			while((line = br.readLine()) != null)
			{	
				System.out.println("进行第"+ i +"行...");
				words = line.split(",0,");
				if(words.length == 10){
					if(Integer.parseInt(words[7]) != 0)
					{
						System.out.println(words[1]+"--"+words[7]+"--"+words[8]);
						bw1.write(line);
						bw1.newLine();
					}
					
				}
//				else{
//					bw2.write(line);
//					bw2.newLine();
//				}
				i++;
			}
			br.close();
			bw1.close();
			bw2.close();
			
		} catch (IOException e) 
		{
			e.printStackTrace();
		}	
	}
	public static void main(String[] args) throws IOException
	{
		String fromFile = "DATA/tweet/uid-nickname-tid-text-source-date-collected-retweet-comment-isretweet.txt";
		String toFile1 = "DATA/tweet/uid-fid-fnickname-ffcount-ftcount-retweet-deletenone.txt";
		String toFile2 = "DATA/tweet/commentwrong.txt";
		DeleteNoneReport dnr = new DeleteNoneReport();
		dnr.delete(fromFile, toFile1,toFile2);
//		FileUtil fu = new FileUtil();
//		fu.mergeFiles("DATA/tweet/", "uid-nickname", "uid-nickname-tid-text-source-date-collected-retweet-comment-isretweet", 8, ".txt");
	}
}
