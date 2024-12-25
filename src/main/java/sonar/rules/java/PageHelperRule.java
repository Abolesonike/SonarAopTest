package sonar.rules.java;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.Statement;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import sonar.rules.JavaMethodRule;
import sonar.utils.JavaParserUtil;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Aspect
public class PageHelperRule extends JavaMethodRule{
    private static final Pattern pageHelperPattern = Pattern.compile("PageHelper\\.startPage\\(");
    private static final Pattern queryPattern = Pattern.compile("Mapper|Service");

    @Override
    @Before("execution(void sonar.sensor.JavaFileSensor.doJavaMethod(..))")
    public void doJavaMethod(JoinPoint joinPoint) {
        try {
            // 获取方法的参数
            MethodDeclaration md;
            Object[] args = joinPoint.getArgs();
            md = (MethodDeclaration) args[0];
            if (Objects.isNull(md)) {
                return;
            }

            // 获取方法体内容
            md.getBody().ifPresent(body -> {
                // 这里可以添加代码来遍历和处理方法体中的语句
                NodeList<Statement> statements = body.getStatements();
                int pageHelperStartIndex = -1;
                int otherStatementCount = 0;
                for (Statement statement : statements) {
                    if (statement instanceof ExpressionStmt) {
                        String expressionString = JavaParserUtil.getStatementString(statement, "");
                        if (pageHelperStartIndex == -1) {
                            Matcher matcher = pageHelperPattern.matcher(expressionString);
                            if (matcher.find()) {
                                pageHelperStartIndex = statements.indexOf(statement);
                            }
                        } else {
                            Matcher matcher = queryPattern.matcher(expressionString);
                            if (matcher.find()) {
                                if (otherStatementCount > 0) {
                                    System.out.println();
                                } else {
                                    otherStatementCount++;
                                }
                            }
                        }
                    } else if ( statement instanceof IfStmt ) {

                    }
                }
            });


            // 其他业务逻辑...
        } catch (Exception e){

        }

    }

}
