package HANU.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    @Before("execution (* HANU.aop.CacutorService.add(..))")
    public void beforeAdd(JoinPoint joinPoint){
        System.out.println("Call method add of caculator");
    }

    @After("execution (* HANU.aop.CacutorService.add(..))")
    public void afferAdd(JoinPoint joinPoint){
        System.out.println("Done!");
    }
}
