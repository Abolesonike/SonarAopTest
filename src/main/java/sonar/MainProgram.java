package sonar;


import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import sonar.sensor.JavaFileSensor;

import java.io.FileInputStream;
import java.io.IOException;

public class MainProgram {

    public static void main(String[] args) {
        JavaFileSensor lineSensor = new JavaFileSensor();
        lineSensor.doJavaMethod(null);

        String inputFilePath = "D:/tools/idea/WorkSpace/fssc-zzkk-4/fssc-bill/core/src/main/java/fssc/erbp/clm/service/impl/CmfClmClaimPaymentServiceImpl.java"; // 替换为您的Java文件路径

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
                        body.getStatements().forEach(statement -> {
                            if (statement instanceof ExpressionStmt) {
                                ExpressionStmt expressionStmt = (ExpressionStmt) statement;
                                Expression expression = expressionStmt.getExpression();
                                String expressionString = expression.toString();
                                if (expressionString.contains("PageHelper")) {
                                    System.out.println("Expression: " + expressionString);
                                }


                            }

                        });
                    });
                }
            }, null);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}
