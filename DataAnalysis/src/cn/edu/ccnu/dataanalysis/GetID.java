package cn.edu.ccnu.dataanalysis;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



/**
 * 从语料中获取关注者id，转发者id和所有的不重复id
 * 返回节点链表
 * @author Jelen
 *
 */
public class GetID {
	
	/**
	 * 语料为.xml格式，为原始语料，格式如下：
	 * <RECORD>
	 *<person_id>10145</person_id>
	 *<guanzhu_id>10029</guanzhu_id>
	 *</RECORD>
	 *<RECORD>
	 *<person_id>10145</person_id>
	 *<guanzhu_id>10318</guanzhu_id>
	 *</RECORD>
	 * 获取语料中的所有id，包括粉丝和被关注者
	 * @param fromPath 语料文件的路径
	 * @throws IOException 
	 * 返回链表，包含：
	 * 被关注人节点链表
	 * 关注人节点链表
	 * 全部不重复人的节点的链表
	 */
	public List getFollowAllIds(String fromPath) throws IOException
	{
		int dimenssion = 0;
		 List<Node> followee = new ArrayList<Node>();    //单纯放被关注人id
		 List<Node> follower = new ArrayList<Node>();    //单纯放粉丝的id
		 List<Node> allNodes = new ArrayList<Node>();         //所有的人的点，将id标上一个号，相当于在矩阵中的阶数
		FileUtil fu = new FileUtil();
		int lineNo = 1;
		BufferedReader br = fu.readFile(fromPath);
		List<String> allIds = new ArrayList<String>();
		System.out.println("处理的文件为："+fromPath);
		System.out.println("获取Id中......");
		while(br.ready())
		{
			System.out.println("进行第"+lineNo+"行");
			String line = br.readLine();
			String id = fu.pickUp(line);         //获得过滤后的id
			
			if(!id.equals(""))
			{
				if(lineNo%4 == 2)             //从语料分析，2,6,10...行为被关注者id
				{
					Node node = new Node();
					node.id = id;
					followee.add(node);
				}
					
				if(lineNo%4 == 3)            //从语料分析，2,6,10...行为被关注者id
				{
					Node node = new Node();
					node.id = id;
					follower.add(node);
				}
								
				if(!allIds.contains(id)) //过滤后id不为空并且不是重复的则添加到List中
				{
					allIds.add(id);
					Node node = new Node();
					node.id = id;
					node.NO = dimenssion;
					node.weight = 0.0;
					allNodes.add(node);  //将各个id号标上编号放入allNodes中
					dimenssion++;
				}
			}
			lineNo++;
		}
		List<List> lists = new ArrayList<List>();
		lists.add(followee);
		lists.add(follower);
		lists.add(allNodes);
		
		System.out.println("被关注的人数（有重复）："+followee.size());
		System.out.println("粉丝的人数："+followee.size());
		System.out.println("所有的人数（去重）："+followee.size());
		return lists;
	}

	/**主要是转发的评论的语料构建使用
	 * 从下载的格式为：uid-tid-commenttid-commentuid 的语料中
	 * 获取所有id，包括粉丝和被关注者
	 * @param filePath 语料文件的路径
	 * @throws IOException 
	 * 
	 * 注：由于获取语料格式不太一致，在转发是为words[2]，若是评论则应该words[3]
	 */
	public List getAllIds(String fromPath) throws IOException
	{
		int dimenssion = 0;
		List<Node> original = new ArrayList<Node>();    //单纯放被关注人id
		List<Node> actionuser = new ArrayList<Node>();    //单纯放粉丝的id
		List<Node> allNodes = new ArrayList<Node>();         //所有的人的点，将id标上一个号，相当于在矩阵中的阶数
		FileUtil fu = new FileUtil();
		GetID ids = new GetID();
		List<String> Ids = new ArrayList<String>();//存放所有的id，可重复,计算同一个人转发另一个人的次数
		int lineNo = 1;
		
		System.out.println("处理文件："+fromPath);
		BufferedReader br = fu.readFile(fromPath);
		List<String> allIds = new ArrayList<String>();
		
		String line;
		while((line = br.readLine()) != null)
		{
			System.out.println("正在处理第"+lineNo+"行...");
			String[] words = line.split(",00,");
			
			if(words[words.length-1].equals("not exit")){
				continue;
			}
			else
				Ids.add(words[words.length-1]);
			
			Node node1 = new Node();
			node1.id = words[0];
			original.add(node1);

			Node node2 = new Node();
			node2.id = words[words.length-1];												
			actionuser.add(node2);
						
			if(!allIds.contains(words[words.length-1])) //过滤后id不为空并且不是重复的则添加到List中
			{
				allIds.add(words[words.length-1]);
				Node node = new Node();
				node.id = words[words.length-1];
				node.NO = dimenssion;
				node.weight = 0.0;
				allNodes.add(node);  //将各个id号标上编号放入allNodes中
				dimenssion++;
			}
			if(!allIds.contains(words[0])) //过滤后id不为空并且不是重复的则添加到List中
			{
				allIds.add(words[0]);
				Node node = new Node();
				node.id = words[0];
				node.NO = dimenssion;
				node.weight = 0.0;
				allNodes.add(node);  //将各个id号标上编号放入allNodes中
				dimenssion++;
			}
			lineNo++;
		}
			List<List> lists = new ArrayList<List>();
			lists.add(original);
			lists.add(actionuser);
			lists.add(allNodes);
	
			System.out.println("被转发/评论微博的人数（有重复）："+original.size());
			System.out.println("转发/评论微博的人数（有重复）："+actionuser.size());
			System.out.println("所有的人数（去重）："+allNodes.size());
			return lists;
	}

	
}
