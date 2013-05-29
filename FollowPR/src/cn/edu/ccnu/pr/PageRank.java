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
	static int dimenssion;			//�����ά��
	static int[][] matrix;		//��ά���飬�����ϵ����				
	static int[] outLinks;      //ÿ����ĳ�������
	static List<Node> allNodes = new ArrayList<Node>();  //���е��˵ĵ㣬��id����һ���ţ��൱���ھ����е�ά��
	static double[] pageRank;   //PageRankֵ
	static double d = 0.85;		//�������أ���Ϊ0.85
	boolean flag = true;		//����ѭ������������Ϊfalse��ֹͣ����
	
	public PageRank()
	{}
	/**
	 * ���캯��
	 * @param dimenssion  //�����ά��
	 */
	public PageRank(int dimenssion,int[][] matrix,List<Node> allNodes)
	{
		this.dimenssion = dimenssion;
		this.matrix = matrix;
		this.allNodes = allNodes;
		pageRank = new double[dimenssion];
		for(int i = 0; i < dimenssion; i++)
			pageRank[i] = 1/dimenssion;           //��ÿ��ҳ��ĳ�ʼPRֵ��Ϊ1
	}
		
	/**
	 * 
	 * ����ÿһ�����������
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
					sum += pageRank[k]*matrix[k][j]/outLinks[k]; //�������PR�ܺ�
			}
			pageRank[j] = (1-d) + d*sum;                  //PR�ļ���
			totle += pageRank[j];
		}
		
		//��һ������
		for(int i = 0; i < dimenssion; i++)
		{
			pageRank[i] = pageRank[i]/totle;
		}
		
		if(getMax(pr,pageRank) < Math.pow(10, -15))//�������������ε�PRֵ�Ĳ�ľ���ֵС��0.0000000001
			flag = false;
		else
			flag = true;
		return pageRank;
	}
	
	/**
	 * 
	 * ��������ֱ������
	 * @param counts ȡǰcounts�����
	 */
	public void CalculateResult(int counts)
	{
		double[] pageRanks = pageRank;
		int i = 0;
		while(flag)
		{
			System.out.println("��"+(i+1)+"�ֵ���:");
			pageRanks = CalculatePR(pageRanks);           //ѭ�����ü���PRֵ
			System.out.println();
			i++;
		}
		
//		for(int j = 0; j < dimenssion; j++)
//		{
//			System.out.println("���յĽ��Ϊ��");
//			//System.out.println((j+1)+"-----"+pageRanks[j]);
//		}
		orderPageRank(pageRanks,counts);	
	}
	
	/**
	 * ��PRֵ�������򣬴Ӷ��ҵ���PRֵ��Ӧ���û���id��id�����allNodes��
	 * ��Ҫ���ҵ�PR��NO�Ķ�Ӧ��Ȼ����ܸ���NO�ҳ���Ӧ��id
	 * @param pageRanks ��ȡ����PRֵ
	 * @param counts    ȡ�����ǰcounts����
	 */
	public void orderPageRank(double[] pageRanks,int counts)
	{
		double flag;
		Map<Integer,Double> prAndNo = new HashMap<Integer,Double>(); //�������е���ź�PRֵ��Ӧ���Ը�����Ų��Ҷ�Ӧ��΢��id
		List<Map.Entry<Integer, Double>> NoToPr = new ArrayList<Map.Entry<Integer, Double>>();
		for (int i = 0; i < pageRanks.length; i++)    //��pageRanks�������ź�prֵ��Ӧ�����map�У���������
		{  
			
			prAndNo.put(i, pageRanks[i]);
		}
		NoToPr = orderMap(prAndNo);         //��map��������
		for(int i = 0; i < counts; i++)
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
		
//		for(int k = 0; k < prAndNo.size(); k++)
//			   System.out.println(prAndNo.);
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
		
//		for (int i = 0; i < keyAndValue.size(); i++) {
//		    String key = keyAndValue.get(i).getKey().toString();
//		    System.out.println(key);
//		}
		
		return keyAndValue;
	}
	public static void main(String[] args) throws IOException
	{
		
		String fromPath = "DATA/334/followrelation.xml";   //ԭʼ���ϵ�·��
		String toPath = "DATA/334/followmatrix.txt";    //��ԭʼ���ϴ���󻻳���Ҫ�ĸ�ʽ���ļ�·��
		String fromGraph = "DATA/334/followmatrix.txt";  //��Ҫ���ļ�·��
		int counts = 30;                                  //ȡ�����ǰ15����
		InitialMatrix im = new InitialMatrix();
		
		im.getAllIds(fromPath);
		//im.initialGraph(toPath);
		im.initialMatrix(fromGraph);		
		PageRank pg = new PageRank(im.dimenssion,im.matrix,im.allNodes);   //nά������n����
		pg.OutLinks();
		pg.CalculateResult(counts);

//		PageRank pg = new PageRank();
//		double[] pr2 = {1.45,2.5,3.6,5.81};
//		double[] pr1 = {1.3,2.6,3.9,5.8};
//		System.out.println(pg.getSmallestNumber(pr1, pr2));
	}
}
