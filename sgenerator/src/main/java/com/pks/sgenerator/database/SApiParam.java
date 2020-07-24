package com.pks.sgenerator.database;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author pks
 * @date 2020年7月16日
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SApiParam {
	
	/**参数名称*/
	String f_name() default "";
	/**参数类型-默认java.lang.String*/
	String f_type() default "java.lang.String";
	/**显示名称*/
	String v_name() default "";
	/**默认值*/
	String defaultValue() default "";
	/**是否必传*/
	boolean required() default false;
	
}
