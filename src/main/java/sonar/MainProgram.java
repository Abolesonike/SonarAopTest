package sonar;


import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import sonar.utils.JavaParserUtil;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainProgram {
    private static final Pattern pageHelperPattern = Pattern.compile("PageHelper\\.startPage\\(");
    private static final Pattern queryPattern = Pattern.compile("Mapper|Service");

    public static void main(String[] args) {
        String inputFilePath = "/path/to/java/file"; // 替换为您的Java文件路径

        try (FileInputStream in = new FileInputStream(inputFilePath)) {
            CompilationUnit cu = JavaParser.parse(in);

            cu.accept(new VoidVisitorAdapter<Void>() {
                @Override
                public void visit(MethodDeclaration md, Void arg) {
                    super.visit(md, arg);

                    // 获取方法名
                    String methodName = String.valueOf(md.getName());
                    System.out.println("Method Name: " + methodName);

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
                                    if (matcher.find()) {
                                        pageHelperStartIndex = statements.indexOf(statement);
                                    }
                                }
                            } else {
                                // 2.找到PageHelper.startPage()之后的第一个Mapper或Service调用
                                String[] split = expressionString.split("\n");
                                Matcher matcher = queryPattern.matcher(split[0]);
                                if (!matcher.find()) {
                                    System.out.println("PageHelper.startPage()之后的第一个Mapper或Service调用: " + split[0]);
                                }
                                pageHelperStartIndex = -1;
                            }
                        }
                    });
                }
            }, null);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}
