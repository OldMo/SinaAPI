package cn.edu.ccnu.reducehits;

/**
 * �?��人物点包含的信息
 * @author Jelen_123
 *
 */
public class Node {
	public int NO;
	public String id;
	public double weight;

	
	public int getNO() {
		return NO;
	}


	public void setNO(int nO) {
		NO = nO;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public double getWeight() {
		return weight;
	}


	public void setWeight(double weight) {
		this.weight = weight;
	}


	  @Override  
	    public int hashCode() {  
	     final int prime = 31;  
	     int result = 1;  
	     result = prime * result + ((id == null) ? 0 : id.hashCode());  
	     return result;  
	    }  
	     
	    @Override  
	    public boolean equals(Object obj) {  
	     //System.out.println("User.equals(Object) has been called.");  
	     if (this == obj)  
	      return true;  
	     if (obj == null)  
	      return false;  
	     if (getClass() != obj.getClass())  
	      return false;  
	     final Node other = (Node) obj;  
	     if (id == null) {  
	      if (other.id != null)  
	       return false;  
	     } else if (!id.equals(other.id))  
	      return false;  
	     return true;  
	    }  

}
