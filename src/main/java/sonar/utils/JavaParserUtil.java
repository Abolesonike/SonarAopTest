package sonar.utils;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.Statement;

public class JavaParserUtil {

    /**
     * 将给定的Statement对象转换为字符串表示形式。
     *
     * @param statement 要转换的Statement对象
     * @param statementString 当前已经构建的语句字符串，用于递归拼接
     * @return 转换后的Java语句字符串
     */
    public static String getStatementString(Statement statement, String statementString) {
        if (statement instanceof ExpressionStmt) {
            ExpressionStmt expressionStmt = (ExpressionStmt) statement;
            Expression expression = expressionStmt.getExpression();
            statementString = expression.toString();
        } else if (statement instanceof IfStmt) {
            IfStmt ifStmt = (IfStmt) statement;
            statementString += getStatementString(ifStmt.getThenStmt(), statementString);
            if (ifStmt.getElseStmt().isPresent()) {
                statementString += getStatementString(ifStmt.getElseStmt().get(), statementString);
            }
        } else if (statement instanceof ForStmt) {
            ForStmt forStmt = (ForStmt) statement;
            Statement body = forStmt.getBody();
            statementString += getStatementString(body, statementString);
        } else if (statement instanceof BlockStmt) {
            BlockStmt blockStmt = (BlockStmt) statement;
            for (Statement stmt : blockStmt.getStatements()) {
                statementString += getStatementString(stmt, statementString) + "\n";
            }
        } else if (statement instanceof ReturnStmt) {
            ReturnStmt returnStmt = (ReturnStmt) statement;
            statementString += returnStmt.toString();
        }

        return statementString;
    }
}
