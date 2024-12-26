package sonar.rules;

import org.aspectj.lang.JoinPoint;

public abstract class SqlFileRule extends BaseSqlRule {
    public abstract void doSqlFile(JoinPoint joinPoint);
}
