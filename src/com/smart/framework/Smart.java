package com.smart.framework;

import com.smart.framework.helper.AOPHelper;
import com.smart.framework.helper.ActionHelper;
import com.smart.framework.helper.BeanHelper;
import com.smart.framework.helper.DBHelper;
import com.smart.framework.helper.EntityHelper;
import com.smart.framework.helper.IOCHelper;
import com.smart.framework.helper.PluginHelper;
import org.apache.log4j.Logger;

public final class Smart
{
  private static final Logger logger = Logger.getLogger(Smart.class);

  public static void init() {
    try {
      Class[] classList = { 
        DBHelper.class, 
        EntityHelper.class, 
        ActionHelper.class, 
        BeanHelper.class, 
        AOPHelper.class, 
        IOCHelper.class, 
        PluginHelper.class };

      Class[] arrayOfClass1 = classList; int j = classList.length; for (int i = 0; i < j; i++) { Class cls = arrayOfClass1[i];
        Class.forName(cls.getName()); }
    }
    catch (Exception e) {
      logger.error("���� Helper ����", e);
    }
  }
}