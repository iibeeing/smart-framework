package com.smart.framework.helper;

import com.smart.framework.annotation.Column;
import com.smart.framework.base.BaseEntity;
import com.smart.framework.util.ArrayUtil;
import com.smart.framework.util.MapUtil;
import com.smart.framework.util.StringUtil;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityHelper {
	/**
	 * 实体类 => 表名
	 */
	private static final Map<Class<?>, Map<String, String>> entityMap = new HashMap();

	static {
		// 获取并遍历所有实体类
		List<Class<?>> entityClassList = ClassHelper.getClassListBySuper(BaseEntity.class);
		for (Class entityClass : entityClassList) {
			Field[] fields = entityClass.getDeclaredFields();
			if (ArrayUtil.isNotEmpty(fields)) {
				Map fieldMap = new HashMap();
				for (Field field : fields) {
					String fieldName = field.getName();
					String columnName;
					if (field.isAnnotationPresent(Column.class))
						columnName = ((Column) field
								.getAnnotation(Column.class)).value();
					else {
						columnName = StringUtil.camelhumpToUnderline(fieldName);
					}

					if (!fieldName.equals(columnName)) {
						fieldMap.put(columnName, fieldName);
					}
				}

				if (MapUtil.isNotEmpty(fieldMap))
					entityMap.put(entityClass, fieldMap);
			}
		}
	}

	public static Map<Class<?>, Map<String, String>> getEntityMap() {
		return entityMap;
	}
}