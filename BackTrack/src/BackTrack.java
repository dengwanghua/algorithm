import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Scanner;


public class BackTrack {
	public static String inputPath="";				//测试文件路径
	public static String outputPath="";				//输出文件路径
	public int cost[][];  							//cost[i][j]表示第i个作业由第j个人完成的花费
	public int n;									//作业或工人的数量
	public int curCost;								//当前花费
	public int minCost;								//最小花费
	public int worker[];							//工人工作状态，1表示已分配作业，0表示没有分配作业
	public int task[];								//作业分配情况，即作业由谁承担
	public int bestSolution[];						//取得最小花费时，作业的最优分配情况
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner input=new Scanner(System.in);
		BackTrack backTrack=new BackTrack();
		System.out.println("请输入测试文件的路径:");
		inputPath=input.next();						//测试文件路径
		System.out.println("请输入输出文件的路径:");
		outputPath=input.next();					//输出文件路径
		backTrack.initData(inputPath);				//初始化数据信息
		backTrack.backTracking(1);					//回溯法计算最小花费和最优分配方案
		backTrack.outputResultInfomation(outputPath);//控制台和文件同时输出最终结果
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
				worker=new int[n+1];		//初始化工人是否被分配作业数组，默认情况没有被分配
				task=new int[n+1];			//初始化作业分配情况数组
				bestSolution=new int[n+1];	//初始化取得最小花费作业的最优分配情况的数组
				for(int k=1;k<=n;k++){
					worker[k]=0;			//默认情况工人没有被分配作业(0表示未分配作业，1表示已分配作业)
					task[k]=0;				//默认情况作业为未分配给工人(0表示为分配给工人，如果已分配，则对应值为工人编号)
					bestSolution[k]=0;		//默认情况作业未分配
				}
				minCost=0;						//初始化最小花费为第m个作业分配给m工人
				for (int m=1;m<= n;m++){
					minCost += cost[m][m];
					bestSolution[m]=m;
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
	public	void backTracking(int t){			//回溯法计算最小花费和最优分配方案
		if(t>n){
			if(curCost<minCost){				//比较当前花费和之前的最小花费
				minCost=curCost;				
				for(int j=1;j<=n;j++){			//取得最小花费时的最优分配情况
					bestSolution[j]=task[j];
				}
			}
		}else{
			for(int j=1;j<=n;j++){
				if(worker[j]==0){	//第j个工人未被分配作业
					worker[j]=1;	//将第t个作业分配给第j个工人
					task[t]=j;		//标记第t个作业已被分配给第j个工人
					curCost+=cost[t][j];	//计算当前花费
					backTracking(t+1);		//分配第t+1个作业给工人
					curCost-=cost[t][j];	//取消第t个作业分配给第j个工人
					worker[j]=0;			
					task[t]=0;				
				}
			}
		}
	}
	public void outputResultInfomation(String outputPath){	//控制台和文件同时输出最终结果
		String resultString="";			//初始化输出结果
		String minCostString="最小花费:"+minCost;
		String assignmentStirng="具体分配情况:"+"\n";
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
