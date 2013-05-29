package cn.edu.ccnu.reducehits;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Order {
	/**
	 * ��PRֵ�������򣬴Ӷ��ҵ���PRֵ��Ӧ���û���id��id�����allNodes��
	 * ��Ҫ���ҵ�PR��NO�Ķ�Ӧ��Ȼ����ܸ���NO�ҳ���Ӧ��id
	 * @param pageRanks ��ȡ����PRֵ
	 * @param counts    ȡ�����ǰcounts����
	 */
	public void orderPageRank(double[] pageRanks,int counts,List<Node> allNodes)
	{
		double flag;
		Map<Integer,Double> prAndNo = new HashMap<Integer,Double>(); //�������е���ź�PRֵ��Ӧ���Ը�����Ų��Ҷ�Ӧ��΢��id
		List<Map.Entry<Integer, Double>> NoToPr = new ArrayList<Map.Entry<Integer, Double>>();
		for (int i = 0; i < pageRanks.length; i++)    //��pageRanks�������ź�prֵ��Ӧ�����map�У���������
		{  
			
			prAndNo.put(i, pageRanks[i]);
		}
		NoToPr = orderMap(prAndNo);         //��map��������
		for(int i = 0; i < counts; i++)
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


}
