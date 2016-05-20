package com.yuan.classifier;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.Map.Entry;

import org.json.JSONObject;

import com.yuan.algorithm.CosineSimilarity;
import com.yuan.bean.InfoMatrix;
import com.yuan.bean.News;
import com.yuan.dao.NewsDao;
import com.yuan.util.ArticleParticiple;
import com.yuan.util.MapSort;

public class KNNClassifier {
    
	public int keyWordNum;
	public KNNClassifier(int keyWordNum){
		this.keyWordNum = keyWordNum;
	}
	public InfoMatrix infoMatrix = new InfoMatrix();
	
    public static void main(String[] args) throws SQLException {
    	testKNNClassifier();
	}
    
    public static void testKNNClassifier() throws SQLException{
    	//int [] a = new int[]{10, 20, 30, 40, 50, 60, 70, 80, 90, 100};
    	int [] a = new int[]{60};
    	for(int i=0; i<a.length; i++){
    		KNNClassifier knnCl = new KNNClassifier(a[i]);
        	knnCl.trainingData();
        	float f = knnCl.testData(10);//10个最相近的类型
            System.out.println("features: " + f);
    	}
    }
    
    /**
	 * 将内容项item拆分为彼此独立的特征，将信息添加到文章-特征矩阵
	 * @param item
	 * @param cat
	 */
	public void train(News news){
		JSONObject features = ArticleParticiple.getKeyWordsScoreJson(news.getTitle(), news.getContent(), keyWordNum);
		infoMatrix.addElement(news.getClassifyid(), features);
		
		//System.out.println("getClassifyid:" + news.getClassifyid() + "features" + features);
		//测试
		/*int rowSize = infoMatrix.getRow0().size();
		int matrixSize  = infoMatrix.getWeightMatrix().size();
		System.out.println("rowSize:" + rowSize + "matrixSize:" + matrixSize);
		System.out.println("分类：" + infoMatrix.getRowNameValue(rowSize - 1)  + "关键词：" + infoMatrix.getWeightMatrix().get(matrixSize - 1));*/
	}
	
	/**
	 * 用KNN思想，得到K个最相近的新闻中，类型新闻数最多的作为分类类型返回
	 * @param news
	 * @param k
	 * @return
	 */
	public String KNNClassify(News news, int k){
		JSONObject features = ArticleParticiple.getKeyWordsScoreJson(news.getTitle(), news.getContent(), keyWordNum);
		infoMatrix.addElement(news.getClassifyid(), features);//添加测试数据
		Map<Float, String> map = matrixOperation();
		
		Set<Entry<Float, String>> mapEntry = map.entrySet();
		int i=0;
		for(Entry<Float, String> entry : mapEntry){
			if(i > 10) break;
			System.out.println("weight:" + entry.getKey() + " ,classifyid:" + entry.getValue());
			i++;
		}
		
		
		return classifyMaxProb(map, k);
	}
	
	/**
	 * 矩阵运算，最后一行测试数据与其它训练数据做向量相乘运算
	 * @return
	 */
	public Map<Float, String> matrixOperation(){
		//测试数据（最后一行）与其它行依次做相乘运算
		ArrayList<ArrayList<Float>> weightMatrix = infoMatrix.getWeightMatrix();
		int NewsNum = weightMatrix.size();
		System.out.println("NewsNum:" + NewsNum);
		ArrayList<Float> testNewsWeight = weightMatrix.get(NewsNum - 1);
		ArrayList<Float> trainNewsWeight;
		Map<Float, String> weightMap = new HashMap<Float, String>();
		float weight;
		
		for(int i=0; i<NewsNum - 1; i++){
			trainNewsWeight = weightMatrix.get(i);
			weight = vectorOperation(testNewsWeight, trainNewsWeight);
			
			weightMap.put(weight, infoMatrix.getRow0().get(i));
		}
		Map<Float, String> resultMap = MapSort.sortMapByKey(weightMap);	//按Key进行排序
		return resultMap;
	}
	
	/**
	 * 计算两个向量相似度
	 * @param testNewsWeight
	 * @param trainNewsWeight
	 * @return
	 */
	public float vectorOperation(ArrayList<Float> testNewsWeight, ArrayList<Float> trainNewsWeight){
		float result = 0.0f;
		int testLen = testNewsWeight.size();
		int trainLen = trainNewsWeight.size();
		int len = Math.min(testLen, trainLen);
		
		ArrayList<Float> a = new ArrayList<Float>();
		ArrayList<Float> b = new ArrayList<Float>();
		
		//去除全零的值
		for(int i=0; i<len; i++){
			if(testNewsWeight.get(i) == 0.0 && trainNewsWeight.get(i) == 0.0){
				continue;
			}else {
				a.add(testNewsWeight.get(i));
				b.add(trainNewsWeight.get(i));
			}
		}
		
		result = CosineSimilarity.cosineSimilarity(new Vector<Float>(a), new Vector<Float>(b));
		/*for(int i=0; i<len; i++){
			result += testNewsWeight.get(i) * trainNewsWeight.get(i);
		}*/
		
		return result;
	}
    
	/**
	 * 返回map中前k个值中分类的个数最多的类型
	 * @param map
	 * @param k
	 * @return
	 */
	public String classifyMaxProb(Map<Float, String> map, int k){
		Map<String, Integer> classMap = new HashMap<String, Integer>();
		Set<Entry<Float, String>> set = map.entrySet();
		String classfy = null;
		int n = 0;
		for(Entry<Float, String> entry : set){
			if(n > k){
				break;
			}
			classfy = entry.getValue();
			if(classMap.containsKey(classfy)){
				classMap.put(classfy, classMap.get(classfy) + 1);
			}else {
				classMap.put(classfy, 1);
			}
			n++;
		}
		Map<String, Integer> sortMap = MapSort.sortMapByValue(classMap);//相似度排名
		Iterator<Entry<String, Integer>> ite = sortMap.entrySet().iterator();
		while(ite.hasNext()){
			classfy = ite.next().getKey();
			break;
		}
		
		return classfy;
	}
	
    /**
	 * 提取训练数据
	 * @return
	 * @throws SQLException
	 */
	public List<News> getTrainData() throws SQLException{
		List<News> newsList = NewsDao.queryAllNews();
		return newsList;
	}
	
	/**
	 * 训练数据
	 * @param nbc
	 * @throws SQLException
	 */
	public  void trainingData() throws SQLException{
		List<News> newsList = getTrainData();
		for(News news : newsList){
			train(news);
		}
	}
	/**
	 * 提取测试数据
	 * @return
	 * @throws SQLException
	 */
	public List<News> getTestData() throws SQLException{
		List<News> testNews = NewsDao.queryAllTestNews();
		return testNews;
	}
	
	/**
	 * 测试数据
	 * @param nbc
	 * @return 正确率
	 * @throws SQLException
	 */
	public float testData(int k) throws SQLException{
		List<News> testNews = getTestData();
		int testNewsNum = testNews.size();
		int trueNums = 0;
		for(News news : testNews){
			if(testNews(news, k)){
				trueNums++;
			}
			//break;//测试一条新闻
		}
		return (float)trueNums / testNewsNum;
	}
	
	/**
	 * 测试新闻news
	 * @param nbc
	 * @param news
	 * @return 若分类器分的类型与新闻原类型相同，返回true，否则返回false
	 */
	public Boolean testNews(News news, int k){
		String classifyid = news.getClassifyid();
		String testCategory = KNNClassify(news, k);
		System.out.println("classifyid:" + classifyid + "testCategory:" + testCategory);
		if(classifyid.equals(testCategory)){
			return true;
		}
		return false;
	}
}
