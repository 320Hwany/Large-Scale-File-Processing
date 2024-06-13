package large_scale_image_processing.global;

import org.aspectj.lang.annotation.Pointcut;

public class TimeCheckPointcut {

    @Pointcut("execution(* large_scale_image_processing..presentation.*.*(..))")
    public void allPresentation() {}
}
