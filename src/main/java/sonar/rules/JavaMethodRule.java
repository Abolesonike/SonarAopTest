package sonar.rules;

import org.aspectj.lang.JoinPoint;

public abstract class JavaMethodRule {
    public abstract void doJavaMethod(JoinPoint joinPoint);
}
