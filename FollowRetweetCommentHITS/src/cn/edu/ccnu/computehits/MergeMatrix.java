package cn.edu.ccnu.computehits;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * 将关注矩阵和转发矩阵想结合，从文件开始结合
 * 
 * 将文件中的关系表示成边的关系并将所有的边存放到链表中
 * 同时将所有的不重复点存放到一个点链表中，链表的容量为要构建的矩阵的阶数
 * 边包含起点fromUser，终点toUser，边的值Sides
 * 点包含 序号no,用户的id
 * @author Jelen
 *
 */
public class MergeMatrix {
	
	
	
	/**
	 * 根据获取到格式为
	 * id1-follwer1,follower2,follower3....
	 * id2-follwer1,follower2,follower3....
	 * 的文件,将一个关注信息存储为一条边，存储到List中
	 * @param fromGraph  txt文件
	 * @return 返回所有边的链表
	 * @throws IOException 
	 * 
	 */
	public List getEdges(String fromGraph) throws IOException{
		List<Edge> edges = new ArrayList<Edge>();
		FileUtil fu = new FileUtil();
		BufferedReader br = fu.readFile(fromGraph);
		String line;
		while((line = br.readLine()) != null){ 							//读一行则需要处理一列的数据，对id所在列的人是否关注
			List<String> fromNodes = new ArrayList<String>();
			 String[] words = new String[2];
			 String toNode;    											//指向的点
			 words = line.split("-");
			 toNode = words[0];                       					//被关注的人
			 fromNodes = Arrays.asList(words[1].split(","));            //粉丝列表
			 Node toUser = new Node();                             		//关注人节点
			 toUser.setId(toNode);
			 int sides = 1;
			 for(String n:fromNodes){
				 if(n.equals("not exit"))                  				//去除not exit的
					 continue;
				 Node fromUser = new Node();                			//粉丝节点
				 fromUser.setId(n);
			
				 Edge edge = new Edge();                   				//粉丝到关注人的一条边
				 edge.setFromUser(fromUser);
				 edge.setToUser(toUser);
				 edge.setSides(sides);					   				//边数为1
				 if(!edges.contains(edge)){                
					 edges.add(edge);       				   			//边不存在添加到边的链表中
				 }
				 else{                                     				//存在则边加1，只发生在转发的情况下
					 int index = edges.indexOf(edge);
					 double side = edges.get(index).getSides();
					 edges.get(index).setSides(side+1);
				 }
				 
			 }
		 }
//		for(int j = 0; j < edges.size(); j++)
//		 {
//				 System.out.println(edges.get(j).getFromUser().getId()+"--"+edges.get(j).getToUser().getId());
//		 }
		return edges;
	}

	/**
	 * 将三个List的边的信息合并，即将关注信息和转发信息的值进行合并
	 * 合并到转发中，并调整边的值
	 * @param followFile   关注文件
	 * @param retweetFile  转发文件
	 * @param weight       关注和转发的比例
	 * @throws IOException
	 */
	public List mergeEdges(String followFile,String retweetFile,String commentFile,double alpha,double beta,double gama) throws IOException{
		int totleSizes = 0;
		List<Edge> fedges = getEdges(followFile);
		System.out.println("关注网的边数:"+fedges.size());
		List<Edge> redges = getEdges(retweetFile);
		System.out.println("转发网的边数:"+redges.size());
		List<Edge> cedges = getEdges(commentFile);
		System.out.println("评论网的边数:"+cedges.size());
		totleSizes = redges.size()+fedges.size()+cedges.size();
		System.out.println("所有的边数和:"+ totleSizes);
		
		int repite = 0,d = 0;
		for(Edge fedge:fedges){
				if(!redges.contains(fedge)){                
					 redges.add(fedge);       				   							//边不存在添加到边的链表中
					 d++;
				}
				 else{                                     											//存在则边加1，只发生在转发的情况下，关注的不会超过1
					 int findex = fedges.indexOf(fedge);                		//查找到该边的位置并获取边的值
					 int rindex = redges.indexOf(fedge);
					 double fside = fedges.get(findex).getSides();
					 double rside = redges.get(rindex).getSides();
					 Edge ed = redges.get(rindex);
					 ed.setSides(beta*rside+alpha*fside);                   		//根据权重重新设置边的值
					 redges.set(rindex, ed);
					 repite++;
				 }
		}
																										//将评论的边融合到转发中
		for(Edge cedge:cedges){
			if(!redges.contains(cedge)){                
				 redges.add(cedge);       				   								//边不存在添加到边的链表中
				 d++;
			}
			else{                                     												//存在则边加1，只发生在转发的情况下，关注的不会超过1
				 int cindex = cedges.indexOf(cedge);                		//查找到该边的位置并获取边的值
				 int rindex = redges.indexOf(cedge);
				 double cside = cedges.get(cindex).getSides();
				 double rside = redges.get(rindex).getSides();
				 Edge ed = redges.get(rindex);
				 ed.setSides(rside+gama*cside);                  							 //评论的权重设置跟转发一致
				 redges.set(rindex, ed);
				 repite++;
			 }
		}
		System.out.println("往转发网中添加的边数:"+d);
		System.out.println("重复的边条数:"+repite);
		System.out.println("合并后剩余的边数为："+(totleSizes-repite));
		return redges;
	}
	
