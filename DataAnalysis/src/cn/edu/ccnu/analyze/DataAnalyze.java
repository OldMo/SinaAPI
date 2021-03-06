package cn.edu.ccnu.analyze;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.edu.ccnu.dataanalysis.FileUtil;

public class DataAnalyze {
	
	/**
	 * 合并文件
	 * @throws IOException
	 */
	public void merge() throws IOException{
		String path = "DATA/粉丝分析/";
		FileUtil fu = new FileUtil();
		fu.mergeFiles(path, "uid-fid-fnickname-ffcount-ftcount", "uid-fid-fnickname-ffcount-ftcount(1-3)",1,3,".txt");
	}
	
	/**
	 * 或许一万个人的id在这一万个人中计算影响力
	 * @param fromPath 数据文件
	 * @param toPath
	 * @throws IOException
	 */
	public void deleteDumplicateIds(String fromPath,String toPath) throws IOException{
		FileUtil fu = new FileUtil();
		List<String> allIds = new ArrayList<String>();
		
		String line;
		String[] words;
		int i = 0;
		BufferedWriter bw = fu.writeFile(toPath);
		BufferedReader br = fu.readFile(fromPath);
		while((line = br.readLine()) != null){
			System.out.println("正在处理第"+ i+"行...");
			words = line.split(",");
				if(!allIds.contains(words[1])){
					allIds.add(words[1]);
					bw.write(line);
					bw.newLine();
			}
			i++;
		}
		br.close();
		bw.close();
		System.out.println("文件"+fromPath+"中不重复id数目为："+allIds.size());
	} 
	
