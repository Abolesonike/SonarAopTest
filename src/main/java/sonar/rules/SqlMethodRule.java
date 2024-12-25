package sonar.rules;

import org.aspectj.lang.JoinPoint;

public abstract class SqlMethodRule {
    public abstract void doSqlMethod(JoinPoint joinPoint);
}