	/**
	 * 将所有的点赋一个编号，同时确定矩阵的阶数
	 * @param edges  所有的边的链表，无重复
	 * 返回不重复的点的个数，同时也是需要构建的矩阵的阶数
	 */
	public List assignId(List<Edge> edges){
		int flag = 0;
		List<Node> allNodes = new ArrayList<Node>();          //所有的节点的存储链表，包含了NO与uid的对应
		for(Edge edge:edges){
			if(!allNodes.contains(edge.getFromUser())){
				edge.getFromUser().setNO(flag);                				//赋编号
				allNodes.add(edge.getFromUser());							//添加到List中
				flag++;
			}
			if(!allNodes.contains(edge.getToUser())){
				edge.getToUser().setNO(flag);
				allNodes.add(edge.getToUser());
				flag++;
			}
		}
		System.out.println("所有的不重复点数："+allNodes.size());
		return allNodes;
	}
	
	/**
	 * 从格式为
	 * id1-follwer1,follower2,follower3....
	 * id2-follwer1,follower2,follower3....
	 * 文件的文件中获取要评估的人的节点集合，也是所有的被关注人
	
	 * 在计算时通过这个对应关系来获取位置的对应
	 * @param allNodes 已经去重后的所有的节点集合
	 * @param fromGraph .txt文件
	 */
	public List getToUsers(String follow,String retweet,String comment) throws IOException{
		List<Node> toUsers = new ArrayList<Node>();
		FileUtil fu = new FileUtil();
		BufferedReader brf = fu.readFile(follow);
		BufferedReader brr = fu.readFile(retweet);
		BufferedReader brc = fu.readFile(comment);
		String line;
		while((line = brf.readLine()) != null){ 							//读一行则需要处理一列的数据
			 String[] words = new String[2];
			 String toNode;    															//指向的点
			 words = line.split("-");
			 toNode = words[0];                       								//被关注的人
			 Node toUser = new Node();                             			//关注人节点
			 toUser.setId(toNode);
			 if(!toUsers.contains(toUser))
				 toUsers.add(toUser);
		}
		brf.close();
		while((line = brr.readLine()) != null){ 							//读一行则需要处理一列的数据
			 String[] words = new String[2];
			 String toNode;    															//指向的点
			 words = line.split("-");
			 toNode = words[0];                       								//被关注的人
			 Node toUser = new Node();                             			//关注人节点
			 toUser.setId(toNode);
			 if(!toUsers.contains(toUser))
				 toUsers.add(toUser);
		}
		brr.close();
		while((line = brc.readLine()) != null){ 							//读一行则需要处理一列的数据
			 String[] words = new String[2];
			 String toNode;    															//指向的点
			 words = line.split("-");
			 toNode = words[0];                       								//被关注的人
			 Node toUser = new Node();                             			//关注人节点
			 toUser.setId(toNode);
			 if(!toUsers.contains(toUser))
				 toUsers.add(toUser);
		}
		brc.close();
		System.out.println("需评估的人有：" + toUsers.size());
		return toUsers;
	}
	
