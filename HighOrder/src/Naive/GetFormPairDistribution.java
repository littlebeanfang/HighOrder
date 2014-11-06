package Naive;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class GetFormPairDistribution implements Serializable{
	public GetFormPairDistribution(){
		//for serializable
	}
	public void train(String pairfileString, String writefile) throws IOException{
		//step 1: get map count;
		Map<String, Integer> featMap=new HashMap<String, Integer>();
		BufferedReader pairReader=new BufferedReader(new FileReader(pairfileString));
		String lineString="";
		while((lineString=pairReader.readLine())!=null){
			if(!lineString.equals("")){
				System.out.println("line:"+lineString);
				String columns[]=lineString.split("\t");
				String parentColumns[]=columns[0].split("\\|");
				System.out.println("parentcolumnnum:"+parentColumns.length);
				for(int i=1;i<columns.length;i++){
					String childcolumns[]=columns[i].split("\\|");
					System.out.println("childcolumnnum:"+childcolumns.length);
					String featString=parentColumns[1]+"|"+parentColumns[2]+"|"+childcolumns[0]+"|"+childcolumns[1];
					if(featMap.containsKey(featString)){
						featMap.put(featString, featMap.get(featString)+1);
					}else{
						featMap.put(featString, 1);
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
		
		//step 2: store map in file(train file).
		File out =new File(writefile);
		if(!out.exists()){
			out.createNewFile();
		}
		FileOutputStream fos=new FileOutputStream(writefile);
		ObjectOutputStream oos=new ObjectOutputStream(fos);
		oos.writeObject(featMap);
		oos.close();
		pairReader.close();
	}
	
}
