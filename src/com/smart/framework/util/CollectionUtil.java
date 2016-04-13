package com.smart.framework.util;

import java.util.Collection;
import org.apache.commons.collections.CollectionUtils;

public class CollectionUtil
{
  public static boolean isNotEmpty(Collection<?> collection)
  {
    return CollectionUtils.isNotEmpty(collection);
  }

  public static boolean isEmpty(Collection<?> collection)
  {
    return CollectionUtils.isEmpty(collection);
  }
}