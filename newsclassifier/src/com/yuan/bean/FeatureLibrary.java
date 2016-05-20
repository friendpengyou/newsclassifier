package com.yuan.bean;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FeatureLibrary {
	private Map<String, Map<String, Integer>> fc = new HashMap<String, Map<String,Integer>>();//统计特征/分类组合的数量
	private Map<String, Integer> cc = new HashMap<String, Integer>();//统计每个分类中的文档数量
	private Set<String> getFeatures;//单个新闻中的去重的词
	
	public Map<String, Map<String, Integer>> getFc() {
		return fc;
	}
	public void setFc(Map<String, Map<String, Integer>> fc) {
		this.fc = fc;
	}
	public Map<String, Integer> getCc() {
		return cc;
	}
	public void setCc(Map<String, Integer> cc) {
		this.cc = cc;
	}
	public Set<String> getGetFeatures() {
		return getFeatures;
	}
	public void setGetFeatures(Set<String> getFeatures) {
		this.getFeatures = getFeatures;
	}
	
	/**
	 * 增加特征/分类组合的计数值
	 * @param f
	 * @param cat
	 */
	public void incf(String f, String cat){
		if(!this.fc.containsKey(f)){
			this.fc.put(f, new HashMap<String,Integer>());
			this.fc.get(f).put(cat, 1);
			return;
		}else if(!this.fc.get(f).containsKey(cat)){
			this.fc.get(f).put(cat, 1);
			return;
		}else {
			int temp = this.fc.get(f).get(cat);
			this.fc.get(f).put(cat, temp + 1);
		}
	}
	
	/**
	 * 增加对某一分类的计数值
	 * @param cat
	 */
	public void incc(String cat){
		if(!this.cc.containsKey(cat)){
			this.cc.put(cat, 1);
			return;
		}else{
			int temp = this.cc.get(cat);
			this.cc.put(cat, temp + 1);
		}
	}
	
	/**
	 * 某一特征出现于某一分类的次数
	 * @param f
	 * @param cat
	 * @return
	 */
	public int fCount(String f, String cat){
		if(this.fc.containsKey(f) && this.fc.get(f).containsKey(cat)){
			return this.fc.get(f).get(cat);
		}
		return 0;
	}
	
	/**
	 * 属于某一分类的内容项目数
	 * @param cat
	 * @return
	 */
	public int catCount(String cat){
		if(this.cc.containsKey(cat)){
			return this.cc.get(cat);
		}
		return 0;
	}
	
	/**
	 * 所有内容项的数目
	 * @return
	 */
	public int totalCount(){
		int sum = 0;
		Set<String> setKeys = this.cc.keySet();
		for(String key : setKeys){
			sum += this.cc.get(key);
		}
		return sum;
	}
	
	/**
	 * 所有分类的列表
	 * @return
	 */
	public Set<String> categories(){
		return this.cc.keySet();
	}
	
}
