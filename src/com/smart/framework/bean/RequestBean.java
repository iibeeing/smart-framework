package com.smart.framework.bean;

public class RequestBean
{
  private String requestMethod;
  private String requestPath;

  public RequestBean(String requestMethod, String requestPath)
  {
    this.requestMethod = requestMethod;
    this.requestPath = requestPath;
  }

  public String getRequestMethod() {
    return this.requestMethod;
  }

  public void setRequestMethod(String requestMethod) {
    this.requestMethod = requestMethod;
  }

  public String getRequestPath() {
    return this.requestPath;
  }

  public void setRequestPath(String requestPath) {
    this.requestPath = requestPath;
  }
}