	/**
	 * 对map进行排序
	 * @param map  包含NO值和对应的PR值的map
	 */
	public List orderMap(Map map)
	{
		List<Map.Entry<String, Integer>> keyAndValue = new ArrayList<Map.Entry<String, Integer>>(map.entrySet());
		Collections.sort(keyAndValue, new Comparator<Map.Entry<String, Integer>>() //调用排序方法
		{   
		    public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) 
		    {       
		        return (o1.getValue().compareTo(o2.getValue()));      //根据PR值进行排序       
		    }
		});
		return keyAndValue;
	}
	
	/**
	 * 统计粉丝数目的次数分布
	 * @param fromPath
	 * @param toPath
	 * @throws IOException
	 */
	public void countFollower(String fromPath,String toPath) throws IOException{
		FileUtil fu = new FileUtil();
		BufferedReader br = fu.readFile(fromPath);
		BufferedWriter bw = fu.writeFile(toPath);
		Map<String,Integer> followers = new HashMap<String,Integer>();
		String line;
		String[] words;
		int i = 0;
		while((line = br.readLine()) != null){
			int wordCount = 1;
			System.out.println("正在处理第"+ i+"行...");
			words = line.split(",");
				if(!followers.containsKey(words[3]))   //统计粉丝数相同的次数
					followers.put(words[3], wordCount);
				else{
					wordCount = followers.get(words[3]);
					wordCount++;
					followers.put(words[3], wordCount);
				}
			i++;
		}
		
		List<Map.Entry> list = orderMap(followers);       //按次数进行排序
		bw.write("粉丝数=次数");
		bw.newLine();
		for(Map.Entry l:list){
			bw.write(l.toString());
			 bw.newLine();
		}
//		Iterator it = followers.entrySet().iterator();
//		while(it.hasNext()){
//			 Map.Entry entry = (Map.Entry) it.next();
//			 bw.write(entry.getKey()+","+entry.getValue());
//			 bw.newLine();
//		}
		br.close();
		bw.close();
		System.out.println("处理结束！");
	}
	
	/**
	 * 获取粉丝数和微博数
	 * @param fromPath
	 * @param toPath
	 * @throws IOException
	 */
	public void followerAndWeiboCount(String fromPath,String toPath) throws IOException{
		FileUtil fu = new FileUtil();
		BufferedReader br = fu.readFile(fromPath);
		BufferedWriter bw = fu.writeFile(toPath);
		Map<String,Integer> followers = new HashMap<String,Integer>();
		String line;
		String[] words;
		int i = 0;
		while((line = br.readLine()) != null){
			System.out.println("正在处理第"+ i+"行...");
			words = line.split(",");
				if(!followers.containsKey(words[3]) && !followers.containsValue(words[4]))   //统计粉丝数相同的次数
					followers.put(words[3], Integer.parseInt(words[4]));
			i++;
		}
		
		bw.write("粉丝数---微博数");
		bw.newLine();
		Iterator it = followers.entrySet().iterator();
		while(it.hasNext()){
			 Map.Entry entry = (Map.Entry) it.next();
			 bw.write(entry.getKey()+"---"+entry.getValue());
			 bw.newLine();
		}
		System.out.println("处理完成！");
		br.close();
		bw.close();
	}
	
	/**
	 * 获取微博是否被收藏
	 * @param fromPath
	 * @param toPath
	 * @throws IOException
	 */
	public void getCollection(String fromPath,String toPath) throws IOException{
		FileUtil fu = new FileUtil();
		BufferedReader br = fu.readFile(fromPath);
		BufferedWriter bw = fu.writeFile(toPath);
		String line;
		String[] words;
		int countTrue = 0,countDeleted = 0;
		int i = 0;
		while((line = br.readLine()) != null){
			System.out.println("正在处理第"+ (i+1)+"行...");
			if(!line.contains(",,")){                 //被删除的微博信息中没有",,"
				i++;
				countDeleted++;
				continue;
			}
			else{
			words = line.split(",,");
				if(words[words.length-3].equals("true")){   //统计粉丝数相同的次数
					countTrue++;	
					System.out.println(line);
				}
			}
			i++;
		}
		br.close();
		bw.close();
		System.out.println("被收藏的微博数："+countTrue);
		System.out.println("删除的微博数："+countDeleted);
		System.out.println("所有的微博数："+i);
	}
	
	/**
	 * 获取含有URL的微博数目
	 * @param fromPath
	 * @param toPath1 保存不规则的微博信息，另外统计URL
	 * @param toPath2 保存含有URL的微博信息
	 * @throws IOException
	 */
	public void getUrl(String fromPath,String toPath1,String toPath2) throws IOException{
		FileUtil fu = new FileUtil();
		BufferedReader br = fu.readFile(fromPath);
		BufferedWriter bw1 = fu.writeFile(toPath1);
		BufferedWriter bw2 = fu.writeFile(toPath2);
		String line;
		String[] words;
		int countUrl = 0;
		int i = 0;
		while((line = br.readLine()) != null){
			System.out.println("正在处理第"+ (i+1)+"行...");
			if(!line.contains(",,")){
				i++;
				continue;
			}
			else{
				words = line.split(",,");
				if(words.length == 9){
					if(words[3].contains("http")){
						//System.out.println(words[3]);
						countUrl++;
						bw2.write(line);
						bw2.newLine();
					}
				}
				else{
					bw1.write(line);
					bw1.newLine();
				}
			}
			i++;
		}
		System.out.println("处理结束！");
		System.out.println("含有URL的微博数为："+countUrl);
		br.close();
		bw1.close();
		bw2.close();
	}
			
	public void getRetweetAndAt(String fromPath) throws IOException{
		FileUtil fu = new FileUtil();
		BufferedReader br = fu.readFile(fromPath);
		//BufferedWriter bw1 = fu.writeFile(toPath1);
		//BufferedWriter bw2 = fu.writeFile(toPath2);
		String line;
		String[] words;
		int countBoth = 0,countRetweet1 = 0,countRetweet2 = 0,countAt = 0;
		int i = 0;
		while((line = br.readLine()) != null){
			System.out.println("正在处理第"+ (i+1)+"行...");
			if(!line.contains(",,")){
				i++;
				continue;
			}
			else{
				words = line.split(",,");
				if(words.length == 9){
					if(words[3].contains("转发微博"))
						countRetweet1++;
					if(words[3].contains("//@"))
							countRetweet2++;
					if(words[3].contains("@"))
						countAt++;
				}
			}
			i++;
		}
		br.close();
		System.out.println("处理结束！");
		System.out.println("总微博数为：" + i);
		System.out.println("含有‘转发微博’："+ countRetweet1);
		System.out.println("含有‘//@’的微博数为："+ countRetweet2);
		System.out.println("含有‘@’的微博数为："+ (countAt-countRetweet2));
	}
	
	/**
	 * 找到结果的uid对应的nickname，根据的是获取到的关注信息的文件
	 * @param fromFollow
	 * @param fromResult
	 * @param toResult
	 * @throws IOException
	 */
	public void findNickName(String fromFollow,String fromResult,String toResult) throws IOException{
		FileUtil fu = new FileUtil();
		BufferedReader br1 = fu.readFile(fromFollow);
		BufferedReader br2 = fu.readFile(fromResult);
		BufferedWriter bw1 = fu.writeFile(toResult);
		Map<String,String> followers = new HashMap<String,String>();
		String line;
		String[] words;
		while((line = br1.readLine()) != null){
			words = line.split(",");
			followers.put(words[1], words[2]);
		}
		br1.close();
		int i = 1;
		while((line = br2.readLine()) != null){
			words = line.split(":");
			String nickname = followers.get(words[1]);
			bw1.write(i+":"+words[1]+":"+nickname+":"+words[2]);
			bw1.newLine();
			i++;
		}
		br2.close();
		bw1.close();
	}
	
	/**
	 * 获取结果中一个uid在不同算法中的不同等级
	 * @param result1
	 * @param result2
	 * @param toRank
	 * @throws IOException
	 */
	public void getRank(String result1,String result2,String toRank) throws IOException{
		FileUtil fu = new FileUtil();
		BufferedReader br1 = fu.readFile(result1);
		BufferedReader br2 = fu.readFile(result2);
		BufferedWriter bw1 = fu.writeFile(toRank);
		Map<String,String> rank1 = new HashMap<String,String>();
		String line;
		String[] words;
		while((line = br1.readLine()) != null){
			words = line.split(":");
			rank1.put(words[1], words[0]);
		}
		br1.close();
		bw1.write("  uid:  "+result1+":"+result2);
		bw1.newLine();
		while((line = br2.readLine()) != null){
			words = line.split(":");
			String rank_1 = rank1.get(words[1]);
			bw1.write(words[1]+":"+ words[0]+":"+rank_1);
			bw1.newLine();
		}
		br2.close();
		bw1.close();
	}
	
	/**
	 * 计算斯皮尔曼等级相关系数
	 * @param fromRank
	 * @throws IOException
	 */
	public void computeCorrelation(String fromRank) throws IOException{
		FileUtil fu = new FileUtil();
		BufferedReader br1 = fu.readFile(fromRank);
		String line;
		String[] words;
		double sum = 0;
		double n = 0;
		double rho;
		br1.readLine();
		while((line = br1.readLine()) != null){
			System.out.println(n+"行...");
			words = line.split(":");
			
			sum += Math.pow((Integer.parseInt(words[1])-Integer.parseInt(words[2])),2);
			n++;
		}
		br1.close();
		rho = 1-(6*sum)/(n*(n*n-1));
		System.out.println(rho);
	}
	
	
	/**
	 * 将通过retweet计算的结果和comment计算的结果进行结合
	 * @param result1
	 * @param result2
	 * @param toRank
	 * @throws IOException
	 */
	public void mergePR(String result1,String result2,String toRank) throws IOException{
		FileUtil fu = new FileUtil();
		BufferedReader br1 = fu.readFile(result1);
		BufferedReader br2 = fu.readFile(result2);
		BufferedWriter bw1 = fu.writeFile(toRank);
		Map<String,String> rank1 = new HashMap<String,String>();
		Map<String,String> rank2 = new HashMap<String,String>();
		String line,rank_1;
		String[] words;
		while((line = br1.readLine()) != null){         //将文件一中的所有id和值保存到map中
			words = line.split(":");
			rank1.put(words[1], words[2]);
		}
		br1.close();
	
		while((line = br2.readLine()) != null){
			words = line.split(":");
			if(rank1.containsKey(words[1])){           //如果文件二中的id已存在，则两个值相加，否则添加到map中
				rank_1 = rank1.get(words[1])+":"+words[2];
				rank1.put(words[1], rank_1);
			}
			else
				rank1.put(words[1], words[2]);
		}
		br2.close();
		
		Set set = rank1.entrySet();
		Iterator it = set.iterator();
		while(it.hasNext()){
			bw1.write(it.next().toString());
			bw1.newLine();
		System.out.println( it.next().toString());
		}
		bw1.close();
	}
	
	/**
	 * 查找到需要评估的人的粉丝数
	 * @param fromfile1
	 * @param fromfile2
	 * @param tofile
	 * @throws IOException
	 */
	public void getFollowers(String fromfile1,String fromfile2,String tofile) throws IOException{
		FileUtil fu = new FileUtil();
		BufferedReader br1 = fu.readFile(fromfile1);
		BufferedReader br2 = fu.readFile(fromfile2);
		BufferedWriter bw1 = fu.writeFile(tofile);
		Map<String,String> rank1 = new HashMap<String,String>();
		Map<String,String> rank2 = new HashMap<String,String>();
		String line,rank_1;
		String[] words;
		while((line = br1.readLine()) != null){         //将文件一中的所有id和值保存到map中
			words = line.split(",");
			rank1.put(words[1], words[3]);
		}
		br1.close();
		while((line = br2.readLine()) != null){
			bw1.write(line+":"+rank1.get(line));
			bw1.newLine();
		}
		br2.close();
		bw1.close();
	}
	public static void main(String[] args) throws IOException{
		DataAnalyze da = new DataAnalyze();
		//da.merge();
		
		String fromPath = "DATA/粉丝分析/评估的105人/uid-fid-fnickname-ffcount-ftcount-deletedumplicateline.txt";
		String fromPath1 = "DATA/粉丝分析/评估的105人/粉丝.txt";
		String tofile = "DATA/粉丝分析/评估的105人/粉丝数.txt";
		String toPath1 = "DATA/粉丝分析/uid-fid-fnickname-ffcount-ftcount(1-3)-deleted.txt";
		String toPath2 = "DATA/粉丝分析/FollowernumberAndTimes.txt";
		String toPath3 = "DATA/粉丝分析/FollowerAndWeiboNumber.txt";
//		String fromPath1 = "DATA/微博分析/uid-fnickname-wid-text-source-date-shoucang-retweetcount-commentcount3.txt";
		String toPath4 = "DATA/微博分析/problemWeibo1.txt";
		String toPath5 = "DATA/微博分析/TextContainUrl1.txt";
		//da.deleteDumplicateIds(fromPath, toPath1);
//		da.countFollower(toPath1, toPath2);
		//da.followerAndWeiboCount(toPath1, toPath3);
		//da.getCollection(fromPath1, toPath4);
		//da.getUrl(fromPath1, toPath4,toPath5);
		//da.getRetweetAndOriginal(fromPath1, toPath1, toPath2);
		
//		da.getRetweetAndAt(fromPath1);
		
//		da.findNickName(toPath1,"DATA/结果分析/HITS.txt", "DATA/结果分析/HITS1.txt");
//		da.findNickName(toPath1,"DATA/结果分析/WPR.txt", "DATA/结果分析/WPR1.txt");
//		
//		da.getRank("DATA/结果分析/new/hits10505.txt", "DATA/结果分析/new/fr.txt", "DATA/结果分析/new/hits_fr_10505.txt");
		
		da.computeCorrelation("DATA/结果分析/new/wpr_abn_111.txt");
		
//		da.mergePR("DATA/结果分析/ABM/RABM.txt", "DATA/结果分析/ABM/CABM.txt", "DATA/结果分析/ABM/ABN.txt");
		
//		da.getFollowers(fromPath, fromPath1, tofile);
	}

}
