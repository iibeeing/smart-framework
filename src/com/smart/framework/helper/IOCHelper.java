package com.smart.framework.helper;

import com.smart.framework.annotation.Impl;
import com.smart.framework.annotation.Inject;
import com.smart.framework.util.ArrayUtil;
import com.smart.framework.util.CollectionUtil;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

public class IOCHelper {
	private static final Logger logger = Logger.getLogger(IOCHelper.class);

	static {
		try {
			Map<Class<?>, Object> beanMap = BeanHelper.getBeanMap();
			for (Map.Entry<Class<?>, Object> beanEntry : beanMap.entrySet()) {
				Class<?> beanClass = beanEntry.getKey();
				Object beanInstance = beanEntry.getValue();

				Field[] beanFields = beanClass.getDeclaredFields();
				if (ArrayUtil.isNotEmpty(beanFields)) {
					for (Field beanField : beanFields) {
						if (beanField.isAnnotationPresent(Inject.class)) {
							Class<?> implementClass = getImplementClass(beanField);
							if (implementClass != null) {
								Object implementInstance = beanMap
										.get(implementClass);

								if (implementInstance != null) {
									beanField.setAccessible(true);
									beanField.set(beanInstance,
											implementInstance);
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("³õÊ¼»¯ IOCHelper ³ö´í£¡", e);
		}
	}

	private static Class<?> getImplementClass(Field beanField) {
		Class<?> implementClass = null;
		Class<?> interfaceClass = beanField.getType();
		if (interfaceClass.isAnnotationPresent(Impl.class)) {
			implementClass = ((Impl) interfaceClass.getAnnotation(Impl.class)).value();
		} else {
			List<Class<?>> implementClassList = ClassHelper.getClassListBySuper(interfaceClass);
			if (CollectionUtil.isNotEmpty(implementClassList)) {
				implementClass = implementClassList.get(0);
			}
		}
		return implementClass;
	}
}