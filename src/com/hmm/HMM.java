package com.hmm;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import util.Evaluation;



public class HMM {
	
	public static void main(String[] args){
		String save_path = "data/hmm_output.pos";
		HMM h = new HMM();
		h.write2file(save_path);
		Evaluation eval = new Evaluation(h._tagSequence, h._tagSequenceFromFile);
		eval.eval();
	}
	
	HashMap<String,Integer> _wordCount = new HashMap<String, Integer>();
	HashMap<String, Integer> _tagCount = new HashMap<String, Integer>();
	HashMap<String, HashMap<String, Integer>> _bigramTagCount = new HashMap<String, HashMap<String,Integer>>();
	HashMap<String, HashMap<String, Integer>> _tag2WordCount = new HashMap<String, HashMap<String,Integer>>();
	HashMap<String, HashMap<String, Double>> _transition = new HashMap<String, HashMap<String, Double>>();
	HashMap<String, HashMap<String, Double>> _emission = new HashMap<String, HashMap<String, Double>>();
	
	
	ArrayList<String> _wordSequence = new ArrayList<String>();
	ArrayList<String> _tagSequence = new ArrayList<String>();
	ArrayList<String> _tagSequenceFromFile = null;
	
	boolean ADDONE = false;
	
	
	public HMM(){
		String train_path = "data/train.pos";
		String test_path = "data/test.pos";
		
		HMMParser hTrain = new HMMParser(train_path);
		hTrain.HMMEncoder();
		this._wordCount = hTrain.getWordCount();
		this._tagCount = hTrain.getTagCount();
		this._tag2WordCount = hTrain.getTag2WordCount();
		this._bigramTagCount = hTrain.getBigramTagCount();
		
		HMMParser hTest = new HMMParser(test_path);
		hTest.HMMEncoder();
		this._wordSequence = hTest.getWordSequence();
		this._tagSequenceFromFile = hTest.getTagSequence();
		Viterbi();
	}
	
	public void Viterbi(){
		boolean isStart = true;
		HashMap<String, Node> prevMap = null;
		int j = 0;
		for(int i=0;i<this._wordSequence.size();i++){
			HashMap<String, Node> subMap = new HashMap<String,Node>();
			String word = this._wordSequence.get(i);
//			if (i%1000==0) {
//                System.out.println("working on "+i+" of "+this._wordSequence.size()+" words");
//            }
			
			if(isStart){
				Node n = new Node("<s>", word, null, 1.0);
				subMap.put("<s>", n);
				isStart= false;
			} else{
				if(this._tag2WordCount.containsKey(word)){
					HashMap<String, Integer> tag2count = this._tag2WordCount.get(word);
					for(String tag : tag2count.keySet()){
						subMap.put(tag, calcNode(word, tag, prevMap));
					}
				} else if(word.matches("[A-Z]\\w*")){
                    subMap.put("NNP", calcNode(word, "NNP", prevMap));
				} else if (word.matches("\\p{Digit}*.\\p{Digit}*") || word.matches("(\\p{Punct}+|\\p{Digit}+)+")) {
                    subMap.put("CD", calcNode(word, "CD", prevMap));
                } else if (word.contains("-") || word.matches(".*able")) {
                    subMap.put("JJ", calcNode(word, "JJ", prevMap));
                } else if (word.matches(".*ing")) {
                    subMap.put("VBG", calcNode(word, "VBG", prevMap));
                } else if (word.matches(".*ly")) {
                    subMap.put("RB", calcNode(word, "RB", prevMap));
                } else if (word.matches(".*ed")) {
                    subMap.put("VBN", calcNode(word, "VBN", prevMap));
                } else if (word.matches(".*s")) {
                    subMap.put("NNS", calcNode(word, "NNS", prevMap));
                } else{
					for(String tag : this._tagCount.keySet()){
						subMap.put(tag, calcNode(word, tag, prevMap));
					}
				}
				if((i == this._wordSequence.size()-1) || (this._wordSequence.get(i+1).equals("<s>"))){
					j++;
					backtrace(subMap);
					isStart = true;
				}
			}
			prevMap = subMap;					
		}
		System.out.println(j);
	}
	
	private int counts(HashMap<String, Integer> map, String key1){
		return map.containsKey(key1)? map.get(key1): 0;
	}

	private int counts(HashMap<String, HashMap<String, Integer>> map, String key1, String key2){
		return map.containsKey(key1)? counts(map.get(key1), key2): 0;	
	}
	
	
	public double calcTrasition(String prevTag, String currTag){
		if(ADDONE){
			return (counts(this._bigramTagCount, prevTag, currTag)+1.0)/(counts(this._tagCount, prevTag)+this._tagCount.keySet().size()+0.0);
		} else{
			return counts(this._bigramTagCount, prevTag, currTag)/(counts(this._tagCount, prevTag)+0.0);
		}
	}
	
	public double calcEmission(String word, String tag){
		if(ADDONE){
			return (counts(this._tag2WordCount, word, tag)+1)/(counts(this._wordCount, word)+this._wordCount.keySet().size()+0.0);
		} else{
			return counts(this._tag2WordCount, word, tag)/(counts(this._wordCount, word)+0.0);

		}
	}
	
	public Node calcNode(String word, String tag, HashMap<String, Node> map){
		Node maxN = null;
		double maxProb = 0.0;
		for(String prevTag : map.keySet()){
			double prevProb = map.get(prevTag).get_prob();
			double currProb = prevProb*calcTrasition(prevTag, tag);
			if (currProb>maxProb){
				maxProb=currProb;
				maxN = map.get(prevTag);
			}
		}
		maxProb *= calcEmission(word, tag);		
				
		return new Node(tag, word, maxN, maxProb);
	}
	
	public void backtrace(HashMap<String, Node> map){
//		Node maxN = new Node("NOMAX", "NOMAX");
		Node maxN = null;
		double maxP = 0.0;
		for(String key:map.keySet()){			
			if(map.get(key).get_prob()>maxP){
				maxN = map.get(key);
				maxP = maxN.get_prob();
			}
			
				
		}
		
		Stack<Node> stack = new Stack<Node>();
		while(maxN != null){
			stack.push(maxN);
			maxN = maxN.get_parent();
		}
		
		while(!stack.isEmpty()){
			Node n = stack.pop();
			this._tagSequence.add(n.get_tag());
		}
	}
	
	
    
	
	public void write2file(String filepath){
		FileWriter writer = null;
		try {
			writer = new FileWriter(new File(filepath));
			for(int i=0; i<this._tagSequence.size(); i++){
				String word = this._wordSequence.get(i);
				String tag = this._tagSequence.get(i);
				try {
					writer.write(tag + " " + word + "\n");
				} catch (Exception e) {
					e.printStackTrace();
					System.exit(1);
				}
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
}
