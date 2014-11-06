package Func;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;

import Util.Instance;
import Util.ReadInst;


public class CheckMalt {
	public void WriteProcessDistribution(String conllfile, String diagfile,String writefile) throws IOException{
		BufferedReader conllreader = new BufferedReader(new FileReader(conllfile));
		BufferedReader diagreader = new BufferedReader(new FileReader(diagfile));
		File outFile = new File(writefile);
		if(!outFile.exists()){
			outFile.createNewFile();
		}
		FileWriter writer = new FileWriter(outFile);
		ReadInst ri=new ReadInst(conllreader);
		Instance inst;
		while((inst=ri.ReadNext())!=null){
			
		}
		
		
		
		conllreader.close();
		diagreader.close();
		writer.close();
	}
	public Map<Integer, Integer> GetProcessDistributiion(String[] columns,int length) throws IOException{
		//return dependent-processcount pairs
		Map<Integer,Integer> distri = new TreeMap<Integer, Integer>();
		for(int i=1;i<=length;i++){
			distri.put(i, -1);
		}
		Stack<Integer> stack=new Stack<Integer>();
		int inputstart=1;
		int processcount=1;
		stack.push(0);
		for(int i=1;i<columns.length;i++){
			if(columns[i].equals("SH")){
				stack.push(inputstart);
				inputstart++;
			}else if(columns[i].startsWith("LA")){
				int head = stack.pop();
				int dependent=stack.pop();
				stack.push(head);
				distri.put(dependent, processcount);
				processcount++;
			}else if(columns[i].startsWith("RA")){
				int dependent = stack.pop();
				distri.put(dependent, processcount);
				processcount++;
			}else{
				System.out.println("not SH,LA,RA:"+columns[i]);
			}
		}
		
		Iterator<Entry<Integer, Integer>> ite = distri.entrySet().iterator();
		while(ite.hasNext()){
			Entry<Integer, Integer> entry= ite.next();
			System.out.println("dependent:"+entry.getKey()+",processcount:"+entry.getValue());
		}
		
		return distri;
	}
	
	public static void main(String args[]) throws IOException{
		BufferedReader reader = new BufferedReader(new FileReader("wsj_00_maltengdiag.txt"));
		String columns[]=reader.readLine().split(" ");
		CheckMalt cm=new CheckMalt();
		cm.GetProcessDistributiion(columns, 18);
		reader.close();
	}
}
