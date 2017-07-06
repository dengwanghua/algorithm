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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;


public class Huffman {
	private static String inputPath="";						//测试文件输入路径
	private static String encodeOutputPath=""; 				//压缩后文件输出路径
	private Map<Character, Integer> charContentAndWeight; 	//所有字符内容和权重
	private Map<Character, String> encodeInfo;				//每个字符内容与对应的编码
	private ArrayList<Node> nodes;							//所有的节点 
	private Node rootNodes;									//根节点 
	public static void main(String[] args) {
		// TODO Auto-generated method stub
			Scanner input=new Scanner(System.in);
			Huffman huffman=new Huffman();
			System.out.println("请输入需要压缩文件的路径:");
			inputPath=input.next();						//测试文件输入路径
			System.out.println("压缩后文件的输出路径:");
			encodeOutputPath=input.next();				//压缩后文件输出路径
			huffman.encode(inputPath,encodeOutputPath);			//压缩文件	
	}
  public String fileContent(String filePath){		//得到文件内容
		String fileContent="";  					//文件全部内容
		String lineContent="";						//文件每行内容
	  try {
			BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
			while((lineContent = bufferedReader.readLine()) != null){
				fileContent+=lineContent+"\n";
			}
			fileContent=fileContent.substring(0,fileContent.length()-1);
			bufferedReader.close();
	  }catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
	 } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
	 }
	 return fileContent;
  }
  public void buildTree(String fileContent){						//建立树结构
		 char[] fileContentArray = fileContent.toCharArray(); 		//将文件内容字符串转化为字符数组
		 charContentAndWeight=new HashMap<Character, Integer>();	//初始化所有的字符和对应权重
		 for(char c:fileContentArray){
			 Character character=new Character(c);					//char类型转换为Character
			 if(charContentAndWeight.containsKey(character)){		//字符已加入charContentAndWeight
				 charContentAndWeight.put(character, charContentAndWeight.get(character) + 1); 
			 }else{													//字符未加入charContentAndWeight
				 charContentAndWeight.put(character, 1);
			 }
			 
		 }
		 PriorityQueue<Node> priorityQueue = new PriorityQueue<Node>(); //初始化优先队列
		 nodes=new ArrayList<Node>();
		 for(Character keyValue:charContentAndWeight.keySet()){
			 Node node=new Node();	//初始化节点
			 node.charConent=keyValue.toString();				//设置节点内容
			 node.weight=charContentAndWeight.get(keyValue);	//设置节点权重
			 nodes.add(node);									//节点中添加新节点
			 priorityQueue.add(node);							//优先队列中添加新节点
		 }
		 int size=priorityQueue.size();
		 for(int i=0;i<size-1;i++){
			 Node nodeone = priorityQueue.poll();  				//最小的节点1
	         Node nodetwo = priorityQueue.poll(); 				//最小的节点2
	         Node newNode = new Node();  						//合并的节点
	         newNode.charConent = nodeone.charConent + nodetwo.charConent; 
	         newNode.weight = nodeone.weight + nodetwo.weight;  
	         newNode.leftChildrenNode = nodeone;  
	         newNode.rightChildrenNode = nodetwo;  
	         nodeone.parentNode = newNode;  
	         nodetwo.parentNode = newNode;  
	         priorityQueue.add(newNode); 						//将合并的节点添加到优先队列中
		 }
		 rootNodes=priorityQueue.poll(); 						//得到根节点
  }
  public void encodeInfo(ArrayList<Node> nodes){						//得到每个字符对应的编码
	  encodeInfo = new HashMap<Character, String>();  
      for (Node node : nodes) {  
          Character character = new Character(node.getCharConent().charAt(0));  //得到节点字符内容
          String encodeValue = "";  
          Node currentNode=node;  
          do {  
              if (currentNode.isLeftChild()) {  					//为左节点
            	  encodeValue ="0" + encodeValue;  
              } else {  											//为右节点
            	  encodeValue = "1" + encodeValue;  
              } 
              currentNode = currentNode.parentNode;  
          } while (currentNode.parentNode != null);  
          encodeInfo.put(character, encodeValue);  
      }     
  }
  public void encode(String inputPath,String Outputpath){			
	  String  encodeString=""; 	//初始化压缩后的文件内容字符串
	  String fileContent=fileContent(inputPath);	//得到文件内容
	  buildTree(fileContent);						//建立树结构
	  encodeInfo(nodes);							//活得每个字符对应编码信息
      for (char c : fileContent.toCharArray()) {  
          Character character = new Character(c);  
          encodeString+=encodeInfo.get(character);  
      }  
      try {
			BufferedWriter outputReuslt= new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(Outputpath))));
			outputReuslt.write(encodeString);  //将结果输出到文件中
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
