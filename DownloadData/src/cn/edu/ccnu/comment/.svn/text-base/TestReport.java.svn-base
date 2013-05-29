package cn.edu.ccnu.comment;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import weibo4j.Timeline;
import weibo4j.Users;
import weibo4j.Weibo;
import weibo4j.model.Paging;
import weibo4j.model.Status;
import weibo4j.model.StatusWapper;
import weibo4j.model.User;
import weibo4j.model.WeiboException;

public class TestReport {
	public TestReport(String token)
	{
		Weibo weibo = new Weibo();
		weibo.setToken(token);
	}

	public String getUserNickName(String uid) throws WeiboException
	{
		Users users = new Users();                 //新浪接口
		User user = users.showUserById(uid);
		System.out.println(user.getScreenName());
		return user.getScreenName();
	}
	
	
	public void getWeibos(String uid,String nickName) throws WeiboException, IOException
	{
		Timeline timeline = new Timeline();
		List<Status> status = new ArrayList<Status>();
		StatusWapper sw;
			Paging page = new Paging(1);
			page.setCount(10);
			try {
				sw = timeline.getUserTimelineByUid(uid, page, 0, 0);
				status = sw.getStatuses();
				for(Status s : status){
					//bw.write(uid+"--"+s.getId()+"--"+s.getRepostsCount());
					System.out.println(uid+"--"+s.getText()+"-"+s.getId()+"--"+s.getRepostsCount());
					//bw.newLine();
				}
			} catch (WeiboException e) {
				e.printStackTrace();
				//bw.close();
			}
			//System.out.println(page.getPage());
		
		System.out.println("下载完毕");
	}
	
	
	public void getRetweet(String uid,String tweetId) throws WeiboException, IOException
	//public void getRetweet() throws WeiboException
	{
		Timeline timeline = new Timeline();
		List<Status> status = new ArrayList<Status>();

			Paging page = new Paging(1);
			page.setCount(50);
			StatusWapper sw;
			try 
			{
				sw = timeline.getRepostTimeline(tweetId,page);
				status = sw.getStatuses();
				//System.out.println(status.size());
				for(Status statu:status)
				{
					System.out.println(uid+"--"+statu.getId()+statu.getText());
					//bw.write(uid+"--"+tweetId+"--"+statu.getId());
					//bw.newLine();
				}
			} catch (WeiboException e) 
			{
				e.printStackTrace();
				//bw.close();
				System.exit(0);
			}
	}
	
	public static void main(String[] args) throws WeiboException, IOException
	{
		String token = "2.00DdJ8lBpRRC5E9851419550GxdN7B";
		String uid = "2246287090";
		String weiboId = "3427589676759303";
		TestReport tr = new TestReport(token);
		//tr.getWeibos(uid, tr.getUserNickName(uid));
		tr.getRetweet(uid, weiboId);
	}
}
