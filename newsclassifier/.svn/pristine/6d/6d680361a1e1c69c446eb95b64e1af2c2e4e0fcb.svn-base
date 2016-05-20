package com.yuan.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import org.json.JSONObject;

import com.yuan.bean.News;
import com.yuan.dao.NewsDao;
import com.yuan.util.ArticleParticiple;

public class NewsParticiple {
	
	/**
	 * 对数据库内的所有文章进行分词，并keys及权重存储到MySQL
	 * @throws SQLException
	 */
	public static void allNewsParticiple() throws SQLException{
		List<News> newsList = NewsDao.queryAllNews();
		JSONObject jb;
		for (News news : newsList){
			jb = newsParticiple(news);
			news.setNewskeys(jb.toString());
			System.out.println(jb);
			//NewsDao.updateKeys(news);
			break;
		}
	}
	
	/**
	 * 对某篇新闻进行分词及权重，返回JSONObject
	 * @param news
	 * @return JSONObject
	 * @throws SQLException
	 */
	public static JSONObject newsParticiple(News news) throws SQLException{
		String title = news.getTitle();
		String content = news.getContent();
		JSONObject jb = ArticleParticiple.getKeyWordsJson(title, content,30);
		return jb;
	}
	
	
	
	public static void test() throws SQLException{
		String docidStr = "BIJJU14200364O9M";
		News news = NewsDao.queryByDocid(docidStr);
		String title = news.getTitle();
		System.out.println(title);
		String content = news.getContent();
		
		/*List<Term> parse  = ArticleParticiple.toAnalysis(content);
		System.out.println(parse.toString());
		for(Term term : parse){
			System.out.println(term);
		}
		
		String test = "法国总统弗朗索瓦?奥朗德赶往现场";
		List<Term> parseTest1  = ToAnalysis.parse(test);
		List<Term> parseTest2  = BaseAnalysis.parse(test);
		List<Term> parseTest3  = NlpAnalysis.parse(test);
		List<Term> parseTest4  = IndexAnalysis.parse(test);
		System.out.println(parseTest1.toString());
		System.out.println(parseTest2.toString());
		System.out.println(parseTest3.toString());
		System.out.println(parseTest4.toString());*/
		
		Set<String> parse  = ArticleParticiple.getWords(content);
		System.out.println(parse.toString());
		for(String term : parse){
			System.out.println(term);
		}
	}
	
	public static void main(String[] args) throws SQLException {
		//allNewsParticiple();
		test();
		
	}
}
