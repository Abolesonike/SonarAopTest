package sonar.rules.java;

import com.github.javaparser.Range;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.ForeachStmt;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import sonar.dto.SensorInfo;
import sonar.rules.JavaMethodRule;

import java.util.Objects;
import java.util.Optional;

@Aspect
public class LoopQueryRule extends JavaMethodRule {

    @Override
    @Before("execution(void sonar.sensor.JavaFileSensor.doJavaMethod(..))")
    public void doJavaMethod(JoinPoint joinPoint) {
        try {
            // 获取方法的参数
            MethodDeclaration md = super.getMethodDeclaration(joinPoint);
            SensorInfo sensorInfo = super.getSensorInfo(joinPoint);
            if (Objects.isNull(md) || Objects.isNull(sensorInfo)) return;

            // 在这里处理每个方法的内容
            md.getBody().ifPresent(body -> {
                body.getStatements().forEach(statement -> {
                    String forBody = "";
                    int lineNum = 0;
                    if (statement instanceof ForeachStmt) {
                        // orEach循环
                        ForeachStmt forStmt = (ForeachStmt) statement;
                        forBody = forStmt.getBody().toString();
                    } else if (statement instanceof ForStmt) {
                        // for i循环
                        ForStmt forStmt = (ForStmt) statement;
                        forBody = forStmt.getBody().toString();
                    } else if (statement instanceof ExpressionStmt) {
                        // lambda表达式的forEach
                        Expression expression = ((ExpressionStmt) statement).getExpression();
                        if (expression.isMethodCallExpr()) {
                            NodeList<Expression> arguments = expression.asMethodCallExpr().getArguments();
                            for (Expression argument : arguments) {
                                if (argument.isLambdaExpr()) {
                                    // 在这里对forEach代码块进行处理
                                    forBody = argument.toString();
                                    break;
                                }
                            }
                        }
                    }

                    if (forBody.contains("select") || forBody.contains("query")) {
                        Optional<Range> range = statement.getRange();
                        if (range.isPresent()) {
                            lineNum = range.get().begin.line;
                        }
                        super.createIssue(
                                sensorInfo.getSensorContext(),
                                sensorInfo.getInputFile(),
                                JavaRuleDefinition.JAVA_LOOP_QUERY,
                                lineNum,
                                "失效的分页查询");
                    }
                });
            });
        } catch (Exception e){

        }

    }
}
