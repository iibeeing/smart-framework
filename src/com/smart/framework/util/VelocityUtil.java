package com.smart.framework.util;

import java.io.FileWriter;
import java.io.StringWriter;
import java.util.Map;
import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.log.NullLogChute;

public class VelocityUtil {
	private static final Logger logger = Logger.getLogger(VelocityUtil.class);

	private static final VelocityEngine engine = new VelocityEngine();

	static {
		engine.setProperty("file.resource.loader.path",
				ClassUtil.getClassPath());
		engine.setProperty("ISO-8859-1", "UTF-8");
		engine.setProperty("runtime.log.logsystem.class",
				NullLogChute.class.getName());
	}

	public static void setVmLoaderPath(String path) {
		engine.setProperty("file.resource.loader.path", path);
	}

	public static void mergeTemplateIntoFile(String vmPath,
			Map<String, Object> dataMap, String filePath) {
		try {
			FileUtil.createFile(filePath);

			Template template = engine.getTemplate(vmPath);
			VelocityContext context = new VelocityContext(dataMap);
			FileWriter writer = new FileWriter(filePath);
			template.merge(context, writer);

			writer.close();
		} catch (Exception e) {
			logger.error("合并模板出错！", e);
			throw new RuntimeException(e);
		}
	}

	public static String mergeTemplateReturnString(String vmPath,
			Map<String, Object> dataMap) {
		String result;
		try {
			Template template = engine.getTemplate(vmPath);
			VelocityContext context = new VelocityContext(dataMap);
			StringWriter writer = new StringWriter();
			template.merge(context, writer);
			result = writer.toString();

			writer.close();
		} catch (Exception e) {
			logger.error("合并模板出错！", e);
			throw new RuntimeException(e);
		}

		return result;
	}
}