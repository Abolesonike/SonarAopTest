package sonar.rules;

import org.aspectj.lang.JoinPoint;

public abstract class SqlMethodRule extends BaseSqlRule {
    public abstract void doSqlMethod(JoinPoint joinPoint);
}
