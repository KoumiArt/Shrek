package org.nicksun.shrek.validator.handler;

import java.lang.reflect.Field;

/**
 * @author nicksun
 *
 */
public class ValidatedContext {

	private Field field;

	private String fieldName;

	private Object fieldValue;

	private Object annotation;

	private Class<?>[] groups;

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public Object getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(Object fieldValue) {
		this.fieldValue = fieldValue;
	}

	public Object getAnnotation() {
		return annotation;
	}

	public void setAnnotation(Object annotation) {
		this.annotation = annotation;
	}

	public Class<?>[] getGroups() {
		return groups;
	}

	public void setGroups(Class<?>[] groups) {
		this.groups = groups;
	}

}
