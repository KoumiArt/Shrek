shrek-logger 
===============

基于spring的日志记录系统框架 <br>
使用步骤：<br>
1.spring 配置文件增加<br>
```
   <context:component-scan base-package="org.nicksun.shrek.logger" />
   <bean id="methodInfoLogExecutor"
		class="org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor">
		<property name="corePoolSize" value="20" />
		<property name="maxPoolSize" value="100" />
		<property name="queueCapacity" value="1000" />
   </bean>
```
2.在需要记录日志的方法上，使用@MethodLog<br>