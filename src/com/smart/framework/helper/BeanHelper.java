package com.smart.framework.helper;

import com.smart.framework.annotation.Bean;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

public class BeanHelper {
	private static final Logger logger = Logger.getLogger(BeanHelper.class);

	/**
	 * Bean Map��Bean �� => Bean ʵ����
	 */
	private static final Map<Class<?>, Object> beanMap = new HashMap<Class<?>, Object>();

	static {
		try {
			List<Class<?>> beanClassList = ClassHelper
					.getClassListByAnnotation(Bean.class);
			for (Class<?> cls : beanClassList) {
				Object beanInstance = cls.newInstance();
				beanMap.put(cls, beanInstance);
			}
		} catch (Exception e) {
			logger.error("��ʼ�� BeanHelper ����", e);
		}
	}

	public static Map<Class<?>, Object> getBeanMap() {
		return beanMap;
	}

	public static <T> T getBean(Class<T> cls) {
		if (!beanMap.containsKey(cls)) {
			throw new RuntimeException("�޷�����������ȡʵ����" + cls);
		}
		return (T) beanMap.get(cls);
	}
}