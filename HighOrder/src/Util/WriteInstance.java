package Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import Func.CheckMalt;

public class WriteInstance {
	private FileWriter writer;
	public WriteInstance(){
		//for call WriteExtendInst
	}
	public WriteInstance(FileWriter writer){
		this.writer=writer;
	}
	public void WriteInst(Instance inst) throws IOException{
		//System.out.println(inst);
		int length = inst.length;
		for(int i=1;i<=length;i++){
			writer.write(i+"\t"+inst.form[i]+"\t_\t"+inst.pos[i]+"\t"+inst.pos[i]+"\t_\t"+inst.head[i]+"\t"+inst.rel[i]+"\t_\t_\n");
		}
		writer.write("\n");
		writer.flush();
	}
	
	public void WriteExtendInst(Instance inst, Map<Integer, Integer> malt) throws IOException{
		int length = inst.length;
		for(int i=1;i<=length;i++){
			if(inst.rel[i].equals("null")&&inst.head[i]==0){
				writer.write(i+"\t"+inst.form[i]+"\t_\t"+inst.pos[i]+"\t"+inst.pos[i]+"\t_\t"+inst.head[i]+"\troot\t_\t_\t"+malt.get(i)+"\n");
			}else{
				writer.write(i+"\t"+inst.form[i]+"\t_\t"+inst.pos[i]+"\t"+inst.pos[i]+"\t_\t"+inst.head[i]+"\t"+inst.rel[i]+"\t_\t_\t"+malt.get(i)+"\n");
			}
		}
		writer.write("\n");
		writer.flush();
	}
	
	public void RewriteExtend(String conllfile, String maltfile, String rewritefile) throws IOException{
		File outFile = new File(rewritefile);
		if(!outFile.exists()){
			outFile.createNewFile();
		}
		FileWriter writer = new FileWriter(outFile);
		WriteInstance wInstance=new WriteInstance(writer);
		
		BufferedReader maltreader = new BufferedReader(new FileReader(maltfile));
		BufferedReader reader = new BufferedReader(new FileReader(new File(conllfile)));
		ReadInst riInst=new ReadInst(reader);
		Instance instance ;
		String columns[];
		Map<Integer, Integer> malt;
		int count=0;
		while((instance = riInst.ReadNext())!=null){
			count++;
			columns=maltreader.readLine().split(" ");
			CheckMalt cm=new CheckMalt();
			malt=cm.GetProcessDistributiion(columns, instance.length);
			wInstance.WriteExtendInst(instance, malt);
		}
		System.out.println("count:"+count);
		reader.close();
		maltreader.close();
		writer.close();
	}
	public static void main(String args[]) throws IOException{
		WriteInstance wi=new WriteInstance();
		//wi.RewriteExtend("wsj_00_maltengout.conll", "wsj_00_maltengdiag.txt", "wsj_00_malt_processindex.txt");
		wi.RewriteExtend("wsj_2-21_maltengout.conll", "wsj_2-21_maltengdiag.txt", "wsj_2-21_malt_processindex.txt");
	}
}
