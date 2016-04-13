package com.smart.framework.aspect;

import com.smart.framework.annotation.Transaction;
import com.smart.framework.base.BaseAspect;
import com.smart.framework.helper.DBHelper;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import org.apache.log4j.Logger;

public class TransactionAspect extends BaseAspect
{
  private static final Logger logger = Logger.getLogger(TransactionAspect.class);

  public boolean intercept(Class<?> cls, Method method, Object[] params)
  {
    return method.isAnnotationPresent(Transaction.class);
  }

  public void before(Class<?> cls, Method method, Object[] params)
    throws Exception
  {
    DBHelper.beginTransaction();
    if (logger.isDebugEnabled()) {
      logger.debug("[Begin Transaction]");
    }

    setTransactionIsolation(method);
  }

  public void after(Class<?> cls, Method method, Object[] params, Object result)
    throws Exception
  {
    DBHelper.commitTransaction();
    if (logger.isDebugEnabled())
      logger.debug("[Commit Transaction]");
  }

  public void error(Class<?> cls, Method method, Object[] params, Exception e)
  {
    DBHelper.rollbackTransaction();
    if (logger.isDebugEnabled())
      logger.debug("[Rollback Transaction]");
  }

  private void setTransactionIsolation(Method method)
    throws SQLException
  {
    Transaction transaction = (Transaction)method.getAnnotation(Transaction.class);
    int currentIsolation = transaction.isolation();
    int defaultIsolation = DBHelper.getDefaultIsolationLevel();
    if (currentIsolation != defaultIsolation) {
      Connection conn = DBHelper.getConnectionFromThreadLocal();
      conn.setTransactionIsolation(currentIsolation);
      if (logger.isDebugEnabled())
        logger.debug("[Set Transaction Isolation] Isolation: " + currentIsolation);
    }
  }
}