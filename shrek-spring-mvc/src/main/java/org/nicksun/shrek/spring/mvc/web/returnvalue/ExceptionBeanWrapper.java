package org.nicksun.shrek.spring.mvc.web.returnvalue;

/**
 * @author nicksun
 *
 */
public interface ExceptionBeanWrapper {
	
	 /**
     * 支持性判断
     * 
     * @param clazz
     * @return
     */
    boolean supportsType(Class<?> clazz);

    /**
     * 对象包装
     * 
     * @param ex
     * @return
     */
    Object wrap(Exception ex);
}
