package com.smart.framework;

import com.smart.framework.helper.ConfigHelper;
import com.smart.framework.util.StringUtil;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebListener;

@WebListener
public class ContainerListener
  implements ServletContextListener
{
  private final String wwwPath = ConfigHelper.getStringProperty("app.www_path");
  private final String jspPath = ConfigHelper.getStringProperty("app.jsp_path");

  public void contextInitialized(ServletContextEvent sce)
  {
    Smart.init();

    addServletMapping(sce.getServletContext());
  }

  public void contextDestroyed(ServletContextEvent sce)
  {
  }

  private void addServletMapping(ServletContext context)
  {
    registerDefaultServlet(context);

    registerJspServlet(context);

    registerUploadServlet(context);
  }

  private void registerDefaultServlet(ServletContext context) {
    ServletRegistration defaultServletRegistration = context.getServletRegistration("default");
    defaultServletRegistration.addMapping(new String[] { "/favicon.ico" });
    if (StringUtil.isNotEmpty(this.wwwPath))
      defaultServletRegistration.addMapping(new String[] { this.wwwPath + "*" });
  }

  private void registerJspServlet(ServletContext context)
  {
    if (StringUtil.isNotEmpty(this.jspPath)) {
      ServletRegistration jspServletRegistration = context.getServletRegistration("jsp");
      jspServletRegistration.addMapping(new String[] { this.jspPath + "*" });
    }
  }

  private void registerUploadServlet(ServletContext context) {
    ServletRegistration uploadServletRegistration = context.getServletRegistration("upload");
    uploadServletRegistration.addMapping(new String[] { "/upload.do" });
  }
}