package com.smart.framework.util;

import org.apache.commons.lang.ArrayUtils;

public class ArrayUtil
{
  public static boolean isNotEmpty(Object[] array)
  {
    return ArrayUtils.isNotEmpty(array);
  }

  public static boolean isEmpty(Object[] array)
  {
    return ArrayUtils.isEmpty(array);
  }
}