
public class Node implements Comparable<Node>{	//节点对象

	public String charConent="";	//节点对应内容
	public int weight=0;				//节点权重
	public Node parentNode; 		//父节点
	public Node leftChildrenNode; 	//左孩子节点
	public Node rightChildrenNode; //右孩子节点
	@Override
	public int compareTo(Node o) {
		// TODO Auto-generated method stub
		return weight-o.weight;
	}
	public String getCharConent() {
		return charConent;
	}
	public void setCharConent(String charConent) {
		this.charConent = charConent;
	}
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	public Node getParentNode() {
		return parentNode;
	}
	public void setParentNode(Node parentNode) {
		this.parentNode = parentNode;
	}
	public Node getLeftChildrenNode() {
		return leftChildrenNode;
	}
	public void setLeftChildrenNode(Node leftChildrenNode) {
		this.leftChildrenNode = leftChildrenNode;
	}
	public Node getRightChildrenNode() {
		return rightChildrenNode;
	}
	public boolean isLeftChild() {  
        return parentNode != null && this == parentNode.leftChildrenNode;  
    }  
	public boolean isRightChild() {  
        return parentNode != null && this == parentNode.rightChildrenNode;  
    }  
	 public boolean isLeaf() {  
         return charConent.length() == 1;  
     } 
}
