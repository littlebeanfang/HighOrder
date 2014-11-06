package Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

public class ReadExtendInst {
	private BufferedReader reader;
	public ReadExtendInst(BufferedReader reader){
		this.reader = reader;
	}
	public ExtendInst ReadNext() throws IOException{
 		ArrayList<String> lines = new ArrayList<String>();
		String line = reader.readLine();
		//System.out.println("line:"+line);
		if(line==null){
			return null;
		}else{
			while(!line.equals("")){
				//System.out.println("line:"+line);
				lines.add(line);
				line = reader.readLine();
			}
		}
		//System.out.println(lines);
		int length = lines.size();
		String form[]=new String[length+1];
		String pos[]=new String[length+1];
		String rel[]=new String[length+1];
		int process[]=new int[length+1];
		int head[]=new int[length+1];
		form[0]="ROOT";
		pos[0]="ROOT";
		rel[0]="no-type";
		head[0]=-1;
		for(int i=0; i<length ; i++){
			String columns[] = lines.get(i).split("\t");
			form[i+1]=columns[1];
			pos[i+1]=columns[4];
			rel[i+1]=columns[7];
			head[i+1]=Integer.parseInt(columns[6]);
			process[i+1]=Integer.parseInt(columns[10]);
		}
		ExtendInst inst = new ExtendInst(form,pos,rel,head,process,length);
		return inst;
	}
}
