package sonar.rules;

import org.aspectj.lang.JoinPoint;

public abstract class SqlFileRule {
    public abstract void doSqlFile(JoinPoint joinPoint);
}
