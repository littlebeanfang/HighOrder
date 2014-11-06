package Naive;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import Util.ExtendInst;
import Util.ReadExtendInst;

public class Evaluation {
	/**
	 * this class is used to test the naive method,test on wsj 00-01, get 2 things:
	 * 1. prediction accuracy
	 * 2. probability distribution
	 * @throws Exception 
	 */
	public void Evaluate(String modelfile, String testfile,int proflag) throws Exception{
		BufferedReader conllreader=new BufferedReader(new FileReader(testfile));
		ReadExtendInst ri=new ReadExtendInst(conllreader);
		ExtendInst ei; 
		String[] feat={};
		PairDisTest pdt=new PairDisTest(modelfile);
		
		//count variables
		int rightcount=0;
		int wrongcount=0;
		int count0001=0;//0.0-0.1 probability
		int count0102=0;//0.1-0.2 probability
		int count0203=0;//0.2-0.3 probability
		int count0304=0;//0.3-0.4 probability
		int count0405=0;//0.4-0.5 probability
		int count0506=0;//0.5-0.6 probability
		int count0607=0;//0.6-0.7 probability
		int count0708=0;//0.7-0.8 probability
		int count0809=0;//0.8-0.9 probability
		int count0910=0;//0.9-1.0 probability
		
		while((ei=ri.ReadNext())!=null){
			for(int i=1;i<ei.length;i++){
				String arc1childform=ei.form[i];
				String arc1childtpos=ei.pos[i];
				int head=ei.head[i];
				//System.out.println("head:"+head);
				String arc1parentform=ei.form[head];
				String arc1parentpos=ei.pos[head];
				//String relation=ei.rel[i];
				int arc1processindex=ei.process[i];
				for(int j=i+1;j<ei.length;j++){
					String arc2childform=ei.form[j];
					String arc2childtpos=ei.pos[j];
					int head2=ei.head[j];
					String arc2parentform=ei.form[head2];
					String arc2parentpos=ei.pos[head2];
					//String relation=ei.rel[i];
					int arc2processindex=ei.process[j];
					//String featString="";
					
					//if(proflag==0){
						feat=new String[]{arc1parentform,arc1parentpos,arc1childform,arc1childtpos,arc2parentform,arc2parentpos,arc2childform,arc2childtpos};
					//}else if(proflag==1){
					//	feat=new String[]{arc1parentform,arc1childform,arc2parentform,arc2childform};
					//}else if(proflag==2){
					//	feat=new String[]{arc1parentpos,arc1childtpos,arc2parentpos,arc2childtpos};
					//}else{
					//	System.out.println("Never happen");
					//}
					//probability: arc1 process before arc2
					//TODO here, choose version 2 or version 3
					//test version 2: probability
					double prob=pdt.GetProbabilityVersion2(feat,proflag);
					//test version 3: flipcoin
					//int prob=pdt.GetProbabilityVersion3(feat,proflag);
					if((prob>0.5&&(arc1processindex<arc2processindex))||(prob<=0.5)&&(arc1processindex>arc2processindex)){
						rightcount++;
					}else{
						wrongcount++;
					}
					if(prob<=0.1){
						count0001++;
					}else if(prob<=0.2){
						count0102++;
					}else if(prob<=0.3){
						count0203++;
					}else if(prob<=0.4){
						count0304++;
					}else if(prob<=0.5){
						count0405++;
					}else if(prob<=0.6){
						count0506++;
					}else if(prob<=0.7){
						count0607++;
					}else if(prob<=0.8){
						count0708++;
					}else if(prob<=0.9){
						count0809++;
					}else{
						count0910++;
					}
				}
			}
		}
		
		System.out.println("Rightcount: "+rightcount+", "+((double)rightcount)/(rightcount+wrongcount));
		System.out.println("Wrongcount: "+wrongcount+", "+((double)wrongcount)/(rightcount+wrongcount));
		System.out.println("0.0-0.1:"+count0001);
		System.out.println("0.1-0.2:"+count0102);
		System.out.println("0.2-0.3:"+count0203);
		System.out.println("0.3-0.4:"+count0304);
		System.out.println("0.4-0.5:"+count0405);
		System.out.println("0.5-0.6:"+count0506);
		System.out.println("0.6-0.7:"+count0607);
		System.out.println("0.7-0.8:"+count0708);
		System.out.println("0.8-0.9:"+count0809);
		System.out.println("0.9-1.0:"+count0910);
	}
	public static void main(String args[]) throws Exception{
		Evaluation ev=new Evaluation();
		ev.Evaluate("wsj_2-21_naive_version2_pos.model", "wsj_00-01_malt_processindex.txt", 2);
	}
}
