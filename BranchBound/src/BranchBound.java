import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Scanner;


public class BranchBound {
	public static String inputPath="";						//测试文件路径
	public static String outputPath="";						//输出文件路径
	public static int cost[][];  							//cost[i][j]表示第i个工人完成第j个任务的花费
	public int n;											//任务或工人的数量
	public static int minCost;								//最小花费
	public static int upValue;								//上界值
	public static int downValue;							//下界值
	public static PriorityQueue<Node> priorityQueue;		//优先队列
	public int assignment[];								//assignment[k]表示第k个人分配的任务
	public ArrayList<Node> stList=new ArrayList<Node>();	//存储最小值节点
	public int tasked[];									//已经分配的工作
	public Node parentNode=new Node();						//父节点
	public Node childNode;									//子节点
	public Node minNode;									//取得最小值时的叶子节点
	public int bestSolution[];								//取得最小花费时，任务的最优分配情况
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner input=new Scanner(System.in);
		BranchBound branchBound=new BranchBound();
		System.out.println("请输入测试文件的路径:");
		inputPath=input.next();						//测试文件路径
		System.out.println("请输入输出文件的路径:");
		outputPath=input.next();					//输出文件路径
		branchBound.initData(inputPath);			//初始化数据信息
		branchBound.upValueAndDownValue(cost);		//获取上界值和下界值
		branchBound.branchBoundCaCulate();			//分支界限法
		branchBound.outputResultInfomation(outputPath);//控制台和文件同时输出最终结果
	}
	public void initData(String inputPath){			//初始化数据信息
		String lineContent="";
		int i=0;									//文件行号，从0开始
		 try {
				BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(new FileInputStream(inputPath)));
				while((lineContent = bufferedReader.readLine()) != null){
					if(i==0){	//读取的内容为文件的第一行数据
						n=Integer.parseInt(lineContent);
						cost=new int[n+1][n+1]; 	//初始化花费矩阵
					}else{			//读取的内容不是文件的第一行数据，每行数据表示一项工作由各个人完成的花费	
						String contentInfo[]=lineContent.split(" ");
						for(int j=1;j<=n;j++){
							cost[i][j]=Integer.parseInt(contentInfo[j-1]);	//cost[i][j]表示第i个作业由第j个人完成的花费
						}
					}
					i++;
				}
			bufferedReader.close();
		  }catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
		 } catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
		 }
	}
	public void upValueAndDownValue(int cost[][]){	//计算值上界以及下界值
		downValue=0;	//初始化下界值
		upValue=0;		//初始化上界值
		int task[]=new int[n+1];	//初始化任务分配情况
		for(int i=1;i<=n;i++){
			task[i]=0;
		}
		
		for(int i=1;i<=n;i++){				//计算目标函数的下界值downValue
			int minValue=cost[i][1];
			for(int j=2;j<=n;j++){
				if(minValue>cost[i][j]){
					minValue=cost[i][j];
				}
			}
		downValue+=minValue;
		}
		
		for(int i=1;i<=n;i++){				//采用贪心算法计算目标函数的上界值upValue
			int  minValue=Integer.MAX_VALUE;
			int flag=0;
			for(int j=1;j<=n;j++){
				if(minValue>=cost[i][j]&&task[j]==0){
					minValue=cost[i][j];
					flag=j;
				}
			}
			task[flag]=1;
			upValue+=minValue;
		}
	}
	public void branchBoundCaCulate(){		//分支界限法计算
		priorityQueue=new PriorityQueue<Node>();	//初始化处理节点的优先队列
		assignment=new int[n+1];					//初始化工人任务数组，
		for(int i=1;i<=n;i++){
			assignment[i]=0;						//0表示人员i为分配任务
		}
		int k=1;			//为第k个人分配任务
		int i=0;			//i表示第k-1个人分配的任务
		while(k>=1){
			assignment[k]=1;
			while(assignment[k]<=n){	//遍历所有任务
				tasked=new int[n+1];
				childNode=new Node();
				childNode.preTask=i;
				childNode.curTask=assignment[k];
				childNode.worker=k;
				childNode.parentNode=parentNode;
				checkTask(childNode); //获取已经分配的任务
				if(tasked[assignment[k]]==0){	//如果人员k分配任务assignment[k]不发生冲突
					int lb=0;
					for(int m=1;m<=n;m++){	//计算当前已对人员1～tasked[m]分配任务的花费lb
						if(tasked[m]!=0){
							lb+=cost[tasked[m]][m];
						}
						
					}
					lb+=cost[k][assignment[k]];
					ArrayList row=new ArrayList();
					ArrayList col=new ArrayList();
					for(int z=1;z<=n;z++){
						if(tasked[z]!=0){
							row.add(tasked[z]);
							col.add(z);
						}
					}
					row.add(k);
					col.add(assignment[k]);	 //计算剩余为未分配任务的人员的最小花费
					for(int x=1;x<=n;x++){
						int min=Integer.MAX_VALUE;
						if(!row.contains(x)){
							for(int y=1;y<=n;y++){
									if(!col.contains(y)){
										if(min>cost[x][y]){
											min=cost[x][y];
										}
									}
									
							}
							lb+=min;
						}
					}
					if(lb<=upValue){	//如果花费小于或等于上界值，将当前节点加入优先队列
						childNode.lb=lb;
						priorityQueue.add(childNode);
					}
					
				}	
				assignment[k]=assignment[k]+1;	
			}
			parentNode=new Node();
			parentNode=priorityQueue.poll();
			if(k==n){	//所有人员都分配了任务
				if(parentNode.worker==n){	//叶子节点的lb值在优先队列中最小，输出最优解
					minCost=parentNode.lb;
					minNode=parentNode;
					return;
				}
				else{	//叶子节点的lb值在优先队列中不是最小
					PriorityQueue<Node> tempriorityQueue=new PriorityQueue<Node>();
					ArrayList<Node> list=new ArrayList<Node>();
					while(priorityQueue.size()!=0){
						Node tmpNode=priorityQueue.poll();
						if(tmpNode.worker==n){
							if(tmpNode.lb==parentNode.lb){
								minCost=tmpNode.lb;
								minNode=tmpNode;
								return;
							}
							tempriorityQueue.add(tmpNode);
						}else{
							list.add(tmpNode);
						}
					}
					priorityQueue.clear();
					if(tempriorityQueue.size()!=0){
						upValue=tempriorityQueue.poll().lb;	 	//上界值等于优先队列中叶子节点最小的lb值
					}
					for(Node e:list){
						if(e.lb<=upValue){	//删除超出目标函数界的节点
							priorityQueue.add(e);
						}
					}
				}
			}
			i=parentNode.curTask;	 //i等于优先队列中lb最小的节点的任务值
			k=parentNode.worker+1;	//k等于优先队列中lb最小的节点的的k值，并加1
		}
	}
	public void  checkTask(Node childNode) { //检查任务是否冲突
		if((childNode.worker-1)>=1){
			tasked[childNode.parentNode.curTask]=childNode.parentNode.worker;
			if(childNode.parentNode!=null){		//判断当前扩展节点是否包含父节点
				checkTask(childNode.parentNode);
			}else{
				return;
			}
			
		}
		
	}
	public void getBest(Node node){		//获取最优路径
		bestSolution[node.curTask]=node.worker;
		if(node.parentNode!=null){	//判断当前扩展节点是否包含父节点
			getBest(node.parentNode);
		}else{
			return;
		}
	}
	public void outputResultInfomation(String outputPath){	//控制台和文件同时输出最终结果
		String resultString="";			//初始化输出结果
		String minCostString="最小花费:"+minCost;
		String assignmentStirng="具体分配情况:"+"\n";
		bestSolution=new int[n+1];	//初始化最优分配
		getBest(minNode);
		for(int i=1;i<=n;i++){
			assignmentStirng+="作业"+i+"被工人"+bestSolution[i]+"执行"+"\n";
		}
		resultString=minCostString+"\n"+assignmentStirng; //获得最终结果
		try {
			BufferedWriter outputReuslt= new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outputPath))));
			System.out.println(resultString);  //控制台输出结果
			outputReuslt.write(resultString);  //将结果输出到文件中
			outputReuslt.flush();
			outputReuslt.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
