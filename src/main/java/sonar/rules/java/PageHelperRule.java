package sonar.rules.java;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import sonar.rules.JavaMethodRule;

@Aspect
public class PageHelperRule extends JavaMethodRule{

    @Override
    @Before("execution(void sonar.sensor.JavaFileSensor.doJavaMethod(..))")
    public void doJavaMethod(JoinPoint joinPoint) {
        try {
            System.out.println("Override");
            // ...
        } catch (Exception e){

        }

    }

}
