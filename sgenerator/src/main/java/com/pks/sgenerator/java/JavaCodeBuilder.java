package com.pks.sgenerator.java;

import org.apache.commons.lang3.StringUtils;

import com.pks.sgenerator.coder.CoderUtil;
import com.pks.sgenerator.generator.DBTable;
import com.pks.sgenerator.java.query.QueryBuilder;

public class JavaCodeBuilder {

	public static JavaCode initJavaCode(Class<?> className,String prefix) {
		String[] allName = className.getName().split("\\.");
		if (allName.length != 6) {// com.thanone.pm2.entity.us.User
			return null;
		}
		JavaCode code = new JavaCode();
		code.setPackageName(allName[0] + "." + allName[1] + "." + allName[2]);// com.thanone.pm2
		code.setModuleName(allName[4]);// us
		code.setClassName(allName[5]);// User
		code.setTableName(convertTableName(prefix, allName[5]));
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
	
	public static String convertTableName(String prefix,String tableName){
		
		if(StringUtils.isNotBlank(prefix)){
			return "t_" + prefix + "_" + tableName.toLowerCase();
		}else{
			return "t_" + tableName.toLowerCase();
		}
		
//		StringBuilder sb = new StringBuilder();
//		char[] charArray = tableName.toCharArray();
//		for (char c : charArray) {
//			if (c >= 'A' && c <= 'Z') {
//				sb.append("_" + c);
//			} else {
//				sb.append(c);
//			}
//		}
//		if(StringUtils.isNotBlank(prefix)){
//			return "t_" + prefix + sb.toString().toLowerCase();
//		}else{
//			return "t" + sb.toString().toLowerCase();
//		}
	}

}
