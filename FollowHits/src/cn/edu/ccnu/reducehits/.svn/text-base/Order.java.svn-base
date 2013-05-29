package cn.edu.ccnu.reducehits;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Order {
	/**
	 * 对PR值进行排序，从而找到跟PR值对应的用户的id，id存放在allNodes中
	 * 需要先找到PR与NO的对应，然后才能根据NO找出对应的id
	 * @param pageRanks 获取到的PR值
	 * @param counts    取结果的前counts个人
	 */
	public void orderPageRank(double[] pageRanks,int counts,List<Node> allNodes)
	{
		double flag;
		Map<Integer,Double> prAndNo = new HashMap<Integer,Double>(); //将数组中的序号和PR值对应，以根据序号查找对应的微博id
		List<Map.Entry<Integer, Double>> NoToPr = new ArrayList<Map.Entry<Integer, Double>>();
		for (int i = 0; i < pageRanks.length; i++)    //将pageRanks数组的序号好pr值对应存放于map中，便于排序
		{  
			
			prAndNo.put(i, pageRanks[i]);
		}
		NoToPr = orderMap(prAndNo);         //对map进行排序
		for(int i = 0; i < counts; i++)
		{
			int number;
			number = NoToPr.get(i).getKey();      //获取NO号
			
			for(int j = 0; j < allNodes.size(); j++)  //在allNodes中找到与NO号对应的用户id号
			{
				if(number == allNodes.get(j).NO)
				{
					System.out.println("第"+(i+1)+"名为:"+allNodes.get(j).id+"--"+pageRanks[j]);
					break;
				}
			}
		}
		
	}
	/**
	 * 对map进行排序
	 * @param map  包含NO值和对应的PR值的map
	 */
	public List orderMap(Map map)
	{
		List<Map.Entry<Integer, Double>> keyAndValue = new ArrayList<Map.Entry<Integer, Double>>(map.entrySet());
		Collections.sort(keyAndValue, new Comparator<Map.Entry<Integer, Double>>() //调用排序方法
		{   
		    public int compare(Map.Entry<Integer, Double> o1, Map.Entry<Integer, Double> o2) 
		    {       
		        return (o2.getValue().compareTo(o1.getValue()));      //根据PR值进行排序       
		    }
		});
		return keyAndValue;
	}


}
