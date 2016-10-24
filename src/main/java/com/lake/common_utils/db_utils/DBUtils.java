package com.lake.common_utils.db_utils;

import java.sql.*;
import java.util.*;

/**
 * 通用数据库工具类JDBC的封装的模板，带数据库连接池,线程安全
 * 带默认参数
 */

/**
 * @author LakeHm
 *
 * 2016年10月23日下午7:18:35
 */
public class DBUtils {
	//最大连接数
	private static int maxSize = 20;
	//初始连接数
	private static int initialSize = 3;
	
	//数据库驱动  
	private static String jdbcDriver = "com.mysql.jdbc.Driver";
	//数据库url  
    private static String dbUrl = "jdbc:mysql://114.212.118.115:3306/springmvc";
    //数据库ip
//    private static String dbIp = "114.212.118.115";
    //数据库名称
//    private static String dbName = "springmvc";
    //数据库表名称
    private static String dbTableName = "user";
    //数据库用户名  
    private static String dbUsername = "root";
    //数据库密码 
    private static String dbPassword = "root";
    //数据库连接池
    private static volatile DBConnectionPool dbConnectionPool;
    //prepareStatement sql语句预存储
    private static Map<String,String> sqlMap;
    
    public static synchronized void init() {
    	if(checkInit()) {
    		System.err.println("DBUtils初始化已完成");
    		return ;
    	}
    	dbConnectionPool = new DBConnectionPool(maxSize,initialSize,
    			jdbcDriver,dbUrl,dbUsername,dbPassword);
    	initSqlMap();
    }

	public static synchronized void init(String tableName) {
    	if(checkInit()) {
    		System.err.println("DBUtils初始化已完成");
    		return ;
    	}
    	dbTableName = tableName;
    	init();
    }

	public static synchronized void init(String dbName, 
			String tableName) {
    	if(checkInit()) {
    		System.err.println("DBUtils初始化已完成");
    		return ;
    	}
    	dbUrl = "jdbc:mysql://114.212.118.115:3306/" + dbName;
    	dbTableName = tableName;
    	init();
    }

	public static synchronized void init(String ip, 
			String dbName, String tableName) {
		if(checkInit()) {
    		System.err.println("DBUtils初始化已完成");
    		return ;
    	}
		dbUrl = "jdbc:mysql://" + ip + ":3306/" + dbName;
		dbTableName = tableName;
		init();
	}
	
	public static synchronized void init(String ip, String dbName, 
			String tableName, int maxSize, int initialSize) {
		if(checkInit()) {
    		System.err.println("DBUtils初始化已完成");
    		return ;
    	}
		dbUrl = "jdbc:mysql://" + ip + ":3306/" + dbName;
		dbTableName = tableName;
		DBUtils.maxSize = maxSize;
		DBUtils.initialSize = initialSize;
		init();
	}
	
    
    private static void initSqlMap() {
		sqlMap = new HashMap<String,String>();
		sqlMap.put("add", "insert into " + dbTableName + 
				" (id, name, age, date) values (?,?,?,?)");
	}
	
	private static boolean checkInit() {
		if(null != dbConnectionPool) {
    		return true;
    	}
		return false;
	}
	
	//运行前检查初始化
	public static void add(Class<?> clazz) {
		if(!checkInit()) {
			throw new IllegalStateException("DBUtils未初始化");
		}
		Connection connection = dbConnectionPool.getConnection();
		PreparedStatement ps = null;
		try {
			 ps = connection.prepareStatement("");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
