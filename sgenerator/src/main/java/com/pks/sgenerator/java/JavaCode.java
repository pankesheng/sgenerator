package com.pks.sgenerator.java;

import java.lang.reflect.Field;
import java.util.List;

import com.pks.sgenerator.java.query.QueryColumn;

public class JavaCode {

	private String packageName;
	private String moduleName;
	private String className;
	private String tableName;
	private List<Field> fieldList;// 数据库字段
	private List<Field> allFieldList;// 数据库字段+显示字段
	private List<QueryColumn> qbuilderList;// 查询条件属性

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getModuleName() {
		return moduleName;
	}

	public List<Field> getAllFieldList() {
		return allFieldList;
	}

	public void setAllFieldList(List<Field> allFieldList) {
		this.allFieldList = allFieldList;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public List<Field> getFieldList() {
		return fieldList;
	}

	public void setFieldList(List<Field> fieldList) {
		this.fieldList = fieldList;
	}

	public List<QueryColumn> getQbuilderList() {
		return qbuilderList;
	}

	public void setQbuilderList(List<QueryColumn> qbuilderList) {
		this.qbuilderList = qbuilderList;
	}

}
