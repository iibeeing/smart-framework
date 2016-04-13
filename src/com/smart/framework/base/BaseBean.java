package com.smart.framework.base;

import com.smart.framework.util.JSONUtil;
import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public abstract class BaseBean
  implements Serializable
{
  public int hashCode()
  {
    return HashCodeBuilder.reflectionHashCode(this);
  }

  public boolean equals(Object obj)
  {
    return EqualsBuilder.reflectionEquals(this, obj);
  }

  public String toString()
  {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
  }

  public String toJson() {
    return JSONUtil.toJSON(this);
  }
}