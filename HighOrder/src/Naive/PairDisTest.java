package Naive;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import Util.Instance;
import Util.ReadInst;

public class PairDisTest {
	private Map<String,Integer> featMap;
	public PairDisTest(String modelfile) throws IOException, Exception{
		//step 1: read trained file
		FileInputStream fis=new FileInputStream(modelfile);
		ObjectInputStream ois=new ObjectInputStream(fis);
		this.featMap=(Map<String,Integer>) ois.readObject();
		//step 2: get two direction arc count, get probability
		ois.close();
	}
	public double GetProbability(String feat[]){
		//version 1
		//feature string: <node1 form>|<node1 pos>|<node2 form>|<node2 pos>
		//feat vector format: <n form>|<node1 pos>|<node2 form>|<node2 pos>
		//System.out.println("mapcount:"+this.featMap.size());
		//test:
		/*
		Iterator<Entry<String, Integer>> iterator=this.featMap.entrySet().iterator();
		while(iterator.hasNext()){
			Entry<String, Integer> entry=iterator.next();
			System.out.println("Key:"+entry.getKey()+",Value:"+entry.getValue());
		}
		*/
		double probability=0;
		int count1=0;
		int count2=0;
		if(feat.length==8){
			String arc1=feat[0]+"|"+feat[1]+"|"+feat[2]+"|"+feat[3];
			System.out.println("arc1:"+arc1);
			if(featMap.containsKey(arc1)){
				count1=featMap.get(arc1);
			}
			String arc2=feat[4]+"|"+feat[5]+"|"+feat[6]+"|"+feat[7];
			System.out.println("arc2:"+arc2);
			if(featMap.containsKey(arc2)){
				count2=featMap.get(arc2);
			}
			
			if(count1+count2!=0){
				probability=((double)count1)/(count1+count2);
			}
		}else{
			System.out.println("Feature is not right!");
		}
		System.out.println("count1:"+count1+",count2:"+count2);
		return probability;
	}
	
	public double GetProbabilityVersion2(String feat[], int proflag){
		//version 2
		//feature string: <node1 form>|<node1 pos>|<node2 form>|<node2 pos>
		//feat vector format: <n form>|<node1 pos>|<node2 form>|<node2 pos>
		//System.out.println("mapcount:"+this.featMap.size());
		//test:
		/*
		Iterator<Entry<String, Integer>> iterator=this.featMap.entrySet().iterator();
		while(iterator.hasNext()){
			Entry<String, Integer> entry=iterator.next();
			System.out.println("Key:"+entry.getKey()+",Value:"+entry.getValue());
		}
		*/
		//!!process flag: 0:form+pos  1:form   2:pos
		//int proflag=2;
		double probability=0;
		int count1=0;
		int count2=0;
		if(feat.length==8){
			String arc1=feat[0]+"|"+feat[1]+"|"+feat[2]+"|"+feat[3];
			System.out.println("arc1:"+arc1);
			String arc1form=feat[0]+"|"+feat[2];
			String arc1pos=feat[1]+"|"+feat[3];
			String arc2=feat[4]+"|"+feat[5]+"|"+feat[6]+"|"+feat[7];
			System.out.println("arc2:"+arc2);
			String arc2form=feat[4]+"|"+feat[6];
			String arc2pos=feat[5]+"|"+feat[7];
			String featstring="";
			if(proflag==0){
				if(featMap.containsKey(arc1+"|"+arc2)){
					count1=featMap.get(arc1+"|"+arc2);
				}
				if(featMap.containsKey(arc2+"|"+arc1)){
					count2=featMap.get(arc2+"|"+arc1);
				}
				featstring=arc1+"|"+arc2;
			}else if(proflag==1){
				if(featMap.containsKey(arc1form+"|"+arc2form)){
					count1=featMap.get(arc1form+"|"+arc2form);
				}
				if(featMap.containsKey(arc2form+"|"+arc1form)){
					count2=featMap.get(arc2form+"|"+arc1form);
				}
				featstring=arc1form+"|"+arc2form;
			}else if(proflag==2){
				if(featMap.containsKey(arc1pos+"|"+arc2pos)){
					count1=featMap.get(arc1pos+"|"+arc2pos);
				}
				if(featMap.containsKey(arc2pos+"|"+arc1pos)){
					count2=featMap.get(arc2pos+"|"+arc1pos);
				}
				featstring=arc1pos+"|"+arc2pos;
			}else{
				System.out.println("Never happen.");
			}
			
			if(count1+count2!=0){
				probability=((double)count1)/(count1+count2);
			}else{
				System.out.println("Not in training data:"+featstring);
			}
		}else{
			System.out.println("Feature is not right!");
		}
		System.out.println("count1:"+count1+",count2:"+count2);
		return probability;
	}
	
