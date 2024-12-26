package sonar.rules;

import org.aspectj.lang.JoinPoint;

public abstract class JavaFileRule extends BaseJavaRule {
    public abstract void doJavaFile(JoinPoint joinPoint);
}
