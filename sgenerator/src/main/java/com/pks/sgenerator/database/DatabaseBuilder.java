package com.pks.sgenerator.database;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;






import org.apache.commons.lang3.StringUtils;

import com.pks.sgenerator.coder.CoderUtil;
import com.pks.sgenerator.generator.DBField;

public class DatabaseBuilder {

	public static Database initDatabase(Class<?>[] carray, String databaseType) {
		List<Table> tables = new ArrayList<Table>();
		for (Class<?> c : carray) {
			tables.add(initTable(c, databaseType));
		}
		String dname = carray[0].getName().split("\\.")[2];

		Database result = new Database();
		result.setName(dname);
		result.setTables(tables);
		return result;
	}

	private static Table initTable(Class<?> c, String databaseType) {
		String tableName = "t_" + c.getSimpleName().toLowerCase();
		List<TableColumn> columns = new ArrayList<TableColumn>();

		List<Field> fs = CoderUtil.allField(c, true);
		for (Field f : fs) {
			String[] typeAndLength = initDefaultTypeAndLengthByField(databaseType, f);
			String tType = typeAndLength[0];
			Integer tLength = typeAndLength[1] == null ? null : Integer.valueOf(typeAndLength[1]);
			Boolean tNullable = true;
			Boolean tIndex = false;
			String tComment = null;
			if (f.isAnnotationPresent(TableColumnType.class)) {
				TableColumnType sqlType = f.getAnnotation(TableColumnType.class);
				if (sqlType.length() != 0) {
					tLength = sqlType.length();
				}
				if (!sqlType.nullable()) {
					tNullable = false;
				}
				if (sqlType.index()) {
					tIndex = true;
				}
				if (StringUtils.isNotBlank(sqlType.comment())) {
					tComment = sqlType.comment();
				}
				if ("text".equals(sqlType.type())) {
					if (Database.TYPE_MYSQL.equals(databaseType)) {
						tType = "text";
						tLength = null;
					}
				}
			}
			columns.add(new TableColumn(f.getName(), tType, tLength, tNullable, tIndex, tComment));
		}
		return new Table(tableName, columns);
	}

	private static String[] initDefaultTypeAndLengthByField(String databaseType, Field ff) {
		if (Database.TYPE_MYSQL.equals(databaseType)) {
			if ("class java.lang.Integer".equals(ff.getType().toString())) {
				if(ff.isAnnotationPresent(DBField.class)){
					DBField f = ff.getAnnotation(DBField.class);
					if(f.length()>0){
						if(f.length()<=4){
							return new String[]{"tinyint",""+f.length()+""};
						}else if(f.length()<15){
							return new String[]{"int",""+f.length()+""};
						}
					}
				}
				return new String[] { "int", "11" };
			} else if ("class java.lang.Long".equals(ff.getType().toString())) {
				return new String[] { "bigint", "20" };
			} else if ("class java.lang.String".equals(ff.getType().toString())) {
				if(ff.isAnnotationPresent(DBField.class)){
					DBField f = ff.getAnnotation(DBField.class);
					if("text".equals(f.type())){
						return new String[]{"text",null};
					}else if("longtext".equals(f.type())){
						return new String[]{"longtext",null};
					}else if("varchar".equals(f.type()) && f.length()>0 && f.length()<65535){
						return new String[]{"varchar",""+f.length()+""};
					}
				}
				return new String[] { "varchar", "100" };
			} else if ("class java.util.Date".equals(ff.getType().toString())) {
				return new String[] { "datetime", null };
			} else if ("class java.lang.Float".equals(ff.getType().toString())) {
				return new String[] { "float", null };
			} else if ("class java.math.BigDecimal".equals(ff.getType().toString())) {
				return new String[] { "decimal(10,2)", null };
			}
		} else if (Database.TYPE_SQLSERVER.equals(databaseType)) {
			if ("class java.lang.Integer".equals(ff.getType().toString())) {
				return new String[] { "int", null };
			} else if ("class java.lang.Long".equals(ff.getType().toString())) {
				return new String[] { "bigint", null };
			} else if ("class java.lang.String".equals(ff.getType().toString())) {
				return new String[] { "nvarchar", "100" };
			} else if ("class java.util.Date".equals(ff.getType().toString())) {
				return new String[] { "datetime", null };
			}
		} else if (Database.TYPE_ORACLE.equals(databaseType)) {
			if ("class java.lang.Integer".equals(ff.getType().toString())) {
				return new String[] { "number", "11" };
			} else if ("class java.lang.Long".equals(ff.getType().toString())) {
				return new String[] { "number", "20" };
			} else if ("class java.lang.String".equals(ff.getType().toString())) {
				return new String[] { "nvarchar2", "100" };
			} else if ("class java.util.Date".equals(ff.getType().toString())) {
				return new String[] { "date", null };
			}
		}
		return new String[] { null, null };
	}

}
