package org.nicksun.shrek.logger;

import java.time.LocalDateTime;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.nicksun.shrek.logger.annotation.MethodLog;
import org.nicksun.shrek.utils.JackJsonUtil;
import org.nicksun.shrek.utils.LocalDateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

@Aspect
@Service
public class MethodLogManager {

	private Logger LOG = LoggerFactory.getLogger(MethodLogManager.class);

	@Resource
	private TaskExecutor methodLogExecutor;

	@Around("@annotation(methodLog)")
	public Object orderLinkLog(ProceedingJoinPoint joinPoint, MethodLog methodLog) throws Throwable {
		LOG.debug("----------------------aop methodLog start----------------------");
		Object result = null;
		String startTime = LocalDateUtil.formatLocalDateTime(LocalDateTime.now());
		String endTime = LocalDateUtil.formatLocalDateTime(LocalDateTime.now());
		try {
			result = joinPoint.proceed();
			endTime = LocalDateUtil.formatLocalDateTime(LocalDateTime.now());
			return result;
		} catch (Throwable t) {
			endTime = LocalDateUtil.formatLocalDateTime(LocalDateTime.now());
			result = ExceptionUtils.getStackTrace(t);
			throw t;
		} finally {
			printLog(joinPoint, methodLog, result, startTime, endTime);
			LOG.debug("----------------------aop methodLog end----------------------");
		}

	}

	private void printLog(ProceedingJoinPoint joinPoint, MethodLog methodLog, Object result, String startTime,
			String endTime) {
		methodLogExecutor.execute(() -> {
			boolean logEnabled = getLogEnabled(methodLog);
			try {
				if (logEnabled) {
					String methodInfo = methodLog.value();
					LOG.info("{}，执行开始时间{}，入参{}",
							new Object[] { methodInfo, startTime, JackJsonUtil.toJson(joinPoint.getArgs()) });
					String resp = StringUtils.EMPTY;
					if (result instanceof String) {
						resp = (String) result;
					} else {
						resp = JackJsonUtil.toJson(result);
					}
					LOG.info("{}，执行结束时间{}，出参{}", new Object[] { methodInfo, endTime, resp });
				}
			} catch (Exception e) {
				LOG.info("记录方法的出入参异常", e);
			}
		});
	}

	private boolean getLogEnabled(MethodLog methodLog) {
		boolean logEnabled = false;
		switch (methodLog.level()) {
		case INFO:
			logEnabled = LOG.isInfoEnabled();
			break;
		case ERROR:
			logEnabled = LOG.isErrorEnabled();
			break;
		case DEBUG:
			logEnabled = LOG.isDebugEnabled();
			break;
		case WARM:
			logEnabled = LOG.isWarnEnabled();
			break;
		case TRACE:
			logEnabled = LOG.isTraceEnabled();
			break;
		default:
			break;
		}
		return logEnabled;
	}

}
