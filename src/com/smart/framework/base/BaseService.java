package com.smart.framework.base;

public abstract class BaseService
{
  protected static final int ISOLATION_READ_UNCOMMITTED = 1;
  protected static final int ISOLATION_READ_COMMITTED = 2;
  protected static final int ISOLATION_REPEATABLE_READ = 4;
  protected static final int ISOLATION_SERIALIZABLE = 8;
}