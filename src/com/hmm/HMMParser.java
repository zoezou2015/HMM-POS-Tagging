/*
 * 
 */
package com.hmm;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class HMMParser {
	
	public static void main(String[] args){
		String train_file = "data/train.pos";
		HMMParser p = new HMMParser(train_file);
		p.HMMEncoder();
	}
	
	Scanner scanner;
	
	private HashMap<String,Integer> wordCount = new HashMap<String, Integer>();
	
	private HashMap<String, Integer> tagCount = new HashMap<String, Integer>();
	private HashMap<String, HashMap<String, Integer>> bigramTagCount = new HashMap<String, HashMap<String,Integer>>();
	private HashMap<String, HashMap<String, Integer>> tag2WordCount = new HashMap<String, HashMap<String,Integer>>();
	private ArrayList<String> wordSequence = new ArrayList<String>();
	private ArrayList<String> tagSequence = new ArrayList<String>();

	
	public ArrayList<String> getWordSequence(){
		return wordSequence;
	}
	

	public ArrayList<String> getTagSequence(){
		return tagSequence;
	}
	
	public HashMap<String, Integer> getWordCount() {
		return wordCount;
	}


	public void setWordCount(HashMap<String, Integer> wordCount) {
		this.wordCount = wordCount;
	}


	public HashMap<String, Integer> getTagCount() {
		return tagCount;
	}


	public void setTagCount(HashMap<String, Integer> tagCount) {
		this.tagCount = tagCount;
	}


	public HashMap<String, HashMap<String, Integer>> getBigramTagCount() {
		return bigramTagCount;
	}


	public void setBigramTagCount(HashMap<String, HashMap<String, Integer>> bigramTagCount) {
		this.bigramTagCount = bigramTagCount;
	}


	public HashMap<String, HashMap<String, Integer>> getTag2WordCount() {
		return tag2WordCount;
	}


	public void setTag2WordCount(HashMap<String, HashMap<String, Integer>> tag2WordCount) {
		this.tag2WordCount = tag2WordCount;
	}

	
	//open target corpus file 
	public HMMParser(String filename){
		File file = new File(filename);
		try{
			scanner = new Scanner(file);
			HMMEncoder();
		} catch(FileNotFoundException e){
			e.printStackTrace();
		}
	}
	
	public void HMMEncoder(){
		if(scanner.hasNext()){
			String prevTag = scanner.next();
			String prevWord = scanner.next();
			wordSequence.add(prevWord);
			tagSequence.add(prevTag);
			addOne(tagCount, prevTag);
			addOne(wordCount, prevWord);
			addOne(tag2WordCount, prevTag, prevWord);
		
		
//		System.out.println(prevWord);
		while(scanner.hasNext()){
			String currTag = scanner.next();
			String currWord = scanner.next();
			addOne(tagCount, currTag);
			addOne(wordCount, currWord);
			addOne(bigramTagCount, prevTag, currTag);
			addOne(tag2WordCount, currWord, currTag);
			prevTag = currTag;
			prevWord = currWord;
			wordSequence.add(currWord);
			tagSequence.add(currTag);
		}
		}
	}
	
	
	private void addOne(HashMap<String, Integer> map, String key1){
		
		if(!map.containsKey(key1)){
			map.put(key1, 1);
		} else{
			map.put(key1, map.get(key1)+1);
		}
	}
	
	private void addOne(HashMap<String, HashMap<String, Integer>> map, String key1, String key2){
		if(map.containsKey(key1)){
			addOne(map.get(key1), key2);
		} else{
			HashMap<String, Integer> subMap = new HashMap<String, Integer>();
			subMap.put(key2, 1);
			map.put(key1, subMap);
		}
	}
	
	
	
}
