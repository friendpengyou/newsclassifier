package com.yuan.bean;

import java.util.ArrayList;
import java.util.Set;

import org.json.JSONObject;

public class InfoMatrix {
	private ArrayList<String> row0 = new ArrayList<String>();
	private ArrayList<String> col0 = new ArrayList<String>();
	private ArrayList<ArrayList<Float>> weightMatrix = new ArrayList<ArrayList<Float>>();
	
	public ArrayList<String> getRow0() {
		return row0;
	}
	public void setRow0(ArrayList<String> row0) {
		this.row0 = row0;
	}
	public ArrayList<String> getCol0() {
		return col0;
	}
	public void setCol0(ArrayList<String> col0) {
		this.col0 = col0;
	}
	public ArrayList<ArrayList<Float>> getWeightMatrix() {
		return weightMatrix;
	}
	public void setWeightMatrix(ArrayList<ArrayList<Float>> weightMatrix) {
		this.weightMatrix = weightMatrix;
	}
	
	/**
	 * 获得第rowValue行的行值，在此例中为文章类型
	 * @param rowValue
	 * @return
	 */
	public String getRowNameValue(int rowValue){
		if(rowValue > row0.size()){
			return "-1";
		}
		return row0.get(rowValue);
	}
	
	/**
	 * 获得第colValue列的列值，在此例中为关键词
	 * @param colValue
	 * @return
	 */
	public String getColNameValue(int colValue){
		if(colValue > col0.size()){
			return "-1";
		}
		return col0.get(colValue);
	}
	
	/**
	 * 获得矩阵第rowValue行，第colValue列对应的值
	 * @param rowValue
	 * @param colValue
	 * @return
	 */
	public Float getWeight(int rowValue, int colValue){
		if(rowValue > row0.size() || colValue > col0.size()){
			return -1f;
		}
		return weightMatrix.get(rowValue).get(colValue);
	}
	
	/**
	 * 加入一条新闻的关键词到矩阵
	 * @param classify
	 * @param keyWordsJSON
	 */
	public void addElement(String classify, JSONObject keyWordsJSON){
		row0.add(classify); //加入该行的分类
		//int rowIndex = row0.size() - 1;
		int colIndex;
		ArrayList<Float> temp = new ArrayList<Float>();
		Set<String> keyWords = keyWordsJSON.keySet();
		for(String keyWord : keyWords){
			colIndex = addKeyWord(keyWord);
			if(colIndex > temp.size() - 1){
				//空缺位置补0
				for(int i=temp.size(); i<colIndex; i++){
					temp.add(0.0f);
				}
				temp.add(colIndex, Float.parseFloat(keyWordsJSON.get(keyWord).toString()));
			}else {
				temp.add(colIndex, Float.parseFloat(keyWordsJSON.get(keyWord).toString()));
			}
		}
		weightMatrix.add(temp);
	}
	
	/**
	 * 把关键词keyWord加入到矩阵
	 * @param keyWord
	 * @return
	 */
	public int addKeyWord(String keyWord){
		int colIndex = col0.indexOf(keyWord);
		if(colIndex != -1){
			return colIndex;
		}else {
			col0.add(keyWord);
			return col0.size() - 1; //新关键词的索引
		}
	}
	
}
