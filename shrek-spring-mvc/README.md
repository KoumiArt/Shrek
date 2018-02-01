shrek-spring-mvc
===============

基于spring-mvc框架的封装 <br>
使用步骤：<br>
1.spring 配置文件增加<br>
```
   <!-- Enables the Spring MVC @Controller programming model -->
	<annotation-driven>
		<async-support default-timeout="57000" task-executor="requestExecutor">
		</async-support>
		<message-converters>
			<beans:bean
				class="org.springframework.http.converter.json.MappingJackson2HttpMessageConverter"></beans:bean>
			<beans:bean
				class="org.springframework.http.converter.ByteArrayHttpMessageConverter"></beans:bean>
			<beans:bean
				class="org.springframework.http.converter.StringHttpMessageConverter"></beans:bean>
			<beans:bean
				class="org.springframework.http.converter.ResourceHttpMessageConverter"></beans:bean>
			<beans:bean
				class="org.springframework.http.converter.xml.SourceHttpMessageConverter"></beans:bean>
			<beans:bean
				class="org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter"></beans:bean>
		</message-converters>
		<argument-resolvers>
			<beans:bean
				class="org.nicksun.shrek.spring.mvc.web.argument.PageArgumentResolver" />
		</argument-resolvers>
	</annotation-driven>

	<beans:bean id="jsonMethodProcessor"
		class="org.nicksun.shrek.spring.mvc.web.JsonMethodProcessor">
		<beans:property name="messageConverter" ref="messageConverter"></beans:property>
		<beans:property name="defaultProtocol" ref="defaultProtocol"></beans:property>
		<beans:property name="protocolProcessors">
			<beans:list>
				<beans:bean
					class="org.nicksun.shrek.spring.mvc.web.protocol.impl.DefaultMessageProtocolProcessor"></beans:bean>
				<beans:bean
					class="org.nicksun.shrek.spring.mvc.web.protocol.impl.Base64MessageProtocolProcessor"></beans:bean>
			</beans:list>
		</beans:property>
		<beans:property name="beanWrappers">
			<beans:list>
				<beans:bean
					class="org.nicksun.shrek.spring.mvc.web.returnvalue.impl.DefaultBeanWrapper" />
			</beans:list>
		</beans:property>
		<!-- 启用 http header accept-encoding 处理器-->
		<beans:property name="enableHttpAcceptEncoding" value="true"></beans:property>
		<!-- http header accept-encoding 处理器-->
		<beans:property name="httpAcceptEncodingHandlers">
			<beans:list>
				<beans:bean
					class="org.nicksun.shrek.spring.mvc.web.http.impl.GzipHttpAcceptEncodingHandler" />
			</beans:list>
		</beans:property>
	</beans:bean>
	
	<beans:bean id="defaultProtocol" class="org.springframework.beans.factory.config.FieldRetrievingFactoryBean">
        <beans:property name="staticField" value="org.nicksun.shrek.spring.mvc.web.http.MessageProtocol.TEXT" />
  </beans:bean>
    
	<beans:bean
		class="org.nicksun.shrek.spring.mvc.web.mapping.RequestMappingHandlerAdapterPostProcessor">
		<beans:property name="jsonMethodProcessor" ref="jsonMethodProcessor"></beans:property>
	</beans:bean>

	<beans:bean id="messageConverter"
		class="org.nicksun.shrek.spring.mvc.web.JsonMethodHttpMessageConverter">
	</beans:bean>

	<beans:bean id="exceptionResolver"
		class="org.nicksun.shrek.spring.mvc.web.JsonMethodHandlerExceptionResolver">
		<beans:property name="messageConverter" ref="messageConverter"></beans:property>
		<beans:property name="defaultProtocol" ref="defaultProtocol"></beans:property>
		<beans:property name="protocolProcessors">
			<beans:list>
				<beans:bean
					class="org.nicksun.shrek.spring.mvc.web.protocol.impl.DefaultMessageProtocolProcessor"></beans:bean>
				<beans:bean
					class="org.nicksun.shrek.spring.mvc.web.protocol.impl.Base64MessageProtocolProcessor"></beans:bean>
			</beans:list>
		</beans:property>
		<beans:property name="httpAcceptEncodingHandlers">
			<beans:list>
				<beans:bean
					class="org.nicksun.shrek.spring.mvc.web.http.impl.GzipHttpAcceptEncodingHandler" />
			</beans:list>
		</beans:property>
	</beans:bean>
```
2.在Controller中是用@RequestJson @ResponseJson<br>
```
  @RequestMapping("/test")
	@ResponseJson
	public void test(@RequestJson Integer id) {
		
	}
	
	@RequestMapping("/test4")
	@ResponseJson(protocol = MessageProtocol.BASE64)
	public TestParams test4(@RequestJson(protocol = MessageProtocol.BASE64) @NotNull @Validated TestParams params) {
		return params;
	}
```