package Naive;

import java.io.IOException;

import ProcessTree.GetRelativePriority;
import Util.WriteInstance;

public class Test {
	public void Run(String pairfile, String modelfile,String testfile,String orderfile) throws IOException{
		
	}
	public static void main(String args[]) throws Exception{
		/*
		//rewrite conll file(add process index)
		WriteInstance wi=new WriteInstance();
		wi.RewriteExtend("wsj_00-01_maltengout.conll", "wsj_00-01_maltengdiag.txt", "wsj_00-01_malt_processindex.txt");
		//get relative priority
		GetRelativePriority grp=new GetRelativePriority();
		grp.WriteFileInst("wsj_00-01_malt_processindex.txt", "wsj_00-01_malt_priority.txt");
		*/
		//train model
		//version 1
		//GetFormPairDistribution gfpd=new GetFormPairDistribution();
		//gfpd.train("wsj_00-01_malt_priority.txt", "wsj_00-01_naive.model");
		//version 2
		TrainFromExtendInstFile tfeif=new TrainFromExtendInstFile();
		//tfeif.Train("wsj_2-21_malt_processindex.txt", "wsj_2-21_naive_version2_formpos.model",0);
		//tfeif.Train("wsj_2-21_malt_processindex.txt", "wsj_2-21_naive_version2_form.model",1);
		tfeif.Train("wsj_2-21_malt_processindex.txt", "wsj_2-21_naive_version2_pos.model",2);
		
		//load model
		/*version 1
		PairDisTest pdt=new PairDisTest("wsj_00-01_naive.model");
		String[] feat={"ROOT","ROOT","chairman","NN","chairman","NN","is","VBZ"};
		double prob=pdt.GetProbability(feat);
		System.out.println("Probability:"+prob);
		*/
		PairDisTest pdt=new PairDisTest("wsj_00-01_naive_version2_pos.model");
		String[] feat={"chairman","NN","is","VBZ","ROOT","ROOT","chairman","NN"};
		double prob=pdt.GetProbabilityVersion2(feat,2);
		System.out.println("Probability:"+prob);
		//String[] feat2={"chairman","NN","is","VBZ","ROOT","ROOT","chairman","NN"};
		String[] feat2={"ROOT","ROOT","is","VBZ","ROOT","ROOT","chairman","NN"};
		prob=pdt.GetProbabilityVersion2(feat2,2);
		System.out.println("Probability:"+prob);
		
//		Test t=new Test();
//		t.Run("wsj_00_malt_priority.txt", "wsj_00_naive.model", "fff", "fff");
	}
}
