package com.yuan.classifier;


import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


import com.yuan.bean.FeatureLibrary;
import com.yuan.bean.News;
import com.yuan.dao.NewsDao;
import com.yuan.util.ArticleParticiple;

public class NaiveBayesClassifier {
	public int keyWordNum;
	public NaiveBayesClassifier(int keyWordNum){
		this.keyWordNum = keyWordNum;
	}
	public FeatureLibrary featureLibrary = new FeatureLibrary();
	
	
	/**
	 * 将内容项item拆分为彼此独立的特征，针对该分类为每个特征增加计数值，增加针对该分类的总计数值
	 * @param item
	 * @param cat
	 */
	public void train(String item, String cat){
		//Set<String> features = getWords(item);
		Set<String> features = ArticleParticiple.getKeyWords(item, keyWordNum);
		//针对该分类为每个特征增加计数值
		for(String feature : features){
			featureLibrary.incf(feature, cat);
		}
		//增加针对该分类的计数值
		featureLibrary.incc(cat);
	}
	
	/**
	 * 单词在分类中出现的概率
	 * @param f
	 * @param cat
	 * @return
	 */
	public float fprob(String f, String cat){
		if(featureLibrary.catCount(cat) == 0){
			return 0;
		}
		return (float)featureLibrary.fCount(f, cat)/featureLibrary.catCount(cat);
	}
	
	/**
	 * 对单词的概率进行加权平均，避免在训练初期，应对极少出现的单词
	 * @param f
	 * @param cat
	 * @return
	 */
	public float weigthedprob(String f, String cat, String FeatureLibraryName){
		//计算当前概率值
		float basicProb = 0;
		if("naiveBayes".equals(FeatureLibraryName)){
			basicProb = fprob(f, cat);
		}else if("fisher".equals(FeatureLibraryName)){
			basicProb = cProb(f, cat);
		}
		//统计特征在所有分类中出现的次数
		int totals = 0;
		float weight = 1.0f, ap = 0.5f;
		Set<String> keysSet = featureLibrary.categories();
		for(String key : keysSet){
			totals += featureLibrary.fCount(f, key);
		}
		//计算加权平均
		return ((weight * ap) + (totals * basicProb)) / (weight + totals);
	}
	
	/**
	 * 计算整篇文档的概率，Pr(Document/Category)
	 * @param item
	 * @param cat
	 * @return
	 */
	public float docProb(String item, String cat){
		//Set<String> keysSet = getWords(item);
		Set<String> keysSet = ArticleParticiple.getKeyWords(item, keyWordNum);
		float p = 1;
		for(String key : keysSet){
			p *= weigthedprob(key, cat, "naiveBayes");
		}
		return p;
	}
	
