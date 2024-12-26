package sonar.rules;

import org.aspectj.lang.JoinPoint;

public abstract class SqlLineRule extends BaseSqlRule {
    public abstract void doSqlLine(JoinPoint joinPoint);
}
