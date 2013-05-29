package cn.edu.ccnu.weightpr;

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

public class WeightedPageRank {
	private static int dimenssion;			//矩阵的维数
	static int[][] matrix;		//二维数组，构造关系矩阵
	//static double[][] weightMatrix; //二维数组，边的权重矩阵
	static double[] inLinks;      //每个点的入链接数
	static double[] outLinks;      //每个点的出链接数
	static double[] sumIn;
	static double[] sumOut;
	static List<Node> allNodes = new ArrayList<Node>();  //所有的人的点，将id标上一个号，相当于在矩阵中的维度
	static double[] pageRank;   //PageRank值
	static double d = 0.85;		//抑制因素，设为0.85
	boolean flag = true;		//控制循环，收敛后则为false，停止计算
	
	/**
	 * 构造函数
	 * @param dimenssion  //矩阵的维数
	 */
	public WeightedPageRank(int dimenssion,int[][] matrix,List<Node> allNodes)
	{
		this.dimenssion = dimenssion;
		this.matrix = matrix;
		this.allNodes = allNodes;
		pageRank = new double[dimenssion];
		for(int i = 0; i < dimenssion; i++)
			pageRank[i] = 1/dimenssion;           //将每个页面的初始PR值设为1
	}
		
	
	/**
	 * 计算每个点的入链接数
	 * 即，计算矩阵中每一列的和
	 */
	public void inLinks()
	{
		inLinks = new double[dimenssion];
		for(int i = 0;i < dimenssion; i++)
		{
			for(int j = 0; j < dimenssion; j++)
			{
				if(matrix[j][i] != 0)
					inLinks[i] += matrix[j][i];
			}
//			if(inLinks[i] > 0)
//			System.out.println(inLinks[i]);
		}
	}
	/**
	 * 
	 * 计算每一个点的链出数
	 * 
	 */
	public void outLinks()
	{
		outLinks = new double[dimenssion];
		for(int i = 0;i < dimenssion; i++)
		{
			for(int j = 0; j < dimenssion; j++)
			{
				if(matrix[i][j] != 0)
					outLinks[i] += matrix[i][j];
			}
			//System.out.println(outLinks[i]);
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
	 * 构建权重矩阵，根据关系矩阵，有边则计算该边的权重
	 * 由入度权重和出度权重相乘得到
	 * 入度权重计算方法为：(i,j)边的入度权重为i指向的边j的入度除以i指向的所有点的入度和
	 * 出度权重计算方法为：(i,j)边的出度权重为i指向的边j的出度除以i指向的所有点的出度和
	 */
	public void weightMatrix()
	{
		for(int i = 0;i < dimenssion;i++)
		{
			for(int j = 0; j < dimenssion; j++)
			{
				if(matrix[i][j] != 0)               //有边，则统计起始点的所指向的点的入度和出度和
				{
					sumIn[i] += inLinks[j];         //统计点i指向的所有点的入度和
					sumOut[i] += outLinks[j];       //统计点i指向的所有点的出度和
				}
			}
		}
		
//		//构建权重矩阵
//		for(int i = 0;i < dimenssion;i++)
//		{
//			for(int j = 0; j < dimenssion; j++)
//			{
//				if(matrix[i][j] != 0)               //有边，则计算该边的权重
//				{
//					if(sumIn[i] > 0 && sumOut[i] > 0)
//						weightMatrix[i][j] = (inLinks[j]/sumIn[i])*(outLinks[j]/sumOut[i]);
//					//System.out.println(weightMatrix[i][j]);
//				}
//			}
//		}
		
		
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
		
		for(int j = 0; j < dimenssion; j++)
		{
			double sum = 0;
			for(int k = 0; k < dimenssion; k++)
			{
				
				if(matrix[k][j] != 0)
					sum += pageRank[k]*(inLinks[j]/sumIn[k])*(outLinks[j]/sumOut[k]); //计算各个PR总和
			}
			pageRank[j] = (1-d) + d*sum;                  //PR的计算
			totle += pageRank[j];
		}
		
		//归一化处理
//		for(int i = 0; i < dimenssion; i++)
//		{
//			pageRank[i] = pageRank[i]/totle;
//		}

		if(getMax(pr,pageRank) < Math.pow(10, -10))//收敛条件，两次的PR值的差的绝对值小于0.0000000001
			flag = false;
		else
			flag = true;
		return pageRank;
	}
	
	/**
	 * 
	 * 迭代计算直到收敛
	 * @param dimenssions 取前dimenssions个结果
	 */
	public void CalculateResult(int dimenssions)
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
		orderPageRank(pageRanks,dimenssions);	
	}
	
	/**
	 * 对PR值进行排序，从而找到跟PR值对应的用户的id，id存放在allNodes中
	 * 需要先找到PR与NO的对应，然后才能根据NO找出对应的id
	 * @param pageRanks 获取到的PR值
	 * @param dimenssions    取结果的前dimenssions个人
	 */
	public void orderPageRank(double[] pageRanks,int dimenssions)
	{
		double flag;
		Map<Integer,Double> prAndNo = new HashMap<Integer,Double>(); //将数组中的序号和PR值对应，以根据序号查找对应的微博id
		List<Map.Entry<Integer, Double>> NoToPr = new ArrayList<Map.Entry<Integer, Double>>();
		for (int i = 0; i < pageRanks.length; i++)    //将pageRanks数组的序号好pr值对应存放于map中，便于排序
		{  
			
			prAndNo.put(i, pageRanks[i]);
		}
		NoToPr = orderMap(prAndNo);         //对map进行排序
		for(int i = 0; i < dimenssions; i++)
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
		
		
		return keyAndValue;
	}
	public static void main(String[] args) throws IOException
	{
		
		String fromPath = "DATA/11/focusrelationship.xml";   //原始语料的路径
		String fromGraph = "DATA/11/followmatrix.txt";  //需要的文件路径
		int topNumber = 15;                                  //取结果的前15个人
		InitialFollowMatrix ifm = new InitialFollowMatrix();
		
		ifm.getAllIds(fromPath);
		//im.initialGraph(toPath);
		ifm.initialMatrix(fromGraph);
		
		PageRank pg = new PageRank(ifm.dimenssion,ifm.matrix,ifm.allNodes);   //n维矩阵，有n个点
		pg.inLinks();
		pg.outLinks();
		pg.weightMatrix();
		//pg.CalculateResult(topNumber);
//		for(int i = 0; i < dimenssion;i++)
//		{
//			for(int j = 0; j < dimenssion; j++)
//				System.out.print(matrix[i][j]);
//			System.out.println();
//		}
		System.out.println("可获取的最大内存数："+Runtime.getRuntime().maxMemory()/1024/1024+"M");	
		System.out.println("已占用内存:"+Runtime.getRuntime().totalMemory()/1024/1024+"M"); 
	}
}
