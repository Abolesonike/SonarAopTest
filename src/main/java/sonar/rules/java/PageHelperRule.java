package sonar.rules.java;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.Statement;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import sonar.dto.SensorInfo;
import sonar.rules.JavaMethodRule;
import sonar.utils.JavaParserUtil;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Aspect
public class PageHelperRule extends JavaMethodRule{
    private static final Pattern pageHelperPattern = Pattern.compile("PageHelper\\.startPage\\(");
    private static final Pattern queryPattern = Pattern.compile("Mapper|Service");

    /**
     * PageHelper检查
     *
     * @param joinPoint 切面连接点，用于获取方法参数等信息
     */
    @Override
    @Before("execution(void sonar.sensor.JavaFileSensor.doJavaMethod(..))")
    public void doJavaMethod(JoinPoint joinPoint) {
        try {
            // 获取方法的参数
            MethodDeclaration md = super.getMethodDeclaration(joinPoint);
            SensorInfo sensorInfo = super.getSensorInfo(joinPoint);
            if (Objects.isNull(md) || Objects.isNull(sensorInfo)) return;

            // 获取方法体内容
            md.getBody().ifPresent(body -> {
                // 这里可以添加代码来遍历和处理方法体中的语句
                NodeList<Statement> statements = body.getStatements();
                int pageHelperStartIndex = -1;
                for (Statement statement : statements) {
                    String expressionString = JavaParserUtil.getStatementString(statement, "");
                    if (pageHelperStartIndex == -1) {
                        // 1.先找到PageHelper.startPage()的位置
                        if (statement instanceof ExpressionStmt) {
                            Matcher matcher = pageHelperPattern.matcher(expressionString);
                            if (matcher.find() && statement.getBegin().isPresent()) {
                                pageHelperStartIndex = statement.getBegin().get().line;
                            }
                        }
                    } else {
                        // 2.找到PageHelper.startPage()之后的第一个Mapper或Service调用
                        String[] split = expressionString.split("\n");
                        Matcher matcher = queryPattern.matcher(split[0]);
                        if (!matcher.find()) {
                            super.createIssue(
                                    sensorInfo.getSensorContext(),
                                    sensorInfo.getInputFile(),
                                    JavaRuleDefinition.JAVA_INVALID_PAGE_HELPER,
                                    pageHelperStartIndex,
                                    "失效的分页查询");
                        }
                        pageHelperStartIndex = -1;
                    }
                }
            });
        } catch (Exception e){

        }

    }

}
