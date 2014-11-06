package BeanParser;

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

import Util.ExtendInst;
import Util.ReadExtendInst;

/**
 * this class is used to get index regression arff
 * @author Bean
 *
 */
public class ArffPrepare {
	public Map<String,String> attributes=new TreeMap<String,String>();
	public Map<String, Integer> formmap=new TreeMap<String,Integer>();
	public Map<String, Integer> posmap=new TreeMap<String,Integer>();
	private int featlen=0;
	public FileWriter writer=null;
	public void InitWriteFile(String file) throws IOException{
		File out=new File(file);
		if(!out.exists()){
			out.createNewFile();
		}
		writer=new FileWriter(out);
	}
	public ArffPrepare(String writefile) throws IOException{
		this.InitWriteFile(writefile);
		this.AddAllAttribute();
	}
	/**
	 * add one attribute
	 * @param attriname
	 * @param datatype:string,numeric,{type1,type2,...},date
	 */
	public void AddOneAttribute(String attriname, String datatype){
		attributes.put(attriname, datatype);
	}
	/**
	 * add all the attributes, if any attribute is considered, register here
	 */
	public void AddAllAttribute(){
		//attention: sentence start and end flag
		this.AddOneAttribute("WordForm", "string");			//form[i]
		this.AddOneAttribute("WordPOS", "string");			//pos[i]
		this.AddOneAttribute("LastWordForm", "string");		//form[i-1]
		this.AddOneAttribute("LastWordPOS", "string");		//pos[i-1]
		this.AddOneAttribute("NextWordForm", "string");		//form[i+1]
		this.AddOneAttribute("NextWordPOS", "string");		//pos[i+1]
		this.AddOneAttribute("BeforeLastPOS", "string");	//pos[i-2]
		this.AddOneAttribute("AfterNextPOS", "string");		//pos[i+2]
		this.AddOneAttribute("IndexPercentage", "numeric");	//index/length
		this.AddOneAttribute("SentenceLength", "numeric");	//length
		this.featlen=this.attributes.size();
	}
	/**
	 * call after AddAllAttribute
	 * @param relation
	 * @throws IOException
	 */
	public void WriteHead(String relation) throws IOException{
		writer.write("@relation "+relation+"\n\n");
		Iterator<Entry<String,String>> iterator=attributes.entrySet().iterator();
		while(iterator.hasNext()){
			Entry<String, String> entry=iterator.next();
			String attriname=entry.getKey();
			String attritype=entry.getValue();
			//writer.write("@attribute "+attriname+" "+attritype+"\n");
			writer.write("@attribute "+attriname+" numeric"+"\n");
		}
		writer.write("@attribute ProcessPercentage numeric\n");
		writer.write("\n@data\n");
		writer.flush();
	}
	/**
	 * write a regression item by attributes pre-defined
	 * @param ei
	 * @param index
	 * @throws IOException 
	 */
	public void WriteOneItem(ExtendInst ei,int index) throws IOException{
		Iterator<Entry<String , String>> iterator=this.attributes.entrySet().iterator();
		String itemString="";
		while(iterator.hasNext()){
			Entry<String,String> entry=iterator.next();
			if(entry.getKey().equals("WordForm")){
				itemString+=","+this.NumericFormFeat(this.CommaProcess(ei.form[index]));
			}else if(entry.getKey().equals("WordPOS")){
				itemString+=","+this.NumericPosFeat(this.CommaProcess(ei.pos[index]));
			}else if(entry.getKey().equals("LastWordForm")){
				itemString+=","+this.NumericFormFeat(this.CommaProcess(ei.form[index-1]));
			}else if(entry.getKey().equals("LastWordPOS")){
				itemString+=","+this.NumericPosFeat(this.CommaProcess(ei.pos[index-1]));
			}else if(entry.getKey().equals("NextWordForm")){
				if(index==ei.length){
					itemString+=","+this.NumericFormFeat("SENTEND");
				}else{
					itemString+=","+this.NumericFormFeat(this.CommaProcess(ei.form[index+1]));
				}
			}else if(entry.getKey().equals("NextWordPOS")){
				if(index==ei.length){
					itemString+=","+this.NumericPosFeat("END");
				}else{
					itemString+=","+this.NumericPosFeat(this.CommaProcess(ei.pos[index+1]));
				}
			}else if(entry.getKey().equals("BeforeLastPOS")){
				if(index==1){
					itemString+=","+this.NumericPosFeat("BEFOROOT");
				}else{
					itemString+=","+this.NumericPosFeat(this.CommaProcess(ei.pos[index-2]));
				}
			}else if(entry.getKey().equals("AfterNextPOS")){
				if(index==ei.length-1){
					itemString+=","+this.NumericPosFeat("END");
				}else if(index==ei.length){
					itemString+=","+this.NumericPosFeat("AFTEREND");
				}else{
					itemString+=","+this.NumericPosFeat(this.CommaProcess(ei.pos[index+2]));
				}
			}else if(entry.getKey().equals("IndexPercentage")){
				itemString+=","+(double)index/ei.length;
			}else if(entry.getKey().equals("SentenceLength")){
				itemString+=","+ei.length;
			}else{
				System.out.println("No RELATED PROCESS:"+entry.getKey());
			}
		}
		//TODO
		//predict:process percentage
		itemString=itemString.substring(1)+","+(double)ei.process[index]/ei.length+"\n";
		writer.write(itemString);
		writer.flush();
	}
	private String CommaProcess(String formorpos){
		String retString="";
		if(formorpos.equals(",")){
			retString="COMMA";
		}else if(formorpos.contains("'")){
			retString=formorpos.replace("'", "~");
		}else if(formorpos.contains(",")){
			retString=formorpos.replace(",", "");
		}else if(formorpos.contains("%")){
			retString=formorpos.replace("%", "PERCENT_");
		}else{
			retString=formorpos;
		}
		return retString;
	}
	public void WriteFileItems(String extendinstancefile) throws IOException{
		
		if(this.writer==null){
			System.out.println("call InitWriteFile() first");
		}else{
			//this.GetFormPosMapFromTrain(extendinstancefile);
			BufferedReader reader=new BufferedReader(new FileReader(new File(extendinstancefile)));
			ReadExtendInst rei=new ReadExtendInst(reader);
			ExtendInst ei;
			while((ei=rei.ReadNext())!=null){
				for(int i=1;i<=ei.length;i++){
					this.WriteOneItem(ei, i);
				}
			}
		}
	}
	public void GetFormPosMapFromTrain(String trainfile) throws IOException{
		BufferedReader reader=new BufferedReader(new FileReader(new File(trainfile)));
		ReadExtendInst rei=new ReadExtendInst(reader);
		ExtendInst ei;
		int formcount=1;
		int poscount=1;
		while((ei=rei.ReadNext())!=null){
			for(int i=0;i<=ei.length;i++){
				String form=this.CommaProcess(ei.form[i]);
				if(!this.formmap.containsKey(form)){
					this.formmap.put(form, formcount);
					formcount++;
				}
				String pos=this.CommaProcess(ei.pos[i]);
				if(!this.posmap.containsKey(pos)){
					this.posmap.put(pos, poscount);
					poscount++;
				}
			}
		}
		this.formmap.put("SENTEND", formcount);
		this.posmap.put("END", poscount);
		this.posmap.put("BEFOROOT", poscount+1);
		this.posmap.put("AFTEREND", poscount+2);
	}
	
	public int NumericFormFeat(String form){
		if(this.formmap.containsKey(form)){
			return this.formmap.get(form);
		}else{
			System.out.println("Not contain form:"+form);
			return 0;
		}
	}
	public int NumericPosFeat(String pos){
		if(this.posmap.containsKey(pos)){
			return this.posmap.get(pos);
		}else{
			System.out.println("Not contain pos:"+pos);
			return 0;
		}
	}
//	private void AddToFormMap(String form){
//		
//	}
	public static void main(String args[]) throws IOException{
		ArffPrepare ap=new ArffPrepare("ProcessIndexPredictTrain.arff");
		ap.WriteHead("processindexfeat");
		ap.GetFormPosMapFromTrain("wsj_2-21_malt_processindex.txt");
		ap.WriteFileItems("wsj_2-21_malt_processindex.txt");
		
		ArffPrepare ap2=new ArffPrepare("ProcessIndexPredictTest.arff");
		ap2.WriteHead("processindexfeat");
		//!!share formmap and posmap of 
		ap2.formmap=ap.formmap;
		ap2.posmap=ap.posmap;
		ap2.WriteFileItems("wsj_00-01_malt_processindex.txt");
	}
}
