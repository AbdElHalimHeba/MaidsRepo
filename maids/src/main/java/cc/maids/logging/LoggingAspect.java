package cc.maids.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class);
			
	@Pointcut("within(cc.maids.service..*)")
	public void allServices() {}
	
	@Pointcut("execution(* cc.maids.service.impl.BorrowingRecordServiceImpl.*(..))")
    public void borrowingService() {}
	
	@AfterReturning(value = "allServices()", returning = "result", argNames = "joinPoint, result")
    public void logServiceAfterReturn(JoinPoint joinPoint, Object result) {
    	
		LOGGER.info("Service " + joinPoint.getSignature().getName() + " returned " + result);
        
    }
	
	@AfterThrowing(value = "allServices()", throwing = "exception", argNames = "joinPoint, exception")
    public void logServiceAfterThrow(JoinPoint joinPoint, Object exception) {
    	
		LOGGER.info("Service " + joinPoint.getSignature().getName() + " thrown " + exception);
        
    }
	
	@Around(value = "borrowingService()", argNames = "joinPoint")
    public void logAroundBorrowingService(ProceedingJoinPoint joinPoint) throws Throwable {
		long start = System.currentTimeMillis();
		
		try {
			joinPoint.proceed();
		} finally {
			long executionTime = System.currentTimeMillis() - start;
			LOGGER.info("Service " + joinPoint.getSignature().getName() + " is executed in " + executionTime + " ms");
		}
        
    }
	
}
