package cn.edu.ccnu.computehits;

import java.util.ArrayList;
import java.util.List;



public class Hits {
	
	static List<Node> allNodes = new ArrayList<Node>();  //���е��˵ĵ㣬��id����һ���ţ��൱���ھ����е�ά��
	static double[][] matrix;		//��ά���飬�����ϵ����
	static int[] tUsers;
	static double[] hubs;              //ÿ��������Ķ�������
	static double[] authorities;       //ÿ�����Ȩ����������
	static double[] hits;
	
	/**
	 * ���캯��
	 * @param dimenssion  //�����ά��
	 */
	public Hits(double[][] matrix,List<Node> allNodes,int[] tUsers){
		this.tUsers = tUsers;
		this.allNodes = allNodes;
		hubs = new double[matrix.length];
		authorities = new double[matrix.length];
		hits = new double[matrix.length];
		this.matrix = matrix;
		for(int i = 0; i < matrix.length; i++){
			hubs[i] = 1;           //��ÿ��ҳ��ĳ�ʼhub��authֵ����Ϊ1
			authorities[i] = 1;
			hits[i] = 0.0;
		}
	}
	
	/**
	 * �����Ĳ�������auth�ĸ��¹���Ϊָ���p�����е��hubֵ�ĺ�
	 * ����ʱͨ����������һ�еĳ˻��͵õ�
	 * ͬʱ��ʽ�ӽ��й�һ������
	 */
	public double[] inOperate(double[] hubs,double[] authorities){	
		double normal = 0;                   //��һ��
		for(int i = 0; i < matrix.length; i++){
			double sumAuth = 0;
			for(int j = 0; j < matrix[0].length; j++)
				if(i == tUsers[j]){
					for(int k = 0; k < matrix.length; k++){
						sumAuth += hubs[k]*matrix[k][j];      //����hubֵ���				
					}
				}
			authorities[i] = sumAuth;
			normal += authorities[i];
		}
		/**
		 * ��һ������ÿ��ֵauthֵ�������ܵ�authֵ
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
	 * �����Ĳ�������hubֵ�ĸ��¹���Ϊp�����ӵ����е��authֵ�ĺ�
	 * ͨ����������һ�еĳ˻��͵õ�
	 * ͬʱ��ʽ�ӽ��й�һ������
	 */
	public double[] outOperate(double[] hubs,double[] authorities){
		double normal = 0; 
		for(int i = 0; i < matrix.length; i++){
			double sumHub = 0;
			for(int j = 0; j < matrix[0].length; j++){
					sumHub += authorities[tUsers[j]]*matrix[i][j];      //����authֵ���
			}
			hubs[i] = sumHub;
			normal += hubs[i];
		}
		/**
		 * ��һ������ÿ��ֵhubֵ�������ܵ�hubֵ
		 */
//		for(int i = 0; i < matrix.length; i++){
//			hubs[i] = hubs[i]/normal;
//		}
		return hubs;
	}
	
	/**
	 * �������յ�hitsֵ��Ϊhubs��authorities���
	 * @param iterator ��������
	 * @param tops ȡǰtops����ߵ���
	 */
	public void calculateHits(int iterator,int tops){
		Order order = new Order();
		double[] hub,auth;
		for(int i = 0; i < iterator;i++){
			System.out.println("��"+(i+1)+"�ֵ�����");
			hub = inOperate(hubs,authorities);
			auth = outOperate(hubs,authorities);;
		}
		//ֻ��Ҫ�ҳ�Ȩ�����ˣ����Ȳ���hits����
//		for(int j = 0; j < matrix.length; j++){
//			hits[j] = hubs[j] + authorities[j];
//			
//		}
		
		order.orderPageRank(authorities, tops, allNodes);   
	}

}
