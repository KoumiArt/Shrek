package org.nicksun.shrek.utils.function;

/**
 * @author nicksun
 *
 */
@FunctionalInterface
public interface Function {

	/**
	 * Performs this operation
	 * 
	 * @throws Exception
	 */
	void accept() throws Exception;
}
