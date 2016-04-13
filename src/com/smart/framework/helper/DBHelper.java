package com.smart.framework.helper;

import com.smart.framework.util.CastUtil;
import com.smart.framework.util.DBUtil;
import com.smart.framework.util.StringUtil;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.List;
import java.util.Map;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.log4j.Logger;

public class DBHelper
{
  private static final Logger logger = Logger.getLogger(DBHelper.class);

  private static final BasicDataSource ds = new BasicDataSource();
  private static final QueryRunner runner = new QueryRunner(ds);

  private static final ThreadLocal<Connection> connContainer = new ThreadLocal();
  private static String dbType;

  static
  {
    initDataSource();

    initDBType();
  }

  private static void initDataSource()
  {
    String driver = ConfigHelper.getStringProperty("jdbc.driver");
    String url = ConfigHelper.getStringProperty("jdbc.url");
    String username = ConfigHelper.getStringProperty("jdbc.username");
    String password = ConfigHelper.getStringProperty("jdbc.password");
    int maxActive = ConfigHelper.getNumberProperty("jdbc.max.active");
    int maxIdle = ConfigHelper.getNumberProperty("jdbc.max.idle");

    if (StringUtil.isNotEmpty(driver)) {
      ds.setDriverClassName(driver);
    }
    if (StringUtil.isNotEmpty(url)) {
      ds.setUrl(url);
    }
    if (StringUtil.isNotEmpty(username)) {
      ds.setUsername(username);
    }
    if (StringUtil.isNotEmpty(password)) {
      ds.setPassword(password);
    }
    if (maxActive != 0) {
      ds.setMaxActive(maxActive);
    }
    if (maxIdle != 0)
      ds.setMaxIdle(maxIdle);
  }

  private static void initDBType()
  {
    try {
      dbType = ds.getConnection().getMetaData().getDatabaseProductName();
    } catch (Exception e) {
      logger.error("初始化 DBHelper 出错！", e);
    }
  }

  public static Connection getConnectionFromDataSource()
  {
	  Connection conn;
    try
    {
      conn = ds.getConnection();
    }
    catch (Exception e)
    {
      logger.error("从数据源中获取数据库连接出错！", e);
      throw new RuntimeException(e);
    }
    return conn;
  }

  public static Connection getConnectionFromThreadLocal()
  {
    return (Connection)connContainer.get();
  }

  public static void beginTransaction()
  {
    Connection conn = getConnectionFromThreadLocal();
    if (conn == null)
      try {
        conn = getConnectionFromDataSource();
        conn.setAutoCommit(false);
      } catch (Exception e) {
        logger.error("开启事务出错！", e);
        throw new RuntimeException(e);
      } finally {
        connContainer.set(conn);
      }
  }

  public static void commitTransaction()
  {
    Connection conn = getConnectionFromThreadLocal();
    if (conn != null)
      try {
        conn.commit();
        conn.close();
      } catch (Exception e) {
        logger.error("提交事务出错！", e);
        throw new RuntimeException(e);
      } finally {
        connContainer.remove();
      }
  }

  public static void rollbackTransaction()
  {
    Connection conn = getConnectionFromThreadLocal();
    if (conn != null)
      try {
        conn.rollback();
        conn.close();
      } catch (Exception e) {
        logger.error("回滚事务出错！", e);
        throw new RuntimeException(e);
      } finally {
        connContainer.remove();
      }
  }

  public static int getDefaultIsolationLevel()
  {
      int level;
    try
    {
      level = getConnectionFromThreadLocal().getMetaData().getDefaultTransactionIsolation();
    }
    catch (Exception e)
    {
      logger.error("获取数据库默认事务隔离级别出错！", e);
      throw new RuntimeException(e);
    }
    return level;
  }

  public static String getDBType()
  {
    return dbType;
  }

  public static <T> T queryBean(Class<T> cls, String sql, Object[] params)
  {
    Map map = (Map)EntityHelper.getEntityMap().get(cls);
    return (T) DBUtil.queryBean(runner, cls, map, sql, params);
  }

  public static <T> List<T> queryBeanList(Class<T> cls, String sql, Object[] params)
  {
    Map map = (Map)EntityHelper.getEntityMap().get(cls);
    return DBUtil.queryBeanList(runner, cls, map, sql, params);
  }

  public static int update(String sql, Object[] params)
  {
    Connection conn = getConnectionFromThreadLocal();
    return DBUtil.update(runner, conn, sql, params);
  }

  public static int queryCount(Class<?> cls, String sql, Object[] params)
  {
    return CastUtil.castInt(DBUtil.queryColumn(runner, "count(*)", sql, params));
  }

  public static List<Map<String, Object>> queryMapList(String sql, Object[] params)
  {
    return DBUtil.queryMapList(runner, sql, params);
  }

  public static Object queryColumn(String column, String sql, Object[] params)
  {
    return DBUtil.queryColumn(runner, column, sql, params);
  }
}