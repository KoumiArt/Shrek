package org.nicksun.shrek.logger.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.nicksun.shrek.logger.Level;

/**
 * 
 * 记录Method入参，出参；请忽使用在大对象入参的方法上
 * 
 * @author nicksun
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MethodLog {
	String value();

	Level level() default Level.INFO;
}
