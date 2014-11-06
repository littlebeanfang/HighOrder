package ProcessTree;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;

import Util.ExtendInst;
import Util.ReadExtendInst;

public class GetRelativePriority {
	public GetRelativePriority(){
		//for calling function: WriteFileInst()
	}
	private FileWriter writer;
	public GetRelativePriority(FileWriter writer){
		this.writer=writer;
	}
	public void WriteOneInst(ExtendInst einst) throws IOException{
		Map<Integer, TreeSet<TreeNode>> instMap = new TreeMap<Integer, TreeSet<TreeNode>>();
		for(int i=0;i<=einst.length;i++){
			instMap.put(i, new TreeSet<TreeNode>());
		}
		for(int i=1;i<=einst.length;i++){
			int head=einst.head[i];
			//System.out.println("head:"+head);
			instMap.get(head).add(new TreeNode(einst.form[i], einst.rel[i], einst.pos[i], einst.process[i]));
		}
		Iterator<Entry<Integer, TreeSet<TreeNode>>> iterator=instMap.entrySet().iterator();
		while(iterator.hasNext()){
			Entry<Integer, TreeSet<TreeNode>> entry=iterator.next();
				if(entry.getValue().size()>0){
				writer.write(entry.getKey()+"|"+einst.form[entry.getKey()]+"|"+einst.pos[entry.getKey()]+"\t");
				TreeSet<TreeNode> childsNodes=entry.getValue();
				Iterator<TreeNode> childite=childsNodes.iterator();
				String childline="";
				while(childite.hasNext()){
					TreeNode child=childite.next();
					childline+=child.form+"|"+child.POS+"|"+child.rel+"\t";
				}
				writer.write(childline.trim()+"\n");
			}
		}
		writer.write("\n");
		writer.flush();
	}
	public void WriteFileInst(String precessindexconllfile, String outpriorityfile) throws IOException{
		BufferedReader reader=new BufferedReader(new FileReader(precessindexconllfile));
		ReadExtendInst rei=new ReadExtendInst(reader);
		File out=new File(outpriorityfile);
		if(!out.exists()){
			out.createNewFile();
		}
		FileWriter writer=new FileWriter(out);
		GetRelativePriority grp=new GetRelativePriority(writer);
		ExtendInst ei;
		while((ei=rei.ReadNext())!=null){
			grp.WriteOneInst(ei);
		}
		reader.close();
		writer.close();
	}
	public static void main(String args[]) throws IOException{
		GetRelativePriority grp=new GetRelativePriority();
		grp.WriteFileInst("wsj_00_malt_processindex.txt", "wsj_00_malt_priority.txt");
	}
}
