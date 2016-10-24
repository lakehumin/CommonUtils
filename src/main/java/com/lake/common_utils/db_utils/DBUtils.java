package com.lake.common_utils.db_utils;
/**
 * 通用数据库工具类JDBC的封装，带数据库连接池,线程安全
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
    private static String dbIp = "114.212.118.115";
    //数据库名称
    private static String dbName = "springmvc";
    //数据库用户名  
    private static String dbUsername = "root";
    //数据库密码 
    private static String dbPassword = "root";
    
    private static DBConnectionPool dbConnectionPool;
    
    public static void init() {
    	
    }
}
