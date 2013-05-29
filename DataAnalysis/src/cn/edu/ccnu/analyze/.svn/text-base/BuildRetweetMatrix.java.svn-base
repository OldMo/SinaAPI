package cn.edu.ccnu.analyze;

import java.io.BufferedReader;
import java.io.IOException;

import cn.edu.ccnu.dataanalysis.BuildCorpus;
import cn.edu.ccnu.dataanalysis.FileUtil;

public class BuildRetweetMatrix {

	public static void main(String[] args) throws IOException{
		
		BuildCorpus bc = new BuildCorpus();
		String toPath = "DATA/评论分析/comment3/commentmatrix3.txt";
//		BufferedWriter bw = new FileUtil().writeFile(toPath);
		//从23个转发的文件中提取出对应的关系放到一个matrix文件中
		//for(int i = 1; i <= 23; i++){
//			String fromPath = "DATA/评论分析/comment3/uid-nickname-tid-rtid-rttext-date-source-ctuid.txt";
			
//			bc.initialGraph(fromPath, bw, 1);
		//}
//		bw.close();
		
		String path = "DATA/new/comment/";
		FileUtil fu = new FileUtil();
		fu.mergeFiles(path, "uid-nickname-tid-text-source-date-collected-retweetcount-commentcount-isretweet-deletenonecomment",
				"uid-nickname-tid-text-source-date-collected-retweetcount-commentcount-isretweet-deletenonecomment",1,18, ".txt");
//		String fromPath = "DATA/微博分析/downloadretweet/1/uid-fnickname-wid-text-source-date-shoucang-retweetcount-commentcount.txt";
//		BufferedReader br = fu.readFile(fromPath);
//		String line;
//		String[] words;
//		int countRetweet = 0,countOriginal = 0;
//		int i = 0;
//		while((line = br.readLine()) != null){
//			System.out.println("正在处理第"+ (i+1)+"行...");
//			words = line.split(",0,");
//			if(words.length == 10)
//			{
//				if(words[0].equals("2269366563")){
//					if(words[9].equals("true"))
//						countRetweet++;
//					else
//						countOriginal++;
//				}
//			}
//			i++;
//		}
//		System.out.println("总微博数:"+i);
//		System.out.println("转发数:"+countRetweet);
//		System.out.println("原创数:"+countOriginal);
//			
	}
}
