package cn.edu.ccnu.dataanalysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataProcess {
	
	/**
	 * 合并文件
	 * @throws IOException
	 */
	public void mergeFile() throws IOException{
		FileUtil fu = new FileUtil();
		fu.mergeFiles("DATA/", "uid-fid-fnickname-ffcount-ftcount", "uid-fid-fnickname-ffcount-ftcount", 1,2, ".txt");
	}

	
	/**
	 * 或许一万个人的id在这一万个人中计算影响力
	 * @param fromPath1 抽取id的文件一，第一层的数据文件
	 * @param fromPath2抽取id的文件二，第二层的数据文件
	 * @param toPath
	 * @throws IOException
	 */
	public void getIds(String fromPath1,String fromPath2,String toPath) throws IOException{
		FileUtil fu = new FileUtil();
		List<String> allIds = new ArrayList<String>();
		
		String line;
		String[] words;
		int i = 0;
		BufferedWriter bw = fu.writeFile(toPath);
		BufferedReader br = fu.readFile(fromPath1);
		while((line = br.readLine()) != null){
			System.out.println("正在处理第"+ i+"行...");
			words = line.split(",");
			if(Integer.parseInt(words[3]) >= 10 && Integer.parseInt(words[4]) >= 10){  //不满足这个条件的id不考虑
				if(!allIds.contains(words[1])){
					allIds.add(words[1]);
					bw.write(words[1]);
					bw.newLine();
				}
			}
			i++;
		}
		br.close();
		System.out.println("文件"+fromPath1+"中不重复id数目为："+allIds.size());
		int j = 0;
		BufferedReader br1 = fu.readFile(fromPath2);
		while((line = br1.readLine()) != null){
			System.out.println("正在处理第"+ j+"行...");
			words = line.split(",");
			if(Integer.parseInt(words[3]) >= 10 && Integer.parseInt(words[4]) >= 10){  //不满足这个条件的id不考虑
				if(!allIds.contains(words[1])){
					allIds.add(words[1]);
					bw.write(words[1]);
					bw.newLine();
				}
			}
			j++;
		}
		br1.close();
		bw.close();
		System.out.println("文件"+fromPath2+"中不重复id数目为："+allIds.size());
			System.out.println("两个文件中不重复id数目为："+allIds.size());
	}

	public void selectLine(String fromPath,String toPath){
		FileUtil fu = new FileUtil();
	}
	public static void main(String[] args) throws IOException{
//		DataProcess dp = new DataProcess();
//		String fromPath1 = "DATA/uid-fid-fnickname-ffcount-ftcount1.txt";
		String fromPath2 = "DATA/new/retweet/uid-nickname-tid-text-source-date-collected-retweetcount-commentcount-isretweet-deletenoneretweet.txt";
		String toPath = "DATA/new/retweet/matrix.txt";
//		dp.getIds(fromPath1,fromPath2, toPath);
		BuildCorpus bc = new BuildCorpus();
		bc.initialGraph(fromPath2, toPath, 1);
//		GetID id = new GetID();
//		id.getAllIds(fromPath2);
		
	}
}
