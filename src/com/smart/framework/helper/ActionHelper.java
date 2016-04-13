package com.smart.framework.helper;

import com.smart.framework.annotation.Request;
import com.smart.framework.base.BaseAction;
import com.smart.framework.bean.ActionBean;
import com.smart.framework.bean.RequestBean;
import com.smart.framework.util.ArrayUtil;
import com.smart.framework.util.StringUtil;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActionHelper
{
  private static final Map<RequestBean, ActionBean> actionMap = new HashMap();

  static
  {
    List<Class<?>> actionClassList = ClassHelper.getClassListBySuper(BaseAction.class);
    for (Class actionClass : actionClassList)
    {
      Method[] actionMethods = actionClass.getDeclaredMethods();
      if (ArrayUtil.isNotEmpty(actionMethods))
        for (Method actionMethod : actionMethods)
        {
          if (actionMethod.isAnnotationPresent(Request.class))
          {
            String[] urlArray = StringUtil.splitString(((Request)actionMethod.getAnnotation(Request.class)).value(), ":");
            if (ArrayUtil.isNotEmpty(urlArray))
            {
              String requestMethod = urlArray[0];
              String requestPath = urlArray[1];

              requestPath = StringUtil.replaceAll(requestPath, "\\{\\w+\\}", "(\\\\w+)");

              actionMap.put(new RequestBean(requestMethod, requestPath), new ActionBean(actionClass, actionMethod));
            }
          }
        }
    }
  }

  public static Map<RequestBean, ActionBean> getActionMap()
  {
    return actionMap;
  }
}