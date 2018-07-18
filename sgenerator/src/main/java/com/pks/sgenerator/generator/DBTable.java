package com.pks.sgenerator.generator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author pks
 * @version 2018年1月12日
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DBTable {
	/**数据库名 不设置的时候默认为t_实体名小写*/
	String name() default "";
	/**普通索引 字符创数组类型，里面为字段名联合索引中间以半角逗号分隔，例 {"field1,field2","field3"}为两条索引：联合索引+单索引*/
	String[] index_normal() default {};
	/**唯一索引 字符创数组类型，里面为字段名联合索引中间以半角逗号分隔，例 {"field1,field2","field3"}为两条索引：联合索引+单索引*/
	String[] index_unique() default {};
}

