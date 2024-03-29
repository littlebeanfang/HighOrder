package BeanParser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import Util.ExtendInst;
import Util.ReadExtendInst;
import weka.classifiers.Classifier;
import weka.classifiers.functions.LinearRegression;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

public class Predict {
	public Classifier classifier;
	public FileWriter writer;
	public ArrayList<Integer> inst_word_num ;
	/**
	 * for advanced predict method
	 * @param writefile
	 * @throws IOException 
	 */
	public void InitWriter(String writefile) throws IOException{
		File outFile=new File(writefile);
		if(!outFile.exists()){
			outFile.createNewFile();
		}
		this.writer=new FileWriter(outFile);
	}
	/**
	 * this function is used to get rough word process_percentage out
	 * @param trainfile
	 * @param testfile
	 * @param ansfile
	 * @throws Exception
	 */
	public void SimpleTrainAndPredict(String trainfile,String testfile,String ansfile,int classindex) throws Exception{
		File outFile=new File(ansfile);
		if(!outFile.exists()){
			outFile.createNewFile();
		}
		FileWriter writer=new FileWriter(outFile);
		
		Classifier lrClassifier=new LinearRegression();
		File inputFile=new File(trainfile);
		ArffLoader atf=new ArffLoader();
		atf.setFile(inputFile);
		Instances trainInstances=atf.getDataSet();
		inputFile=new File(testfile);
		atf.setFile(inputFile);
		Instances testInstances=atf.getDataSet();
		testInstances.setClassIndex(classindex);
		trainInstances.setClassIndex(classindex);
		lrClassifier.buildClassifier(trainInstances);
		
		int testcount=testInstances.numInstances();
		for(int i=0;i<testcount;i++){
			double gold=testInstances.instance(i).value(10);
			double predict=lrClassifier.classifyInstance(testInstances.instance(i));
			writer.write(gold+"\t"+predict+"\n");
		}
		
		writer.close();
	}
	public void GetSquareError(String SimpleTrainOut) throws IOException{
		BufferedReader reader=new BufferedReader(new FileReader(new File(SimpleTrainOut)));
		String line;
		double SquareerrorSum=0;
		int count=0;
		while((line=reader.readLine())!=null){
			String columns[]=line.split("\t");
			double gold=Double.parseDouble(columns[0]);
			double predict=Double.parseDouble(columns[1]);
			SquareerrorSum+=Math.pow(gold-predict, 2);
			count++;
		}
		System.out.println("Square Error:"+SquareerrorSum/count);
	}
	public void Train(Classifier cf,String trainfile,int labelindex ) throws Exception{
		this.classifier=cf;
		File inputFile=new File(trainfile);
		ArffLoader atf=new ArffLoader();
		atf.setFile(inputFile);
		Instances trainInstances=atf.getDataSet();
		trainInstances.setClassIndex(labelindex);
		this.classifier.buildClassifier(trainInstances);
	}
	/**
	 * add normalization in a sentence instance prediction
	 * @param insts
	 * @throws Exception
	 */
	public void PredictInstance(Instance[] insts,int labelindex) throws Exception{
		//get predict vector
		System.out.println("inst length"+insts.length);
		double predicts[]=new double[insts.length];
		for(int i=0;i<insts.length;i++){
			predicts[i]=this.classifier.classifyInstance(insts[i]);
			//System.out.println("predict["+i+"]:"+predicts[i]);
		}
		//order and normalize
		Map<Integer,Double> index_percentage = new HashMap<Integer, Double>();
		for(int i=0;i<insts.length;i++){
			index_percentage.put(i, predicts[i]);
		}
		//System.out.println("Tranverse index_percentage map:");
		//this.TranverseMap(index_percentage);
		Map<Integer,Double> sortedmap=this.sortMapByValue(index_percentage);
		//System.out.println("Tranverse sorted map:");
		//this.TranverseMap(sortedmap);
		Map<Integer,Double> normalizedmap=new TreeMap<Integer, Double>();
		
		Iterator ite=sortedmap.entrySet().iterator();
		double ordercount=insts.length;
		while(ite.hasNext()){
			Map.Entry<Integer, Double> item=(Entry<Integer, Double>) ite.next();
			normalizedmap.put(item.getKey(), ordercount/insts.length);
			ordercount-=1;
		}
		//System.out.println("Tranverse normalized map:");
		//this.TranverseMap(normalizedmap);
		//System.out.println("map 1:"+index_percentage.size()+",map 2:"+sortedmap.size()+",map 3:"+normalizedmap.size());
		//write file
		Iterator ite2=normalizedmap.entrySet().iterator();
		int index=0;
		while(ite2.hasNext()){
			Map.Entry<Integer, Double> word=(Entry<Integer, Double>) ite2.next();
			//System.out.println(insts[index].value(labelindex)+"\t"+word.getValue());
			//this.writer.write(insts[index].value(labelindex)+"\t"+predicts[index]+"\t"+word.getValue()+"\n");
			this.writer.write((index+1)+"\t"+insts[index].value(labelindex)*insts.length+"\t"+word.getValue()*insts.length+"\n");
			writer.flush();
			index++;
		}
		writer.write("\n");
		writer.flush();
	}
	
	public void WordCountToArray(String extendinstfile) throws IOException{
		this.inst_word_num=new ArrayList<Integer>();
		BufferedReader reader=new BufferedReader(new FileReader(new File(extendinstfile)));
		ReadExtendInst rei=new ReadExtendInst(reader);
		ExtendInst ei;
		int count=0;
		while((ei=rei.ReadNext())!=null){
			this.inst_word_num.add(ei.length);
			count+=ei.length;
		}
		System.out.println("Total Length of word num:"+count);
		reader.close();
	}
	
