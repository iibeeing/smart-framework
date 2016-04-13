package com.smart.framework.helper;

import com.smart.framework.Plugin;
import com.smart.framework.util.ClassUtil;
import java.util.List;
import org.apache.log4j.Logger;

public class PluginHelper {
	private static final Logger logger = Logger.getLogger(PluginHelper.class);

	static {
		try {
			List<Class<?>> pluginClassList = ClassUtil.getClassListBySuper("com.smart.plugin", Plugin.class);
			for (Class<?> pluginClass : pluginClassList) {
				Plugin plugin = (Plugin) pluginClass.newInstance();
				plugin.init();
			}
		} catch (Exception e) {
			logger.error("³õÊ¼»¯ BeanHelper ³ö´í£¡", e);
		}
	}
}