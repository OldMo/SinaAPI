package cn.edu.ccnu.computehits;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * ����ע�����ת���������ϣ����ļ���ʼ���
 * 
 * ���ļ��еĹ�ϵ��ʾ�ɱߵĹ�ϵ�������еıߴ�ŵ�������
 * ͬʱ�����еĲ��ظ����ŵ�һ���������У����������ΪҪ�����ľ���Ľ���
 * �߰������fromUser���յ�toUser���ߵ�ֵSides
 * ����� ���no,�û���id
 * @author Jelen
 *
 */
public class MergeMatrix {
	
	
	
	/**
	 * ���ݻ�ȡ����ʽΪ
	 * id1-follwer1,follower2,follower3....
	 * id2-follwer1,follower2,follower3....
	 * ���ļ�,��һ����ע��Ϣ�洢Ϊһ���ߣ��洢��List��
	 * @param fromGraph  txt�ļ�
	 * @return �������бߵ�����
	 * @throws IOException 
	 * 
	 */
	public List getEdges(String fromGraph) throws IOException{
		List<Edge> edges = new ArrayList<Edge>();
		FileUtil fu = new FileUtil();
		BufferedReader br = fu.readFile(fromGraph);
		String line;
		while((line = br.readLine()) != null){ 							//��һ������Ҫ����һ�е����ݣ���id�����е����Ƿ��ע
			List<String> fromNodes = new ArrayList<String>();
			 String[] words = new String[2];
			 String toNode;    											//ָ��ĵ�
			 words = line.split("-");
			 toNode = words[0];                       					//����ע����
			 fromNodes = Arrays.asList(words[1].split(","));            //��˿�б�
			 Node toUser = new Node();                             		//��ע�˽ڵ�
			 toUser.setId(toNode);
			 int sides = 1;
			 for(String n:fromNodes){
				 if(n.equals("not exit"))                  				//ȥ��not exit��
					 continue;
				 Node fromUser = new Node();                			//��˿�ڵ�
				 fromUser.setId(n);
			
				 Edge edge = new Edge();                   				//��˿����ע�˵�һ����
				 edge.setFromUser(fromUser);
				 edge.setToUser(toUser);
				 edge.setSides(sides);					   				//����Ϊ1
				 if(!edges.contains(edge)){                
					 edges.add(edge);       				   			//�߲�������ӵ��ߵ�������
				 }
				 else{                                     				//������߼�1��ֻ������ת���������
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
	 * ������List�ıߵ���Ϣ�ϲ���������ע��Ϣ��ת����Ϣ��ֵ���кϲ�
	 * �ϲ���ת���У��������ߵ�ֵ
	 * @param followFile   ��ע�ļ�
	 * @param retweetFile  ת���ļ�
	 * @param weight       ��ע��ת���ı���
	 * @throws IOException
	 */
	public List mergeEdges(String followFile,String retweetFile,String commentFile,double alpha,double beta,double gama) throws IOException{
		int totleSizes = 0;
		List<Edge> fedges = getEdges(followFile);
		System.out.println("��ע���ı���:"+fedges.size());
		List<Edge> redges = getEdges(retweetFile);
		System.out.println("ת�����ı���:"+redges.size());
		List<Edge> cedges = getEdges(commentFile);
		System.out.println("�������ı���:"+cedges.size());
		totleSizes = redges.size()+fedges.size()+cedges.size();
		System.out.println("���еı�����:"+ totleSizes);
		
		int repite = 0,d = 0;
		for(Edge fedge:fedges){
				if(!redges.contains(fedge)){                
					 redges.add(fedge);       				   							//�߲�������ӵ��ߵ�������
					 d++;
				}
				 else{                                     											//������߼�1��ֻ������ת��������£���ע�Ĳ��ᳬ��1
					 int findex = fedges.indexOf(fedge);                		//���ҵ��ñߵ�λ�ò���ȡ�ߵ�ֵ
					 int rindex = redges.indexOf(fedge);
					 double fside = fedges.get(findex).getSides();
					 double rside = redges.get(rindex).getSides();
					 Edge ed = redges.get(rindex);
					 ed.setSides(beta*rside+alpha*fside);                   		//����Ȩ���������ñߵ�ֵ
					 redges.set(rindex, ed);
					 repite++;
				 }
		}
																										//�����۵ı��ںϵ�ת����
		for(Edge cedge:cedges){
			if(!redges.contains(cedge)){                
				 redges.add(cedge);       				   								//�߲�������ӵ��ߵ�������
				 d++;
			}
			else{                                     												//������߼�1��ֻ������ת��������£���ע�Ĳ��ᳬ��1
				 int cindex = cedges.indexOf(cedge);                		//���ҵ��ñߵ�λ�ò���ȡ�ߵ�ֵ
				 int rindex = redges.indexOf(cedge);
				 double cside = cedges.get(cindex).getSides();
				 double rside = redges.get(rindex).getSides();
				 Edge ed = redges.get(rindex);
				 ed.setSides(rside+gama*cside);                  							 //���۵�Ȩ�����ø�ת��һ��
				 redges.set(rindex, ed);
				 repite++;
			 }
		}
		System.out.println("��ת��������ӵı���:"+d);
		System.out.println("�ظ��ı�����:"+repite);
		System.out.println("�ϲ���ʣ��ı���Ϊ��"+(totleSizes-repite));
		return redges;
	}
	
	/**
	 * �����еĵ㸳һ����ţ�ͬʱȷ������Ľ���
	 * @param edges  ���еıߵ��������ظ�
	 * ���ز��ظ��ĵ�ĸ�����ͬʱҲ����Ҫ�����ľ���Ľ���
	 */
	public List assignId(List<Edge> edges){
		int flag = 0;
		List<Node> allNodes = new ArrayList<Node>();          //���еĽڵ�Ĵ洢����������NO��uid�Ķ�Ӧ
		for(Edge edge:edges){
			if(!allNodes.contains(edge.getFromUser())){
				edge.getFromUser().setNO(flag);                				//�����
				allNodes.add(edge.getFromUser());							//��ӵ�List��
				flag++;
			}
			if(!allNodes.contains(edge.getToUser())){
				edge.getToUser().setNO(flag);
				allNodes.add(edge.getToUser());
				flag++;
			}
		}
		System.out.println("���еĲ��ظ�������"+allNodes.size());
		return allNodes;
	}
	
	/**
	 * �Ӹ�ʽΪ
	 * id1-follwer1,follower2,follower3....
	 * id2-follwer1,follower2,follower3....
	 * �ļ����ļ��л�ȡҪ�������˵Ľڵ㼯�ϣ�Ҳ�����еı���ע��
	
	 * �ڼ���ʱͨ�������Ӧ��ϵ����ȡλ�õĶ�Ӧ
	 * @param allNodes �Ѿ�ȥ�غ�����еĽڵ㼯��
	 * @param fromGraph .txt�ļ�
	 */
	public List getToUsers(String follow,String retweet,String comment) throws IOException{
		List<Node> toUsers = new ArrayList<Node>();
		FileUtil fu = new FileUtil();
		BufferedReader brf = fu.readFile(follow);
		BufferedReader brr = fu.readFile(retweet);
		BufferedReader brc = fu.readFile(comment);
		String line;
		while((line = brf.readLine()) != null){ 							//��һ������Ҫ����һ�е�����
			 String[] words = new String[2];
			 String toNode;    															//ָ��ĵ�
			 words = line.split("-");
			 toNode = words[0];                       								//����ע����
			 Node toUser = new Node();                             			//��ע�˽ڵ�
			 toUser.setId(toNode);
			 if(!toUsers.contains(toUser))
				 toUsers.add(toUser);
		}
		brf.close();
		while((line = brr.readLine()) != null){ 							//��һ������Ҫ����һ�е�����
			 String[] words = new String[2];
			 String toNode;    															//ָ��ĵ�
			 words = line.split("-");
			 toNode = words[0];                       								//����ע����
			 Node toUser = new Node();                             			//��ע�˽ڵ�
			 toUser.setId(toNode);
			 if(!toUsers.contains(toUser))
				 toUsers.add(toUser);
		}
		brr.close();
		while((line = brc.readLine()) != null){ 							//��һ������Ҫ����һ�е�����
			 String[] words = new String[2];
			 String toNode;    															//ָ��ĵ�
			 words = line.split("-");
			 toNode = words[0];                       								//����ע����
			 Node toUser = new Node();                             			//��ע�˽ڵ�
			 toUser.setId(toNode);
			 if(!toUsers.contains(toUser))
				 toUsers.add(toUser);
		}
		brc.close();
		System.out.println("�����������У�" + toUsers.size());
		return toUsers;
	}
	
	/**
	 *  *��ָ��ĵ�ֻ��ʮ��������������ֻ��ʮ���������ֻ��ʮ�У�
	 * tUsers���ŵ���ÿ������allNodes�����λ����Ϣ
	 * @param allNodes
	 * @param toUsers
	 * @return
	 */
		public int[] getTUsers(List<Node> allNodes,List<Node> toUsers){
			int[] tUsers = new int[toUsers.size()];                         //
			int i = 0;
			for(Node node:toUsers){
				int index = allNodes.indexOf(node);							//�ҵ��ڵ���allnodes���λ��
				tUsers[i] = index;
				i++;
		}
		return tUsers;
	}
	
	
	/**
	 * ����allNodes��ź�id�Ķ�Ӧ�����бߵĹ�ϵ��������
	 * ���ҵ���������ÿ���ߵ������յ㣬���õ������ҵ�����Ӧ�������и�ֵ
	 * @param allNodes
	 * @param toUsersΪҪ�������˵Ľڵ㼯�ϣ���СΪ���������
	 * @param tUsersΪҪ�����˵�id��allNodes���λ�õ����飬���ڶ�Ӧ
	 * @param edges���еıߵļ��ϣ�Ҳ�Ǿ����з���ֵ�Ķ���
	 * @return ��ϵ��ʽ�ľ��󣬾����ֵ��һ��ֻ��0,��1
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
	 * ����Ȩ�ؾ��󣬸��ݹ�ϵ�����б������ñߵ�Ȩ��
	 * �����Ȩ�غͳ���Ȩ����˵õ�
	 * ���Ȩ�ؼ��㷽��Ϊ��(i,j)�ߵ����Ȩ��Ϊiָ��ı�j����ȳ���iָ������е����Ⱥ�
	 * ����Ȩ�ؼ��㷽��Ϊ��(i,j)�ߵĳ���Ȩ��Ϊiָ��ı�j�ĳ��ȳ���iָ������е�ĳ��Ⱥ�
	 * matrixΪ�����õĹ�ϵ���󣬱ߵ�ֵ��һ��Ϊ0,1
	 * tUsersΪһ�����飬�����д�ŵ��Ǳ���ע��uid��allNodes�е�λ�ã�����λ�õĶ�Ӧ
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

					sumIn[i] += inLinks[j];         //ͳ�Ƶ�iָ������е����Ⱥ�
					sumOut[i] += outLinks[tUsers[j]];       //ͳ�Ƶ�iָ������е�ĳ��Ⱥ�
					//iָ��jʵ���ϲ��Ƕ�Ӧj�У����Ƕ�Ӧ��j��tUser���ֵ��Ӧ�����е�ĳ���
				}
			}
		}
		//����Ȩ�ؾ���
		for(int i = 0;i < matrix.length;i++)
		{
			for(int j = 0; j < matrix[0].length; j++)
			{
				if(matrix[i][j] != 0)               //�бߣ������ñߵ�Ȩ��
				{
					if(sumIn[i] != 0 && sumOut[i] != 0){
						weightMatrix[i][j] = ((double)inLinks[j]/(double)sumIn[i])*((double)outLinks[tUsers[j]]/(double)sumOut[i]);
					//iָ��jͬ����ʵ����ָ�����j��tUsers���ֵ���ڵ���
					}
				}
			}
		}
		return weightMatrix;
		
	}
}
