package sonar.rules;

import org.aspectj.lang.JoinPoint;

public abstract class JavaLineRule extends BaseJavaRule {
    public abstract void doJavaFileLine(JoinPoint joinPoint);
}