	public void TestOneInstance(String testfile,int classindex) throws Exception{
		//call PredictInstance(), send one instance there
		//before using this function should call WordCountToArray() to init inst_word_num
		if(this.inst_word_num.size()==0){
			System.out.println("Init inst_word_num before calling TestOneInstance.");
			return;
		}
		File inputFile=new File(testfile);
		ArffLoader atf=new ArffLoader();
		atf.setFile(inputFile);
		Instances testInstances=atf.getDataSet();
		testInstances.setClassIndex(classindex);
		/*
		int testcount=testInstances.numInstances();
		for(int i=0;i<testcount;i++){
			double gold=testInstances.instance(i).value(10);
			double predict=lrClassifier.classifyInstance(testInstances.instance(i));
			writer.write(gold+"\t"+predict+"\n");
		}
		*/
		//double gold=testInstances.instance(0).value(classindex);
		//get the word num of first sentence
		int word_num=inst_word_num.get(0);
		Instance inst[]=new Instance[word_num];
		System.out.println("word num:"+word_num);
		for(int i=0;i<word_num;i++){
			inst[i]=testInstances.instance(i);
		}
		this.PredictInstance(inst, classindex);
	}
	public void Test(String testfile,int classindex) throws Exception{
		//call PredictInstance(), send one instance there
		//before using this function should call WordCountToArray() to init inst_word_num
		if(this.inst_word_num.size()==0){
			System.out.println("Init inst_word_num before calling TestOneInstance.");
			return;
		}
		File inputFile=new File(testfile);
		ArffLoader atf=new ArffLoader();
		atf.setFile(inputFile);
		Instances testInstances=atf.getDataSet();
		testInstances.setClassIndex(classindex);
		/*
		int testcount=testInstances.numInstances();
		for(int i=0;i<testcount;i++){
			double gold=testInstances.instance(i).value(10);
			double predict=lrClassifier.classifyInstance(testInstances.instance(i));
			writer.write(gold+"\t"+predict+"\n");
		}
		*/
		//double gold=testInstances.instance(0).value(classindex);
		//get the word num of first sentence
		int count=inst_word_num.size();
		int index=0;
		while(index++<count-1){
			int word_num=inst_word_num.get(index);
			Instance inst[]=new Instance[word_num];
			System.out.println("word num:"+word_num);
			for(int i=0;i<word_num;i++){
				inst[i]=testInstances.instance(i);
			}
			this.PredictInstance(inst, classindex);
		}
	}
//	public int[] GetOrder(double[] list){
//		int order[]=new int[list.length];
//		double lastmin=-1;
//		double currentmin=Double.POSITIVE_INFINITY;
//		int minindex=0;
//		for(int i=0;i<list.length;i++){
//			//select ith smallest
//			for(int j=0;j<list.length;j++){
//				if(list[j]>lastmin){
//					
//				}
//			}
//			
//			order[i]=currentmin;
//			
//		}
//		return order;
//	}
	
	public Map<Integer, Double> sortMapByValue(Map<Integer, Double> oriMap) {  
		Map<Integer, Double> sortedMap = new LinkedHashMap<Integer, Double>();  
		if (oriMap != null && !oriMap.isEmpty()) {  
			List<Map.Entry<Integer, Double>> entryList = new ArrayList<Map.Entry<Integer, Double>>(oriMap.entrySet());  
			Collections.sort(entryList,  
					new Comparator<Map.Entry<Integer, Double>>(){  
						public int compare(Entry<Integer, Double> entry1,  
						Entry<Integer, Double> entry2) {  
						double value1 = 0, value2 = 0;  
						try {  
								value1 = entry1.getValue();  
								value2 = entry2.getValue();  
				            } catch (NumberFormatException e) {  
				            	value1 = 0;  
				            	value2 = 0;  
				            }  
						
						if(value2>value1){
							return 1;
						}else if(value2==value1){
							return 0;
						}else{
							return -1;
						}
			        }  
			             });  
			    Iterator<Map.Entry<Integer, Double>> iter = entryList.iterator();  
			    Map.Entry<Integer, Double> tmpEntry = null;  
			     while (iter.hasNext()) {  
			           tmpEntry = iter.next();  
			          sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());  
			      }  
			 }  
			   return sortedMap;  
	}
	
	public void TranverseMap(Map<Integer,Double> m){
		Iterator<Entry<Integer, Double>> iterator=m.entrySet().iterator();
		System.out.println("Map:");
		while(iterator.hasNext()){
			Entry<Integer, Double> entry=iterator.next();
			System.out.println("key:"+entry.getKey()+",value:"+entry.getValue());
		}
	}

	public static void main(String args[]) throws Exception{
		Predict predict=new Predict();
		//predict.SimpleTrainAndPredict("ProcessIndexPredictTrain.arff", "ProcessIndexPredictTest.arff", "LRpredictout.txt",10);
		//predict.GetSquareError("LRpredictout.txt");
		//Classifier lrClassifier=new LinearRegression();
		Classifier lrClassifier=new MultilayerPerceptron();
		predict.Train(lrClassifier, "ProcessIndexPredictTrain.arff",10);
		predict.InitWriter("Test_MultilayerPerceptron.txt");
		predict.WordCountToArray("wsj_00-01_malt_processindex.txt");
		//predict.TestOneInstance("ProcessIndexPredictTest_remove_index.arff", 9);
		predict.Test("ProcessIndexPredictTest.arff", 10);
	}
}
