package org.nicksun.shrek.spring.mvc.web.returnvalue;

import org.springframework.core.MethodParameter;

/**
 * 
 * @author nicksun
 *
 */
public interface BeanWrapper {
	
	/**
     * 支持性判断
     * 
     * @param bean
     * @return
     */
    boolean supportsType(MethodParameter returnType);

    /**
     * 对象包装
     * 
     * @param bean
     * @return
     */
    Object wrap(Object bean);
}
