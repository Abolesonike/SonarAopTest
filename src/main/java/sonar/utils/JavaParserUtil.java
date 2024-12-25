package sonar.utils;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.Statement;

public class JavaParserUtil {

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
        }

        return statementString;
    }
}
