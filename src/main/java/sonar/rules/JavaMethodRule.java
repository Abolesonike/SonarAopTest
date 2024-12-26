package sonar.rules;

import com.github.javaparser.ast.body.MethodDeclaration;
import org.aspectj.lang.JoinPoint;

public abstract class JavaMethodRule extends BaseJavaRule {
    public abstract void doJavaMethod(JoinPoint joinPoint);

    protected MethodDeclaration getMethodDeclaration(JoinPoint joinPoint) {
        try {
            MethodDeclaration md;
            Object[] args = joinPoint.getArgs();
            md = (MethodDeclaration) args[0];
            return md;
        } catch (Exception e) {
            return null;
        }
    }
}