	/**
	 *  *被指向的点只有十个，即评估的人只有十个，则矩阵只需十列，
	 * tUsers里存放的是每个点在allNodes里面的位置信息
	 * @param allNodes
	 * @param toUsers
	 * @return
	 */
		public int[] getTUsers(List<Node> allNodes,List<Node> toUsers){
			int[] tUsers = new int[toUsers.size()];                         //
			int i = 0;
			for(Node node:toUsers){
				int index = allNodes.indexOf(node);							//找到节点在allnodes里的位置
				tUsers[i] = index;
				i++;
		}
		return tUsers;
	}
	
	
	/**
	 * 根据allNodes序号和id的对应，还有边的关系构建矩阵
	 * 查找到边链表中每条边的起点和终点，将该点的序号找到，对应到矩阵中赋值
	 * @param allNodes
	 * @param toUsers为要评估的人的节点集合，大小为矩阵的列数
	 * @param tUsers为要评估人的id在allNodes里的位置的数组，便于对应
	 * @param edges所有的边的集合，也是矩阵中非零值的多少
	 * @return 关系形式的矩阵，矩阵的值不一定只有0,或1
	 * @throws IOException
	 */
	public double[][] initialMatrix(List<Node> allNodes,List<Node> toUsers,int[] tUsers,List<Edge> edges) throws IOException{
		double[][] matrix = new double[allNodes.size()][toUsers.size()];
		int row = 0,column = 0;
		int i = 0;
		for(Edge edge:edges){
			row = allNodes.indexOf(edge.getFromUser());
			column = toUsers.indexOf(edge.getToUser());
			//System.out.println("column:"+tUsers[column]+"col:"+allNodes.indexOf(edge.getToUser()));
			//System.out.println(i+"-("+row+","+column+")"+"-"+edge.getToUser().getId());
			matrix[row][column] = edge.getSides();
			//System.out.println(edge.getSides());
			i++;
		}		
		
		return matrix;
	}
	
	
	/**
	 * 构建权重矩阵，根据关系矩阵，有边则计算该边的权重
	 * 由入度权重和出度权重相乘得到
	 * 入度权重计算方法为：(i,j)边的入度权重为i指向的边j的入度除以i指向的所有点的入度和
	 * 出度权重计算方法为：(i,j)边的出度权重为i指向的边j的出度除以i指向的所有点的出度和
	 * matrix为构建好的关系矩阵，边的值不一定为0,1
	 * tUsers为一个数组，数组中存放的是被关注的uid在allNodes中的位置，便于位置的对应
	 */
	public double[][] weightMatrix(double[][] matrix,int[] tUsers)
	{
		ComputeLinks cl = new ComputeLinks();
		double[] inLinks = cl.inLinks(matrix);
		double[] outLinks = cl.outLinks(matrix);
		double[][] weightMatrix = new double[matrix.length][matrix[0].length];
		int[] sumIn = new int[matrix.length];
		int[] sumOut = new int[matrix.length];
		for(int i = 0;i < matrix.length;i++)
		{
			for(int j = 0; j < matrix[0].length; j++)
			{
				if(matrix[i][j] != 0){

					sumIn[i] += inLinks[j];         //统计点i指向的所有点的入度和
					sumOut[i] += outLinks[tUsers[j]];       //统计点i指向的所有点的出度和
					//i指向j实际上不是对应j行，而是对应的j在tUser里的值对应的所有点的出度
				}
			}
		}
		//构建权重矩阵
		for(int i = 0;i < matrix.length;i++)
		{
			for(int j = 0; j < matrix[0].length; j++)
			{
				if(matrix[i][j] != 0)               //有边，则计算该边的权重
				{
					if(sumIn[i] != 0 && sumOut[i] != 0){
						weightMatrix[i][j] = ((double)inLinks[j]/(double)sumIn[i])*((double)outLinks[tUsers[j]]/(double)sumOut[i]);
					//i指向j同样的实际上指向的是j在tUsers里的值所在的行
					}
				}
			}
		}
		return weightMatrix;
		
	}
}
