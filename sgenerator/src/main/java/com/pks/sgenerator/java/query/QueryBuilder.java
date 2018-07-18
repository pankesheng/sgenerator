package com.pks.sgenerator.java.query;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.pks.sgenerator.coder.CoderUtil;

public class QueryBuilder {

	public static List<QueryColumn> initQueryColumnList(Class<?> className) {
		List<QueryColumn> qbuilderList = new ArrayList<QueryColumn>();

		List<Field> fs = CoderUtil.allField(className, false);
		for (Field f : fs) {
			if (f.isAnnotationPresent(QueryColumnType.class)) {
				QueryColumnType qtype = f.getAnnotation(QueryColumnType.class);
				String[] es = qtype.value();

				String srcTableName = qtype.srcTableName();
				String srcFieldName = qtype.srcFieldName();
				if (StringUtils.isBlank(srcTableName)) {
					srcTableName = "t_" + className.getName().split("\\.")[5].toLowerCase();
				}
				if (StringUtils.isBlank(srcFieldName)) {
					srcFieldName = f.getName();
				}

				String fieldType = "";
				if ("class java.lang.Integer".equals(f.getType().toString())) {
					fieldType = "Integer";
				} else if ("class java.lang.Long".equals(f.getType().toString())) {
					fieldType = "Long";
				} else if ("class java.lang.String".equals(f.getType().toString())) {
					fieldType = "String";
				} else if ("class java.util.Date".equals(f.getType().toString())) {
					fieldType = "Date";
				} else if ("class java.lang.Float".equals(f.getType().toString())) {
					fieldType = "Float";
				} else if ("class java.math.BigDecimal".equals(f.getType().toString())) {
					fieldType = "BigDecimal";
				}

				for (String e : es) {
					qbuilderList.add(new QueryColumn(f.getName(), fieldType, e, qtype.listQuery(), srcTableName,
							srcFieldName));
				}
			}
		}

		return qbuilderList;
	}

}
