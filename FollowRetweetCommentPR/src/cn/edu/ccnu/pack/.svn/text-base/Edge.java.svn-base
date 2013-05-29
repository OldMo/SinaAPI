package cn.edu.ccnu.pack;

public class Edge {
	Node fromUser;
	Node toUser;
	double sides;
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
	public double getSides() {
		return sides;
	}
	public void setSides(double d) {
		this.sides = d;
	}
	
	//比较Edge是否相等就是比较Edge里面的Node是否相等
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
