
public class Node implements Comparable<Node>{

	public int lb=0;		//当前状态界限值
	public int preTask;		//上一个工人被分配的任务
	public int worker;		//当前工人
	public int curTask;			//当前工人所分配的任务
	public Node parentNode;		//父亲节点
	public int compareTo(Node o) {
		return lb-o.lb;
	}
	public int getLb() {
		return lb;
	}
	public void setLb(int lb) {
		this.lb = lb;
	}
	public int getPreTask() {
		return preTask;
	}
	public void setPreTask(int preTask) {
		this.preTask = preTask;
	}
	public int getWorker() {
		return worker;
	}
	public void setWorker(int worker) {
		this.worker = worker;
	}
	public int getCurTask() {
		return curTask;
	}
	public void setCurTask(int curTask) {
		this.curTask = curTask;
	}
    
}
