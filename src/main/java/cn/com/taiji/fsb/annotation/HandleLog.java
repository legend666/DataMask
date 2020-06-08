package cn.com.taiji.fsb.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface HandleLog {

	/**
	 * 操作模块
	 * @return
	 */
	String types();
	
	/**
	 * 操作描述
	 */
	String describe();
}
