package com.smart.framework.util;

import java.util.Map;
import org.apache.commons.collections.MapUtils;

public class MapUtil
{
  public static boolean isNotEmpty(Map<?, ?> map)
  {
    return MapUtils.isNotEmpty(map);
  }

  public static boolean isEmpty(Map<?, ?> map)
  {
    return MapUtils.isEmpty(map);
  }
}