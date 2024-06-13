package large_scale_image_processing.global;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Aspect
public class TimeCheckAspect {

    @Around("large_scale_image_processing.global.TimeCheckPointcut.allPresentation()")
    public Object presentationTimeLog(final ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        log.info("Time taken: {} ms", endTime - startTime);
        return result;
    }
}
