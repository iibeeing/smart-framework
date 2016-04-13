package com.smart.framework.util;

import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import org.apache.log4j.Logger;

public class WebUtil {
	private static final Logger logger = Logger.getLogger(WebUtil.class);

	public static void writeText(HttpServletResponse response, Object data) {
		try {
			response.setContentType("text/plain");
			response.setCharacterEncoding("UTF-8");
			PrintWriter writer = response.getWriter();
			writer.write(data.toString());
		} catch (Exception e) {
			logger.error("在响应中写数据出错！", e);
			throw new RuntimeException(e);
		}
	}

	public static void writeJSON(HttpServletResponse response, Object data) {
		try {
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");

			PrintWriter writer = response.getWriter();
			writer.write(JSONUtil.toJSON(data));
		} catch (Exception e) {
			logger.error("在响应中写数据出错！", e);
			throw new RuntimeException(e);
		}
	}

	public static void writeHTML(HttpServletResponse response, Object data) {
		try {
			response.setContentType("text/html");
			response.setCharacterEncoding("UTF-8");

			PrintWriter writer = response.getWriter();
			writer.write(JSONUtil.toJSON(data));
		} catch (Exception e) {
			logger.error("在响应中写数据出错！", e);
			throw new RuntimeException(e);
		}
	}

	public static String getUploadFilePath(HttpServletRequest request,
			String relativePath) {
		String filePath = request.getServletContext().getRealPath("/")
				+ relativePath;

		FileUtil.createFile(filePath);

		return filePath;
	}

	public static String getUploadFileName(HttpServletRequest request, Part part) {
		String cd = part.getHeader("Content-Disposition");
		String fileName = cd
				.substring(cd.lastIndexOf("=") + 2, cd.length() - 1);

		if (fileName.contains("\\")) {
			fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
		}

		return fileName;
	}

	public static Map<String, String> getRequestParamMap(
			HttpServletRequest request) {
		Map paramMap = new HashMap();
		try {
			String method = request.getMethod();
			if ((method.equalsIgnoreCase("put"))
					|| (method.equalsIgnoreCase("delete"))) {
				String queryString = CodecUtil.decodeForUTF8(StreamUtil
						.toString(request.getInputStream()));
				if (StringUtil.isNotEmpty(queryString)) {
					String[] qsArray = StringUtil.splitString(queryString, "&");
					if (ArrayUtil.isNotEmpty(qsArray))
						for (String qs : qsArray) {
							String[] array = StringUtil.splitString(qs, "=");
							if ((ArrayUtil.isNotEmpty(array))
									&& (array.length == 2)) {
								String paramName = array[0];
								String paramValue = array[1];
								if (checkParamName(paramName))
									paramMap.put(paramName, paramValue);
							}
						}
				}
			} else {
				Enumeration paramNames = request.getParameterNames();
				while (paramNames.hasMoreElements()) {
					String paramName = (String) paramNames.nextElement();
					if (checkParamName(paramName)) {
						String[] paramValues = request
								.getParameterValues(paramName);
						if (ArrayUtil.isNotEmpty(paramValues))
							if (paramValues.length == 1) {
								paramMap.put(paramName, paramValues[0]);
							} else {
								StringBuilder paramValue = new StringBuilder("");
								for (int i = 0; i < paramValues.length; i++) {
									paramValue.append(paramValues[i]);
									if (i != paramValues.length - 1) {
										paramValue.append(StringUtil.SEPARATOR);
									}
								}
								paramMap.put(paramName, paramValue.toString());
							}
					}
				}
			}
		} catch (Exception e) {
			logger.error("获取请求参数出错！", e);
			throw new RuntimeException(e);
		}
		return paramMap;
	}

	private static boolean checkParamName(String paramName) {
		return !paramName.equals("_");
	}

	public static Map<String, String> createQueryMap(String queryString) {
		Map queryMap = new HashMap();
		if (StringUtil.isNotEmpty(queryString)) {
			String[] queryArray = queryString.split(";");
			if (ArrayUtil.isNotEmpty(queryArray)) {
				for (String query : queryArray) {
					String[] fieldArray = query.split(":");
					if ((ArrayUtil.isNotEmpty(fieldArray))
							&& (fieldArray.length == 2)) {
						queryMap.put(fieldArray[0], fieldArray[1]);
					}
				}
			}
		}
		return queryMap;
	}

	public static void forwardRequest(String path, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			request.getRequestDispatcher(path).forward(request, response);
		} catch (Exception e) {
			logger.error("转发请求出错！", e);
			throw new RuntimeException(e);
		}
	}

	public static void redirectRequest(String path, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			response.sendRedirect(request.getContextPath() + path);
		} catch (Exception e) {
			logger.error("重定向请求出错！", e);
			throw new RuntimeException(e);
		}
	}

	public static void sendError(int code, HttpServletResponse response) {
		try {
			response.sendError(code);
		} catch (Exception e) {
			logger.error("发送错误代码出错！", e);
			throw new RuntimeException(e);
		}
	}

	public static boolean isAJAX(HttpServletRequest request) {
		return request.getHeader("X-Requested-With") != null;
	}

	public static String getRequestPath(HttpServletRequest request) {
		String servletPath = request.getServletPath();
		String pathInfo = StringUtil.defaultIfEmpty(request.getPathInfo(), "");
		return servletPath + pathInfo;
	}
}