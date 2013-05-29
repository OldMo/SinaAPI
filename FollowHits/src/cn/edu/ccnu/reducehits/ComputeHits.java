package cn.edu.ccnu.reducehits;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.ccnu.InitialMatrix;


public class ComputeHits {
	
	public static void main(String[] args) throws IOException{
		
		String followFile = "DATA/334/followmatrix.txt"; 
		String fromFile = followFile;                       																					//跟三个文件任一个相同都可以
		Matrix mm = new Matrix();
		List<Edge> edges = mm.getEdges(followFile);      //获取三个网络结合后的所有的边
		List<Node> allNodes = mm.assignId(edges);																			//获取三个网络的所有的非重复点
		List<Node> toUsers = mm.getToUsers(fromFile);													//评估的人节点
		int[] tUsers = mm.getTUsers(allNodes,toUsers);																		//位置对应数组
		double[][] matrix = mm.initialMatrix(allNodes, toUsers, tUsers, edges);										//构建的关系矩阵
		//double[][] weightMatrix = mm.weightMatrix(matrix, tUsers);													//通过关系矩阵得到的权重矩阵
		
		int iterator = 8,tops = 50;
		Hits hits = new Hits(matrix,allNodes,tUsers);
		hits.calculateHits(iterator, tops);
//		
		
		System.out.println("可获取的最大内存数："+Runtime.getRuntime().maxMemory()/1024/1024+"M");	
		System.out.print("已占用内存:"); 
		System.out.println(
		Runtime.getRuntime().totalMemory()/1024/1024+"M");
		
//		BufferedWriter bw = new FileUtil().writeFile("DATA/reducematrix.txt");
//		for(int i = 0; i < matrix.length;i++)
//		{
//			for(int j = 0; j < matrix[0].length; j++){
//				//if(matrix[i][j] != 0)
//					//System.out.print(matrix[i][j]);
//				bw.write(matrix[i][j]+" ");
//			}
//			bw.newLine();
//				
//			//System.out.println();
//		}
//		bw.close();
//		for(int i = 0; i<allNodes.size();i++)
//			System.out.println(allNodes.get(i).getId()+"--"+i);
	}
	
}
