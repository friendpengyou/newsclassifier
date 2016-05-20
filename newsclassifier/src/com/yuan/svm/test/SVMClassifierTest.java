package com.yuan.svm.test;

import java.io.IOException;

public class SVMClassifierTest {
	 public static void main(String[] args) throws IOException {  
		 //test();
		 testScale();
	    }  
	 
	 public static void test() throws IOException{
		 
		 		// TODO Auto-generated method stub
		 		//Test for svm_train and svm_predict
		 		//svm_train: 
		 		//	  param: String[], parse result of command line parameter of svm-train
		 		//    return: String, the directory of modelFile
		 		//svm_predect:
		 		//	  param: String[], parse result of command line parameter of svm-predict, including the modelfile
		 		//    return: Double, the accuracy of SVM classification
		 		String[] trainArgs = {"UCI-breast-cancer-tra"};//directory of training file
		 		String modelFile = svm_train.main(trainArgs);
		 		String[] testArgs = {"UCI-breast-cancer-test", modelFile, "UCI-breast-cancer-result"};//directory of test file, model file, result file
		 		Double accuracy = svm_predict.main(testArgs);
		 		System.out.println("SVM Classification is done! The accuracy is " + accuracy);
		 		
		 		//Test for cross validation
		 		//String[] crossValidationTrainArgs = {"-v", "10", "UCI-breast-cancer-tra"};// 10 fold cross validation
		 		//modelFile = svm_train.main(crossValidationTrainArgs);
		 		//System.out.print("Cross validation is done! The modelFile is " + modelFile);
		 
	 }
	 
	 public static void testScale() throws IOException{
		 // TODO Auto-generated method stub  
	        //Test for svm_train and svm_predict  
	        //svm_train:   
	        //    param: String[], parse result of command line parameter of svm-train  
	        //    return: String, the directory of modelFile  
	        //svm_predect:  
	        //    param: String[], parse result of command line parameter of svm-predict, including the modelfile  
	        //    return: Double, the accuracy of SVM classification  
	        String[] trainArgs = {"UCI-breast-cancer-tra"};//directory of training file  
	        svm_scale.main(new String[]{"-p", "UCI-breast-cancer-tra-scale", "UCI-breast-cancer-tra"});
	        svm_scale.main(new String[]{"-p", "UCI-breast-cancer-test-scale", "UCI-breast-cancer-test"});
	        
	        String[] scaleTrainArgs = {"UCI-breast-cancer-tra-scale"};//directory of training file  
	        String modelFile = svm_train.main(scaleTrainArgs); 
	        
	        String[] testArgs = {"UCI-breast-cancer-test-scale", modelFile, "UCI-breast-cancer-result"};//directory of test file, model file, result file  
	        Double accuracy = svm_predict.main(testArgs);  
	        System.out.println("SVM Classification is done! The accuracy is " + accuracy);  
	          
	        //Test for cross validation  
	        //String[] crossValidationTrainArgs = {"-v", "10", "UCI-breast-cancer-tra"};// 10 fold cross validation  
	        //modelFile = svm_train.main(crossValidationTrainArgs);  
	        //System.out.print("Cross validation is done! The modelFile is " + modelFile);
	 }
}
