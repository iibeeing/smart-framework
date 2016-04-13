package com.smart.framework;

import com.smart.framework.bean.ActionBean;
import com.smart.framework.bean.Page;
import com.smart.framework.bean.RequestBean;
import com.smart.framework.bean.Result;
import com.smart.framework.helper.ActionHelper;
import com.smart.framework.helper.BeanHelper;
import com.smart.framework.helper.ConfigHelper;
import com.smart.framework.util.CastUtil;
import com.smart.framework.util.MapUtil;
import com.smart.framework.util.StringUtil;
import com.smart.framework.util.WebUtil;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

@WebServlet({ "/*" })
public class DispatcherServlet extends HttpServlet {
	private static final Logger logger = Logger.getLogger(DispatcherServlet.class);

	private final String homePage = ConfigHelper.getStringProperty("app.home_page");
	private final String jspPath = ConfigHelper.getStringProperty("app.jsp_path");

	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String currentRequestMethod = request.getMethod();
		String currentRequestPath = WebUtil.getRequestPath(request);
		if (logger.isDebugEnabled()) {
			logger.debug(currentRequestMethod + ":" + currentRequestPath);
		}

		if (currentRequestPath.equals("/")) {
			WebUtil.redirectRequest(this.homePage, request, response);
			return;
		}

		if (currentRequestPath.endsWith("/")) {
			currentRequestPath = currentRequestPath.substring(0, currentRequestPath.length() - 1);
		}

		boolean jspMapped = false;
		DataContext.init(request, response);
		Map<String, String> requestParamMap = WebUtil.getRequestParamMap(request);
		try {
			Map<RequestBean, ActionBean> actionMap = ActionHelper.getActionMap();
			for (Entry<RequestBean, ActionBean> actionEntry : actionMap.entrySet()) {
				RequestBean requestBean = (RequestBean) actionEntry.getKey();
				String requestMethod = requestBean.getRequestMethod();
				String requestPath = requestBean.getRequestPath();

				Matcher matcher = Pattern.compile(requestPath).matcher(currentRequestPath);

				if ((requestMethod.equalsIgnoreCase(currentRequestMethod)) && (matcher.matches())) {
					ActionBean actionBean = (ActionBean) actionEntry.getValue();
					Class<?>[] requestParamTypes = actionBean.getActionMethod().getParameterTypes();
					List<Object> paramList = createParamList(matcher, requestParamMap,requestParamTypes);
					handleActionMethod(request, response, actionBean, paramList);
					jspMapped = true;
					break;
				}
			}
		} finally {
			DataContext.destroy();
		}

		if ((!jspMapped) && (StringUtil.isNotEmpty(this.jspPath))) {
			String path = this.jspPath + currentRequestPath.substring(1).replace("/", "_") + ".jsp";
			request.setAttribute("path", path);
			WebUtil.forwardRequest(path, request, response);
		}
	}

	private List<Object> createParamList(Matcher matcher,Map<String, String> requestParamMap, Class<?>[] requestParamTypes) {
		List<Object> paramList = new ArrayList<Object>();
		
		for (int i = 1; i <= matcher.groupCount(); i++) {
			String param = matcher.group(i);
			Class<?> paramType = requestParamTypes[(i - 1)];
			if ((paramType.equals(Integer.TYPE)) || (paramType.equals(Integer.class)))
				paramList.add(Integer.valueOf(CastUtil.castInt(param)));
			else if ((paramType.equals(Long.TYPE)) || (paramType.equals(Long.class)))
				paramList.add(Long.valueOf(CastUtil.castLong(param)));
			else if ((paramType.equals(Double.TYPE)) || (paramType.equals(Double.class)))
				paramList.add(Double.valueOf(CastUtil.castDouble(param)));
			else if (paramType.equals(String.class)) {
				paramList.add(param);
			}
		}

		if (MapUtil.isNotEmpty(requestParamMap)) {
			paramList.add(requestParamMap);
		}
		return paramList;
	}

	private void handleActionMethod(HttpServletRequest request,HttpServletResponse response, ActionBean actionBean,List<Object> paramList) {
		Class<?> actionClass = actionBean.getActionClass();
		Method actionMethod = actionBean.getActionMethod();
		Object actionMethodResult;
		Object actionInstance = BeanHelper.getBean(actionClass);
		try {
			actionMethod.setAccessible(true);
			actionMethodResult = actionMethod.invoke(actionInstance,paramList.toArray());
		} catch (Exception e) {
			handleActionMethodException(request, response, e);
			return;
		}
		handleActionMethodReturn(request, response, actionMethodResult);
	}

	private void handleActionMethodException(HttpServletRequest request,HttpServletResponse response, Exception e) {
		if ((e.getCause() instanceof AuthException)) {
			if (WebUtil.isAJAX(request)) {
				WebUtil.sendError(403, response);
			} else
				WebUtil.redirectRequest("/", request, response);
		} else {
			logger.error("调用 Action 方法出错！", e);
			throw new RuntimeException(e);
		}
	}

	private void handleActionMethodReturn(HttpServletRequest request,HttpServletResponse response, Object actionMethodResult) {
		if (actionMethodResult != null)
			if ((actionMethodResult instanceof Result)) {
				Result result = (Result) actionMethodResult;
				WebUtil.writeJSON(response, result);
			} else if ((actionMethodResult instanceof Page)) {
				Page page = (Page) actionMethodResult;
				if (page.isRedirect()) {
					String path = page.getPath();
					WebUtil.redirectRequest(path, request, response);
				} else {
					String path = this.jspPath + page.getPath();
					Map<String, Object> data = page.getData();
					if (MapUtil.isNotEmpty(data)) {
						for (Map.Entry<String, Object> entry : data.entrySet()) {
							request.setAttribute((String) entry.getKey(),entry.getValue());
						}
					}
					WebUtil.forwardRequest(path, request, response);
				}
			}
	}
}