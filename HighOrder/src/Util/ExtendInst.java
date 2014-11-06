package Util;

public class ExtendInst {
	public String form[];
	public String pos[];
	public String rel[];
	public int head[];
	public int process[];
	public int length;
	public ExtendInst(String form[],String pos[],String rel[],int head[],int process[],int length){
		this.form = form;
		this.pos = pos;
		this.rel = rel;
		this.head = head;
		this.length = length;
		this.process=process;
	}
	public String toString(){
		String instString="";
		for(int i=0;i<this.length;i++){
			instString+=form[i]+" ";
		}
		instString=instString.trim()+"\n";
		return instString;
	}
}
