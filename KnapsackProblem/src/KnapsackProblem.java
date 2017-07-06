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
import java.util.Scanner;


public class KnapsackProblem {
	private static String inputFilePath="";					//测试数据输入文件路径
	private static String outputFilePath="";				//输出文件路径
	private static int number;								//物品的数量
	private static int weight[];							//物品的重量
	private static int value[];								//物品的价值
	private static int bagSize;								//背包容量
	private static int bestValues[][];						//背包最大价值最优解矩阵
	private static int bestPath[];						    //背包中放入的所有物品，1表示已放入，0表示未放入
	private static int bestValue;						   //背包最大价值
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner input=new Scanner(System.in);
		do{
			KnapsackProblem knapsackProblem=new KnapsackProblem();
			System.out.println("请输入测试数据文件路径:");
			inputFilePath=input.next();
			System.out.println("请输入输出文件路径:");
			outputFilePath=input.next();
			knapsackProblem.inputData(inputFilePath);	 //获取输入数据
			knapsackProblem.obtainBestValue(number,weight,value,bagSize);	//获得背包最大价值最优解矩阵
			bestPath=new int[number];										//初始化所放入的物品
			bestValue=0;													//初始化最大价值
			knapsackProblem.obtainBestPathAndBestValue(number,bagSize,bestValues,weight,bestPath);	//获得背包最大价值和背包中放入的所有物品
			knapsackProblem.outputBestInformation(outputFilePath); 	//控制台和文件同时输出：输入的相关数据信息以及最后的计算结果，包括（背包最大价值和背包中放入的所有物品）
		}while(true);
	}
	 /**
	  * @param inputFilePath 测试数据文件输入路径
	  */
	public void inputData(String inputFilePath){	//获取物品的数量、物品的重量、物品的价值和背包容量等数据
			try {
				String str="";						//读取的文件的每行数据
				weight=new int[100];  				//物品的重量
				value=new  int[100];  				//物品的价值
				BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(new FileInputStream(inputFilePath))); //由路径获取输入流
				int flag=0;							//区分读取的数据内容是否是文件的第一行
				while ((str = bufferedReader.readLine()) != null) {			//读取一行数据
					String contentInfo[]=str.split(" ");					//读取的数据信息
					if(flag==0){											//读取的文件内容为文件的第一行，数据代表的是背包容量以及物品数量
						number=Integer.parseInt(contentInfo[0]);			//物品数量
						bagSize=Integer.parseInt(contentInfo[1]);			//背包容量
					}else{													//读取的文件内容不是文件的第一行，数据代表的是物品的重量和物品的价值
						weight[flag]=Integer.parseInt(contentInfo[0]);		//物品重量
						value[flag]=Integer.parseInt(contentInfo[1]);		//物品价值
					}
					flag++; 
				}
				bufferedReader.close();
			} catch (FileNotFoundException e) {
				System.out.println("打开测试数据文件失败,可能输入文件不存在导致，请重新输入文件路径");
			}catch (IOException e) {
			}
	}
	/**
	 * 
	 * @param number 物品的数量
	 * @param weight 物品的重量
	 * @param value	 物品价值
	 * @param bagSize 背包容量
	 */
	public void obtainBestValue(int number,int weight[],int value[],int bagSize){	//获得背包最大价值最优解矩阵
		bestValues=new int[number+1][bagSize+1];									//初始化背包最大价值最优解矩阵
		for(int i=0;i<number+1;i++){
			for(int j=0;j<bagSize+1;j++){
				if(i==0||j==0){
					bestValues[i][j]=0;
				}
				else if(weight[i]>j){
					bestValues[i][j]=bestValues[i-1][j];
				}else{
					if(value[i]+bestValues[i-1][j-weight[i]]>bestValues[i-1][j]){
						bestValues[i][j]=value[i]+bestValues[i-1][j-weight[i]];
					}else{
						bestValues[i][j]=bestValues[i-1][j];
					}	
				}
			}
		}
	}
	/**
	 * 
	 * @param number 物品数量
	 * @param bagSize 背包容量
	 * @param bestValues 最优矩阵
	 * @param weight 物品重量
	 * @param bestPath 最优路径
	 */
	public void obtainBestPathAndBestValue(int number,int bagSize,int bestValues[][],int weight[],int bestPath[]){ //获得背包最大价值和背包中放入的所有物品
		bestValue=bestValues[number][bagSize]; //背包最大价值
		int temBagSize=bagSize; 
		for(int i=number;i>=2;i--){
			 if(bestValues[i][temBagSize]==bestValues[i-1][temBagSize]){
				 bestPath[i-1]=0;    //未放入背包
			 }else{
				 bestPath[i-1]=1;	//已放入背包
				 temBagSize=temBagSize-weight[i];
			 }
		}
		if(bestValues[1][temBagSize]==0){
			bestPath[0]=0;	//未放入背包
		}else{
			bestPath[0]=1;	//已放入背包
		}
	}
	public void outputBestInformation(String outputFilePath){	//控制台和文件同时输出：输入的相关数据信息以及最后的计算结果，包括（背包最大价值和背包中放入的所有物品）
		String resultString="";	//初始化输出结果
		String bagSizeString="背包容量为："+bagSize;	//背包容量
		String thingsNumber="物品数量为："+number;		//物品数量
		String thingsWeightString="物品重量：";      	//各个物品重量
		for(int i=1;i<number+1;i++){
			thingsWeightString+=weight[i]+" ";
		}
		String thingsValueString="物品价值：";      	//各个物品价值
		for(int i=1;i<number+1;i++){
			thingsValueString+=value[i]+" ";
		}
		String bagMaxValueString="背包中的最大价值："+bestValue;	 //背包取得的最大价值
		String bagThings="背包取得最大价值时装入的物品为:";			//背包取得最大价值时装入的物品
		for(int i=0;i<number;i++){
			if(bestPath[i]==1){
				bagThings+="物品"+(i+1)+" ";
			}
		}
		resultString=bagSizeString+"\n"+thingsNumber+"\n"+thingsWeightString+"\n"+thingsValueString+"\n"+bagMaxValueString+"\n"+bagThings; //获得最终结果
		try {
			BufferedWriter outputReuslt= new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outputFilePath))));
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
