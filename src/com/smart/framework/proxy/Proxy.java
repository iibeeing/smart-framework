package com.smart.framework.proxy;

public abstract interface Proxy
{
  public abstract Object doProxy(ProxyChain paramProxyChain)
    throws Exception;
}