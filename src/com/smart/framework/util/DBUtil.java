package com.smart.framework.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayHandler;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.KeyedHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.log4j.Logger;

public class DBUtil {
	private static final Logger logger = Logger.getLogger(DBUtil.class);

	public static Connection openConnection(String type, String host,String port, String name, String username, String password) {
		String url;
		Connection conn;
		String driver;
		try {
			if (type.equalsIgnoreCase("MySQL")) {
				driver = "com.mysql.jdbc.Driver";
				url = "jdbc:mysql://" + host + ":" + port + "/" + name;
			} else {
				if (type.equalsIgnoreCase("Oracle")) {
					driver = "oracle.jdbc.driver.OracleDriver";
					url = "jdbc:oracle:thin:@" + host + ":" + port + ":" + name;
				} else {
					if (type.equalsIgnoreCase("SQLServer")) {
						driver = "com.microsoft.jdbc.sqlserver.SQLServerDriver";
						url = "jdbc:sqlserver://" + host + ":" + port
								+ ";databaseName=" + name;
					} else {
						throw new RuntimeException();
					}
				}
			}
			Class.forName(driver);
			conn = DriverManager.getConnection(url, username, password);
		} catch (Exception e) {
			logger.error("打开数据库连接出错！", e);
			throw new RuntimeException(e);
		}
		
		return conn;
	}

	public static void closeConnection(Connection conn) {
		try {
			if (conn != null)
				conn.close();
		} catch (Exception e) {
			logger.error("关闭数据库连接出错！", e);
			throw new RuntimeException(e);
		}
	}

	public static Object[] queryArray(QueryRunner runner, String sql,
			Object[] params) {
		Object[] result;
		try {
			result = (Object[]) runner.query(sql, new ArrayHandler(), params);
		} catch (SQLException e) {
			logger.error("查询出错！", e);
			throw new RuntimeException(e);
		}
		printSQL(sql);
		return result;
	}

	public static List<Object[]> queryArrayList(QueryRunner runner, String sql,
			Object[] params) {
		List result;
		try {
			result = (List) runner.query(sql, new ArrayListHandler(), params);
		} catch (SQLException e) {
			logger.error("查询出错！", e);
			throw new RuntimeException(e);
		}
		printSQL(sql);
		return result;
	}

	public static Map<String, Object> queryMap(QueryRunner runner, String sql,
			Object[] params) {
		Map result;
		try {
			result = (Map) runner.query(sql, new MapHandler(), params);
		} catch (SQLException e) {
			logger.error("查询出错！", e);
			throw new RuntimeException(e);
		}
		printSQL(sql);
		return result;
	}

	public static List<Map<String, Object>> queryMapList(QueryRunner runner,
			String sql, Object[] params) {
		List result;
		try {
			result = (List) runner.query(sql, new MapListHandler(), params);
		} catch (SQLException e) {
			logger.error("查询出错！", e);
			throw new RuntimeException(e);
		}
		printSQL(sql);
		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> T queryBean(QueryRunner runner, Class<T> cls,Map<String, String> map, String sql, Object[] params) {
		Object result;
		try {
			if (MapUtil.isNotEmpty(map))
				result = runner.query(sql, new BeanHandler(cls,new BasicRowProcessor(new BeanProcessor(map))), params);
			else {
				result = runner.query(sql, new BeanHandler(cls), params);
			}
			printSQL(sql);
		} catch (SQLException e) {
			logger.error("查询出错！", e);
			throw new RuntimeException(e);
		}
		return (T) result;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> List<T> queryBeanList(QueryRunner runner, Class<T> cls,
			Map<String, String> map, String sql, Object[] params) {
		List result;
		try {
			if (MapUtil.isNotEmpty(map))
				result = (List) runner.query(sql, new BeanListHandler(cls,new BasicRowProcessor(new BeanProcessor(map))), params);
			else {
				result = (List) runner.query(sql, new BeanListHandler(cls),params);
			}
			printSQL(sql);
		} catch (SQLException e) {
			logger.error("查询出错！", e);
			throw new RuntimeException(e);
		}
		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Object queryColumn(QueryRunner runner, String column,String sql, Object[] params) {
		Object result;
		try {
			result = runner.query(sql, new ScalarHandler(column), params);
		} catch (SQLException e) {
			logger.error("查询出错！", e);
			throw new RuntimeException(e);
		}

		printSQL(sql);
		return result;
	}

	public static <T> List<T> queryColumnList(QueryRunner runner,
			String column, String sql, Object[] params) {
		List result;
		try {
			result = (List) runner.query(sql, new ColumnListHandler(column),params);
		} catch (SQLException e) {
			logger.error("查询出错！", e);
			throw new RuntimeException(e);
		}
		printSQL(sql);
		return result;
	}

	public static <T> Map<T, Map<String, Object>> queryKeyMap(QueryRunner runner, String column, String sql, Object[] params) {
		Map result;
		try {
			result = (Map) runner.query(sql, new KeyedHandler(column), params);
		} catch (SQLException e) {
			logger.error("查询出错！", e);
			throw new RuntimeException(e);
		}
		printSQL(sql);
		return result;
	}

	public static int update(QueryRunner runner, Connection conn, String sql,Object[] params) {
		int result;
		try {
			if (conn != null)
				result = runner.update(conn, sql, params);
			else {
				result = runner.update(sql, params);
			}
			printSQL(sql);
		} catch (SQLException e) {
			logger.error("更新出错！", e);
			throw new RuntimeException(e);
		}
		return result;
	}

	private static void printSQL(String sql) {
		if (logger.isDebugEnabled())
			logger.debug("SQL - " + sql);
	}
}