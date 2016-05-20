package com.yuan.bean;

public class News {
	private int id; //新闻id
	private String classifyid;//新闻所属类别id
	private String sourceid;//新闻源类别id
	private String docid;//原新闻id
	private String title;//标题
	private String content;//正文
	private int commentnum;//评论数
	private int scannum;//浏览数
	private String ptime;//新闻时间
	private String newskeys;//新闻关键词
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getClassifyid() {
		return classifyid;
	}
	public void setClassifyid(String classifyid) {
		this.classifyid = classifyid;
	}
	public String getSourceid() {
		return sourceid;
	}
	public void setSourceid(String sourceid) {
		this.sourceid = sourceid;
	}
	public String getDocid() {
		return docid;
	}
	public void setDocid(String docid) {
		this.docid = docid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getCommentnum() {
		return commentnum;
	}
	public void setCommentnum(int commentnum) {
		this.commentnum = commentnum;
	}
	public int getScannum() {
		return scannum;
	}
	public void setScannum(int scannum) {
		this.scannum = scannum;
	}
	public String getPtime() {
		return ptime;
	}
	public void setPtime(String ptime) {
		this.ptime = ptime;
	}
	public String getNewskeys() {
		return newskeys;
	}
	public void setNewskeys(String newskeys) {
		this.newskeys = newskeys;
	}
	
	
}
