package sonar.rules;

import org.aspectj.lang.JoinPoint;

public abstract class SqlLineRule {
    public abstract void doSqlLine(JoinPoint joinPoint);
}
