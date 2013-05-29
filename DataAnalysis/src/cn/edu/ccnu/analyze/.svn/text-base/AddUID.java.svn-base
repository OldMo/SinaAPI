package cn.edu.ccnu.analyze;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cn.edu.ccnu.dataanalysis.FileUtil;

/**
 * 给只有昵称没有id的文件添加id
 * @author Jelen
 *
 */
public class AddUID {
	
	public static void main(String[] args) throws IOException{
		String fromPath1 = "DATA/粉丝分析/评估的105人/uid-fid-fnickname-ffcount-ftcount-deleteddumplicate.txt";
		
		
		FileUtil fu = new FileUtil();
		BufferedReader br1 = fu.readFile(fromPath1);
		
		
		Map<String,String> map = new HashMap<String,String>();
		String line;
		String[] words;
		while((line=br1.readLine())!=null){
			words = line.split(",");
			map.put(words[2], words[1]);
		}
		br1.close();
		
		for(int i = 1; i <= 4; i++){
			String fromPath2 = "DATA/转发分析/retweet1/nickname-tid-rtid-text-date-source-rtcount-ctcount-rtuid"+i+".txt";
			String toPath = "DATA/转发分析/retweet1/uid-nickname-tid-rtid-text-date-source-rtcount-ctcount-rtuid"+i+".txt";
			BufferedWriter bw = fu.writeFile(toPath);
			BufferedReader br2 = fu.readFile(fromPath2);
			int j = 1;
			while((line = br2.readLine()) != null){
				System.out.println("替换第"+j+"行...");
				words = line.split("-00-");
				String uid = map.get(words[0]);
				bw.write(uid + "-00-" + line);
				bw.newLine();
				j++;
			}
			br2.close();
			bw.close();
		}
	}

}
