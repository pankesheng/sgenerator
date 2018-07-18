package com.pks.sgenerator.java;

import org.apache.commons.lang3.StringUtils;

import com.pks.sgenerator.coder.CoderUtil;
import com.pks.sgenerator.generator.DBTable;
import com.pks.sgenerator.java.query.QueryBuilder;

public class JavaCodeBuilder {

	public static JavaCode initJavaCode(Class<?> c) {
		String name = c.getName();
		String[] allName = name.split("\\.");
		if (allName.length <2) {
			return null;
		}
		JavaCode code = new JavaCode();
		String packageName = name.substring(0,name.lastIndexOf("."));
		code.setPackageName(packageName);
		code.setModuleName(packageName.endsWith(".entity")?"":packageName.substring(packageName.lastIndexOf(".")+1));// us
		String className = name.substring(name.lastIndexOf(".")+1);
		code.setClassName(className);
		String tableName = "t_" + className.toLowerCase();
		if(c.isAnnotationPresent(DBTable.class)){
			DBTable dbTable = c.getAnnotation(DBTable.class);
			if(StringUtils.isNotBlank(dbTable.name())){
				tableName = dbTable.name();
			}
		}
		code.setTableName(tableName);
		code.setFieldList(CoderUtil.allField(c, true));
		code.setAllFieldList(CoderUtil.allField(c, false));
		code.setQbuilderList(QueryBuilder.initQueryColumnList(c));
		return code;
	}

}
