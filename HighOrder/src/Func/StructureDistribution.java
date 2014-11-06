package Func;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import Util.ReadInst;
import Util.Instance;


public class StructureDistribution {
	private BufferedReader reader;
	private Instance inst;
	static private HashMap<String, Integer> dependency= new HashMap<String,Integer>();
	static private HashMap<String, Integer> sibling= new HashMap<String,Integer>();
	static private HashMap<String, Integer> grandchild= new HashMap<String,Integer>();
	static private HashMap<String, Integer> grandsibling= new HashMap<String,Integer>();
	static private HashMap<String, Integer> trisibling= new HashMap<String,Integer>();
	//private ArrayList<int[]> sentinfo;//parent, child infomation of every node
	
	private void Add(HashMap<String, Integer> map, String relations){
		if(!map.containsKey(relations)){
			map.put(relations, 1);
		}else{
			int count = map.get(relations)+1;
			map.put(relations, count);
		}
	}
	
	public StructureDistribution() {
		
	}
	public StructureDistribution(String file) throws IOException{
		//File in = new File(file);
		reader = new BufferedReader(new FileReader(file));	
	}
	public ArrayList<int[]> GetNextSentInfo() throws IOException{
		ReadInst ri = new ReadInst(reader);
		Instance  inst = ri.ReadNext();
		this.inst = inst;
		if(inst==null)
			return null;
		ArrayList<int[]> sentinfo = new ArrayList<int[]>();
		//initialize
		for(int i=0;i<=inst.length;i++){
			sentinfo.add(new int[inst.length+2]);
		}
		//single headed
		/*
		 * format of sentinfo
		 * <child#><parent index><child 1 index><child 2 index>...
		 */
		for(int index=1;index<=inst.length;index++){
			int child = index;
			int parent = inst.head[index];
			int[] childinfo = sentinfo.get(child);
			if(childinfo[1]!=0)
				System.out.println("error: multi-headed!");
			childinfo[1]=parent;
			sentinfo.set(child, childinfo);
			int[] parentinfo = sentinfo.get(parent);
			int childnum = ++parentinfo[0];//add one child
			parentinfo[childnum+1] = child;
			sentinfo.set(parent, parentinfo);
		}
		return sentinfo;
	}
	
	public void TestGetNextSentInfo() throws IOException{
		int count = 0 ;
		StructureDistribution sd = new StructureDistribution("a100.conll");
		ArrayList<int[]> sentinfo=sd.GetNextSentInfo();
		while(sentinfo!=null){
			count++;
			if(count==1){
				for(int i=0;i<sentinfo.size();i++){
					int[] info = sentinfo.get(i);
					for(int j=0;j<info.length;j++){
						System.out.print("\t"+info[j]);
					}
					System.out.println("\n");
				}
			}
			sentinfo=sd.GetNextSentInfo();
			
		}
		System.out.println();
	}
	
	public void DistributionCountSingle(ArrayList<int[]> sentinfo){
		//count for one sentence
		for(int row=0;row<sentinfo.size();row++){
			//row is the index of current node
			int[] info = sentinfo.get(row);
			int childnum = info[0];
			int parentindex = info[1];
			int childstartindex = 2;
			int childendindex = childstartindex+childnum-1;
			
			if(childnum>1){
				for(int i = childstartindex ; i<=childendindex ;i++){
					int child1 = info[i];
					if(child1<row)
						continue;
					for(int j = i+1 ; j<=childendindex ;j++){
						//siblings
						int child2 = info[j];
						this.Add(this.sibling,FindRelation(child1)+"+"+FindRelation(child2));
						//grandsibling
						this.Add(grandsibling, FindRelation(row)+"+"+FindRelation(child1)+"+"+FindRelation(child2));
					}
				}
			}
			//grandchild
			if(childnum>0){
				for(int i = childstartindex ; i<=childendindex ;i++){
					this.Add(this.grandchild,FindRelation(row)+"+"+FindRelation(info[i]));
				}
			}
			//trisibling
			if(childnum>2){
				for(int i = childstartindex ; i<=childendindex ;i++){
					int child1 = info[i];
					if(child1<row)
						continue;
					for(int j = i+1 ; j<=childendindex ;j++){
						for(int k=j+1;k<=childendindex ;k++){
							this.Add(this.trisibling,FindRelation(info[i])+"+"+FindRelation(info[j])+"+"+FindRelation(info[k]));
						}
					}
				}
			}
		}
		
	}
	
	public void TestSingleDistributionCount() throws IOException{
		StructureDistribution sd = new StructureDistribution("a100.conll");
		sd.GetNextSentInfo();//skip sentence 1
		sd.DistributionCountSingle(sd.GetNextSentInfo());
		System.out.println("sibling:"+this.sibling.size());
		System.out.println("grandchild:"+this.grandchild.size());
		System.out.println("grandsibling:"+this.grandsibling.size());
		System.out.println("trisibling:"+this.trisibling.size());
	}
	
	private String FindRelation(int index){
		return inst.rel[index];
	}
	
	public void DistributionCount() throws IOException{
		//StructureDistribution sd = new StructureDistribution("a100.conll");
		ArrayList<int[]> sentinfo = this.GetNextSentInfo();
		while(sentinfo!=null){
			this.DistributionCountSingle(sentinfo);
			sentinfo = this.GetNextSentInfo();
		}
	}
	
	private void DisplayCount(){
		System.out.println("Sibling:");
		this.DisplayMap(this.sibling);
		System.out.println("Grandchild:");
		this.DisplayMap(this.grandchild);
		System.out.println("grandsibling:");
		this.DisplayMap(this.grandsibling);
		System.out.println("tri-sibling:");
		this.DisplayMap(this.trisibling);
	}
	
	private void DisplayMap(Map<String, Integer> map){
		Iterator ite = map.entrySet().iterator();
		while(ite.hasNext()){
			Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>)ite.next();
			System.out.println(entry.getKey()+"\t"+entry.getValue());
		}
	}
	public void TestDistributionCount() throws IOException{
		StructureDistribution sd = new StructureDistribution("wsj_2-21.conll");
		sd.DistributionCount();
		sd.DisplayCount();
	}
	public static void main(String args[]) throws IOException{
		StructureDistribution sd = new StructureDistribution();
		//sd.TestGetNextSentInfo();
		//sd.TestSingleDistributionCount();
		sd.TestDistributionCount();
	}
}
