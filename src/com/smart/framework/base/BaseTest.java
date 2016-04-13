package com.smart.framework.base;

import com.smart.framework.OrderedRunner;
import com.smart.framework.Smart;
import com.smart.framework.helper.DBHelper;
import com.smart.framework.util.ClassUtil;
import java.io.File;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.junit.runner.RunWith;

@RunWith(OrderedRunner.class)
public abstract class BaseTest
{
  private static final Logger logger = Logger.getLogger(BaseTest.class);

  static {
    Smart.init();
  }

  protected static void initSQL(String sqlPath) {
    try {
      File sqlFile = new File(ClassUtil.getClassPath() + sqlPath);
      List<String> sqlList = FileUtils.readLines(sqlFile);
      for (String sql : sqlList)
        DBHelper.update(sql, new Object[0]);
    }
    catch (Exception e) {
      logger.error("ִ�����ݳ�ʼ���ű�����", e);
      throw new RuntimeException(e);
    }
  }
}