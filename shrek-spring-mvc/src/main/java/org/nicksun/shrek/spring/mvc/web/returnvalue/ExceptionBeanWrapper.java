package org.nicksun.shrek.spring.mvc.web.returnvalue;

public interface ExceptionBeanWrapper {
	
	 /**
     * 支持性判断
     * 
     * @param bean
     * @return
     */
    boolean supportsType(Class<?> clazz);

    /**
     * 对象包装
     * 
     * @param bean
     * @return
     */
    Object wrap(Exception ex);
}
