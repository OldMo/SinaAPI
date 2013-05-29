package cn.edu.ccnu.retweet;

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
	public void delete(String fromFile,String toFile)
	{
		
		FileUtil fu = new FileUtil();
		BufferedReader br;
		BufferedWriter bw;
		String[] line;
		try 
		{
			br = fu.readFile(fromFile);
			bw = fu.writeFile(toFile);
			while(br.ready())
			{
				line = br.readLine().split("--");
				if(Integer.parseInt(line[2]) != 0)
				{
					System.out.println(line[0]+"--"+line[1]+"--"+line[2]);
					bw.write(line[0]+"--"+line[1]+"--"+line[2]);
					bw.newLine();
				}
			}
			br.close();
			bw.close();
			
		} catch (IOException e) 
		{
			e.printStackTrace();
		}	
	}
	public static void main(String[] args)
	{
		String fromFile = "DATA/tweet/uid-tid-retweetcount.txt";
		String toFile = "DATA/tweet/uid-tid-retweetcount-deletenone.txt";
		DeleteNoneReport dnr = new DeleteNoneReport();
		dnr.delete(fromFile, toFile);
	}
}
