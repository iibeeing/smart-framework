package com.smart.framework.bean;

import com.smart.framework.base.BaseBean;
import java.util.HashMap;
import java.util.Map;

public class Page extends BaseBean
{
  private String path;
  private Map<String, Object> data;

  public Page(String path)
  {
    this.path = path;
    this.data = new HashMap();
  }

  public Page data(String key, Object value) {
    this.data.put(key, value);
    return this;
  }

  public boolean isRedirect() {
    return this.path.startsWith("/");
  }

  public String getPath() {
    return this.path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public Map<String, Object> getData() {
    return this.data;
  }

  public void setData(Map<String, Object> data) {
    this.data = data;
  }
}