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

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SApiMethod {
	
	/**接口方法名*/
	String methodName() default "";
	/**接口参数列表*/
	SApiParam[] params();

}
