package com.yuan.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBConnection {
	private static final String DBDRIVER = "com.mysql.jdbc.Driver" ;			//驱动类类名
    private static final String DBURL = "jdbc:mysql://sys.friendpengyou.cn:3306/newsclassify?useUnicode=true&characterEncoding=utf-8&autoReconnect=true";//连接URL
    private static final String DBUSER = "root" ;								//数据库用户名
    private static final String DBPASSWORD = "root";							//数据库密码
    
    /**
     * 声明一个连接对象
     * @return
     */
	public static Connection getConnection(){
		Connection conn = null;													
		try {
			Class.forName(DBDRIVER);											//注册驱动
			conn = DriverManager.getConnection(DBURL,DBUSER,DBPASSWORD);		//获得连接对象
		} catch (ClassNotFoundException e) {									//捕获驱动类无法找到异常
			e.printStackTrace();										
		} catch (SQLException e) {												//捕获SQL异常
			e.printStackTrace();
		}
		return conn;
	}
	
	/**
	 * 关闭连接对象
	 * @param conn
	 */
	public static void close(Connection conn) {
		if(conn != null) {				//如果conn连接对象不为空
			try {
				conn.close();			//关闭conn连接对象对象
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 关闭预处理对象
	 * @param pstmt
	 */
	public static void close(PreparedStatement pstmt) {
		if(pstmt != null) {				//如果pstmt预处理对象不为空
			try {
				pstmt.close();			//关闭pstmt预处理对象
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 关闭结果集对象
	 * @param rs
	 */
	public static void close(ResultSet rs) {
		if(rs != null) {				//如果rs结果集对象不为null
			try {
				rs.close();				//关闭rs结果集对象
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) throws SQLException {
		Connection conn = getConnection();
		DBConnection.close(conn);
	}
}
