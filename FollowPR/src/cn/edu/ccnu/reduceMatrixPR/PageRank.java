package cn.edu.ccnu.reduceMatrixPR;

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
	static int[][] matrix;		//��ά���飬�����ϵ����
	static double[][] weightMatrix; //��ά���飬�ߵ�Ȩ�ؾ���
	static List<Node> allNodes = new ArrayList<Node>();
	static int[] tUsers;
	static double[] pageRank;   //PageRankֵ
	static double d = 0.85;		//�������أ���Ϊ0.85
	boolean flag = true;		//����ѭ������������Ϊfalse��ֹͣ����
	
	public PageRank(int[][] matrix,double[][] weightMatrix){
		pageRank = new double[weightMatrix.length];
		this.matrix = matrix;
		this.weightMatrix = weightMatrix; 
		System.out.println("weightmatrix:"+ weightMatrix.length);
		for(int i = 0; i < weightMatrix.length; i++)
			pageRank[i] = 1; 
	}
	
	/**
	 * ��ȡ������������Ĳ�ֵ
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
		java.util.Arrays.sort(substract);         //����
		System.out.println(substract[pr1.length-1]);
		return substract[pr1.length-1];
	}

	/**
	 * ����ÿһ��ҳ��PRֵ
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
				if(j == tUsers[k]){
					int index = k;
					for(int l = 0;l < weightMatrix.length; l++){
							sum += pageRank[l]*weightMatrix[l][k]; //�������PR�ܺ�
					}
				}
			}
			pageRank[j] = (1-d) + d*sum;                  //PR�ļ���
			totle += pageRank[j];
		}
		
		//��һ������
		for(int i = 0; i < weightMatrix.length; i++)
		{
			pageRank[i] = pageRank[i]/totle;
		}

		if(getMax(pr,pageRank) < Math.pow(10, -10))//�������������ε�PRֵ�Ĳ�ľ���ֵС��0.0000000001
			flag = false;
		else
			flag = true;
//		for(int i = 0; i < weightMatrix.length; i++)
//			System.out.println(pageRank[i]);
		return pageRank;
	}
	
	/**
	 * 
	 * ��������ֱ������
	 * @param dimenssions ȡǰdimenssions�����
	 */
	public void CalculateResult(int dimenssions)
	{
		System.out.println("��ʼִ��PageRank�㷨....");
		double[] pageRanks = pageRank;
		int i = 0;
		while(flag)
		{
			System.out.println("��"+(i+1)+"�ֵ���:");
			pageRanks = CalculatePR(pageRanks);           //ѭ�����ü���PRֵ
			System.out.println();
			i++;
		}
		orderPageRank(pageRanks,dimenssions);	
	}
	
	/**
	 * ��PRֵ�������򣬴Ӷ��ҵ���PRֵ��Ӧ���û���id��id�����allNodes��
	 * ��Ҫ���ҵ�PR��NO�Ķ�Ӧ��Ȼ����ܸ���NO�ҳ���Ӧ��id
	 * @param pageRanks ��ȡ����PRֵ
	 * @param dimenssions    ȡ�����ǰdimenssions����
	 */
	public void orderPageRank(double[] pageRanks,int dimenssions)
	{
		double flag;
		Map<Integer,Double> prAndNo = new HashMap<Integer,Double>(); //�������е���ź�PRֵ��Ӧ���Ը�����Ų��Ҷ�Ӧ��΢��id
		List<Map.Entry<Integer, Double>> NoToPr = new ArrayList<Map.Entry<Integer, Double>>();
		for (int i = 0; i < pageRanks.length; i++)    //��pageRanks�������ź�prֵ��Ӧ�����map�У���������
		{  
			
			prAndNo.put(i, pageRanks[i]);
		}
		NoToPr = orderMap(prAndNo);         //��map��������
		for(int i = 0; i < dimenssions; i++)
		{
			int number;
			number = NoToPr.get(i).getKey();      //��ȡNO��
			
			for(int j = 0; j < allNodes.size(); j++)  //��allNodes���ҵ���NO�Ŷ�Ӧ���û�id��
			{
				if(number == allNodes.get(j).NO)
				{
					System.out.println("��"+(i+1)+"��Ϊ:"+allNodes.get(j).id+"--"+pageRanks[j]);
					break;
				}
			}
		}
	}
	/**
	 * ��map��������
	 * @param map  ����NOֵ�Ͷ�Ӧ��PRֵ��map
	 */
	public List orderMap(Map map)
	{
		List<Map.Entry<Integer, Double>> keyAndValue = new ArrayList<Map.Entry<Integer, Double>>(map.entrySet());
		Collections.sort(keyAndValue, new Comparator<Map.Entry<Integer, Double>>() //�������򷽷�
		{   
		    public int compare(Map.Entry<Integer, Double> o1, Map.Entry<Integer, Double> o2) 
		    {       
		        return (o2.getValue().compareTo(o1.getValue()));      //����PRֵ��������       
		    }
		});
		
		
		return keyAndValue;
	}
	public static void main(String[] args) throws IOException
	{
		
		String followFile = "DATA/334/followmatrix.txt"; 
		String fromFile = followFile;                       					//�������ļ���һ����ͬ������
		Matrix mm = new Matrix();
		List<Edge> edges = mm.getEdges(followFile);      						//��ȡ���������Ϻ�����еı�
		 allNodes = mm.assignId(edges);											//��ȡ������������еķ��ظ���
		List<Node> toUsers = mm.getToUsers( fromFile);							//�������˽ڵ�
		 tUsers = mm.getTUsers(allNodes,toUsers);								//λ�ö�Ӧ����
		int[][] matrix = mm.initialMatrix(allNodes, toUsers, tUsers, edges);	//�����Ĺ�ϵ����
		double[][] weightMatrix = mm.weightMatrix(matrix, tUsers);				//ͨ����ϵ����õ���Ȩ�ؾ���
		
		PageRank wpr = new PageRank(matrix,weightMatrix);
		wpr.CalculateResult(50);
//		pg.CalculateResult(topNumber);
//		for(int i = 0; i < dimenssion;i++)
//		{
//			for(int j = 0; j < dimenssion; j++)
//				System.out.print(matrix[i][j]);
//			System.out.println();
//		}
		System.out.println("�ɻ�ȡ������ڴ�����"+Runtime.getRuntime().maxMemory()/1024/1024+"M");	
		System.out.print("��ռ���ڴ�:"); 
		System.out.println(
		Runtime.getRuntime().totalMemory()/1024/1024+"M");
	}
}
