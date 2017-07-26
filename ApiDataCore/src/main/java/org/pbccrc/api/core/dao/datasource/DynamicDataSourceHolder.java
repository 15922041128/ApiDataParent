//package org.pbccrc.api.core.dao.datasource;
//
//public class DynamicDataSourceHolder {
//
//	private static final ThreadLocal<String> THREAD_DATA_SOURCE = new ThreadLocal<String>();
//	
//	private static final String DS_MYSQL = "mysqlDataSource";
//	private static final String DS_ORACLE = "oracleDataSource";
//
//	public static String getDataSource() {
//		return THREAD_DATA_SOURCE.get();
//	}
//
//	private static void setDataSource(String dataSource) {
//		THREAD_DATA_SOURCE.set(dataSource);
//	}
//
//	public static void clearDataSource() {
//		THREAD_DATA_SOURCE.remove();
//	}
//	
//	public static void change2mysql() {
//		setDataSource(DS_MYSQL);
//	}
//	
//	public static void change2oracle() {
//		setDataSource(DS_ORACLE);
//	}
//	
//	
//}
