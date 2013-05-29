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
	private static int dimenssion;			//�����ά��
	static int[][] matrix;		//��ά���飬�����ϵ����
	//static double[][] weightMatrix; //��ά���飬�ߵ�Ȩ�ؾ���
	static double[] inLinks;      //ÿ�������������
	static double[] outLinks;      //ÿ����ĳ�������
	static double[] sumIn;
	static double[] sumOut;
	static List<Node> allNodes = new ArrayList<Node>();  //���е��˵ĵ㣬��id����һ���ţ��൱���ھ����е�ά��
	static double[] pageRank;   //PageRankֵ
	static double d = 0.85;		//�������أ���Ϊ0.85
	boolean flag = true;		//����ѭ������������Ϊfalse��ֹͣ����
	
	/**
	 * ���캯��
	 * @param dimenssion  //�����ά��
	 */
	public WeightedPageRank(int dimenssion,int[][] matrix,List<Node> allNodes)
	{
		this.dimenssion = dimenssion;
		this.matrix = matrix;
		this.allNodes = allNodes;
		pageRank = new double[dimenssion];
		for(int i = 0; i < dimenssion; i++)
			pageRank[i] = 1/dimenssion;           //��ÿ��ҳ��ĳ�ʼPRֵ��Ϊ1
	}
		
	
	/**
	 * ����ÿ�������������
	 * �������������ÿһ�еĺ�
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
	 * ����ÿһ�����������
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
	 * ����Ȩ�ؾ��󣬸��ݹ�ϵ�����б������ñߵ�Ȩ��
	 * �����Ȩ�غͳ���Ȩ����˵õ�
	 * ���Ȩ�ؼ��㷽��Ϊ��(i,j)�ߵ����Ȩ��Ϊiָ��ı�j����ȳ���iָ������е����Ⱥ�
	 * ����Ȩ�ؼ��㷽��Ϊ��(i,j)�ߵĳ���Ȩ��Ϊiָ��ı�j�ĳ��ȳ���iָ������е�ĳ��Ⱥ�
	 */
	public void weightMatrix()
	{
		for(int i = 0;i < dimenssion;i++)
		{
			for(int j = 0; j < dimenssion; j++)
			{
				if(matrix[i][j] != 0)               //�бߣ���ͳ����ʼ�����ָ��ĵ����Ⱥͳ��Ⱥ�
				{
					sumIn[i] += inLinks[j];         //ͳ�Ƶ�iָ������е����Ⱥ�
					sumOut[i] += outLinks[j];       //ͳ�Ƶ�iָ������е�ĳ��Ⱥ�
				}
			}
		}
		
//		//����Ȩ�ؾ���
//		for(int i = 0;i < dimenssion;i++)
//		{
//			for(int j = 0; j < dimenssion; j++)
//			{
//				if(matrix[i][j] != 0)               //�бߣ������ñߵ�Ȩ��
//				{
//					if(sumIn[i] > 0 && sumOut[i] > 0)
//						weightMatrix[i][j] = (inLinks[j]/sumIn[i])*(outLinks[j]/sumOut[i]);
//					//System.out.println(weightMatrix[i][j]);
//				}
//			}
//		}
		
		
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
		
		for(int j = 0; j < dimenssion; j++)
		{
			double sum = 0;
			for(int k = 0; k < dimenssion; k++)
			{
				
				if(matrix[k][j] != 0)
					sum += pageRank[k]*(inLinks[j]/sumIn[k])*(outLinks[j]/sumOut[k]); //�������PR�ܺ�
			}
			pageRank[j] = (1-d) + d*sum;                  //PR�ļ���
			totle += pageRank[j];
		}
		
		//��һ������
//		for(int i = 0; i < dimenssion; i++)
//		{
//			pageRank[i] = pageRank[i]/totle;
//		}

		if(getMax(pr,pageRank) < Math.pow(10, -10))//�������������ε�PRֵ�Ĳ�ľ���ֵС��0.0000000001
			flag = false;
		else
			flag = true;
		return pageRank;
	}
	
	/**
	 * 
	 * ��������ֱ������
	 * @param dimenssions ȡǰdimenssions�����
	 */
	public void CalculateResult(int dimenssions)
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
		
		String fromPath = "DATA/11/focusrelationship.xml";   //ԭʼ���ϵ�·��
		String fromGraph = "DATA/11/followmatrix.txt";  //��Ҫ���ļ�·��
		int topNumber = 15;                                  //ȡ�����ǰ15����
		InitialFollowMatrix ifm = new InitialFollowMatrix();
		
		ifm.getAllIds(fromPath);
		//im.initialGraph(toPath);
		ifm.initialMatrix(fromGraph);
		
		PageRank pg = new PageRank(ifm.dimenssion,ifm.matrix,ifm.allNodes);   //nά������n����
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
		System.out.println("�ɻ�ȡ������ڴ�����"+Runtime.getRuntime().maxMemory()/1024/1024+"M");	
		System.out.println("��ռ���ڴ�:"+Runtime.getRuntime().totalMemory()/1024/1024+"M"); 
	}
}
