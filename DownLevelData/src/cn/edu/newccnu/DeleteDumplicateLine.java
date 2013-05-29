package cn.edu.newccnu;

import java.io.IOException;

/**
 * 删除重复的用户id的行
 * @author Jelen
 *
 */
public class DeleteDumplicateLine {
	
	public static void main(String[] args) throws IOException{
		String fromPath = "DATA/newdownload/uid-fid-fnickname-ffcount-ftcount.txt";
		String toPath = "DATA/newdownload/uid-fid-fnickname-ffcount-ftcount-deleteddumplicate.txt";
		FileUtil fu = new FileUtil();
		fu.deleteDuplicateID(fromPath, toPath);
	}

}
