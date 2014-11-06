package Util;

public class Instance {
	public String form[];
	public String pos[];
	public String rel[];
	public int head[];
	public int length;
	public Instance(String form[],String pos[],String rel[],int head[],int length){
		this.form = form;
		this.pos = pos;
		this.rel = rel;
		this.head = head;
		this.length = length;
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
