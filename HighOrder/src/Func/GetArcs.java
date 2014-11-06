package Func;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import Util.Instance;
import Util.ReadInst;

/**
 * for jack's code, get 2 arcs in google n-gram's format
 * @author Bean
 *
 */
public class GetArcs {
	public void Get2(String conllfile,String writefile) throws IOException{
		BufferedReader reader = new BufferedReader(new FileReader(conllfile));
		File out = new File(writefile);
		if(!out.exists()){
			out.createNewFile();
		}
		FileWriter writer = new FileWriter(out);
		//step 1: get instance
		ReadInst ri = new ReadInst(reader);
		Instance inst = ri.ReadNext();
		while(inst!=null){
			//step 2: enum all 3 nodes
			int length = inst.length ;
			for(int i=1;i<length;i++){
				for(int j=i+1;j<length;j++){
					for(int k=j+1;k<length;k++){
						//step 3: 2 arc, there should be only 1 arc out of this span, others inner dependency
						ArrayList<Integer> indexs = new ArrayList<Integer>();
						indexs.add(i);
						indexs.add(j);
						indexs.add(k);
						ArrayList<Integer> heads = new ArrayList<Integer>();
						heads.add(inst.head[i]);
						heads.add(inst.head[j]);
						heads.add(inst.head[k]);
						int[] r = this.CheckHead(indexs, heads);
						int count = r[0];
						int root = r[1];
						//step 4: write 
						if(count==1){
							this.PrintArc(indexs, heads,inst, root, writer);
						}
					}
				}
			}
			
			inst = ri.ReadNext();
		}
		
		reader.close();
		writer.close();
	}
	
	public void Get3(String conllfile,String writefile) throws IOException{
		BufferedReader reader = new BufferedReader(new FileReader(conllfile));
		File out = new File(writefile);
		if(!out.exists()){
			out.createNewFile();
		}
		FileWriter writer = new FileWriter(out);
		//step 1: get instance
		ReadInst ri = new ReadInst(reader);
		Instance inst = ri.ReadNext();
		while(inst!=null){
			//step 2: enum all 4 nodes
			int length = inst.length ;
			for(int i=1;i<length;i++){
				for(int j=i+1;j<length;j++){
					for(int k=j+1;k<length;k++){
						for(int m=k+1;m<length;m++){
							//step 3: 2 arc, there should be only 1 arc out of this span, others inner dependency
							ArrayList<Integer> indexs = new ArrayList<Integer>();
							indexs.add(i);
							indexs.add(j);
							indexs.add(k);
							indexs.add(m);
							ArrayList<Integer> heads = new ArrayList<Integer>();
							heads.add(inst.head[i]);
							heads.add(inst.head[j]);
							heads.add(inst.head[k]);
							heads.add(inst.head[m]);
							int[] r = this.CheckHead(indexs, heads);
							int count = r[0];
							int root = r[1];
							//step 4: write 
							if(count==1){
								this.PrintArc(indexs, heads,inst, root, writer);
							}
						}
					}
				}
			}
			
			inst=ri.ReadNext();
			//System.out.println(inst);
		}
		
		reader.close();
		writer.close();
	}
	
	private int[] CheckHead(ArrayList<Integer> indexs, ArrayList<Integer> heads){
		int count = 0 ;
		int root = 0 ;
		for(int i=0;i<heads.size();i++){
			if(!indexs.contains(heads.get(i))){
				count++;
				root=i;
			}
		}
		int[] r = {count,root};
		return r;
	}
	
		
	private void PrintArc(ArrayList<Integer> indexs,ArrayList<Integer> heads, Instance inst, int root, FileWriter writer) throws IOException{
		writer.write(inst.form[indexs.get(root)]+"\t");
		String info="";
		for(int i=0;i<indexs.size();i++){
			int dependent = indexs.get(i);
			int head = inst.head[dependent];
			if(i==root){
				info+=inst.form[dependent]+"/"+inst.pos[dependent]+"/ROOT/0 ";
			}else{
				info+=(inst.form[dependent]+"/"+inst.pos[dependent]+"/"+inst.rel[dependent]+"/"+(indexs.indexOf(inst.head[dependent])+1)+" ");
				}
		}
		writer.write(info.trim()+"\t"+"1"+"\n");
		writer.flush();
	}
	
	public static void main(String args[]) throws IOException{
		GetArcs ga = new GetArcs();
//		ga.Get2("a100.conll", "a100_2arc.txt");
//		ga.Get3("a100.conll", "a100_3arc.txt");
		double start=System.currentTimeMillis();
		ga.Get2("wsj_2-21.conll", "wsj_2-21_2arc.txt");
		double end2 = System.currentTimeMillis();
		ga.Get3("wsj_2-21.conll", "wsj_2-21_3arc.txt");
		double end3 = System.currentTimeMillis();
		System.out.println("wsj_2-21 2 arc:"+(end2-start)/1000+" senconds, 3 arc:"+(end3-end2)/1000+" seconds.");
	}
}
