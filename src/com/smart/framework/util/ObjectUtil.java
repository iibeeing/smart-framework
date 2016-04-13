package com.smart.framework.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;

public class ObjectUtil {
	private static final Logger logger = Logger.getLogger(ObjectUtil.class);

	public static void setField(Object obj, String fieldName, Object fieldValue) {
		try {
			if (PropertyUtils.isWriteable(obj, fieldName))
				PropertyUtils.setProperty(obj, fieldName, fieldValue);
		} catch (Exception e) {
			logger.error("���ó�Ա��������", e);
			throw new RuntimeException(e);
		}
	}

	public static Object getFieldValue(Object obj, String fieldName) {
		Object propertyValue = null;
		try {
			if (PropertyUtils.isReadable(obj, fieldName))
				propertyValue = PropertyUtils.getProperty(obj, fieldName);
		} catch (Exception e) {
			logger.error("��ȡ��Ա��������", e);
			throw new RuntimeException(e);
		}
		return propertyValue;
	}

	public static void copyFields(Object source, Object target) {
		try {
			for (Field field : source.getClass().getDeclaredFields()) {
				if (!Modifier.isStatic(field.getModifiers())) {
					field.setAccessible(true);
					field.set(target, field.get(source));
				}
			}
		} catch (Exception e) {
			logger.error("���Ƴ�Ա��������", e);
			throw new RuntimeException(e);
		}
	}

	public static <T> T newInstance(String className) {
		Object instance;
		try {
			Class commandClass = Class.forName(className);
			instance = commandClass.newInstance();
		} catch (Exception e) {
			logger.error("����ʵ������", e);
			throw new RuntimeException(e);
		}
		return (T) instance;
	}
}