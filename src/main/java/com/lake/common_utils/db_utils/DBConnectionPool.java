package com.lake.common_utils.db_utils;

import java.sql.*;
import java.util.*;

/**
 * 数据库连接池。为简单应用，只定义最大连接和初始连接数，省去最大最小空闲
 * 和最长等待时间等。
 * 
 * @author LakeHm
 *
 * 2016年10月23日下午7:16:35
 */
public class DBConnectionPool {
	//最大连接数
	private int maxSize;
	//初始连接数
	private int initialSize;
	
	//数据库驱动  
	private String jdbcDriver;
	//数据库url  
    private String dbUrl;
    //数据库用户名  
    private String dbUsername;
    //数据库密码 
    private String dbPassword;
    //数据库连接存储
    private List<DBConnection> connections;
    
	DBConnectionPool(int maxSize, int initialSize, String jdbcDriver,
			String dbUrl, String dbUsername, String dbPassword) {
		this.maxSize = maxSize;
		this.initialSize = initialSize;
		this.jdbcDriver = jdbcDriver;
		this.dbUrl = dbUrl;
		this.dbUsername = dbUsername;
		this.dbPassword = dbPassword;
		connections = new ArrayList<DBConnection>(initialSize);
		init();
	}

	private void init() {
		try {
			Class.forName(jdbcDriver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < initialSize; i++) {
			connections.add(new DBConnection(createOneConnection()));
		}
	}
	
	private Connection createOneConnection() {
		Connection connection = null;
        try {
			connection = DriverManager.getConnection(dbUrl,   
			        dbUsername, dbPassword);
		} catch (SQLException e) {
			return createOneConnection();
		}
        return connection;
	}
	
	public Connection getConnection() {
		DBConnection dbConnection = getFreeConnection();
		synchronized (dbConnection) {
			if(!dbConnection.isBusy()) {
				dbConnection.setBusy(true);
				return dbConnection.getConnection();
			} else {
				return getConnection();
			}
		}
	}
	
	private DBConnection getFreeConnection() {
		synchronized (connections) {
			DBConnection dbConnection = findFreeConnection();
			
			//retry
			if(null == dbConnection) {
				dbConnection = findFreeConnection();
				if(null != dbConnection) {
					return dbConnection;
				}
			} else {
				return dbConnection;
			}
			
			/** 可能没有空闲连接，那就新建 */
			//判断是否超出最大连接数
			if(connections.size() < maxSize) {
				dbConnection = new DBConnection(createOneConnection());
				connections.add(dbConnection);
				return dbConnection;
			} else {
				return getFreeConnection();
			}
		}
	}

	private DBConnection findFreeConnection() {
		for (DBConnection dbConnection : connections) {
			if(!dbConnection.isBusy()) {
				return dbConnection;
			}
		}
		return null;
	}
	
	public void freeConnection(Connection connection) {
		synchronized (connections) {
			for (DBConnection dbConnection : connections) {
				if(dbConnection.getConnection() == connection) {
					dbConnection.setBusy(false);
					return ;
				}
			}
		}
	}
	
	private static class DBConnection {
		private Connection connection;
		private volatile boolean busy;
		
		public DBConnection(Connection connection) {
			this.connection = connection;
			busy = false;
		}

		public Connection getConnection() {
			return connection;
		}

		public void setConnection(Connection connection) {
			this.connection = connection;
		}

		public boolean isBusy() {
			return busy;
		}

		public void setBusy(boolean busy) {
			this.busy = busy;
		}
	}
}
