package Naive;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import Util.ExtendInst;
import Util.ReadExtendInst;

public class TrainFromExtendInstFile {
	//update version, for every arc pair in the conll file, count the arc process add in a map, then parse
	public void Train(String extendinstfile, String modelfile,int proflag) throws IOException{
		//!!process flag: 0:form+pos  1:form   2:pos
		//int proflag=2;
		BufferedReader conllreader=new BufferedReader(new FileReader(extendinstfile));
		Map<String,Integer> featMap=new HashMap<String,Integer>();
		ReadExtendInst ri=new ReadExtendInst(conllreader);
		ExtendInst ei; 
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
					String featString="";
					if(arc1processindex<arc2processindex){
						if(proflag==0){
							featString=arc1parentform+"|"+arc1parentpos+"|"+arc1childform+"|"+arc1childtpos+"|"+arc2parentform+"|"+arc2parentpos+"|"+arc2childform+"|"+arc2childtpos;
						}else if(proflag==1){
							featString=arc1parentform+"|"+arc1childform+"|"+arc2parentform+"|"+arc2childform;
						}else if(proflag==2){
							featString=arc1parentpos+"|"+arc1childtpos+"|"+arc2parentpos+"|"+arc2childtpos;
						}else{
							System.out.println("Never happen");
						}
						
					}else{
						if(proflag==0){
							featString=arc2parentform+"|"+arc2parentpos+"|"+arc2childform+"|"+arc2childtpos+"|"+arc1parentform+"|"+arc1parentpos+"|"+arc1childform+"|"+arc1childtpos;
						}else if(proflag==1){
							featString=arc2parentform+"|"+arc2childform+"|"+arc1parentform+"|"+arc1childform;
						}else if(proflag==2){
							featString=arc2parentpos+"|"+arc2childtpos+"|"+arc1parentpos+"|"+arc1childtpos;
						}else{
							System.out.println("Never happen");
						}
					}
					if(featMap.containsKey(featString)){
						featMap.put(featString, featMap.get(featString)+1);
					}else{
						featMap.put(featString, 1);
					}
				}
				
				//TODO yizhong said: add other arc compare
				for(int i1=0;i1<ei.length;i1++){
					if(i1!=head){
						String arc2childform=arc1childform;
						String arc2childtpos=arc1childtpos;
						String arc2parentform=ei.form[i1];
						String arc2parentpos=ei.pos[i1];
						String featString="";
						if(proflag==0){
							featString=arc1parentform+"|"+arc1parentpos+"|"+arc1childform+"|"+arc1childtpos+"|"+arc2parentform+"|"+arc2parentpos+"|"+arc2childform+"|"+arc2childtpos;
						}else if(proflag==1){
							featString=arc1parentform+"|"+arc1childform+"|"+arc2parentform+"|"+arc2childform;
						}else if(proflag==2){
							featString=arc1parentpos+"|"+arc1childtpos+"|"+arc2parentpos+"|"+arc2childtpos;
						}else{
							System.out.println("Never happen");
						}
						if(featMap.containsKey(featString)){
							//System.out.println(featString);
							featMap.put(featString, featMap.get(featString)+1);
						}else{
							//System.out.println(featString);
							featMap.put(featString, 1);
						}
					}
				}
				
			}
		}
		Iterator<Entry<String, Integer>> iterator=featMap.entrySet().iterator();
		int count=0;
		while(iterator.hasNext()){
			count+=iterator.next().getValue();
		}
		System.out.println("Count:"+count);
		System.out.println("Mapsize:"+featMap.size());
		File out =new File(modelfile);
		if(!out.exists()){
			out.createNewFile();
		}
		FileOutputStream fos=new FileOutputStream(modelfile);
		ObjectOutputStream oos=new ObjectOutputStream(fos);
		oos.writeObject(featMap);
		oos.close();
		conllreader.close();
	}
}