	public int GetProbabilityVersion3(String feat[],int proflag){
		//version 3
		//flip coin
		//test:
		/*
		Iterator<Entry<String, Integer>> iterator=this.featMap.entrySet().iterator();
		while(iterator.hasNext()){
			Entry<String, Integer> entry=iterator.next();
			System.out.println("Key:"+entry.getKey()+",Value:"+entry.getValue());
		}
		*/
		//!!process flag: 0:form+pos  1:form   2:pos
		//int proflag=2;
		double probability=0;
		int count1=0;
		int count2=0;
		if(feat.length==8){
			String arc1=feat[0]+"|"+feat[1]+"|"+feat[2]+"|"+feat[3];
			System.out.println("arc1:"+arc1);
			String arc1form=feat[0]+"|"+feat[2];
			String arc1pos=feat[1]+"|"+feat[3];
			String arc2=feat[4]+"|"+feat[5]+"|"+feat[6]+"|"+feat[7];
			System.out.println("arc2:"+arc2);
			String arc2form=feat[4]+"|"+feat[6];
			String arc2pos=feat[5]+"|"+feat[7];
			String featstring="";
			if(proflag==0){
				if(featMap.containsKey(arc1+"|"+arc2)){
					count1=featMap.get(arc1+"|"+arc2);
				}
				if(featMap.containsKey(arc2+"|"+arc1)){
					count2=featMap.get(arc2+"|"+arc1);
				}
				featstring=arc1+"|"+arc2;
			}else if(proflag==1){
				if(featMap.containsKey(arc1form+"|"+arc2form)){
					count1=featMap.get(arc1form+"|"+arc2form);
				}
				if(featMap.containsKey(arc2form+"|"+arc1form)){
					count2=featMap.get(arc2form+"|"+arc1form);
				}
				featstring=arc1form+"|"+arc2form;
			}else if(proflag==2){
				if(featMap.containsKey(arc1pos+"|"+arc2pos)){
					count1=featMap.get(arc1pos+"|"+arc2pos);
				}
				if(featMap.containsKey(arc2pos+"|"+arc1pos)){
					count2=featMap.get(arc2pos+"|"+arc1pos);
				}
				featstring=arc1pos+"|"+arc2pos;
			}else{
				System.out.println("Never happen.");
			}
			
			if(count1+count2!=0){
				probability=((double)count1)/(count1+count2);
			}else{
				System.out.println("Not in training data:"+featstring);
			}
		}else{
			System.out.println("Feature is not right!");
		}
		System.out.println("count1:"+count1+",count2:"+count2);
		return Math.random()<probability?1:0;
	}
	
	public void Test(String testfile,String orderfile) throws IOException{
		/**
		 * this method order the arcs, useful function: GetProbability(String feat[])get relative probability of 2 arcs
		 * format of GetProbability() input: 8 dimension string array: <parent1 form><parent1 pos><child1 form><child1 pos><parent2 form><parent2 pos><child2 form><child2 pos> 
		 */
		//TODO: Yizhong
		
		BufferedReader reader=new BufferedReader(new FileReader(testfile));
		ReadInst riInst=new ReadInst(reader);
		Instance inst;
		//init: feature string of every node
		while((inst=riInst.ReadNext())!=null){
			int len=inst.length;
			String feat[][]=new String[len+1][2];
			for(int i=0;i<inst.length;i++){
				feat[i][0]=inst.form[i];
				feat[i][1]=inst.pos[i];
			}
		}
		//feature combination
		Map<String, Double> feattable=new HashMap<String, Double>();
		for(int i=0;i<inst.length;i++){
			for(int j=0;j<inst.length;j++){
				//TODO
				//this.GetProbability(feat[i][0]);
			}
		}
		
		reader.close();
	}
	public void GetProbabilityExample(){
		
	}
}
