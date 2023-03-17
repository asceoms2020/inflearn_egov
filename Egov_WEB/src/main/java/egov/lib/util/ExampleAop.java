package egov.lib.util;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class ExampleAop {
	
	private static final Logger logger = LoggerFactory.getLogger(ExampleAop.class);
	
//	@Pointcut("execution(* egov.**.web.*Controller.*(..))")
	@Pointcut("within(egov.main.web.*)") //로도 사용
	public void themeMethod()
	{
		
	}
	
	
	@Before("themeMethod()")
	public void beforeMethod(JoinPoint joinPoint)
	{
		System.out.println("사용자의 요청"+joinPoint.getTarget());
		logger.info("사용자의 요청"+joinPoint.getTarget());
	}

	@AfterThrowing(pointcut="themeMethod()", throwing="exception")
	public void exceptionMethod(JoinPoint joinPoint, Exception exception) throws Exception
	{
		logger.error("ST에러발생===============");
		logger.error("에러위치:"+exception.getClass());
		logger.error("에러내용:"+exception.getMessage());
		logger.error("ED에러발생===============");
	
	}
	
	@Around("themeMethod()")
	public Object aroundMethod(ProceedingJoinPoint joinPoint ) throws Throwable
	{
		//시간
		long st = System.currentTimeMillis();
		//핵심기능
		Object rtn=joinPoint.proceed();
		System.out.println("공통기능1");

		long ed = System.currentTimeMillis();
		System.out.println("공통기능2");
		
		System.out.println("걸린시각"+(ed-st));
		return rtn;
		//시간
	
	}
}
