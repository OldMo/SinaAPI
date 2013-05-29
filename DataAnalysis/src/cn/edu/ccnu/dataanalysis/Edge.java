package cn.edu.ccnu.dataanalysis;

public class Edge {
	Node fromUser;
	Node toUser;
	int sides;
	public Node getFromUser() {
		return fromUser;
	}
	public void setFromUser(Node fromUser) {
		this.fromUser = fromUser;
	}
	public Node getToUser() {
		return toUser;
	}
	public void setToUser(Node toUser) {
		this.toUser = toUser;
	}
	public int getSides() {
		return sides;
	}
	public void setSides(int sides) {
		this.sides = sides;
	}
	
	//�Ƚ�Edge�Ƿ���Ⱦ��ǱȽ�Edge�����Node�Ƿ����
	 @Override  
	    public boolean equals(Object obj) {  
	     //System.out.println("User.equals(Object) has been called.");  
	     if (this == obj)  
	      return true;  
	     if (obj == null)  
	      return false;  
	     if (getClass() != obj.getClass())  
	      return false;  
	     final Edge other = (Edge) obj;  
	     if ( fromUser == null || toUser == null) {  
	      if (other.fromUser != null || other.toUser != null)  
	       return false;  
	     } else if (!fromUser.equals(other.fromUser) || !toUser.equals(other.toUser))  
	      return false;  
	     return true;  
	    }  
	

}
