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
	/**参数类型-默认java.lang.String
	 * 支持： java.lang.String,java.lang.Integer,java.lang.Long,java.util.Date
	 * 支持简写，如 ： String,Integer,Long,Date
	 * */
	String f_type() default "java.lang.String";
	/**显示名称或说明内容 */
	String v_name() default "";
	/**默认值 ，仅在参数列表中显示该内容*/
	String defaultValue() default "";
	/**参数是否必须，仅在参数列表中显示该内容*/
	boolean required() default false;
	
}
