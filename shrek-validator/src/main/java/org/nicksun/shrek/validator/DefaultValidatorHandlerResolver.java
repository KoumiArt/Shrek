package org.nicksun.shrek.validator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.nicksun.shrek.utils.ReflectUtil;
import org.nicksun.shrek.validator.handler.LengthHandler;
import org.nicksun.shrek.validator.handler.MaxHandler;
import org.nicksun.shrek.validator.handler.MinHandler;
import org.nicksun.shrek.validator.handler.NotEmpyHandler;
import org.nicksun.shrek.validator.handler.NotNullHandler;
import org.nicksun.shrek.validator.handler.RangeHandler;
import org.nicksun.shrek.validator.handler.SizeHandler;
import org.nicksun.shrek.validator.handler.ValidHandler;
import org.nicksun.shrek.validator.handler.ValidatedContext;
import org.nicksun.shrek.validator.handler.ValidatedHandler;

/**
 * @author nicksun
 *
 */
public class DefaultValidatorHandlerResolver implements ValidatorHandlerResolver {

	private List<ValidHandler> customValidator;
	private List<ValidHandler> validator;

	public DefaultValidatorHandlerResolver() {
		validator = new ArrayList<>();
		validator.add(new NotEmpyHandler());
		validator.add(new NotNullHandler());
		validator.add(new MaxHandler());
		validator.add(new MinHandler());
		validator.add(new RangeHandler());
		validator.add(new LengthHandler());
		validator.add(new SizeHandler());
		validator.add(new ValidatedHandler(this));
		if (Objects.nonNull(customValidator) && customValidator.size() > 0) {
			validator.addAll(customValidator);
		}
	}

	public void setCustomValidator(List<ValidHandler> customValidator) {
		this.customValidator = customValidator;
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void handle(Object arg, Class<?>[] groups) throws ValidException {
		if (Collection.class.isAssignableFrom(arg.getClass())) {
			for (Object o : ((Collection<Object>) arg)) {
				Field[] fields = ReflectUtil.getDeclaredFields(o.getClass());
				execution(o, fields, groups);
			}
		} else if (Map.class.isAssignableFrom(arg.getClass())) {
			Set<Map.Entry<?, ?>> map = ((Map) arg).entrySet();
			for (Map.Entry e : map) {
				Object value = e.getValue();
				handle(value, groups);
			}
		} else {
			Field[] fields = ReflectUtil.getDeclaredFields(arg.getClass());
			execution(arg, fields, groups);
		}
	}

	private void execution(Object arg, Field[] fields, Class<?>[] groups) throws ValidException {
		try {
			for (int i = 0; i < fields.length; i++) {
				Field f = fields[i];
				for (ValidHandler h : validator) {
					Class<? extends Annotation> t = h.supportsAnnotation();
					Annotation[] annotations = f.getAnnotations();
					for (int j = 0; j < annotations.length; j++) {
						Annotation a = annotations[j];
						if (a.annotationType().isAssignableFrom(t)) {
							f.setAccessible(true);
							Object value = f.get(arg);
							ValidatedContext context = new ValidatedContext();
							context.setField(f);
							context.setFieldName(f.getName());
							context.setFieldValue(value);
							context.setAnnotation(f.getAnnotation(t));
							context.setGroups(groups);
							h.handleRequest(context);
						}
					}
				}
			}
		} catch (IllegalArgumentException e) {
			throw new ValidException(e);
		} catch (IllegalAccessException e) {
			throw new ValidException(e);
		}
	}
}
