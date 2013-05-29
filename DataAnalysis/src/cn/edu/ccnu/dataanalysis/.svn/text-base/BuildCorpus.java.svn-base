package cn.edu.ccnu.dataanalysis;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * 构建语料的格式，最终形式为
 *  id1-follwer1,follower2,follower3....
 *  id2-follwer1,follower2,follower3....
 *  id3-follwer1,follower2,follower3....
 *  ...
 *  id为需要评估的人，后面的粉丝，同时也可以是转发、评论人的id
 * @author Jelen
 *
 */
public class BuildCorpus {
	
	/**主要是关注语料的构建使用
	 * 从语料中获取粉丝节点，关注人节点，和所有不重复节点
	 * 根据这些信息构建成 “id-follwer1,follower2,follower3....”这个模式，
	 * 并保存到.txt文件中
	 * @param fromPath 原始语料的路径
	 * @param toPath  保存文件的路径
	 * @param flag 判断是否为关注的语料，是则flag = 0，因为关注和转发获取id的方法不一致
	 * @throws IOException
	 */
	public void initialGraph(String fromPath,String toPath,int flag) throws IOException
	{
		FileUtil fu = new FileUtil();
		GetID ids = new GetID();
		List<List> nodes = new ArrayList<List>();
		if(flag == 0)
			 nodes = ids.getFollowAllIds(fromPath);        //获取关注里的ID
		else
			 nodes = ids.getAllIds(fromPath);
		List<Node> followee = nodes.get(0);    //单纯放被关注人id
		List<Node> follower = nodes.get(1);    //单纯放粉丝的id
		List<Node> allNodes = nodes.get(2);    //所有的人的点，将id标上一个号，相当于在矩阵中的阶数
		
		System.out.println("将要生成文件为："+toPath);
		System.out.println("构建生成文件中......");
		BufferedWriter bw = fu.writeFile(toPath);
		String line = followee.get(0).id+"-"+follower.get(0).id;    //初始化一个句子的模式“id-follower”
		for(int i = 1; i < followee.size(); i++)
		{	
			if(followee.get(i).id.equals(followee.get(i-1).id))
				line = line + "," + follower.get(i).id;           //如果是同一个关注者，则在其后添加",follower"
			else
			{
				bw.write(line+"\r");
				line = followee.get(i).id + "-" + follower.get(i).id; //如果不是同一个关注者，则重新初始化句子模式为“id-follower”
				continue;
			}
		}
		bw.write(line);
		bw.close();
		System.out.println("构建完成！");
	}
	
	
	/**主要是关注语料的构建使用
	 * 从语料中获取粉丝节点，关注人节点，和所有不重复节点
	 * 根据这些信息构建成 “id-follwer1,follower2,follower3....”这个模式，
	 * 并保存到.txt文件中
	 * @param fromPath 原始语料的路径
	 * @param bw  写道一个文件中
	 * @param flag 判断是否为关注的语料，是则flag = 0，因为关注和转发获取id的方法不一致
	 * @throws IOException
	 */
	public void initialGraph(String fromPath,BufferedWriter bw,int flag) throws IOException
	{
		FileUtil fu = new FileUtil();
		GetID ids = new GetID();
		List<List> nodes = new ArrayList<List>();
		if(flag == 0)
			 nodes = ids.getFollowAllIds(fromPath);        //获取关注里的ID
		else
			 nodes = ids.getAllIds(fromPath);
		List<Node> followee = nodes.get(0);    //单纯放被关注人id
		List<Node> follower = nodes.get(1);    //单纯放粉丝的id
		List<Node> allNodes = nodes.get(2);    //所有的人的点，将id标上一个号，相当于在矩阵中的阶数
		
		//System.out.println("将要生成文件为："+toPath);
		System.out.println("构建生成文件中......");
		//BufferedWriter bw = fu.writeFile(toPath);
		String line = followee.get(0).id+"-"+follower.get(0).id;    //初始化一个句子的模式“id-follower”
		for(int i = 1; i < followee.size(); i++)
		{	
			if(followee.get(i).id.equals(followee.get(i-1).id))
				line = line + "," + follower.get(i).id;           //如果是同一个关注者，则在其后添加",follower"
			else
			{
				bw.write(line+"\r");
				line = followee.get(i).id + "-" + follower.get(i).id; //如果不是同一个关注者，则重新初始化句子模式为“id-follower”
				continue;
			}
		}
		bw.write(line);
		bw.newLine();
		System.out.println("构建完成！");
	}
	
}
