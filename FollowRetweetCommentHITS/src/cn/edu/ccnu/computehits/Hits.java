package cn.edu.ccnu.computehits;

import java.util.ArrayList;
import java.util.List;



public class Hits {
	
	static List<Node> allNodes = new ArrayList<Node>();  //所有的人的点，将id标上一个号，相当于在矩阵中的维度
	static double[][] matrix;		//二维数组，构造关系矩阵
	static int[] tUsers;
	static double[] hubs;              //每个点的中心度量级别
	static double[] authorities;       //每个点的权威度量级别
	static double[] hits;
	
	/**
	 * 构造函数
	 * @param dimenssion  //矩阵的维数
	 */
	public Hits(double[][] matrix,List<Node> allNodes,int[] tUsers){
		this.tUsers = tUsers;
		this.allNodes = allNodes;
		hubs = new double[matrix.length];
		authorities = new double[matrix.length];
		hits = new double[matrix.length];
		this.matrix = matrix;
		for(int i = 0; i < matrix.length; i++){
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
	public double[] inOperate(double[] hubs,double[] authorities){	
		double normal = 0;                   //归一化
		for(int i = 0; i < matrix.length; i++){
			double sumAuth = 0;
			for(int j = 0; j < matrix[0].length; j++)
				if(i == tUsers[j]){
					for(int k = 0; k < matrix.length; k++){
						sumAuth += hubs[k]*matrix[k][j];      //所有hub值求和				
					}
				}
			authorities[i] = sumAuth;
			normal += authorities[i];
		}
		/**
		 * 归一化处理，每个值auth值都除以总的auth值
		 */
		for(int i = 0; i < matrix.length; i++)
		{
			authorities[i] = authorities[i]/normal;
		}
//		for(int i = 0; i < matrix.length; i++){
//			if(authorities[i] != 0)
//				System.out.println("auth:"+authorities[i]);
//		}
		
		return authorities;
	}
	
	
	/**
	 * 链出的操作，即hub值的更新规则：为p所链接的所有点的auth值的和
	 * 通过计算矩阵的一行的乘积和得到
	 * 同时对式子进行归一化处理
	 */
	public double[] outOperate(double[] hubs,double[] authorities){
		double normal = 0; 
		for(int i = 0; i < matrix.length; i++){
			double sumHub = 0;
			for(int j = 0; j < matrix[0].length; j++){
					sumHub += authorities[tUsers[j]]*matrix[i][j];      //所有auth值求和
			}
			hubs[i] = sumHub;
			normal += hubs[i];
		}
		/**
		 * 归一化处理，每个值hub值都除以总的hub值
		 */
//		for(int i = 0; i < matrix.length; i++){
//			hubs[i] = hubs[i]/normal;
//		}
		return hubs;
	}
	
	/**
	 * 计算最终的hits值，为hubs和authorities相加
	 * @param iterator 迭代次数
	 * @param tops 取前tops个最高的人
	 */
	public void calculateHits(int iterator,int tops){
		Order order = new Order();
		double[] hub,auth;
		for(int i = 0; i < iterator;i++){
			System.out.println("第"+(i+1)+"轮迭代：");
			hub = inOperate(hubs,authorities);
			auth = outOperate(hubs,authorities);;
		}
		//只需要找出权威的人，则先不对hits排序
//		for(int j = 0; j < matrix.length; j++){
//			hits[j] = hubs[j] + authorities[j];
//			
//		}
		
		order.orderPageRank(authorities, tops, allNodes);   
	}

}
