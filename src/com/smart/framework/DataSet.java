package com.smart.framework;

import com.smart.framework.helper.DBHelper;
import com.smart.framework.helper.SQLHelper;
import com.smart.framework.util.CastUtil;
import com.smart.framework.util.ObjectUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataSet
{
  public static <T> T select(Class<T> cls, String condition, Object[] params)
  {
    String sql = SQLHelper.generateSelectSQL(cls, condition, "", params);
    return DBHelper.queryBean(cls, sql, new Object[0]);
  }

  public static <T> List<T> selectList(Class<T> cls, String condition, String sort, Object[] params)
  {
    String sql = SQLHelper.generateSelectSQL(cls, condition, sort, params);
    return DBHelper.queryBeanList(cls, sql, new Object[0]);
  }

  public static boolean insert(Class<?> cls, Map<String, Object> fieldMap)
  {
    String sql = SQLHelper.generateInsertSQL(cls, fieldMap);
    int rows = DBHelper.update(sql, new Object[0]);
    return rows > 0;
  }

  public static boolean update(Class<?> cls, Map<String, Object> fieldMap, String condition, Object[] params)
  {
    String sql = SQLHelper.generateUpdateSQL(cls, fieldMap, condition, params);
    int rows = DBHelper.update(sql, new Object[0]);
    return rows > 0;
  }

  public static boolean delete(Class<?> cls, String condition, Object[] params)
  {
    String sql = SQLHelper.generateDeleteSQL(cls, condition, params);
    int rows = DBHelper.update(sql, new Object[0]);
    return rows > 0;
  }

  public static int selectCount(Class<?> cls, String condition, Object[] params)
  {
    String sql = SQLHelper.generateSelectSQLForCount(cls, condition, params);
    return DBHelper.queryCount(cls, sql, new Object[0]);
  }

  public static <T> List<T> selectListForPager(int pageNumber, int pageSize, Class<T> cls, String condition, String sort, Object[] params)
  {
    String sql = SQLHelper.generateSelectSQLForPager(pageNumber, pageSize, cls, condition, sort, params);
    return DBHelper.queryBeanList(cls, sql, new Object[0]);
  }

  public static <T> Map<Long, T> selectMap(Class<T> cls, String condition, Object[] params)
  {
    Map map = new HashMap();
    List list = selectList(cls, condition, "", params);
    for (Object obj : list) {
      Long id = Long.valueOf(CastUtil.castLong(ObjectUtil.getFieldValue(obj, "id")));
      map.put(id, obj);
    }
    return map;
  }
}