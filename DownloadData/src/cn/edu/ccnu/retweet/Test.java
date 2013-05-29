package cn.edu.ccnu.retweet;

import java.util.ArrayList;
import java.util.List;

import weibo4j.Account;
import weibo4j.Timeline;
import weibo4j.Weibo;
import weibo4j.model.Paging;
import weibo4j.model.Status;
import weibo4j.model.StatusWapper;
import weibo4j.model.WeiboException;

public class Test {
	public Test(String token)
	{
		Weibo weibo = new Weibo();
		weibo.setToken(token);
	}
	
	public static void main(String[] args) throws WeiboException
	{
		String token = "2.00DdJ8lB0FVbrafafc4e5bf0xkPSZC";
		Test test = new Test(token);
//		String tid = "3378503404643145";
//		Paging page = new Paging(1);
//		page.setCount(5);
//		Timeline timeline1 = new Timeline();
//		List<Status> status = new ArrayList<Status>();
//		StatusWapper sw;
//		try{
//		sw = timeline1.getRepostTimeline(tid,page);
//		status = sw.getStatuses();
//		for(Status s : status){
////			if(s.getText().equals("此微博已被删除。"))  //微博删除则标记为deleted
////				System.out.println("delete!");
////				if(s.getSource() == null)
////				System.out.println("not exit!");
////			else
//			System.out.println(s.getId()+"--"+s.getUser().getId());
//		}
//		}catch(Exception e)
//		{
//			System.out.println(e.getStackTrace());
//		}
//		
//		Timeline timeline2 = new Timeline();
//		Status statu = timeline2.showStatus("3425254666402521");
//		System.out.println("3425254666402521----"+statu.getUser().getId());
		

		String uid = "1617092497";
		Timeline timeline = new Timeline();
		StatusWapper sw;
		List<Status> status = new ArrayList<Status>();
			Paging page = new Paging(1);
			page.setCount(5);
			try{
					sw = timeline.getUserTimelineByUid(uid, page, 0, 0);
					status = sw.getStatuses();
					for(Status s : status)
					{
						if(s.getRetweetedStatus() == null)
							System.out.println("true");
						System.out.println("false");
					}
			}catch(Exception e){
					e.getStackTrace();
			}
		
	}
}
