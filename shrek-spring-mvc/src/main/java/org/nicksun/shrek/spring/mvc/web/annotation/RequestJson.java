package org.nicksun.shrek.spring.mvc.web.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.nicksun.shrek.spring.mvc.web.http.MessageProtocol;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestJson {
	String path() default "";

	MessageProtocol protocol() default MessageProtocol.DEFAULT;
}
