package cn.edu.ccnu.computehits;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ComputeHits {
	
	public static void main(String[] args) throws IOException{
		
		String followFile = "DATA/105��/followmatrix.txt"; 
		String retweetFile = "DATA/105��/retweetmatrix.txt";
		String commentFile = "DATA/105��/commentmatrix.txt";
//		
//		String followFile = "DATA/follow/followmatrix.txt"; 
//		String retweetFile = "DATA/retweet/retweetmatrix.txt";
//		String commentFile = "DATA/comment/commentmatrix.txt";
		String fromFile = followFile;                       																					//�������ļ���һ����ͬ������
		double alpha = 1,beta = 0.1,gama = 0.1;
		MergeMatrix mm = new MergeMatrix();
		List<Edge> edges = mm.mergeEdges(followFile, retweetFile, commentFile, alpha,beta,gama);      //��ȡ���������Ϻ�����еı�
		List<Node> allNodes = mm.assignId(edges);																			//��ȡ������������еķ��ظ���
		List<Node> toUsers = mm.getToUsers(followFile,retweetFile,commentFile);													//�������˽ڵ�
		int[] tUsers = mm.getTUsers(allNodes,toUsers);																		//λ�ö�Ӧ����
		double[][] matrix = mm.initialMatrix(allNodes, toUsers, tUsers, edges);										//�����Ĺ�ϵ����
		double[][] weightMatrix = mm.weightMatrix(matrix, tUsers);													//ͨ����ϵ����õ���Ȩ�ؾ���
		
		int iterator = 8,tops = 853;
		Hits hits = new Hits(weightMatrix,allNodes,tUsers);
		hits.calculateHits(iterator, tops);
		
		
		System.out.println("�ɻ�ȡ������ڴ�����"+Runtime.getRuntime().maxMemory()/1024/1024+"M");	
		System.out.print("��ռ���ڴ�:"); 
		System.out.println(
		Runtime.getRuntime().totalMemory()/1024/1024+"M");
	}
	
}
