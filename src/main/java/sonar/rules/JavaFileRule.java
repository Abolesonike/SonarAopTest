package sonar.rules;

import org.aspectj.lang.JoinPoint;

public abstract class JavaFileRule {
    public abstract void doJavaFile(JoinPoint joinPoint);
}