	/**
	 * 计算分类item属于类cat的概率
	 * @param item
	 * @param cat
	 * @return
	 */
	public float prob(String item, String cat){
		float catProb = (float)featureLibrary.catCount(cat) / featureLibrary.totalCount();
		float docProb = docProb(item, cat);
		return catProb * docProb*10000;
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
	public void trainingData() throws SQLException{
		List<News> newsList = getTrainData();
		String classifyid, content;
		for(News news : newsList){
			classifyid = news.getClassifyid();
			content = news.getContent();
			train(content, classifyid);
		}
		//训练结果集
		System.out.println(featureLibrary.getFc().toString());
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
	public float testData(String FeatureLibraryName) throws SQLException{
		List<News> testNews = getTestData();
		int testNewsNum = testNews.size();
		int trueNums = 0;
		for(News news : testNews){
			if(testNews(news, FeatureLibraryName)){
				trueNums++;
			}
		}
		return (float)trueNums / testNewsNum;
	}
	
	/**
	 * 测试新闻news
	 * @param nbc
	 * @param news
	 * @return 若分类器分的类型与新闻原类型相同，返回true，否则返回false
	 */
	public Boolean testNews(News news, String FeatureLibraryName){
		String classifyid = news.getClassifyid();
		String content = news.getContent();
		
		Set<String> categoriesSet = featureLibrary.categories();
		float d;
		Map<String, Float> probMap = new HashMap<String, Float>();
		if("naiveBayes".equals(FeatureLibraryName)){
			for(String categorie : categoriesSet){
				d = prob(content, categorie);
				probMap.put(categorie, d);
			}
		}else if("fisher".equals(FeatureLibraryName)){
			for(String categorie : categoriesSet){
				d = fisherProb(content, categorie);
				probMap.put(categorie, d);
			}
		}
		
		
		String testCategory = classifyMaxProb(probMap);
		if(classifyid.equals(testCategory)){
			return true;
		}
		return false;
	}
	
	/**
	 * 返回测试结果集中属于最大分类概率的分类项
	 * @param probMap
	 * @return
	 */
	public String classifyMaxProb(Map<String, Float> probMap){
		float d;
		float maxProb = 0;
		String targetCategory = null;
		Set<String> keySet = probMap.keySet();
		for(String key : keySet){
			d = probMap.get(key);
			if(d > maxProb){
				maxProb = d;
				targetCategory = key;
			}
		}
		return targetCategory;
	}
	
	
	//费谢尔方法
	
	//假设未来将会收到的文档在各个分类中 的数量是相当的
	//需要用到朴素贝叶斯分类器中的一些方法
	
	/**
	 * 一篇文档出现某个特征时，该文档属于某个分类的可能性
	 * @param f
	 * @param cat
	 * @return
	 */
	public float cProb(String f, String cat){
		//特征在该分类中出现的概率
		float clf = fprob(f, cat);
		if(clf == 0){
			return 0;
		}
		//特征在所有分类中出现的概率
		float freqSum = 0;
		Set<String> cateSet = featureLibrary.categories();
		for(String cate : cateSet){
			freqSum += fprob(f, cate);
		}
		//概率等于特征在该分类中出现的概率除以总体概率
		return clf / freqSum;
	}
	
	
	/**
	 * 将各个特征的概率值组合起来， 形成一个总的概率值
	 * @param item
	 * @param cat
	 * @return
	 */
	public float fisherProb(String item, String cat){
		//将所有概率值相乘
		float p = 1;
		Set<String> kwSet = ArticleParticiple.getKeyWords(item, keyWordNum);
		for(String kw : kwSet){
			p *= weigthedprob(kw, cat, "fisher");
		}
		//取自然对数，并乘以-2
		//double fScore = -2 * Math.log(p);
		//利用倒置对数卡方函数求得概率
		//return (float) invchi2(fScore, kwSet.size()*2);
		return p;
	}
	
	/**
	 * 倒置对数卡方函数
	 * @param chi
	 * @param df
	 * @return
	 */
	public double invchi2(double chi, int df){
		double m = chi / 2.0;
		double sum, term;
		sum = term = Math.exp(m);
		for(int i=0; i<df/2; i++){
			term *= m / i;
			sum += term;
		}
		return Math.min(sum, 1.0);
	}
	
	/**
	 * 测试不同的分类器fisher、naiveBayes
	 * @param FeatureLibraryName
	 * @throws SQLException
	 */
	public static void run(String FeatureLibraryName) throws SQLException{
		int [] keyWordNum = new int[]{10,20,25,30,35,40,50,60,70,80,90,100};
		//int [] keyWordNum = new int[]{35};
		for(int i=0; i<keyWordNum.length; i++){
			NaiveBayesClassifier nbc = new NaiveBayesClassifier(keyWordNum[i]);
			//nbc.testNaiveBayes(nbc);
			nbc.trainingData();//训练数据
			
			float CorrectRate = nbc.testData(FeatureLibraryName);//测试正确率
			System.out.println("keyWordNum:" + keyWordNum[i] + ",CorrectRate:" + CorrectRate);
		}
		
	}
	
	public static void main(String[] args) throws SQLException {
		/*NaiveBayesFeatureLibrary nbc = new NaiveBayesFeatureLibrary();
		nbc.train("你好，能借我钱吗？", "好");
		nbc.train("小偷盗窃钱，还偷钱", "坏");
		nbc.train("还偷钱", "坏");
		int a = nbc.FeatureLibrary.fCount("钱", "好");
		int b = nbc.FeatureLibrary.fCount("钱", "坏");
		float c = nbc.fprob("盗窃", "坏");
		float d = nbc.prob("借", "好");
		float e = nbc.prob("借", "坏");
		
		System.out.println(nbc.FeatureLibrary.getFc().toString());
		System.out.println("a:" + a + "b:" + b + "c:" + c);
		System.out.println("d:" + d + "e:" + e);*/
		
		//naiveBayes,fisher
		run("fisher");
	}
}
