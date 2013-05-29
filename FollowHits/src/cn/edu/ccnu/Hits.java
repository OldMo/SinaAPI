package cn.edu.ccnu;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.ccnu.reducehits.FileUtil;

public class Hits {
	private static int dimenssion;			//矩阵的维度,点的个数
	static List<Node> allNodes = new ArrayList<Node>();  //所有的人的点，将id标上一个号，相当于在矩阵中的维度
	static int[][] matrix;		//二维数组，构造关系矩阵
	static double[] hubs;              //每个点的中心度量级别
	static double[] authorities;       //每个点的权威度量级别
	static double[] hits;
	
	/**
	 * 构造函数
	 * @param dimenssion  //矩阵的维数
	 */
	public Hits(int dimenssion,int[][] matrix,List<Node> allNodes)
	{
		Hits.dimenssion = dimenssion;
		Hits.allNodes = allNodes;
		hubs = new double[dimenssion];
		authorities = new double[dimenssion];
		hits = new double[dimenssion];
		Hits.matrix = matrix;
		for(int i = 0; i < dimenssion; i++)
		{
			hubs[i] = 1;           //将每个页面的初始hub和auth值都设为1
			authorities[i] = 1;
			hits[i] = 0.0;
		}
	}
	
	/**
	 * 链入点的操作，即auth的更新规则：为指向点p的所有点的hub值的和
	 * 计算时通过计算矩阵的一列的乘积和得到
	 * 同时对式子进行归一化处理
	 */
	public void inOperate(double[] hubs,double[] authorities)
	{
		
		double normal = 0;                   //归一化
		for(int i = 0; i < dimenssion; i++)
		{
			double sumAuth = 0;
			for(int j = 0; j < dimenssion; j++)
			{
					sumAuth += hubs[j]*matrix[j][i];      //所有hub值求和				
			}
//			if(sumAuth == 0)
//				authorities[i] = 0;
//			else
			authorities[i] = sumAuth;
			
			normal += authorities[i];
		}
		/**
		 * 归一化处理，每个值auth值都除以总的auth值
		 */
		for(int i = 0; i < dimenssion; i++)
		{
			authorities[i] = authorities[i]/normal;
		}
	}
	
	
	/**
	 * 链出的操作，即hub值的更新规则：为p所链接的所有点的auth值的和
	 * 通过计算矩阵的一行的乘积和得到
	 * 同时对式子进行归一化处理
	 */
	public void outOperate(double[] hubs,double[] authorities)
	{
		double normal = 0; 
		for(int i = 0; i < dimenssion; i++)
		{
			double sumHub = 0;
			for(int j = 0; j < dimenssion; j++)
			{
					sumHub += authorities[j]*matrix[i][j];      //所有auth值求和
			}
//			if(sumHub == 0)
//				hubs[i] = 0;
//			else
				hubs[i] = sumHub;
			normal += hubs[i];
		}
		/**
		 * 归一化处理，每个值hub值都除以总的hub值
		 */
		for(int i = 0; i < dimenssion; i++)
		{
			hubs[i] = hubs[i]/normal;
		}
	}
	
	/**
	 * 计算最终的hits值，为hubs和authorities相加
	 * @param iterator 迭代次数
	 */
	public void calculateHits(int iterator)
	{
		for(int i = 0; i < iterator;i++)
		{
			inOperate(hubs,authorities);
			outOperate(hubs,authorities);
		}
		for(int j = 0; j < dimenssion; j++)
		{
			hits[j] = hubs[j] + authorities[j];
			//System.out.println("result:"+ hits[j]);
		}
	}
	
	/**
	 * 对Hits值进行排序，从而找到跟Hits值对应的用户的id，id存放在allNodes中
	 * 需要先找到Hits与NO的对应，然后才能根据NO找出对应的id
	 * @param Hits 获取到的Hits值
	 * @param counts    取结果的前counts个人
	 */
	public void orderHits(double[] hits,int counts)
	{
		double flag;
		Map<Integer,Double> hitsAndNo = new HashMap<Integer,Double>(); //将数组中的序号和PR值对应，以根据序号查找对应的微博id
		List<Map.Entry<Integer, Double>> NoToHits = new ArrayList<Map.Entry<Integer, Double>>();
		for (int i = 0; i < hits.length; i++)    //将Hits数组的序号好pr值对应存放于map中，便于排序
		{  
			
			hitsAndNo.put(i, hits[i]);
		}
			
		NoToHits = orderMap(hitsAndNo);         //对map进行排序
		
		for(int i = 0; i < counts; i++)
		{
			int number;
			number = NoToHits.get(i).getKey();      //获取NO号
			
			for(int j = 0; j < allNodes.size(); j++)  //在allNodes中找到与NO号对应的用户id号
			{
				if(number == allNodes.get(j).NO)
				{
					System.out.println("第"+(i+1)+"名为:"+allNodes.get(j).id+":"+hits[j]);
					break;
				}
			}
		}
		
//		for(int k = 0; k < prAndNo.size(); k++)
//			   System.out.println(prAndNo.);
	}
	/**
	 * 对map进行排序
	 * @param map  包含NO值和对应的Hits值的map
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
	
	/**
	 * 将最终的结果排序
	 */
	public void order()
	{
		double flag;
		for(int i = 0; i < hits.length; i++)
			for(int j = i+1; j < hits.length; j++)
			{
				if(hits[i] < hits[j])
				{
					flag = hits[i];
					hits[i] = hits[j];
					hits[j] = flag;
				}
			}
		for(int i = 0; i < hits.length; i++)
			System.out.println(hits[i]);
	}
	public static void main(String[] args) throws IOException
	{
		int counts = 15;         //取钱counts个排名的id
		int iterator = 10;       //迭代次数，一般设为20
		String fromPath = "DATA/10/focusrelationship.xml";
		String toPath = "DATA/10/focusrelationship.txt";
		String fromGraph = "DATA/10/focusrelationship.txt";
		InitialMatrix im = new InitialMatrix();
		im.getAllIds(fromPath);
		im.initialGraph(toPath);
		im.initialMatrix(fromGraph);
		
		
		Hits hits = new Hits(InitialMatrix.dimenssion,InitialMatrix.matrix,InitialMatrix.allNodes);
		hits.calculateHits(iterator);
		hits.orderHits(Hits.hits, counts);
//		hits.orderHits(Hits.hubs, counts);
//		hits.orderHits(Hits.authorities, counts);
		
//		BufferedWriter bw = new FileUtil().writeFile("DATA/hitmatrix.txt");
//		for(int i = 0; i < InitialMatrix.matrix.length;i++)
//		{
//			for(int j = 0; j < InitialMatrix.matrix.length; j++){
//				//if(InitialMatrix.matrix[i][j] != 0)
//					//System.out.print(matrix[i][j]);
//				bw.write(InitialMatrix.matrix[i][j]+" ");
//			}
//			bw.newLine();
//				
//			//System.out.println();
//		}
//		bw.close();
			
	}
}
