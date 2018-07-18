package com.pks.sgenerator.java;

import org.apache.commons.lang3.StringUtils;

import com.pks.sgenerator.coder.CoderUtil;
import com.pks.sgenerator.generator.DBTable;
import com.pks.sgenerator.java.query.QueryBuilder;

public class JavaCodeBuilder {

	public static JavaCode initJavaCode(Class<?> className) {
		String[] allName = className.getName().split("\\.");
		if (allName.length != 6) {// com.thanone.pm2.entity.us.User
			return null;
		}
		JavaCode code = new JavaCode();
		code.setPackageName(allName[0] + "." + allName[1] + "." + allName[2]);// com.thanone.pm2
		code.setModuleName(allName[4]);// us
		code.setClassName(allName[5]);// User
		code.setTableName("t_" + allName[5].toLowerCase());// t_user
		if(className.isAnnotationPresent(DBTable.class)){
			DBTable dbTable = className.getAnnotation(DBTable.class);
			if(StringUtils.isNotBlank(dbTable.name())){
				code.setTableName(dbTable.name());
			}
		}
		code.setFieldList(CoderUtil.allField(className, true));
		code.setAllFieldList(CoderUtil.allField(className, false));
		code.setQbuilderList(QueryBuilder.initQueryColumnList(className));
		return code;
	}

}
