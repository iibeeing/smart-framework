package com.smart.framework.helper;

import com.smart.framework.annotation.Aspect;
import com.smart.framework.annotation.Order;
import com.smart.framework.aspect.PluginAspect;
import com.smart.framework.aspect.TransactionAspect;
import com.smart.framework.base.BaseAspect;
import com.smart.framework.base.BaseService;
import com.smart.framework.proxy.Proxy;
import com.smart.framework.proxy.ProxyManager;
import com.smart.framework.util.ClassUtil;
import com.smart.framework.util.CollectionUtil;
import com.smart.framework.util.ObjectUtil;
import com.smart.framework.util.StringUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.log4j.Logger;

/**
 * @ClassName: AOPHelper
 * @Description: 初始化 AOP 框架
 * @author BEE
 * @date 2016-4-12 下午5:20:39
 */
public class AOPHelper {
	private static final Logger logger = Logger.getLogger(AOPHelper.class);

	static {
		try {
			Map<Class<?>, List<Class<?>>> aspectMap = createAspectMap();
			Map<Class<?>, List<Proxy>> targetMap = createTargetMap(aspectMap);
			for (Map.Entry<Class<?>, List<Proxy>> targetEntry : targetMap
					.entrySet()) {
				Class<?> targetClass = targetEntry.getKey();
				List<Proxy> baseAspectList = targetEntry.getValue();
				Object proxyInstance = ProxyManager.getInstance().createProxy(
						targetClass, baseAspectList);
				Object targetInstance = BeanHelper.getBean(targetClass);
				ObjectUtil.copyFields(targetInstance, proxyInstance);
				BeanHelper.getBeanMap().put(targetClass, proxyInstance);
			}
		} catch (Exception e) {
			logger.error("初始化 AOPHelper 出错！", e);
		}
	}

	private static Map<Class<?>, List<Class<?>>> createAspectMap()
			throws Exception {
		Map<Class<?>, List<Class<?>>> aspectMap = new LinkedHashMap<Class<?>, List<Class<?>>>();
		addPluginAspect(aspectMap);
		addUserAspect(aspectMap);
		addTransactionAspect(aspectMap);
		return aspectMap;
	}

	private static void addPluginAspect(Map<Class<?>, List<Class<?>>> aspectMap)
			throws Exception {
		List<Class<?>> pluginAspectClassList = ClassUtil.getClassListBySuper(
				"com.smart.plugin", PluginAspect.class);
		if (CollectionUtil.isNotEmpty(pluginAspectClassList)) {
			for (Class<?> pluginAspectClass : pluginAspectClassList) {
				PluginAspect pluginAspect = (PluginAspect) pluginAspectClass
						.newInstance();
				aspectMap.put(pluginAspectClass,
						pluginAspect.getTargetClassList());
			}
		}
	}

	private static void addUserAspect(Map<Class<?>, List<Class<?>>> aspectMap)
			throws Exception {
		List<Class<?>> aspectClassList = ClassHelper
				.getClassListBySuper(BaseAspect.class);
		sortAspectClassList(aspectClassList);
		for (Class<?> aspectClass : aspectClassList) {
			if (aspectClass.isAnnotationPresent(Aspect.class)) {
				Aspect aspect = (Aspect) aspectClass
						.getAnnotation(Aspect.class);
				List<Class<?>> targetClassList = createTargetClassList(aspect);
				aspectMap.put(aspectClass, targetClassList);
			}
		}
	}

	private static void addTransactionAspect(
			Map<Class<?>, List<Class<?>>> aspectMap) {
		List<Class<?>> serviceClassList = ClassHelper
				.getClassListBySuper(BaseService.class);
		aspectMap.put(TransactionAspect.class, serviceClassList);
	}

	private static void sortAspectClassList(List<Class<?>> aspectClassList) {
		Collections.sort(aspectClassList, new Comparator<Class<?>>() {
			public int compare(Class<?> aspect1, Class<?> aspect2) {
				if ((aspect1.isAnnotationPresent(Order.class))
						|| (aspect2.isAnnotationPresent(Order.class))) {
					if (aspect1.isAnnotationPresent(Order.class)) {
						return getOrderValue(aspect1) - getOrderValue(aspect2);
					}
					return getOrderValue(aspect2) - getOrderValue(aspect1);
				}
				return aspect1.hashCode() - aspect2.hashCode();
			}

			private int getOrderValue(Class<?> aspect) {
				return aspect.getAnnotation(Order.class) != null ? ((Order) aspect
						.getAnnotation(Order.class)).value() : 0;
			}
		});
	}

	private static List<Class<?>> createTargetClassList(Aspect aspect)throws Exception {
		List<Class<?>> targetClassList = new ArrayList<Class<?>>();
		String pkg = aspect.pkg();
		String cls = aspect.cls();
		if ((StringUtil.isNotEmpty(pkg)) && (StringUtil.isNotEmpty(cls))) {
			targetClassList.add(Class.forName(pkg + "." + cls));
		} else {
			targetClassList.addAll(ClassHelper.getClassListByPackage(pkg));
		}
		return targetClassList;
	}

	private static Map<Class<?>, List<Proxy>> createTargetMap(Map<Class<?>, List<Class<?>>> aspectMap) throws Exception {
		Map<Class<?>, List<Proxy>> targetMap = new HashMap<Class<?>, List<Proxy>>();
		for (Map.Entry<Class<?>, List<Class<?>>> proxyEntry : aspectMap.entrySet()) {
			Class<?> proxyClass = proxyEntry.getKey();
			List<Class<?>> targetClassList = proxyEntry.getValue();
			for (Class<?> targetClass : targetClassList) {
				Proxy baseAspect = (Proxy) proxyClass.newInstance();
				if (targetMap.containsKey(targetClass)) {
					targetMap.get(targetClass).add(baseAspect);
				} else {
					List<Proxy> baseAspectList = new ArrayList<Proxy>();
					baseAspectList.add(baseAspect);
					targetMap.put(targetClass, baseAspectList);
				}
			}
		}
		return targetMap;
	}
}