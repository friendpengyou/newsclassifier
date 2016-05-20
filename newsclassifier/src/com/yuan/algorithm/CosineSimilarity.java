package com.yuan.algorithm;

import java.util.Vector;

public class CosineSimilarity {
	
	/**
	 * 向量a与向量b的余弦相似度
	 * @param a
	 * @param b
	 * @return  float
	 */
	public static float cosineSimilarity(Vector<Float> a, Vector<Float> b){
		float sum = 0f;
		for(int i=0; i<a.size(); i++){
			sum += a.get(i) * b.get(i);
		}
		
		float aSqureSum = 0;
		for(int i=0; i<a.size(); i++){
			aSqureSum += a.get(i)* a.get(i);
		}
		float bSqureSum = 0;
		for(int i=0; i<a.size(); i++){
			bSqureSum += b.get(i)* b.get(i);
		}
		
		return sum / (float)Math.sqrt(aSqureSum * bSqureSum);
	}
	
	public static void main(String[] args) {
		Vector<Float> a = new Vector<Float>();
		Vector<Float> b = new Vector<Float>();
		a.add(1f);
		a.add(2f);
		b.add(3f);
		
		float c = cosineSimilarity(a, b);
		System.out.println(c);
	}
}
