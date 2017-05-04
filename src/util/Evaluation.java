//baseline: accuracy: 0.9196649205401941

package util;


import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Evaluation
{
//    public static void main(String[] args)
//    {
//        int agreeCount = 0;
//        int disagreeCount = 0;
//        
//        HashMap<String, HashMap<String, ArrayList<String>>> tagChosenForTag = new HashMap<String, HashMap<String, ArrayList<String>>>();
//        
//        File testFile = new File("data/test.pos");
//        File outputFile = new File("data/hmm_output.pos");
//        try {
////            outFile = new FileWriter(new File("scoring/score.html"));
//            
//            Scanner testScanner = new Scanner(testFile);
//            Scanner outputScanner = new Scanner(outputFile);
//            
//            String testPOS;
//            String outputPOS;
//            int i = 0;
//            while (testScanner.hasNext() && outputScanner.hasNext()) {
//                i++;
//                // Get the parts of speech
//                testPOS = testScanner.next();
//                outputPOS = outputScanner.next();
//                
//                // Get the associated words
//                String testWord = testScanner.next();
//                String outputWord = outputScanner.next();
//                
//                // Count agreement and disagreement
//                if (testPOS.equals(outputPOS)) {
//                    agreeCount++;
//                } else {
//                    disagreeCount++;
//                }
//                
//                // Count which tag is mistaken for which
//                if (!tagChosenForTag.containsKey(testPOS)) {
//                    tagChosenForTag.put(testPOS, new HashMap<String, ArrayList<String>>());
//                }
//                if (!tagChosenForTag.get(testPOS).containsKey(outputPOS)) {
//                    tagChosenForTag.get(testPOS).put(outputPOS, new ArrayList<String>());
//                }
//                tagChosenForTag.get(testPOS).get(outputPOS).add(testWord);
//                //tagChosenForTag.get(testPOS).put(outputPOS, tagChosenForTag.get(testPOS).get(outputPOS)+1);
//                
//                if(!testWord.equals(outputWord)) {
//                    System.out.println("line "+i+" ERROR: "+testWord+" does not match "+outputWord);
//                }
//            }
//            System.out.println("accuracy: "+agreeCount/(agreeCount+disagreeCount+0.0));
//            testScanner.close();
//            outputScanner.close();
//        }catch (Exception e){
//			System.out.println("excep..");
//			e.printStackTrace();
//		}
//        
//        
//    }
	ArrayList<String> list1 =null;
	ArrayList<String> list2 =null;

    public Evaluation(ArrayList<String> pred, ArrayList<String> truth){
    	list1 = pred;
    	list2 = truth;

    }
    public void eval(){
    	if(list1.size()!=list2.size()){
    		System.out.println("the sizes of two list are not equal "+list1.size()+" "+list2.size());
    		System.exit(1);
    	} else{

    		int agreeCount = 0;
    		int disagreeCount = 0;
    		for (int i =0; i<list2.size();i++){
    			
    			if(list1.get(i).equals(list2.get(i))){
    				agreeCount++;
    			} else {
    				disagreeCount++;
    			}   				
    		}
          System.out.println("accuracy: "+agreeCount/(agreeCount+disagreeCount+0.0));

    	}
    }
    
}
