package cn.edu.ccnu.retweet;

import java.io.IOException;

/**
 * 将分别获取的子文件合并到一个文件中
 * @author Jelen
 *
 */
public class MergeFiles {
	
	public static void main(String[] args) throws IOException
	{
		new FileUtil().mergeFiles("DATA/tweet/", "uid-tid-retweetcount", "uid-tid-retweetcount", 10, ".txt");
	}

}
