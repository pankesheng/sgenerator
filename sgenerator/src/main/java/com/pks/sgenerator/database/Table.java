package com.pks.sgenerator.database;

import java.util.List;

/**
 * 表
 * 
 * @author zouchongjin@sina.com
 * @data 2015年12月23日
 */
public class Table {
	
	private String name;// 表名
	private List<TableColumn> columns;// 表的各字段

	public Table() {
		super();
	}

	public Table(String name, List<TableColumn> columns) {
		super();
		this.name = name;
		this.columns = columns;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<TableColumn> getColumns() {
		return columns;
	}

	public void setColumns(List<TableColumn> columns) {
		this.columns = columns;
	}
}