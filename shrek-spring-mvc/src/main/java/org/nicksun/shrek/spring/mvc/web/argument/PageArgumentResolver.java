package org.nicksun.shrek.spring.mvc.web.argument;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @author nicksun
 *
 */
public class PageArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return Page.class.isAssignableFrom(parameter.getParameterType());
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		String start = webRequest.getParameter("start");
		String limit = webRequest.getParameter("limit");
		Page p = new Page();
		p.setStart(Integer.valueOf(start));
		p.setLimit(Integer.valueOf(limit));
		return p;
	}

}
