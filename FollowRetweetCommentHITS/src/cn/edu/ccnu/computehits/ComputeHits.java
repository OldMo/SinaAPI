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
		
		String followFile = "DATA/105人/followmatrix.txt"; 
		String retweetFile = "DATA/105人/retweetmatrix.txt";
		String commentFile = "DATA/105人/commentmatrix.txt";
//		
//		String followFile = "DATA/follow/followmatrix.txt"; 
//		String retweetFile = "DATA/retweet/retweetmatrix.txt";
//		String commentFile = "DATA/comment/commentmatrix.txt";
		String fromFile = followFile;                       																					//跟三个文件任一个相同都可以
		double alpha = 1,beta = 0.1,gama = 0.1;
		MergeMatrix mm = new MergeMatrix();
		List<Edge> edges = mm.mergeEdges(followFile, retweetFile, commentFile, alpha,beta,gama);      //获取三个网络结合后的所有的边
		List<Node> allNodes = mm.assignId(edges);																			//获取三个网络的所有的非重复点
		List<Node> toUsers = mm.getToUsers(followFile,retweetFile,commentFile);													//评估的人节点
		int[] tUsers = mm.getTUsers(allNodes,toUsers);																		//位置对应数组
		double[][] matrix = mm.initialMatrix(allNodes, toUsers, tUsers, edges);										//构建的关系矩阵
		double[][] weightMatrix = mm.weightMatrix(matrix, tUsers);													//通过关系矩阵得到的权重矩阵
		
		int iterator = 8,tops = 853;
		Hits hits = new Hits(weightMatrix,allNodes,tUsers);
		hits.calculateHits(iterator, tops);
		
		
		System.out.println("可获取的最大内存数："+Runtime.getRuntime().maxMemory()/1024/1024+"M");	
		System.out.print("已占用内存:"); 
		System.out.println(
		Runtime.getRuntime().totalMemory()/1024/1024+"M");
	}
	
}
