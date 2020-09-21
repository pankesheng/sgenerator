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
public @interface SApiReturn {
	
	/**接口返回值列表*/
	SApiParam[] params();
	
}
