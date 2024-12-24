package sonar.rules;

import org.aspectj.lang.JoinPoint;

public abstract class JavaLineRule {
    public abstract void doJavaFileLine(JoinPoint joinPoint);
}
