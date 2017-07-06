import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;


public class PostOffice {
	private int houseNumber;								//居民点数量
	private int minDistance;								//居民点到邮局最小距离总和
	private static ArrayList positionX;						//居民点横坐标
	private static ArrayList positionY;						//居民点纵坐标
	private static String inputFilePath="";					//居民点位置坐标输入文件路径
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		Scanner input=new Scanner(System.in);
		do{
			PostOffice postOffice=new PostOffice();
			System.out.println("请输入居民点位置坐输入文件路径:");
			inputFilePath=input.next();
			postOffice.obtainPosition(inputFilePath);	 //获得居民点位置
			postOffice.outputPostOfficeInformation(positionX,positionY);	//输出邮局具体位置以及居民点到邮局最小距离总和
		}while(true);
	}
	 /**
	  * @param inputFilePath 居民点位置坐标输入文件路径
	  */
	public void obtainPosition(String inputFilePath){	//获取居民点横坐标和纵坐标
			try {
				String str="";				//读取的文件的每行数据
				positionX=new ArrayList();  //居民点横坐标
				positionY=new ArrayList();  //居民点纵坐标
				BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(new FileInputStream(inputFilePath))); //由路径获取输入流
				while ((str = bufferedReader.readLine()) != null) {			//读取一行数据，即一个居民点
					   String positionXY[]=str.split(" ");					//居民点横坐标和纵坐标
					   positionX.add(Integer.parseInt(positionXY[0]));		//居民点横坐标
					   positionY.add(Integer.parseInt(positionXY[1]));		//居民点纵坐标
				}
				houseNumber=positionX.size();								//获得居民点数量
			} catch (FileNotFoundException e) {
				System.out.println("打开输居民点位置坐标输入文件失败,可能输入文件不存在导致，请重新输入文件路径");
			}catch (IOException e) {
			}
	}
	/**
	 * 
	 * @param positionX 居民点横坐标
	 * @param positionY 居民点纵坐标
	 * @return 居民点到邮局最小距离总和
	 */
	public int getMinDistance(ArrayList positionX,ArrayList positionY){		//计算居民点到邮局最小距离总和
		int Distance=0;	// 居民点到邮局最小距离总和 
		for(int i=0;i<houseNumber;i++){
			Distance+=Math.abs((int)positionX.get(i)-(int)positionX.get(houseNumber/2));
			Distance+=Math.abs((int)positionY.get(i)-(int)positionY.get(houseNumber/2));
		}
		 return Distance;
	}
	/**
	 * 
	 * @param positionX 居民点横坐标
	 * @param positionY 居民点纵坐标
	 */
	public void outputPostOfficeInformation(ArrayList positionX,ArrayList positionY){	//输出邮局具体位置以及居民点到邮局最小距离总和
		try{
			Collections.sort(positionX);	//对居民点横坐标由小到大排序
			Collections.sort(positionY);	//对居民点纵坐标由小到大排序
			minDistance=getMinDistance(positionX,positionY);
			if(houseNumber%2==0){	//居民点数量为偶数，输出邮局具体位置
			  System.out.println("邮局的位置为:");
			  System.out.println("("+positionX.get(houseNumber/2-1)+"~"+positionX.get(houseNumber/2)+","+positionY.get(houseNumber/2-1)+"~"+positionY.get(houseNumber/2)+")");
			}else{	//居民点为奇数，输出邮局具体位置
				System.out.print("邮局的位置为:");
				System.out.println("("+positionX.get(houseNumber/2)+","+positionY.get(houseNumber/2)+")");
			}
				System.out.println("居民点到邮局的最小距离总和:"+minDistance);	//输出居民点到邮局最小距离总和
		}catch(ArrayIndexOutOfBoundsException e){
			 System.out.println("数组越界,可能输入文件不存在导致，请重新输入文件路径");
		}
	}
}
