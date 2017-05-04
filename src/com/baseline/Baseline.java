package com.baseline;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;

import util.Evaluation;

public class Baseline {
	public static void main(String[] args){
		String savepath = "data/baseline_output.pos";
		Baseline b = new Baseline();
		b.BaselineParser();
		b.write2File(savepath);
		Evaluation eval = new Evaluation(b.tagSequence, b.tagSequenceFromFile);
		eval.eval();
	}
	
	HashMap<String,Integer> wordCount = new HashMap<String, Integer>();
	HashMap<String, Integer> tagCount = new HashMap<String, Integer>();
	HashMap<String, HashMap<String, Integer>> bigramTagCount = new HashMap<String, HashMap<String,Integer>>();
	HashMap<String, HashMap<String, Integer>> tag2WordCount = new HashMap<String, HashMap<String,Integer>>();
	ArrayList<String> wordSequence = new ArrayList<String>();
	ArrayList<String> tagSequence = new ArrayList<String>();
	ArrayList<String> tagSequenceFromFile = null;

	public Baseline(){
		String train_file = "data/train.pos";
		String test_file = "data/test.pos";
		BaselineParser hTrain = new BaselineParser(train_file);
		hTrain.Encoder();
		this.wordCount = hTrain.getWordCount();
		this.tagCount = hTrain.getTagCount();
		this.bigramTagCount = hTrain.getBigramTagCount();
		this.tag2WordCount = hTrain.getTag2WordCount();
		
		BaselineParser hTest = new BaselineParser(test_file);
		hTest.Encoder();
		this.wordSequence = hTest.getWordSequence();
		this.tagSequenceFromFile = hTest.getTagSequence();		
	}
	
	public String getMaxTag(HashMap<String, Integer> map){
		int currMax = 0;
		String bestTag = "NOTAG";
		for(String tag:map.keySet()){
			if(map.get(tag)>currMax){
				currMax=map.get(tag);
				bestTag=tag;
			}
		}
		return bestTag;
	}
	
	public void BaselineParser(){
		String BestOverallTag = getMaxTag(tagCount);
		
		for(int i=0; i<this.wordSequence.size();i++){
			String word = this.wordSequence.get(i);
			if(this.tag2WordCount.containsKey(word)){
				String bestTag = getMaxTag(this.tag2WordCount.get(word));
				this.tagSequence.add(bestTag);
			} else{
				// unseen word labeled with best overall tag
				this.tagSequence.add(BestOverallTag);
			}
		}
	}

	public void write2File(String filepath){
		FileWriter writer = null;
		try {
			writer = new FileWriter(new File(filepath));
			for(int i=0; i<this.tagSequence.size();i++){
				String tag = this.tagSequence.get(i);
				String word = this.wordSequence.get(i);
				try {
		            writer.write(tag + " " + word + "\n");
		        } catch (Exception e) {
		            e.printStackTrace();
		            System.exit(1);
		        }
			}
			writer.close();
		} catch (Exception e) {
			// TODO: handle exception
		    e.printStackTrace();
            System.exit(1);
		}
		
		
		
	}
	
}
