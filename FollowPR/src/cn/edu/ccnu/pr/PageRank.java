package cn.edu.ccnu.pr;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PageRank {
	static int dimenssion;			//矩阵的维数
	static int[][] matrix;		//二维数组，构造关系矩阵				
	static int[] outLinks;      //每个点的出链接数
	static List<Node> allNodes = new ArrayList<Node>();  //所有的人的点，将id标上一个号，相当于在矩阵中的维度
	static double[] pageRank;   //PageRank值
	static double d = 0.85;		//抑制因素，设为0.85
	boolean flag = true;		//控制循环，收敛后则为false，停止计算
	
	public PageRank()
	{}
	/**
	 * 构造函数
	 * @param dimenssion  //矩阵的维数
	 */
	public PageRank(int dimenssion,int[][] matrix,List<Node> allNodes)
	{
		this.dimenssion = dimenssion;
		this.matrix = matrix;
		this.allNodes = allNodes;
		pageRank = new double[dimenssion];
		for(int i = 0; i < dimenssion; i++)
			pageRank[i] = 1/dimenssion;           //将每个页面的初始PR值设为1
	}
		
	/**
	 * 
	 * 计算每一个点的链出数
	 * 
	 */
	public void OutLinks()
	{
		outLinks = new int[dimenssion];
		for(int i = 0;i < dimenssion; i++)
		{
			for(int j = 0; j < dimenssion; j++)
			{
				outLinks[i] += matrix[i][j];
			}
		}
	}
	
	/**
	 * 获取两个数组的最大的差值
	 * @param pr1
	 * @param pr2
	 * @return
	 */
	public double getMax(double[] pr1,double[] pr2)
	{
		double[] substract = new double[pr1.length];
		for(int i = 0; i < pr1.length; i++)
		{
			substract[i] = Math.abs(pr2[i] - pr1[i]);
		}
		java.util.Arrays.sort(substract);         //排序
		System.out.println(substract[pr1.length-1]);
		return substract[pr1.length-1];
	}
	/**
	 * 计算每一个页面PR值
	 */
	public double[] CalculatePR(double[] pagerank)
	{
		double totle = 0;
		double[] pr = new double[dimenssion];
		for(int i = 0;i<dimenssion;i++)
			pr[i] = pagerank[i];
		//System.out.println(pagerank1[0]);
		for(int j = 0; j < dimenssion; j++)
		{
			double sum = 0;
			for(int k = 0; k < dimenssion; k++)
			{
				
				if(matrix[k][j] != 0)
					sum += pageRank[k]*matrix[k][j]/outLinks[k]; //计算各个PR总和
			}
			pageRank[j] = (1-d) + d*sum;                  //PR的计算
			totle += pageRank[j];
		}
		
		//归一化处理
		for(int i = 0; i < dimenssion; i++)
		{
			pageRank[i] = pageRank[i]/totle;
		}
		
		if(getMax(pr,pageRank) < Math.pow(10, -15))//收敛条件，两次的PR值的差的绝对值小于0.0000000001
			flag = false;
		else
			flag = true;
		return pageRank;
	}
	
	/**
	 * 
	 * 迭代计算直到收敛
	 * @param counts 取前counts个结果
	 */
	public void CalculateResult(int counts)
	{
		double[] pageRanks = pageRank;
		int i = 0;
		while(flag)
		{
			System.out.println("第"+(i+1)+"轮迭代:");
			pageRanks = CalculatePR(pageRanks);           //循环调用计算PR值
			System.out.println();
			i++;
		}
		
//		for(int j = 0; j < dimenssion; j++)
//		{
//			System.out.println("最终的结果为：");
//			//System.out.println((j+1)+"-----"+pageRanks[j]);
//		}
		orderPageRank(pageRanks,counts);	
	}
	
	/**
	 * 对PR值进行排序，从而找到跟PR值对应的用户的id，id存放在allNodes中
	 * 需要先找到PR与NO的对应，然后才能根据NO找出对应的id
	 * @param pageRanks 获取到的PR值
	 * @param counts    取结果的前counts个人
	 */
	public void orderPageRank(double[] pageRanks,int counts)
	{
		double flag;
		Map<Integer,Double> prAndNo = new HashMap<Integer,Double>(); //将数组中的序号和PR值对应，以根据序号查找对应的微博id
		List<Map.Entry<Integer, Double>> NoToPr = new ArrayList<Map.Entry<Integer, Double>>();
		for (int i = 0; i < pageRanks.length; i++)    //将pageRanks数组的序号好pr值对应存放于map中，便于排序
		{  
			
			prAndNo.put(i, pageRanks[i]);
		}
		NoToPr = orderMap(prAndNo);         //对map进行排序
		for(int i = 0; i < counts; i++)
		{
			int number;
			number = NoToPr.get(i).getKey();      //获取NO号
			
			for(int j = 0; j < allNodes.size(); j++)  //在allNodes中找到与NO号对应的用户id号
			{
				if(number == allNodes.get(j).NO)
				{
					System.out.println("第"+(i+1)+"名为:"+allNodes.get(j).id+"--"+pageRanks[j]);
					break;
				}
			}
		}
		
//		for(int k = 0; k < prAndNo.size(); k++)
//			   System.out.println(prAndNo.);
	}
	/**
	 * 对map进行排序
	 * @param map  包含NO值和对应的PR值的map
	 */
	public List orderMap(Map map)
	{
		List<Map.Entry<Integer, Double>> keyAndValue = new ArrayList<Map.Entry<Integer, Double>>(map.entrySet());
		Collections.sort(keyAndValue, new Comparator<Map.Entry<Integer, Double>>() //调用排序方法
		{   
		    public int compare(Map.Entry<Integer, Double> o1, Map.Entry<Integer, Double> o2) 
		    {       
		        return (o2.getValue().compareTo(o1.getValue()));      //根据PR值进行排序       
		    }
		});
		
//		for (int i = 0; i < keyAndValue.size(); i++) {
//		    String key = keyAndValue.get(i).getKey().toString();
//		    System.out.println(key);
//		}
		
		return keyAndValue;
	}
	public static void main(String[] args) throws IOException
	{
		
		String fromPath = "DATA/334/followrelation.xml";   //原始语料的路径
		String toPath = "DATA/334/followmatrix.txt";    //将原始语料处理后换成需要的格式的文件路径
		String fromGraph = "DATA/334/followmatrix.txt";  //需要的文件路径
		int counts = 30;                                  //取结果的前15个人
		InitialMatrix im = new InitialMatrix();
		
		im.getAllIds(fromPath);
		//im.initialGraph(toPath);
		im.initialMatrix(fromGraph);		
		PageRank pg = new PageRank(im.dimenssion,im.matrix,im.allNodes);   //n维矩阵，有n个点
		pg.OutLinks();
		pg.CalculateResult(counts);

//		PageRank pg = new PageRank();
//		double[] pr2 = {1.45,2.5,3.6,5.81};
//		double[] pr1 = {1.3,2.6,3.9,5.8};
//		System.out.println(pg.getSmallestNumber(pr1, pr2));
	}
}
