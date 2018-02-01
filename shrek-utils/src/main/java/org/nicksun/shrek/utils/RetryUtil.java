package org.nicksun.shrek.utils;

import org.nicksun.shrek.utils.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author nicksun
 *
 */
public class RetryUtil {

	private static Logger logger = LoggerFactory.getLogger(RetryUtil.class);

	private RetryUtil() {
		
	}
	
	/**
	 * 异常重试
	 * 
	 * @param num
	 *            重试次数
	 * @param func
	 *            重试执行个函数
	 * @throws ErrorCodeException 
	 */
	public static void exceptionRetry(int num, Function func) throws Exception {
		int n = num;
		try {
			func.accept();
		} catch (Exception e) {
			n--;
			if (n >= 0) {
				logger.error(String.format("[RETRY_NUM:%d]", n), e);
				exceptionRetry(n, func);
			} else {
				throw e;
			}
		} 
	}

}
