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
@Target(ElementType.FIELD)
public @interface DBField {
	
	/**数据库字段名 默认为实体属性名*/
	String name() default "";
	/**字段类型*/
	String type() default "";
	/**数据库字段长度*/
	int length() default 0;
	/**是否允许空*/
	boolean allownull() default true;
	/**字段默认值*/
	String  Default() default "";
	/**数据库字段备注*/
	String comment() default "";
	
}

