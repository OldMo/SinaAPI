package cn.edu.ccnu.computepr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.ccnu.pack.Edge;
import cn.edu.ccnu.pack.MergeMatrix;
import cn.edu.ccnu.pack.Node;

public class WeightPageRank {
	
	static double[][] matrix;		//二维数组，构造关系矩阵
	static double[][] weightMatrix; //二维数组，边的权重矩阵
	static List<Node> allNodes = new ArrayList<Node>();
	static int[] tUsers;
	static double[] pageRank;   //PageRank值
	static double d = 0.85;		//抑制因素，设为0.85
	boolean flag = true;		//控制循环，收敛后则为false，停止计算
	
	public WeightPageRank(double[][] matrix,double[][] weightMatrix){
		pageRank = new double[weightMatrix.length];
		this.matrix = matrix;
		this.weightMatrix = weightMatrix; 
		System.out.println("weightmatrix:"+ weightMatrix.length);
		for(int i = 0; i < weightMatrix.length; i++)
			pageRank[i] = 1; 
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
		java.util.Arrays.sort(substract);         										//排序
		System.out.println(substract[pr1.length-1]);
		return substract[pr1.length-1];
	}

	/**
	 * 计算每一个页面PR值
	 */
	public double[] CalculatePR(double[] pagerank)
	{
		double totle = 0;
		double[] pr = new double[weightMatrix.length];
		for(int i = 0;i<weightMatrix.length;i++)
			pr[i] = pagerank[i];
		
		for(int j = 0; j < weightMatrix.length; j++)
		{
			double sum = 0;
			for(int k = 0; k < tUsers.length; k++){
				if(j == tUsers[k]){                                      						//如果j为要评估的人的序号，那么就对j在tUsers中对应的列求PR总和
					for(int l = 0;l < weightMatrix.length; l++){
							sum += pageRank[l]*weightMatrix[l][k]; 		//计算各个PR总和
					}
				}
			}
			pageRank[j] = (1-d) + d*sum;                  							//PR的计算
			totle += pageRank[j];
		}
		
		//归一化处理
//		for(int i = 0; i < weightMatrix.length; i++)
//		{
//			pageRank[i] = pageRank[i]/totle;
//		}

		if(getMax(pr,pageRank) < Math.pow(10, -10))//收敛条件，两次的PR值的差的绝对值小于0.0000000001
			flag = false;
		else
			flag = true;
//		for(int i = 0; i < weightMatrix.length; i++)
//			System.out.println(pageRank[i]);
		return pageRank;
	}
	
	/**
	 * 
	 * 迭代计算直到收敛
	 * @param tops 取前tops个结果
	 */
	public void CalculateResult(int tops)
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
		orderPageRank(pageRanks,tops);	
	}

	/**
	 * 对PR值进行排序，从而找到跟PR值对应的用户的id，id存放在allNodes中
	 * 需要先找到PR与NO的对应，然后才能根据NO找出对应的id
	 * @param pageRanks 获取到的PR值
	 * @param tops    取结果的前tops个人
	 */
	public void orderPageRank(double[] pageRanks,int tops)
	{
		double flag;
		Map<Integer,Double> prAndNo = new HashMap<Integer,Double>(); //将数组中的序号和PR值对应，以根据序号查找对应的微博id
		List<Map.Entry<Integer, Double>> NoToPr = new ArrayList<Map.Entry<Integer, Double>>();
		for (int i = 0; i < pageRanks.length; i++)    //将pageRanks数组的序号好pr值对应存放于map中，便于排序
		{  
			
			prAndNo.put(i, pageRanks[i]);
		}
		NoToPr = orderMap(prAndNo);         //对map进行排序
		for(int i = 0; i < tops; i++)
		{
			int number;
			number = NoToPr.get(i).getKey();      //获取NO号
			
			for(int j = 0; j < allNodes.size(); j++)  //在allNodes中找到与NO号对应的用户id号
			{
				if(number == allNodes.get(j).NO)
				{
					System.out.println(""+(i+1)+":"+allNodes.get(j).id+":"+pageRanks[j]);
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
	
	
	public static void main(String[] args) throws IOException{
		
		String followFile = "DATA/105人/followmatrix.txt"; 
		String retweetFile = "DATA/105人/retweetmatrix.txt";
		String commentFile = "DATA/105人/commentmatrix.txt";
//		String followFile = "DATA/follow/followmatrix.txt"; 
//		String retweetFile = "DATA/retweet/retweetmatrix.txt";
//		String commentFile = "DATA/comment/commentmatrix.txt";
		String fromFile = followFile; //跟三个文件任一个相同都可以
		double alpha = 1,beta = 1,gama = 1;
		MergeMatrix mm = new MergeMatrix();
		List<Edge> edges = mm.mergeEdges(followFile, retweetFile, commentFile, alpha,beta,gama);      //获取三个网络结合后的所有的边
		allNodes = mm.assignId(edges);																			//获取三个网络的所有的非重复点
		List<Node> toUsers = mm.getToUsers(fromFile,retweetFile,commentFile);													//评估的人节点
		tUsers = mm.getTUsers(allNodes,toUsers);																		//位置对应数组
		double[][] matrix = mm.initialMatrix(allNodes, toUsers, tUsers, edges);										//构建的关系矩阵
		double[][] weightMatrix = mm.weightMatrix(matrix, tUsers);													//通过关系矩阵得到的权重矩阵
		
		WeightPageRank wpr = new WeightPageRank(matrix,weightMatrix);
		
		wpr.CalculateResult(853);
		
		
		System.out.println("可获取的最大内存数："+Runtime.getRuntime().maxMemory()/1024/1024+"M");	
		System.out.print("已占用内存:"); 
		System.out.println(
		Runtime.getRuntime().totalMemory()/1024/1024+"M");
	}
	
}
