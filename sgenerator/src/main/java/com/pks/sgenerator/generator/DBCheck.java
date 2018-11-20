package com.pks.sgenerator.generator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author pks
 * @version 2018年7月18日
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DBCheck {
	
	/**验证数据最大程度，针对String*/
	int len() default 0;
	/**验证数据是否为身份证*/
	boolean isIdcard() default false;
	/**验证数据是否为手机号*/
	boolean isPhone() default false;
	/**验证数值是否为某个范围的值，针对 数值类型字段 其他类型目前直接忽略*/
	double[] val_scope() default {};
	/**验证类型的区间，可多项,针对Ineteger 其他类型目前直接忽略*/
	int[] type_scope() default {};
	
}

