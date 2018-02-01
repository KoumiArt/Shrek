package org.nicksun.shrek.validator;

public interface ValidatorHandlerResolver {

	void handle(Object arg, Class<?>[] groups) throws ValidException;

}